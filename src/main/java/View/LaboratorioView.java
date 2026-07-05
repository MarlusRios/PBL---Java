package View;

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

public class LaboratorioView extends Application {
    private final SalaController salaController = new SalaController();
    private final List<Rectangle> obstaculos = new ArrayList<>();
    private final long intervalo = 120_000_000;

    private ImageView playerView;
    private Movimento teclado;
    private Rectangle playerHitbox;
    private Rectangle blocoProfessor;
    private Pane caixaDialogo;
    private Label textoDialogo;

    private Image[] andarFrente;
    private Image[] andarCostas;
    private Image[] andarEsquerda;
    private Image[] andarDireita;

    private int frameIndex = 0;
    private long ultimoTempoAnimacao = 0;
    private Direcao ultimaDirecao = Direcao.CIMA;
    private boolean emDialogo = false;




    private double playerXRel = -1;
    private double playerYRel = -1;

    public static String pontoEntrada = "CORREDOR2";
    private Stage stage;
    private AnimationTimer gameLoop;
    private Rectangle transicaoCorredor2;
    private Rectangle transicaoColegiado;





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
        } else {
            if (teclado.isBaixo())    movimentoY += velocidade;
            if (teclado.isEsquerda()) movimentoX -= velocidade;
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
            if (movimentoX < 0) ultimaDirecao = Direcao.ESQUERDA;
            if (movimentoX > 0) ultimaDirecao = Direcao.DIREITA;
            estaSeMovendo = true;
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
            if (movimentoY < 0) ultimaDirecao = Direcao.CIMA;
            if (movimentoY > 0) ultimaDirecao = Direcao.BAIXO;
            estaSeMovendo = true;
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

        if (playerHitbox.getBoundsInParent().intersects(transicaoCorredor2.getBoundsInParent())) {
            gameLoop.stop();
            try {
                Corredor2View.pontoEntrada = "LABORATORIO";
                Corredor2View mapaAnterior = new Corredor2View();
                mapaAnterior.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (playerHitbox.getBoundsInParent().intersects(transicaoColegiado.getBoundsInParent())) {
            gameLoop.stop();
            try {
                ColegiadoView proximoMapa = new ColegiadoView();
                proximoMapa.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        Pane root = new Pane();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Style.css")).toExternalForm());
        teclado = new Movimento(scene);

        Image imagemMapa = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/laboratorio.png")));
        ImageView mapa = new ImageView(imagemMapa);
        root.getChildren().add(mapa);



        Rectangle estante1 = new Rectangle();
        estante1.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(estante1);
        obstaculos.add(estante1);

        Rectangle estante2 = new Rectangle();
        estante2.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(estante2);
        obstaculos.add(estante2);

        Rectangle quadro = new Rectangle();
        quadro.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(quadro);
        obstaculos.add(quadro);

        Rectangle caixas = new Rectangle();
        caixas.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(caixas);
        obstaculos.add(caixas);

        Rectangle mesa1 = new Rectangle();
        mesa1.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesa1);
        obstaculos.add(mesa1);

        Rectangle mesa2 = new Rectangle();
        mesa2.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesa2);
        obstaculos.add(mesa2);

        Rectangle mesa3 = new Rectangle();
        mesa3.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesa3);
        obstaculos.add(mesa3);

        Rectangle mesa4 = new Rectangle();
        mesa4.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesa4);
        obstaculos.add(mesa4);

        Rectangle mesa5 = new Rectangle();
        mesa5.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesa5);
        obstaculos.add(mesa5);

        Rectangle mesa6 = new Rectangle();
        mesa6.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesa6);
        obstaculos.add(mesa6);


        double largMapa = imagemMapa.getWidth();
        double altMapa = imagemMapa.getHeight();

        Rectangle bordaEsquerdaCima = new Rectangle();
        bordaEsquerdaCima.setFill(Color.rgb(0, 0, 255, 0.5));
        root.getChildren().add(bordaEsquerdaCima);
        obstaculos.add(bordaEsquerdaCima);

        Rectangle bordaEsquerdaBaixo = new Rectangle();
        bordaEsquerdaBaixo.setFill(Color.rgb(0, 0, 255, 0.5));
        root.getChildren().add(bordaEsquerdaBaixo);
        obstaculos.add(bordaEsquerdaBaixo);

        Rectangle bordaDireitaCima = new Rectangle();
        bordaDireitaCima.setFill(Color.rgb(0, 0, 255, 0.5));
        root.getChildren().add(bordaDireitaCima);
        obstaculos.add(bordaDireitaCima);

        Rectangle bordaDireitaBaixo = new Rectangle();
        bordaDireitaBaixo.setFill(Color.rgb(0, 0, 255, 0.5));
        root.getChildren().add(bordaDireitaBaixo);
        obstaculos.add(bordaDireitaBaixo);

        Rectangle bordaInferior = new Rectangle();
        bordaInferior.setFill(Color.rgb(0, 0, 255, 0.5));
        root.getChildren().add(bordaInferior);
        obstaculos.add(bordaInferior);

        Rectangle paredeTopo1 = new Rectangle();
        paredeTopo1.setFill(Color.rgb(0, 0, 255, 0.5));
        root.getChildren().add(paredeTopo1);
        obstaculos.add(paredeTopo1);

        Rectangle paredeTopo2 = new Rectangle();
        paredeTopo2.setFill(Color.rgb(0, 0, 255, 0.5));
        root.getChildren().add(paredeTopo2);
        obstaculos.add(paredeTopo2);

        transicaoCorredor2 = new Rectangle(); transicaoCorredor2.setFill(Color.rgb(0, 255, 0, 0.5));
        transicaoColegiado = new Rectangle(); transicaoColegiado.setFill(Color.rgb(0, 255, 0, 0.5));
        root.getChildren().addAll(transicaoCorredor2, transicaoColegiado);







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


            estante1.setX(mapaX + 131.0);
            estante1.setY(mapaY + 169.0);
            estante1.setWidth(159.0);
            estante1.setHeight(124.0);

            estante2.setX(mapaX + 371.0);
            estante2.setY(mapaY + 167.0);
            estante2.setWidth(190.0);
            estante2.setHeight(131.0);

            quadro.setX(mapaX + 621.0);
            quadro.setY(mapaY + 180.0);
            quadro.setWidth(223.0);
            quadro.setHeight(87.0);

            caixas.setX(mapaX + 69.0);
            caixas.setY(mapaY + 883.0);
            caixas.setWidth(178.0);
            caixas.setHeight(101.0);

            mesa1.setX(mapaX + 323.0);
            mesa1.setY(mapaY + 464.0);
            mesa1.setWidth(178.0);
            mesa1.setHeight(96.0);

            mesa2.setX(mapaX + 667.0);
            mesa2.setY(mapaY + 461.0);
            mesa2.setWidth(178.0);
            mesa2.setHeight(100.0);

            mesa3.setX(mapaX + 978.0);
            mesa3.setY(mapaY + 460.0);
            mesa3.setWidth(178.0);
            mesa3.setHeight(99.0);

            mesa4.setX(mapaX + 325.0);
            mesa4.setY(mapaY + 724.0);
            mesa4.setWidth(173.0);
            mesa4.setHeight(100.0);

            mesa5.setX(mapaX + 667.0);
            mesa5.setY(mapaY + 723.0);
            mesa5.setWidth(173.0);
            mesa5.setHeight(104.0);

            mesa6.setX(mapaX + 981.0);
            mesa6.setY(mapaY + 724.0);
            mesa6.setWidth(169.0);
            mesa6.setHeight(104.0);

            bordaEsquerdaCima.setX(mapaX + 43.0);
            bordaEsquerdaCima.setY(mapaY + 76.0);
            bordaEsquerdaCima.setWidth(20.0);
            bordaEsquerdaCima.setHeight(411.0);

            bordaEsquerdaBaixo.setX(mapaX + 44.0);
            bordaEsquerdaBaixo.setY(mapaY + 613.0);
            bordaEsquerdaBaixo.setWidth(17.0);
            bordaEsquerdaBaixo.setHeight(390.0);

            bordaDireitaCima.setX(mapaX + 1429.0);
            bordaDireitaCima.setY(mapaY + 243.0);
            bordaDireitaCima.setWidth(13.0);
            bordaDireitaCima.setHeight(230.0);

            bordaDireitaBaixo.setX(mapaX + 1427.0);
            bordaDireitaBaixo.setY(mapaY + 607.0);
            bordaDireitaBaixo.setWidth(15.0);
            bordaDireitaBaixo.setHeight(391.0);

            bordaInferior.setX(mapaX + 64.0);
            bordaInferior.setY(mapaY + 993.0);
            bordaInferior.setWidth(1358.0);
            bordaInferior.setHeight(30.0);

            paredeTopo1.setX(mapaX + 68.0);
            paredeTopo1.setY(mapaY + 75.0);
            paredeTopo1.setWidth(970.0);
            paredeTopo1.setHeight(166.0);

            paredeTopo2.setX(mapaX + 1047.0);
            paredeTopo2.setY(mapaY + 75.0);
            paredeTopo2.setWidth(367.0);
            paredeTopo2.setHeight(248.0);

            transicaoCorredor2.setX(mapaX + 34.0);
            transicaoCorredor2.setY(mapaY + 501.0);
            transicaoCorredor2.setWidth(34.0);
            transicaoCorredor2.setHeight(100.0);

            transicaoColegiado.setX(mapaX + 1408.0);
            transicaoColegiado.setY(mapaY + 494.0);
            transicaoColegiado.setWidth(31.0);
            transicaoColegiado.setHeight(97.0);





            if (playerXRel == -1) {
                if ("COLEGIADO".equals(pontoEntrada)) {
                    playerXRel = 1300.0;
                    playerYRel = 494;
                    ultimaDirecao = Direcao.ESQUERDA;
                } else {
                    playerXRel = 90.0;
                    playerYRel = 494.0;
                    ultimaDirecao = Direcao.DIREITA;
                }
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
        primaryStage.show();
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
