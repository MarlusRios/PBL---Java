package View;

import Controller.Relogio;
import Controller.SalaController;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.AnimationTimer;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ColegiadoView extends Application {
    private final SalaController salaController = new SalaController();
    private final List<Rectangle> obstaculos = new ArrayList<>();
    private final long intervalo = 120_000_000;

    private ImageView playerView;
    private Movimento teclado;
    private Rectangle playerHitbox;
    private Pane caixaDialogo;
    private Label textoDialogo;

    private Image[] andarFrente;
    private Image[] andarCostas;
    private Image[] andarEsquerda;
    private Image[] andarDireita;

    private Label labelRelogio;

    private int frameIndex = 0;
    private long ultimoTempoAnimacao = 0;
    private Direcao ultimaDirecao = Direcao.CIMA;
    private boolean emDialogo = false;

    private double playerXRel = -1;
    private double playerYRel = -1;

    public static String pontoEntrada = "LABORATORIO";
    private Stage stage;
    private AnimationTimer gameLoop;
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
            playerXRel += movimentoX;
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
            playerYRel += movimentoY;
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

        if (playerHitbox.getBoundsInParent().intersects(transicaoLab.getBoundsInParent())) {
            gameLoop.stop();
            try {
                LaboratorioView.pontoEntrada = "COLEGIADO";
                LaboratorioView mapaAnterior = new LaboratorioView();
                mapaAnterior.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 1. Faz o tempo passar
        Relogio.incrementarTempo();

        // 2. Atualiza o texto visual do relógio na tela
        labelRelogio.setText(Relogio.obterTempoFormatado());

        // 3. A cada X segundos/minutos (ex: 30), atualiza atributos do jogador
        if (Relogio.segundosTotais % 30 == 0 && Relogio.frames == 0) {
            // salaController.atualizarJogo(jogo, jogadorService);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        Pane root = new Pane();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Style.css")).toExternalForm());
        teclado = new Movimento(scene);

        Image imagemMapa = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Colegiado.png")));
        ImageView mapa = new ImageView(imagemMapa);
        root.getChildren().add(mapa);

        // Criando o relógio
        labelRelogio = new Label("07:00");
        labelRelogio.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding: 5px; -fx-background-radius: 5px;");
        labelRelogio.setLayoutX(20);
        labelRelogio.setLayoutY(20);
        root.getChildren().add(labelRelogio);

        Rectangle bordaEsquerda = new Rectangle();  bordaEsquerda.setFill(Color.rgb(0, 0, 255, 0.5));
        Rectangle bordaDireita = new Rectangle();   bordaDireita.setFill(Color.rgb(0, 0, 255, 0.5));
        Rectangle bordaSuperior = new Rectangle();  bordaSuperior.setFill(Color.rgb(0, 0, 255, 0.5));
        Rectangle bordaInferior = new Rectangle();  bordaInferior.setFill(Color.rgb(0, 0, 255, 0.5));
        Rectangle bancada = new Rectangle(); bancada.setFill(Color.rgb(255, 0, 0, 0.5));
        Rectangle planta = new Rectangle();   planta.setFill(Color.rgb(255, 0, 0, 0.5));
        Rectangle cadeira = new Rectangle();  cadeira.setFill(Color.rgb(255, 0, 0, 0.5));
        transicaoLab = new Rectangle(); transicaoLab.setFill(Color.rgb(0, 255, 0, 0.5));

        root.getChildren().add(transicaoLab);

        root.getChildren().addAll(bordaEsquerda, bordaDireita, bordaSuperior, bordaInferior, bancada, planta, cadeira);

        obstaculos.clear();
        obstaculos.add(bordaEsquerda);
        obstaculos.add(bordaDireita);
        obstaculos.add(bordaSuperior);
        obstaculos.add(bordaInferior);
        obstaculos.add(bancada);
        obstaculos.add(planta);
        obstaculos.add(cadeira);

        inicializarImagensAnimacao();

        playerView = new ImageView(andarFrente[0]);
        root.getChildren().add(playerView);
        playerHitbox = new Rectangle(0, 0, andarFrente[0].getWidth() * 0.8, andarFrente[0].getHeight() * 0.4);

        inicializarCaixaDialogo(root);

        Runnable reposicionarElementos = () -> {
            double larguraAtual = scene.getWidth() <= 0 ? 800 : scene.getWidth();
            double alturaAtual = scene.getHeight() <= 0 ? 600 : scene.getHeight();

            double mapaX = (larguraAtual - imagemMapa.getWidth()) / 2;
            double mapaY = (alturaAtual - imagemMapa.getHeight()) / 2;
            mapa.setLayoutX(mapaX);
            mapa.setLayoutY(mapaY);

            double largMapa = imagemMapa.getWidth();
            double altMapa = imagemMapa.getHeight();

            bordaEsquerda.setX(mapaX);
            bordaEsquerda.setY(mapaY);
            bordaEsquerda.setWidth(30.0);
            bordaEsquerda.setHeight(altMapa);

            bordaDireita.setX(mapaX + largMapa - 30.0);
            bordaDireita.setY(mapaY);
            bordaDireita.setWidth(30.0);
            bordaDireita.setHeight(altMapa);

            bordaSuperior.setX(mapaX);
            bordaSuperior.setY(mapaY);
            bordaSuperior.setWidth(largMapa);
            bordaSuperior.setHeight(268.0);

            bordaInferior.setX(mapaX);
            bordaInferior.setY(mapaY + altMapa - 40.0);
            bordaInferior.setWidth(largMapa);
            bordaInferior.setHeight(40.0);

            bancada.setX(mapaX + 241.0);
            bancada.setY(mapaY + 271.0);
            bancada.setWidth(482.0);
            bancada.setHeight(122.0);

            planta.setX(mapaX + 38.0);
            planta.setY(mapaY + 630.0);
            planta.setWidth(66.0);
            planta.setHeight(72.0);

            cadeira.setX(mapaX + 935.0);
            cadeira.setY(mapaY + 376.0);
            cadeira.setWidth(24.0);
            cadeira.setHeight(230.0);

            transicaoLab.setX(mapaX + 32.0);
            transicaoLab.setY(mapaY + 368.0);
            transicaoLab.setWidth(5.0);
            transicaoLab.setHeight(81.0);

            if (playerXRel == -1) {
                playerXRel = 55.0;
                playerYRel = 355;
                ultimaDirecao = Direcao.DIREITA;
            }

            playerView.setLayoutX(mapaX + playerXRel);
            playerView.setLayoutY(mapaY + playerYRel);

            caixaDialogo.setLayoutX((larguraAtual - caixaDialogo.getPrefWidth()) / 2);
            caixaDialogo.setLayoutY(alturaAtual - caixaDialogo.getPrefHeight() - 40);
        };

        scene.widthProperty().addListener((obs, velho, novo) -> reposicionarElementos.run());
        scene.heightProperty().addListener((obs, velho, novo) -> reposicionarElementos.run());

        if (!primaryStage.isMaximized()) {
            primaryStage.setWidth(800);
            primaryStage.setHeight(600);
        }
        primaryStage.setMaximized(true);
        primaryStage.show();
        reposicionarElementos.run();

        mapa.setOnMouseClicked(e -> System.out.println("X: " + e.getX() + " | Y: " + e.getY()));

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long tempoAtualNano) {
                loopDoJogo(tempoAtualNano);
            }
        };
        gameLoop.start();

        primaryStage.setScene(scene);
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

    private void inicializarCaixaDialogo(Pane root) {
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
        root.getChildren().add(caixaDialogo);
    }

    public static void main(String[] args) {
        launch(args);
    }
}