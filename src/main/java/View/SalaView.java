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

    private void loopDoJogo(long tempoAtualNano) {
        double velocidade = 2.0;
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

        double proximoX = playerView.getLayoutX() + movimentoX;
        playerHitbox.setX(proximoX + (playerView.getBoundsInLocal().getWidth() - playerHitbox.getWidth()) / 2);
        playerHitbox.setY(playerView.getLayoutY() + (playerView.getBoundsInLocal().getHeight() - playerHitbox.getHeight()));

        boolean colidiuX = false;
        for (Rectangle obs : obstaculos) {
            if (playerHitbox.getBoundsInParent().intersects(obs.getBoundsInParent())) {
                colidiuX = true;
                break;
            }
        }
        if (!colidiuX && movimentoX != 0) {
            playerView.setLayoutX(proximoX);
            if (movimentoX < 0) ultimaDirecao = Direcao.ESQUERDA;
            if (movimentoX > 0) ultimaDirecao = Direcao.DIREITA;
            estaSeMovendo = true;
        }

        double proximoY = playerView.getLayoutY() + movimentoY;
        playerHitbox.setX(playerView.getLayoutX() + (playerView.getBoundsInLocal().getWidth() - playerHitbox.getWidth()) / 2);
        playerHitbox.setY(proximoY + (playerView.getBoundsInLocal().getHeight() - playerHitbox.getHeight()));

        boolean colidiuY = false;
        for (Rectangle obs : obstaculos) {
            if (playerHitbox.getBoundsInParent().intersects(obs.getBoundsInParent())) {
                colidiuY = true;
                break;
            }
        }
        if (!colidiuY && movimentoY != 0) {
            playerView.setLayoutY(proximoY);
            if (movimentoY < 0) ultimaDirecao = Direcao.CIMA;
            if (movimentoY > 0) ultimaDirecao = Direcao.BAIXO;
            estaSeMovendo = true;
        }

        playerHitbox.setX(playerView.getLayoutX() + (playerView.getBoundsInLocal().getWidth() - playerHitbox.getWidth()) / 2);
        playerHitbox.setY(playerView.getLayoutY() + (playerView.getBoundsInLocal().getHeight() - playerHitbox.getHeight()));

        if (playerHitbox.getBoundsInParent().intersects(blocoProfessor.getBoundsInParent())) {
            if (!emDialogo) {
                emDialogo = true;
                caixaDialogo.setVisible(true);
                textoDialogo.setText("Professor: Luiza, que bom que chegou! Pronto para apresentar o projeto? \n\nAtributos Modificados");
                salaController.Conversar(jogo);
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
    }

    @Override
    public void start(Stage primaryStage) {
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
        Rectangle tetoCima = new Rectangle();
        Rectangle limiteBaixo = new Rectangle();

        obstaculos.clear();
        obstaculos.add(mesasEsquerdaCima);
        obstaculos.add(mesasDireitaCima);
        obstaculos.add(mesasEsquerdaMeio);
        obstaculos.add(mesasDireitaMeio);
        obstaculos.add(mesasEsquerdaBaixo);
        obstaculos.add(mesasDireitaBaixo);
        obstaculos.add(paredeEsquerda);
        obstaculos.add(paredeDireita);
        obstaculos.add(tetoCima);
        obstaculos.add(limiteBaixo);

        inicializarImagensAnimacao();

        playerView = new ImageView(andarFrente[0]);
        root.getChildren().add(playerView);
        playerHitbox = new Rectangle(0, 0, andarFrente[0].getWidth() * 0.8, andarFrente[0].getHeight() * 0.4);

        inicializarCaixaDialogo(root);

        Runnable reposicionarElementos = () -> {
            double larguraAtual = primaryStage.getWidth() <= 0 ? 800 : primaryStage.getWidth();
            double alturaAtual = primaryStage.getHeight() <= 0 ? 600 : primaryStage.getHeight();

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

            tetoCima.setX(mapaX + 0);
            tetoCima.setY(mapaY + 0);
            tetoCima.setWidth(1500);
            tetoCima.setHeight(133);

            limiteBaixo.setX(mapaX + 0);
            limiteBaixo.setY(mapaY + 680);
            limiteBaixo.setWidth(1500);
            limiteBaixo.setHeight(200);

            playerView.setLayoutX((larguraAtual - andarFrente[0].getWidth()) / 2);
            playerView.setLayoutY((alturaAtual - andarFrente[0].getHeight()) / 2);

            caixaDialogo.setLayoutX((larguraAtual - caixaDialogo.getPrefWidth()) / 2);
            caixaDialogo.setLayoutY(alturaAtual - caixaDialogo.getPrefHeight() - 40);
        };

        primaryStage.widthProperty().addListener((obs, velho, novo) -> reposicionarElementos.run());
        primaryStage.heightProperty().addListener((obs, velho, novo) -> reposicionarElementos.run());

        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        reposicionarElementos.run();

        mapa.setOnMouseClicked(e -> System.out.println("X: " + e.getX() + " | Y: " + e.getY()));

        AnimationTimer gameLoop = new AnimationTimer() {
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