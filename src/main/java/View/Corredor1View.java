package View;

import Controller.CorredorCachorroController;
import Controller.Relogio;
import Controller.SalaController;
import Model.Jogador;
import Repository.JogoRepository;
import javafx.application.Application;
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

public class Corredor1View extends Application implements Observador {
    private final SalaController salaController = new SalaController();
    private final CorredorCachorroController cachorroController = new CorredorCachorroController();
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

    public static String pontoEntrada = "ESQUERDA";
    private Stage stage;
    private AnimationTimer gameLoop;
    private Rectangle transicaoPonto;
    private Rectangle transicaoCantina;

    private Image[] andarFrente;
    private Image[] andarCostas;
    private Image[] andarEsquerda;
    private Image[] andarDireita;

    private Label labelRelogio;

    private int frameIndex = 0;
    private long ultimoTempoAnimacao = 0;
    private Direcao ultimaDirecao = Direcao.CIMA;
    private boolean emDialogo = false;

    private ImageView cachorroView;
    private Rectangle cachorroHitbox;
    private Rectangle cachorroSensor;
    private int eventoCachorroSorteado = -1;

    private void loopDoJogo(long tempoAtualNano) {
        double velocidade = 1.2;
        boolean estaSeMovendo = false;
        double movimentoX = 0;
        double movimentoY = 0;

        if (teclado.isCima())    movimentoY -= velocidade;
        if (teclado.isBaixo())   movimentoY += velocidade;
        if (teclado.isEsquerda()) movimentoX -= velocidade;
        if (teclado.isDireita())  movimentoX += velocidade;

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
        if (playerHitbox.getBoundsInParent().intersects(transicaoPonto.getBoundsInParent())) {
            gameLoop.stop();
            try {
                PontoDeOnibusView.pontoEntrada = "PORTA_CORREDOR";
                PontoDeOnibusView mapaAnterior = new PontoDeOnibusView();
                mapaAnterior.start(stage);
            } catch (Exception e) { e.printStackTrace(); }
        }

        if (playerHitbox.getBoundsInParent().intersects(transicaoCantina.getBoundsInParent())) {
            gameLoop.stop();
            try {
                CantinaView.pontoEntrada = "CORREDOR";
                CantinaView proximoMapa = new CantinaView();
                proximoMapa.start(stage);
            } catch (Exception e) { e.printStackTrace(); }
        }
        Relogio.incrementarTempo();
        labelRelogio.setText(Relogio.obterTempoFormatado());

        if(playerHitbox.getBoundsInParent().intersects(cachorroSensor.getBoundsInParent())) {
            if (!emDialogo) {
                emDialogo = true;
                caixaDialogo.setVisible(true);

                if (eventoCachorroSorteado == -1) {
                    eventoCachorroSorteado = cachorroController.carinho();

                    if (eventoCachorroSorteado == 0) {
                        textoDialogo.setText("Você olha para o Caramelo... \n\nMas você está cansada demais para conseguir brincar ou fazer carinho nele agora.\n\nEnergia insuficiente (mínimo 0.2)");
                    }
                    else if (eventoCachorroSorteado == 1) {
                        textoDialogo.setText("Caramelo: Au au! \n\nVocê encontrou o doguinho do campus e fez carinho nele! \n\nEnergia -0.2 | Motivação +0.5");
                    }
                    else if (eventoCachorroSorteado == 2) {
                        textoDialogo.setText("Caramelo: GRRR... AU! \n\nO doguinho se assustou e te mordeu!");
                    }
                    atualizar();
                }
            }
        } else {
            if (emDialogo && eventoCachorroSorteado != -1) {
                emDialogo = false;
                caixaDialogo.setVisible(false);
                eventoCachorroSorteado = -1;
            }
        }
    }


    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        Image imagemMapa = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Corredor1.png")));
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
        criarObstaculo(0, 0, mapW, 70.0, mundoBox);
        criarObstaculo(0, mapH - 20.0, mapW, 20.0, mundoBox);
        criarObstaculo(184.0, 20.5, 159.0, 249.0, mundoBox);
        criarObstaculo(544.0, 31.5, 105.0, 205.0, mundoBox);
        criarObstaculo(767.0, 209.5, 69.0, 62.0, mundoBox);
        criarObstaculo(918.0, 62.5, 41.0, 226.0, mundoBox);
        criarObstaculo(1083.0, 13.5, 167.0, 226.0, mundoBox);
        criarObstaculo(1344.0, 131.5, 108.0, 90.0, mundoBox);
        criarObstaculo(610.0, 644.5, 88.0, 210.0, mundoBox);
        criarObstaculo(1276.0, 640.5, 169.0, 243.0, mundoBox);
        criarObstaculo(1278.0, 235.5, 133.0, 17.0, mundoBox);
        criarObstaculo(24.0, 278.5, 58.0, 95.0, mundoBox);
        criarObstaculo(22.0, 519.5, 58.0, 110.0, mundoBox);
        criarObstaculo(1574.0, 283.5, 67.0, 93.0, mundoBox);
        criarObstaculo(1574.0, 520.5, 64.0, 113.0, mundoBox);

        transicaoPonto = criarTransicao(55.0, 380.5, 21.0, 134.0, mundoBox);
        transicaoCantina = criarTransicao(1578.0, 386.5, 19.0, 127.0, mundoBox);

        Image imgCachorro = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/cachorro.gif")));
        cachorroView = new ImageView(imgCachorro);
        cachorroView.setLayoutX(950.0);
        cachorroView.setLayoutY(570.0);

        double dogW = imgCachorro.getWidth();
        double dogH = imgCachorro.getHeight();

        double hitboxW = dogW * 0.6;
        double hitboxH = dogH * 0.75;
        double hitboxX = 950.0 + (dogW - hitboxW) / 2;
        double hitboxY = 570.0 + (dogH - hitboxH);

        cachorroHitbox = new Rectangle(hitboxX, hitboxY, hitboxW, hitboxH);
        cachorroHitbox.setFill(Color.TRANSPARENT);

        cachorroSensor = new Rectangle(hitboxX - 15, hitboxY - 15, hitboxW + 30, hitboxH + 30);
        cachorroSensor.setFill(Color.TRANSPARENT);

        mundoBox.getChildren().addAll(cachorroView, cachorroHitbox, cachorroSensor);
        obstaculos.add(cachorroHitbox);

        inicializarImagensAnimacao();
        playerView = new ImageView(andarFrente[0]);
        mundoBox.getChildren().add(playerView);
        playerHitbox = new Rectangle(0, 0, andarFrente[0].getWidth() * 0.8, andarFrente[0].getHeight() * 0.4);

        inicializarCaixaDialogo(mundoBox, mapW, mapH);

        if ("DIREITA".equals(pontoEntrada)) {
            playerView.setLayoutX(1460.0); playerView.setLayoutY(401.0); ultimaDirecao = Direcao.ESQUERDA;
        } else {
            playerView.setLayoutX(96); playerView.setLayoutY(401); ultimaDirecao = Direcao.DIREITA;
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
        caixaDialogo.setPrefSize(650, 145);
        caixaDialogo.getStyleClass().add("caixa-dialogo");

        textoDialogo = new Label();
        textoDialogo.getStyleClass().add("texto-dialogo");
        textoDialogo.setLayoutX(20);
        textoDialogo.setLayoutY(15);
        textoDialogo.setWrapText(true);
        textoDialogo.setPrefWidth(610);

        caixaDialogo.getChildren().add(textoDialogo);
        caixaDialogo.setVisible(false);
        caixaDialogo.setLayoutX((mapW - 650) / 2.0);
        caixaDialogo.setLayoutY(mapH - 145 - 25);
        root.getChildren().add(caixaDialogo);
    }

    @Override
    public void atualizar() {
        Jogador jogador = JogoRepository.getJogoAtual().getPlayer();
        barraEnergia.setProgress(jogador.getEnergia() / 100.0);
    }

    public static void main(String[] args) { launch(args); }
}