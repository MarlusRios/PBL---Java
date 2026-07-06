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

public class Corredor2View extends Application implements Observador {
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

    private Image[] andarFrente;
    private Image[] andarCostas;
    private Image[] andarEsquerda;
    private Image[] andarDireita;

    private Label labelRelogio;

    private int frameIndex = 0;
    private long ultimoTempoAnimacao = 0;
    private Direcao ultimaDirecao = Direcao.CIMA;
    private boolean emDialogo = false;

    public static String pontoEntrada = "SALA";
    private Stage stage;
    private AnimationTimer gameLoop;
    private Rectangle transicaoSala;
    private Rectangle transicaoLab;

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
        if (playerHitbox.getBoundsInParent().intersects(transicaoSala.getBoundsInParent())) {
            gameLoop.stop();
            try {
                SalaView.pontoEntrada = "CORREDOR2";
                SalaView mapaAnterior = new SalaView();
                mapaAnterior.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (playerHitbox.getBoundsInParent().intersects(transicaoLab.getBoundsInParent())) {
            gameLoop.stop();
            try {
                LaboratorioView.pontoEntrada = "CORREDOR2";
                LaboratorioView proximoMapa = new LaboratorioView();
                proximoMapa.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Relogio.incrementarTempo();
        labelRelogio.setText(Relogio.obterTempoFormatado());
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        Image imagemMapa = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Corredor2.png")));
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

        criarObstaculo(0, 0, 20.0, mapH, mundoBox);
        criarObstaculo(mapW - 20.0, 0, 20.0, mapH, mundoBox);
        criarObstaculo(0, 0, mapW, 50.0, mundoBox);
        criarObstaculo(0, mapH - 20.0, mapW, 20.0, mundoBox);
        criarObstaculo(184.0, 52.0, 167.0, 192.0, mundoBox);
        criarObstaculo(763.0, 212.0, 71.0, 56.0, mundoBox);
        criarObstaculo(937.0, 53.0, 141.0, 175.0, mundoBox);
        criarObstaculo(1319.0, 56.0, 114.0, 167.0, mundoBox);
        criarObstaculo(1528.0, 116.0, 64.0, 63.0, mundoBox);
        criarObstaculo(181.0, 599.0, 147.0, 218.0, mundoBox);
        criarObstaculo(542.0, 716.0, 89.0, 83.0, mundoBox);
        criarObstaculo(852.0, 634.0, 90.0, 192.0, mundoBox);
        criarObstaculo(1310.0, 606.0, 133.0, 213.0, mundoBox);
        criarObstaculo(550.0, 156.0, 156.0, 23.0, mundoBox);
        criarObstaculo(22.0, 278.0, 52.0, 82.0, mundoBox);
        criarObstaculo(23.0, 523.0, 51.0, 94.0, mundoBox);
        criarObstaculo(1660.0, 277.0, 72.0, 102.0, mundoBox);
        criarObstaculo(1659.0, 512.0, 72.0, 109.0, mundoBox);

        transicaoSala = criarTransicao(39.0, 366.0, 28.0, 148.0, mundoBox);
        transicaoLab = criarTransicao(1668.0, 383.0, 17.0, 120.0, mundoBox);

        inicializarImagensAnimacao();
        playerView = new ImageView(andarFrente[0]);
        mundoBox.getChildren().add(playerView);
        playerHitbox = new Rectangle(0, 0, andarFrente[0].getWidth() * 0.8, andarFrente[0].getHeight() * 0.4);

        inicializarCaixaDialogo(mundoBox, mapW, mapH);

        if ("LABORATORIO".equals(pontoEntrada)) {
            playerView.setLayoutX(1550.0); playerView.setLayoutY(400.0); ultimaDirecao = Direcao.ESQUERDA;
        } else {
            playerView.setLayoutX(85.0); playerView.setLayoutY(400.0); ultimaDirecao = Direcao.DIREITA;
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