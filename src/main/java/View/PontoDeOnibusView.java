package View;

import Controller.Relogio;
import Controller.SalaController;
import Model.Jogador;
import Repository.JogoRepository;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.AnimationTimer;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PontoDeOnibusView extends Application implements Observador {
    private final SalaController salaController = new SalaController();
    private final List<Rectangle> obstaculos = new ArrayList<>();
    private final long intervalo = 120_000_000;

    private ImageView playerView;
    private Movimento teclado;
    private Rectangle playerHitbox;
    private Pane caixaDialogo;
    private Label textoDialogo;
    private ProgressBar barraEnergia;
    private Label textoBarraEnergia;
    private StackPane containerEnergia;

    private Stage stage;
    private AnimationTimer gameLoop;
    private Rectangle transicaoCorredor1;
    public static String pontoEntrada = "NOVO_JOGO";

    private Image[] andarFrente;
    private Image[] andarCostas;
    private Image[] andarEsquerda;
    private Image[] andarDireita;

    private Label labelRelogio;

    private int frameIndex = 0;
    private long ultimoTempoAnimacao = 0;
    private Direcao ultimaDirecao = Direcao.CIMA;
    private boolean emDialogo = false;

    private void loopDoJogo(long tempoAtualNano) {
        double velocidade = 1.2;
        boolean estaSeMovendo = false;
        double movimentoX = 0;
        double movimentoY = 0;

        if (!emDialogo) {
            if (teclado.isCima())    movimentoY -= velocidade;
            if (teclado.isBaixo())   movimentoY += velocidade;
            if (teclado.isEsquerda()) movimentoX -= velocidade;
            if (teclado.isDireita())  movimentoX += velocidade;
        }

        if (movimentoX < 0) ultimaDirecao = Direcao.ESQUERDA;
        if (movimentoX > 0) ultimaDirecao = Direcao.DIREITA;
        if (movimentoY < 0) ultimaDirecao = Direcao.CIMA;
        if (movimentoY > 0) ultimaDirecao = Direcao.BAIXO;

        if (movimentoX != 0 || movimentoY != 0) estaSeMovendo = true;

        double larguraPadrao = andarFrente[0].getWidth();
        double alturaPadrao = andarFrente[0].getHeight();

        double proximoX = playerView.getLayoutX() + movimentoX;
        playerHitbox.setX(proximoX + (larguraPadrao - playerHitbox.getWidth()) / 2);
        playerHitbox.setY(playerView.getLayoutY() + (alturaPadrao - playerHitbox.getHeight()));

        boolean colidiuX = false;
        for (Rectangle obs : obstaculos) {
            if (playerHitbox.getBoundsInParent().intersects(obs.getBoundsInParent())) {
                colidiuX = true;
                break;
            }
        }
        if (!colidiuX && movimentoX != 0) {
            playerView.setLayoutX(proximoX);
        }

        double proximoY = playerView.getLayoutY() + movimentoY;
        playerHitbox.setX(playerView.getLayoutX() + (larguraPadrao - playerHitbox.getWidth()) / 2);
        playerHitbox.setY(proximoY + (alturaPadrao - playerHitbox.getHeight()));

        boolean colidiuY = false;
        for (Rectangle obs : obstaculos) {
            if (playerHitbox.getBoundsInParent().intersects(obs.getBoundsInParent())) {
                colidiuY = true;
                break;
            }
        }
        if (!colidiuY && movimentoY != 0) {
            playerView.setLayoutY(proximoY);
        }

        playerHitbox.setX(playerView.getLayoutX() + (larguraPadrao - playerHitbox.getWidth()) / 2);
        playerHitbox.setY(playerView.getLayoutY() + (alturaPadrao - playerHitbox.getHeight()));

        if (estaSeMovendo) {
            if (tempoAtualNano - ultimoTempoAnimacao >= intervalo) {
                frameIndex++;
                ultimoTempoAnimacao = tempoAtualNano;
                switch (ultimaDirecao) {
                    case BAIXO:    playerView.setImage(andarFrente[frameIndex % andarFrente.length]); break;
                    case CIMA:     playerView.setImage(andarCostas[frameIndex % andarCostas.length]); break;
                    case DIREITA:  playerView.setImage(andarDireita[frameIndex % andarDireita.length]); break;
                    case ESQUERDA: playerView.setImage(andarEsquerda[frameIndex % andarEsquerda.length]); break;
                }
            }
        } else {
            switch (ultimaDirecao) {
                case BAIXO:    playerView.setImage(andarFrente[0]); break;
                case CIMA:     playerView.setImage(andarCostas[0]); break;
                case DIREITA:  playerView.setImage(andarDireita[0]); break;
                case ESQUERDA: playerView.setImage(andarEsquerda[0]); break;
            }
        }
        if (playerHitbox.getBoundsInParent().intersects(transicaoCorredor1.getBoundsInParent())) {
            gameLoop.stop();
            try {
                Corredor1View.pontoEntrada = "ESQUERDA";
                Corredor1View proximoMapa = new Corredor1View();
                proximoMapa.start(stage);
            } catch (Exception e) { e.printStackTrace(); }
        }
        Relogio.incrementarTempo();
        labelRelogio.setText(Relogio.obterTempoFormatado());
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        Image imagemMapa = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/PontoDeOnibus.png")));
        double mapW = imagemMapa.getWidth();
        double mapH = imagemMapa.getHeight();

        Pane mundoBox = new Pane();
        mundoBox.setPrefSize(mapW, mapH);
        mundoBox.setMinSize(mapW, mapH);
        mundoBox.setMaxSize(mapW, mapH);

        Group mundoGroup = new Group(mundoBox);
        StackPane root = new StackPane(mundoGroup);
        root.setStyle("-fx-background-color: #000000;");

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Style.css")).toExternalForm());
        teclado = new Movimento(scene);

        ImageView mapa = new ImageView(imagemMapa);
        mundoBox.getChildren().add(mapa);

        labelRelogio = new Label("07:00");
        labelRelogio.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding: 5px; -fx-background-radius: 5px;");
        labelRelogio.setLayoutX(20); labelRelogio.setLayoutY(20);
        mundoBox.getChildren().add(labelRelogio);

        barraEnergia = new ProgressBar(1.0);
        barraEnergia.setStyle("-fx-accent: #27ae60;");
        barraEnergia.setPrefWidth(200); barraEnergia.setPrefHeight(22);

        textoBarraEnergia = new Label("Energia");
        textoBarraEnergia.setStyle("-fx-font-size: 11px; -fx-text-fill: white; -fx-font-weight: bold; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 3, 0, 0, 0);");

        containerEnergia = new StackPane();
        containerEnergia.getChildren().addAll(barraEnergia, textoBarraEnergia);
        containerEnergia.setLayoutX(20); containerEnergia.setLayoutY(65);
        mundoBox.getChildren().add(containerEnergia);

        JogoRepository.getJogoAtual().getPlayer().adicionarObservador(this);
        atualizar();

        criarObstaculo(53.0, 898.5, 70.0, 82.0, mundoBox);
        criarObstaculo(178.0, 768.5, 103.0, 56.0, mundoBox);
        criarObstaculo(394.0, 733.5, 83.0, 87.0, mundoBox);
        criarObstaculo(1206.0, 762.5, 31.0, 58.0, mundoBox);
        criarObstaculo(1357.0, 900.5, 63.0, 75.0, mundoBox);
        criarObstaculo(1362.0, 593.5, 16.0, 138.0, mundoBox);
        criarObstaculo(823.0, 299.5, 317.0, 137.0, mundoBox);
        criarObstaculo(457.0, 34.5, 53.0, 83.0, mundoBox);
        criarObstaculo(994.0, 38.5, 56.0, 86.0, mundoBox);
        criarObstaculo(1109.0, 68.5, 131.0, 59.0, mundoBox);
        criarObstaculo(1355.0, 33.5, 26.0, 136.0, mundoBox);
        criarObstaculo(0, 0, 20.0, mapH, mundoBox);
        criarObstaculo(mapW - 20.0, 0, 20.0, mapH, mundoBox);
        criarObstaculo(0, 0, mapW, 20.0, mundoBox);
        criarObstaculo(0, mapH - 20.0, mapW, 20.0, mundoBox);

        transicaoCorredor1 = criarTransicao(648.0, 982.5, 143.0, 8.0, mundoBox);

        inicializarImagensAnimacao();
        playerView = new ImageView(andarFrente[0]);
        mundoBox.getChildren().add(playerView);
        playerHitbox = new Rectangle(0, 0, andarFrente[0].getWidth() * 0.8, andarFrente[0].getHeight() * 0.4);

        inicializarCaixaDialogo(mundoBox, mapW, mapH);

        if ("PORTA_CORREDOR".equals(pontoEntrada)) {
            playerView.setLayoutX(670.0); playerView.setLayoutY(890.0); ultimaDirecao = Direcao.CIMA;
        } else {
            playerView.setLayoutX((mapW - andarFrente[0].getWidth()) / 2); playerView.setLayoutY(500.0); ultimaDirecao = Direcao.BAIXO;
        }

        Runnable aplicarZoom = () -> {
            double janelaW = root.getWidth();
            double janelaH = root.getHeight();
            if (janelaW <= 0 || janelaH <= 0) return;
            double zoom = Math.min(janelaW / mapW, janelaH / mapH);
            mundoGroup.setScaleX(zoom);
            mundoGroup.setScaleY(zoom);
        };

        root.widthProperty().addListener((obs, velho, novo) -> aplicarZoom.run());
        root.heightProperty().addListener((obs, velho, novo) -> aplicarZoom.run());

        primaryStage.setScene(scene);
        if (!primaryStage.isMaximized()) {
            primaryStage.setWidth(800);
            primaryStage.setHeight(600);
        }
        primaryStage.setMaximized(true);
        primaryStage.show();

        // FIX para o Wayland/GNOME (Linux)
        javafx.animation.PauseTransition pause = new javafx.animation.PauseTransition(javafx.util.Duration.millis(150));
        pause.setOnFinished(e -> aplicarZoom.run());
        pause.play();

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long tempoAtualNano) { loopDoJogo(tempoAtualNano); }
        };
        gameLoop.start();
    }

    private Rectangle criarObstaculo(double x, double y, double w, double h, Pane root) {
        Rectangle r = new Rectangle(x, y, w, h);
        r.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(r);
        obstaculos.add(r);
        return r;
    }

    private Rectangle criarTransicao(double x, double y, double w, double h, Pane root) {
        Rectangle r = new Rectangle(x, y, w, h);
        r.setFill(Color.rgb(0, 255, 0, 0.5));
        root.getChildren().add(r);
        return r;
    }

    private void inicializarImagensAnimacao() {
        andarCostas = new Image[]{
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprite_BF_parada.png"))),
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprite_BF.png"))),
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprite_BF2.png")))
        };
        andarFrente = new Image[]{
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprite_FD_parada.png"))),
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprite_FD.png"))),
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprite_FD2.png")))
        };
        andarEsquerda = new Image[]{
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprite_LF_parada.png"))),
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprite_LF.png"))),
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprite_LF2.png")))
        };
        andarDireita = new Image[]{
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprite_RL_parada.png"))),
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprite_RL.png"))),
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/sprite_RL2.png")))
        };
    }

    private void inicializarCaixaDialogo(Pane root, double mapW, double mapH) {
        caixaDialogo = new Pane();
        caixaDialogo.setPrefSize(650, 110);
        caixaDialogo.getStyleClass().add("caixa-dialogo");

        textoDialogo = new Label();
        textoDialogo.getStyleClass().add("texto-dialogo");
        textoDialogo.setLayoutX(20);
        textoDialogo.setLayoutY(20);
        textoDialogo.setWrapText(true);
        textoDialogo.setPrefWidth(610);

        caixaDialogo.getChildren().add(textoDialogo);
        caixaDialogo.setVisible(false);
        caixaDialogo.setLayoutX((mapW - 650) / 2.0);
        caixaDialogo.setLayoutY(mapH - 110 - 40);
        root.getChildren().add(caixaDialogo);
    }

    @Override
    public void atualizar() {
        Jogador jogador = JogoRepository.getJogoAtual().getPlayer();
        barraEnergia.setProgress(jogador.getEnergia() / 100.0);
    }

    public static void main(String[] args) { launch(args); }
}