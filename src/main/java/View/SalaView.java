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

public class SalaView extends Application {
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

    public static String pontoEntrada = "CANTINA";
    private Stage stage;
    private AnimationTimer gameLoop;
    private Rectangle transicaoCantina;
    private Rectangle transicaoCorredor2;

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

        if (playerHitbox.getBoundsInParent().intersects(blocoProfessor.getBoundsInParent())) {
            if (!emDialogo) {
                emDialogo = true;
                caixaDialogo.setVisible(true);
                textoDialogo.setText("Professor: Luiza, que bom que chegou! Pronto para apresentar o projeto? \n\nAtributos Modificados");
                salaController.Conversar();
            }
            estaSeMovendo = false;
        } else {
            if (emDialogo) {
                emDialogo = false;
                caixaDialogo.setVisible(false);
            }
        }

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
        if (playerHitbox.getBoundsInParent().intersects(transicaoCantina.getBoundsInParent())) {
            gameLoop.stop();
            try {
                CantinaView.pontoEntrada = "SALA";
                CantinaView mapaAnterior = new CantinaView();
                mapaAnterior.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (playerHitbox.getBoundsInParent().intersects(transicaoCorredor2.getBoundsInParent())) {
            gameLoop.stop();
            try {
                Corredor2View proximoMapa = new Corredor2View();
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

        Image imagemMapa = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/salaDeAula.png")));
        ImageView mapa = new ImageView(imagemMapa);
        root.getChildren().add(mapa);

        blocoProfessor = new Rectangle();
        blocoProfessor.setFill(Color.TRANSPARENT);
        root.getChildren().add(blocoProfessor);

        Rectangle mesasEsquerdaCima = new Rectangle();
        Rectangle mesasDireitaCima = new Rectangle();
        Rectangle mesasEsquerdaMeio = new Rectangle();
        Rectangle mesasDireitaMeio = new Rectangle();
        Rectangle mesasEsquerdaBaixo = new Rectangle();
        Rectangle mesasDireitaBaixo = new Rectangle();
        Rectangle paredeEsquerda = new Rectangle();
        Rectangle paredeDireita = new Rectangle();
        Rectangle tetoEsquerda = new Rectangle(); tetoEsquerda.setFill(Color.rgb(0, 255, 0, 0.5));
        Rectangle tetoDireita = new Rectangle();  tetoDireita.setFill(Color.rgb(0, 255, 0, 0.5));
        Rectangle limiteBaixoEsquerda = new Rectangle();  limiteBaixoEsquerda.setFill(Color.rgb(0, 255, 0, 0.5));
        Rectangle limiteBaixoDireita = new Rectangle();  limiteBaixoDireita.setFill(Color.rgb(0, 255, 0, 0.5));

        transicaoCantina = new Rectangle();   transicaoCantina.setFill(Color.rgb(0, 255, 0, 0.5));
        transicaoCorredor2 = new Rectangle(); transicaoCorredor2.setFill(Color.rgb(0, 255, 0, 0.5));

        root.getChildren().addAll(transicaoCantina, transicaoCorredor2);

        obstaculos.clear();
        obstaculos.add(mesasEsquerdaCima);
        obstaculos.add(mesasDireitaCima);
        obstaculos.add(mesasEsquerdaMeio);
        obstaculos.add(mesasDireitaMeio);
        obstaculos.add(mesasEsquerdaBaixo);
        obstaculos.add(mesasDireitaBaixo);
        obstaculos.add(paredeEsquerda);
        obstaculos.add(paredeDireita);
        obstaculos.add(tetoEsquerda);
        obstaculos.add(tetoDireita);
        obstaculos.add(limiteBaixoDireita);
        obstaculos.add(limiteBaixoEsquerda);

        root.getChildren().addAll(tetoEsquerda, tetoDireita, limiteBaixoEsquerda, limiteBaixoDireita);

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

            blocoProfessor.setX(mapaX + 775);
            blocoProfessor.setY(mapaY + 55);
            blocoProfessor.setWidth(100);
            blocoProfessor.setHeight(120);

            mesasEsquerdaCima.setX(mapaX + 136);
            mesasEsquerdaCima.setY(mapaY + 214.5);
            mesasEsquerdaCima.setWidth(481);
            mesasEsquerdaCima.setHeight(112);

            mesasDireitaCima.setX(mapaX + 798);
            mesasDireitaCima.setY(mapaY + 216.5);
            mesasDireitaCima.setWidth(467);
            mesasDireitaCima.setHeight(111);

            mesasEsquerdaMeio.setX(mapaX + 136);
            mesasEsquerdaMeio.setY(mapaY + 380);
            mesasEsquerdaMeio.setWidth(481);
            mesasEsquerdaMeio.setHeight(112);

            mesasDireitaMeio.setX(mapaX + 798);
            mesasDireitaMeio.setY(mapaY + 380);
            mesasDireitaMeio.setWidth(467);
            mesasDireitaMeio.setHeight(111);

            mesasEsquerdaBaixo.setX(mapaX + 136);
            mesasEsquerdaBaixo.setY(mapaY + 550);
            mesasEsquerdaBaixo.setWidth(481);
            mesasEsquerdaBaixo.setHeight(112);

            mesasDireitaBaixo.setX(mapaX + 798);
            mesasDireitaBaixo.setY(mapaY + 550);
            mesasDireitaBaixo.setWidth(467);
            mesasDireitaBaixo.setHeight(111);

            paredeEsquerda.setX(mapaX + 0);
            paredeEsquerda.setY(mapaY + 0);
            paredeEsquerda.setWidth(48);
            paredeEsquerda.setHeight(800);

            paredeDireita.setX(mapaX + 1360);
            paredeDireita.setY(mapaY + 0);
            paredeDireita.setWidth(100);
            paredeDireita.setHeight(800);

            tetoEsquerda.setX(mapaX + 0);
            tetoEsquerda.setY(mapaY + 0);
            tetoEsquerda.setWidth(656);
            tetoEsquerda.setHeight(133);

            tetoDireita.setX(mapaX + 749);
            tetoDireita.setY(mapaY + 0);
            tetoDireita.setWidth(751);
            tetoDireita.setHeight(133);

            limiteBaixoEsquerda.setX(mapaX + 0);
            limiteBaixoEsquerda.setY(mapaY + 680);
            limiteBaixoEsquerda.setWidth(658);
            limiteBaixoEsquerda.setHeight(200);

            limiteBaixoDireita.setX(mapaX + 748);
            limiteBaixoDireita.setY(mapaY + 680);
            limiteBaixoDireita.setWidth(752);
            limiteBaixoDireita.setHeight(200);

            transicaoCantina.setX(mapaX + 658.0);
            transicaoCantina.setY(mapaY + 690.0);
            transicaoCantina.setWidth(90.0);
            transicaoCantina.setHeight(36.0);


            transicaoCorredor2.setX(mapaX + 656.0);
            transicaoCorredor2.setY(mapaY + 103.5);
            transicaoCorredor2.setWidth(93.0);
            transicaoCorredor2.setHeight(31.0);

            if (playerXRel == -1) {
                if ("CORREDOR2".equals(pontoEntrada)) {
                    playerXRel = 660.0;
                    playerYRel = 140.0;
                    ultimaDirecao = Direcao.BAIXO;
                } else {

                    playerXRel = 660.0;
                    playerYRel = 580.0;
                    ultimaDirecao = Direcao.CIMA;
                }
            }

            playerView.setLayoutX(mapaX + playerXRel);
            playerView.setLayoutY(mapaY + playerYRel);

            caixaDialogo.setLayoutX((larguraAtual - caixaDialogo.getPrefWidth()) / 2);
            caixaDialogo.setLayoutY(alturaAtual - caixaDialogo.getPrefHeight() - 40);
        };

        primaryStage.widthProperty().addListener((obs, velho, novo) -> reposicionarElementos.run());
        primaryStage.heightProperty().addListener((obs, velho, novo) -> reposicionarElementos.run());

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