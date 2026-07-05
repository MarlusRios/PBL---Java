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

public class Corredor1View extends Application {
    private final SalaController salaController = new SalaController();
    private final List<Rectangle> obstaculos = new ArrayList<>();
    private final long intervalo = 120_000_000;

    private ImageView playerView;
    private Movimento teclado;
    private Rectangle playerHitbox;
    private Pane caixaDialogo;
    private Label textoDialogo;

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
        if (playerHitbox.getBoundsInParent().intersects(transicaoPonto.getBoundsInParent())) {
            gameLoop.stop();
            try {
                PontoDeOnibusView.pontoEntrada = "PORTA_CORREDOR";
                PontoDeOnibusView mapaAnterior = new PontoDeOnibusView();
                mapaAnterior.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (playerHitbox.getBoundsInParent().intersects(transicaoCantina.getBoundsInParent())) {
            gameLoop.stop();
            try {
                CantinaView.pontoEntrada = "CORREDOR";
                CantinaView proximoMapa = new CantinaView();
                proximoMapa.start(stage);
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

        Image imagemMapa = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Corredor1.png")));
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

        Rectangle arvore1 = new Rectangle();   arvore1.setFill(Color.rgb(255, 0, 0, 0.5));
        Rectangle arvore2 = new Rectangle();   arvore2.setFill(Color.rgb(255, 0, 0, 0.5));
        Rectangle arbusto1 = new Rectangle();  arbusto1.setFill(Color.rgb(255, 0, 0, 0.5));
        Rectangle poste = new Rectangle();     poste.setFill(Color.rgb(255, 0, 0, 0.5));
        Rectangle arvore3 = new Rectangle();   arvore3.setFill(Color.rgb(255, 0, 0, 0.5));
        Rectangle arbusto2 = new Rectangle();  arbusto2.setFill(Color.rgb(255, 0, 0, 0.5));
        Rectangle arvore4 = new Rectangle();   arvore4.setFill(Color.rgb(255, 0, 0, 0.5));
        Rectangle arvore5 = new Rectangle();   arvore5.setFill(Color.rgb(255, 0, 0, 0.5));
        Rectangle banco = new Rectangle(); banco.setFill(Color.rgb(255, 0, 0, 0.5));
        Rectangle parede1 = new Rectangle(); parede1.setFill(Color.rgb(0, 0, 255, 0.5));
        Rectangle parede2 = new Rectangle(); parede2.setFill(Color.rgb(0, 0, 255, 0.5));
        Rectangle parede3 = new Rectangle(); parede3.setFill(Color.rgb(0, 0, 255, 0.5));
        Rectangle parede4 = new Rectangle(); parede4.setFill(Color.rgb(0, 0, 255, 0.5));
        transicaoPonto = new Rectangle();   transicaoPonto.setFill(Color.rgb(0, 255, 0, 0.5));
        transicaoCantina = new Rectangle(); transicaoCantina.setFill(Color.rgb(0, 255, 0, 0.5));

        root.getChildren().addAll(transicaoPonto, transicaoCantina);

        root.getChildren().addAll(parede1, parede2, parede3, parede4, arvore2, arbusto1, poste, arvore3, arbusto2, arvore4, arvore5, bordaEsquerda, bordaDireita, bordaSuperior, bordaInferior,banco);

        obstaculos.clear();
        obstaculos.add(bordaEsquerda);
        obstaculos.add(bordaDireita);
        obstaculos.add(bordaSuperior);
        obstaculos.add(bordaInferior);
        obstaculos.add(arvore1);
        obstaculos.add(arvore2);
        obstaculos.add(arbusto1);
        obstaculos.add(poste);
        obstaculos.add(arvore3);
        obstaculos.add(arbusto2);
        obstaculos.add(arvore4);
        obstaculos.add(arvore5);
        obstaculos.add(banco);
        obstaculos.add(parede1);
        obstaculos.add(parede2);
        obstaculos.add(parede3);
        obstaculos.add(parede4);



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
            bordaSuperior.setHeight(70.0);

            bordaInferior.setX(mapaX);
            bordaInferior.setY(mapaY + altMapa - espessura);
            bordaInferior.setWidth(largMapa);
            bordaInferior.setHeight(espessura);

            arvore1.setX(mapaX + 184.0);
            arvore1.setY(mapaY + 20.5);
            arvore1.setWidth(159.0);
            arvore1.setHeight(249.0);

            arvore2.setX(mapaX + 544.0);
            arvore2.setY(mapaY + 31.5);
            arvore2.setWidth(105.0);
            arvore2.setHeight(205.0);

            arbusto1.setX(mapaX + 767.0);
            arbusto1.setY(mapaY + 209.5);
            arbusto1.setWidth(69.0);
            arbusto1.setHeight(62.0);

            poste.setX(mapaX + 918.0);
            poste.setY(mapaY + 62.5);
            poste.setWidth(41.0);
            poste.setHeight(226.0);

            arvore3.setX(mapaX + 1083.0);
            arvore3.setY(mapaY + 13.5);
            arvore3.setWidth(167.0);
            arvore3.setHeight(226.0);

            arbusto2.setX(mapaX + 1344.0);
            arbusto2.setY(mapaY + 131.5);
            arbusto2.setWidth(108.0);
            arbusto2.setHeight(90.0);

            arvore4.setX(mapaX + 610.0);
            arvore4.setY(mapaY + 644.5);
            arvore4.setWidth(88.0);
            arvore4.setHeight(210.0);

            arvore5.setX(mapaX + 1276.0);
            arvore5.setY(mapaY + 640.5);
            arvore5.setWidth(169.0);
            arvore5.setHeight(243.0);

            banco.setX(mapaX + 1278.0);
            banco.setY(mapaY + 235.5);
            banco.setWidth(133.0);
            banco.setHeight(17.0);

            parede1.setX(mapaX + 24.0);
            parede1.setY(mapaY + 278.5);
            parede1.setWidth(58.0);
            parede1.setHeight(95.0);

            parede2.setX(mapaX + 22.0);
            parede2.setY(mapaY + 519.5);
            parede2.setWidth(58.0);
            parede2.setHeight(110.0);

            parede3.setX(mapaX + 1574.0);
            parede3.setY(mapaY + 283.5);
            parede3.setWidth(67.0);
            parede3.setHeight(93.0);

            parede4.setX(mapaX + 1574.0);
            parede4.setY(mapaY + 520.5);
            parede4.setWidth(64.0);
            parede4.setHeight(113.0);

            transicaoPonto.setX(mapaX + 55.0);
            transicaoPonto.setY(mapaY + 380.5);
            transicaoPonto.setWidth(21.0);
            transicaoPonto.setHeight(134.0);

            transicaoCantina.setX(mapaX + 1578.0);
            transicaoCantina.setY(mapaY + 386.5);
            transicaoCantina.setWidth(19.0);
            transicaoCantina.setHeight(127.0);

            if (playerXRel == -1) {
                if ("DIREITA".equals(pontoEntrada)) {
                    playerXRel = 1460.0;
                    playerYRel = 401.0;
                    ultimaDirecao = Direcao.ESQUERDA;
                } else {
                    playerXRel = 96;
                    playerYRel = 401;
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