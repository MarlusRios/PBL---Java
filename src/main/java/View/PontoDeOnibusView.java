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

public class PontoDeOnibusView extends Application {
    private final SalaController salaController = new SalaController();
    private final List<Rectangle> obstaculos = new ArrayList<>();
    private final long intervalo = 120_000_000;

    private ImageView playerView;
    private Movimento teclado;
    private Rectangle playerHitbox;
    private Pane caixaDialogo;
    private Label textoDialogo;

    private Stage stage;
    private AnimationTimer gameLoop;
    private Rectangle transicaoCorredor1;
    public static String pontoEntrada = "NOVO_JOGO";

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
        if (playerHitbox.getBoundsInParent().intersects(transicaoCorredor1.getBoundsInParent())) {
            gameLoop.stop();

            try {
                Corredor1View.pontoEntrada = "ESQUERDA";
                Corredor1View proximoMapa = new Corredor1View();
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

        Image imagemMapa = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/PontoDeOnibus.png")));
        ImageView mapa = new ImageView(imagemMapa);
        root.getChildren().add(mapa);



        Rectangle arvore1 = new Rectangle();   arvore1.setFill(Color.rgb(255, 0, 0, 0.5));
        Rectangle banco = new Rectangle();      banco.setFill(Color.rgb(255, 0, 0, 0.5));
        Rectangle vaso = new Rectangle();       vaso.setFill(Color.rgb(255, 0, 0, 0.5));
        Rectangle hidrante = new Rectangle();   hidrante.setFill(Color.rgb(255, 0, 0, 0.5));
        Rectangle arvore2 = new Rectangle();   arvore2.setFill(Color.rgb(255, 0, 0, 0.5));
        Rectangle poste1 = new Rectangle();    poste1.setFill(Color.rgb(255, 0, 0, 0.5));
        Rectangle onibus = new Rectangle();    onibus.setFill(Color.rgb(255, 0, 0, 0.5));
        Rectangle arvore3 = new Rectangle();   arvore3.setFill(Color.rgb(255, 0, 0, 0.5));
        Rectangle arvore4 = new Rectangle();   arvore4.setFill(Color.rgb(255, 0, 0, 0.5));
        Rectangle banco2 = new Rectangle();    banco2.setFill(Color.rgb(255, 0, 0, 0.5));
        Rectangle poste2 = new Rectangle();    poste2.setFill(Color.rgb(255, 0, 0, 0.5));
        Rectangle bordaEsquerda = new Rectangle();  bordaEsquerda.setFill(Color.rgb(0, 0, 255, 0.5));
        Rectangle bordaDireita = new Rectangle();   bordaDireita.setFill(Color.rgb(0, 0, 255, 0.5));
        Rectangle bordaSuperior = new Rectangle();  bordaSuperior.setFill(Color.rgb(0, 0, 255, 0.5));
        Rectangle bordaInferior = new Rectangle();  bordaInferior.setFill(Color.rgb(0, 0, 255, 0.5));
        transicaoCorredor1 = new Rectangle();     transicaoCorredor1.setFill(Color.rgb(0, 255, 0, 0.5));


        root.getChildren().addAll(poste1, onibus, arvore3, arvore4, banco2, poste2);
        root.getChildren().addAll(bordaEsquerda, bordaDireita, bordaSuperior, bordaInferior);
        root.getChildren().add(transicaoCorredor1);

        obstaculos.clear();
        obstaculos.add(arvore1);
        obstaculos.add(banco);
        obstaculos.add(vaso);
        obstaculos.add(hidrante);
        obstaculos.add(arvore2);
        obstaculos.add(poste1);
        obstaculos.add(onibus);
        obstaculos.add(arvore3);
        obstaculos.add(arvore4);
        obstaculos.add(banco2);
        obstaculos.add(poste2);
        obstaculos.add(bordaEsquerda);
        obstaculos.add(bordaDireita);
        obstaculos.add(bordaSuperior);
        obstaculos.add(bordaInferior);


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



            arvore1.setX(mapaX + 53.0);
            arvore1.setY(mapaY + 898.5);
            arvore1.setWidth(70.0);
            arvore1.setHeight(82.0);

            banco.setX(mapaX + 178.0);
            banco.setY(mapaY + 768.5);
            banco.setWidth(103.0);
            banco.setHeight(56.0);

            vaso.setX(mapaX + 394.0);
            vaso.setY(mapaY + 733.5);
            vaso.setWidth(83.0);
            vaso.setHeight(87.0);

            hidrante.setX(mapaX + 1206.0);
            hidrante.setY(mapaY + 762.5);
            hidrante.setWidth(31.0);
            hidrante.setHeight(58.0);

            arvore2.setX(mapaX + 1357.0);
            arvore2.setY(mapaY + 900.5);
            arvore2.setWidth(63.0);
            arvore2.setHeight(75.0);

            poste1.setX(mapaX + 1362.0);
            poste1.setY(mapaY + 593.5);
            poste1.setWidth(16.0);
            poste1.setHeight(138.0);

            onibus.setX(mapaX + 823.0);
            onibus.setY(mapaY + 299.5);
            onibus.setWidth(317.0);
            onibus.setHeight(137.0);

            arvore3.setX(mapaX + 457.0);
            arvore3.setY(mapaY + 34.5);
            arvore3.setWidth(53.0);
            arvore3.setHeight(83.0);

            arvore4.setX(mapaX + 994.0);
            arvore4.setY(mapaY + 38.5);
            arvore4.setWidth(56.0);
            arvore4.setHeight(86.0);

            banco2.setX(mapaX + 1109.0);
            banco2.setY(mapaY + 68.5);
            banco2.setWidth(131.0);
            banco2.setHeight(59.0);

            poste2.setX(mapaX + 1355.0);
            poste2.setY(mapaY + 33.5);
            poste2.setWidth(26.0);
            poste2.setHeight(136.0);

            double largMapa = imagemMapa.getWidth();
            double altMapa = imagemMapa.getHeight();
            double espessura = 20.0;

            bordaEsquerda.setX(mapaX);
            bordaEsquerda.setY(mapaY);
            bordaEsquerda.setWidth(espessura);
            bordaEsquerda.setHeight(altMapa);

            bordaDireita.setX(mapaX + largMapa - espessura);
            bordaDireita.setY(mapaY);
            bordaDireita.setWidth(espessura);
            bordaDireita.setHeight(altMapa);

            bordaSuperior.setX(mapaX);
            bordaSuperior.setY(mapaY);
            bordaSuperior.setWidth(largMapa);
            bordaSuperior.setHeight(espessura);

            bordaInferior.setX(mapaX);
            bordaInferior.setY(mapaY + altMapa - espessura);
            bordaInferior.setWidth(largMapa);
            bordaInferior.setHeight(espessura);

            transicaoCorredor1.setX(mapaX + 648.0);
            transicaoCorredor1.setY(mapaY + 982.5);
            transicaoCorredor1.setWidth(143.0);
            transicaoCorredor1.setHeight(8.0);

            if (playerXRel == -1) {
                if ("PORTA_CORREDOR".equals(pontoEntrada)) {
                    playerXRel = 670.0;
                    playerYRel = 890.0;
                    ultimaDirecao = Direcao.CIMA;
                } else {
                    playerXRel = (imagemMapa.getWidth() - andarFrente[0].getWidth()) / 2;
                    playerYRel = 500.0;
                    ultimaDirecao = Direcao.BAIXO;
                }
            }

            playerView.setLayoutX(mapaX + playerXRel);
            playerView.setLayoutY(mapaY + playerYRel);

            caixaDialogo.setLayoutX((larguraAtual - caixaDialogo.getPrefWidth()) / 2);
            caixaDialogo.setLayoutY(alturaAtual - caixaDialogo.getPrefHeight() - 40);
        };

        primaryStage.widthProperty().addListener((obs, velho, novo) -> reposicionarElementos.run());
        primaryStage.heightProperty().addListener((obs, velho, novo) -> reposicionarElementos.run());

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