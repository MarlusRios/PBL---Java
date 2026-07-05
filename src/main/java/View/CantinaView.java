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

public class CantinaView extends Application {
    private final SalaController salaController = new SalaController();
    private final List<Rectangle> obstaculos = new ArrayList<>();
    private final long intervalo = 120_000_000;

    private ImageView playerView;
    private Movimento teclado;
    private Rectangle playerHitbox;
    private Rectangle blocoProfessor;
    private Pane caixaDialogo;
    private Label textoDialogo;

    public static String pontoEntrada = "CORREDOR";
    private Stage stage;
    private AnimationTimer gameLoop;
    private Rectangle transicaoCorredor1;
    private Rectangle transicaoSala;

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
        if (playerHitbox.getBoundsInParent().intersects(transicaoCorredor1.getBoundsInParent())) {
            gameLoop.stop();
            try {
                Corredor1View.pontoEntrada = "DIREITA";
                Corredor1View mapaAnterior = new Corredor1View();
                mapaAnterior.start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (playerHitbox.getBoundsInParent().intersects(transicaoSala.getBoundsInParent())) {
            gameLoop.stop();
            try {
                SalaView.pontoEntrada = "CANTINA";
                SalaView proximoMapa = new SalaView();
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

        Image imagemMapa = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Cantina.png")));
        ImageView mapa = new ImageView(imagemMapa);
        root.getChildren().add(mapa);

        // Criando o relógio
        labelRelogio = new Label("0:00");
        labelRelogio.setStyle("-fx-font-size: 24px; -fx-text-fill: white; -fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding: 5px; -fx-background-radius: 5px;");
        labelRelogio.setLayoutX(20);
        labelRelogio.setLayoutY(20);
        root.getChildren().add(labelRelogio);

        Rectangle balcaoVendedor = new Rectangle();
        balcaoVendedor.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(balcaoVendedor);

        Rectangle mesaCantina1 = new Rectangle();
        mesaCantina1.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesaCantina1);

        Rectangle mesaCantina2_A = new Rectangle();
        mesaCantina2_A.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesaCantina2_A);
        Rectangle mesaCantina2_B = new Rectangle();
        mesaCantina2_B.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesaCantina2_B);

        Rectangle mesaCantina3 = new Rectangle();
        mesaCantina3.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesaCantina3);

        Rectangle mesaCantina4_A = new Rectangle();
        mesaCantina4_A.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesaCantina4_A);
        Rectangle mesaCantina4_B = new Rectangle();
        mesaCantina4_B.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesaCantina4_B);

        Rectangle mesaCantina5 = new Rectangle();
        mesaCantina5.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesaCantina5);

        Rectangle mesaCantina6 = new Rectangle();
        mesaCantina6.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesaCantina6);

        Rectangle mesaCantina7 = new Rectangle();
        mesaCantina7.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesaCantina7);

        Rectangle mesaCantina8 = new Rectangle();
        mesaCantina8.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesaCantina8);

        Rectangle mesaCantina9_A = new Rectangle();
        mesaCantina9_A.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesaCantina9_A);
        Rectangle mesaCantina9_B = new Rectangle();
        mesaCantina9_B.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesaCantina9_B);

        Rectangle banco1 = new Rectangle();
        banco1.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(banco1);

        Rectangle banco2 = new Rectangle();
        banco2.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(banco2);


        Rectangle mesa1_A = new Rectangle();
        mesa1_A.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesa1_A);
        Rectangle mesa1_B = new Rectangle();
        mesa1_B.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesa1_B);
        Rectangle mesa1_C = new Rectangle();
        mesa1_C.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesa1_C);


        Rectangle mesa2_A = new Rectangle();
        mesa2_A.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesa2_A);
        Rectangle mesa2_B = new Rectangle();
        mesa2_B.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesa2_B);
        Rectangle mesa2_C = new Rectangle();
        mesa2_C.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesa2_C);

        Rectangle objetoCantina1 = new Rectangle();
        objetoCantina1.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(objetoCantina1);

        Rectangle objetoCantina2 = new Rectangle();
        objetoCantina2.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(objetoCantina2);

        Rectangle objetoCantina3 = new Rectangle();
        objetoCantina3.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(objetoCantina3);

        Rectangle objetoCantina4 = new Rectangle();
        objetoCantina4.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(objetoCantina4);

        Rectangle mesaCantina10 = new Rectangle();
        mesaCantina10.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesaCantina10);

        Rectangle mesaCantina11 = new Rectangle();
        mesaCantina11.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesaCantina11);

        Rectangle mesaCantina12 = new Rectangle();
        mesaCantina12.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesaCantina12);

        Rectangle mesaCantina13 = new Rectangle();
        mesaCantina13.setFill(Color.rgb(255, 0, 0, 0.5));
        root.getChildren().add(mesaCantina13);

        Rectangle bordaEsquerda = new Rectangle();
        bordaEsquerda.setFill(Color.rgb(0, 0, 255, 0.5));
        root.getChildren().add(bordaEsquerda);
        Rectangle bordaDireita = new Rectangle();
        bordaDireita.setFill(Color.rgb(0, 0, 255, 0.5));
        root.getChildren().add(bordaDireita);
        Rectangle bordaInferior = new Rectangle();
        bordaInferior.setFill(Color.rgb(0, 0, 255, 0.5));
        root.getChildren().add(bordaInferior);

        Rectangle paredeTopo1 = new Rectangle();
        paredeTopo1.setFill(Color.rgb(0, 0, 255, 0.5));
        root.getChildren().add(paredeTopo1);
        Rectangle paredeTopo2 = new Rectangle();
        paredeTopo2.setFill(Color.rgb(0, 0, 255, 0.5));
        root.getChildren().add(paredeTopo2);
        Rectangle paredeTopo3 = new Rectangle();
        paredeTopo3.setFill(Color.rgb(0, 0, 255, 0.5));
        root.getChildren().add(paredeTopo3);
        Rectangle paredeTopo4 = new Rectangle();
        paredeTopo4.setFill(Color.rgb(0, 0, 255, 0.5));
        root.getChildren().add(paredeTopo4);

        transicaoCorredor1 = new Rectangle(); transicaoCorredor1.setFill(Color.rgb(0, 255, 0, 0.5));
        transicaoSala = new Rectangle();      transicaoSala.setFill(Color.rgb(0, 255, 0, 0.5));

        root.getChildren().addAll(transicaoCorredor1, transicaoSala);

        obstaculos.clear();
        obstaculos.add(balcaoVendedor);
        obstaculos.add(mesaCantina1);
        obstaculos.add(mesaCantina2_A);
        obstaculos.add(mesaCantina2_B);
        obstaculos.add(mesaCantina3);
        obstaculos.add(mesaCantina4_A);
        obstaculos.add(mesaCantina4_B);
        obstaculos.add(mesaCantina5);
        obstaculos.add(mesaCantina6);
        obstaculos.add(mesaCantina7);
        obstaculos.add(mesaCantina8);
        obstaculos.add(mesaCantina9_A);
        obstaculos.add(mesaCantina9_B);
        obstaculos.add(banco1);
        obstaculos.add(banco2);
        obstaculos.add(mesa1_A);
        obstaculos.add(mesa1_B);
        obstaculos.add(mesa1_C);
        obstaculos.add(mesa2_A);
        obstaculos.add(mesa2_B);
        obstaculos.add(mesa2_C);
        obstaculos.add(objetoCantina1);
        obstaculos.add(objetoCantina2);
        obstaculos.add(objetoCantina3);
        obstaculos.add(objetoCantina4);
        obstaculos.add(mesaCantina10);
        obstaculos.add(mesaCantina11);
        obstaculos.add(mesaCantina12);
        obstaculos.add(mesaCantina13);
        obstaculos.add(bordaEsquerda);
        obstaculos.add(bordaDireita);
        obstaculos.add(bordaInferior);
        obstaculos.add(paredeTopo1);
        obstaculos.add(paredeTopo2);
        obstaculos.add(paredeTopo3);
        obstaculos.add(paredeTopo4);





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

            balcaoVendedor.setX(mapaX + 1333.0);
            balcaoVendedor.setY(mapaY + 288.5);
            balcaoVendedor.setWidth(266.0);
            balcaoVendedor.setHeight(425.0);

            mesaCantina1.setX(mapaX + 1289.0);
            mesaCantina1.setY(mapaY + 84.5);
            mesaCantina1.setWidth(174.0);
            mesaCantina1.setHeight(147.0);

            mesaCantina2_A.setX(mapaX + 948.0);
            mesaCantina2_A.setY(mapaY + 259.5);
            mesaCantina2_A.setWidth(117.0);
            mesaCantina2_A.setHeight(177.0);
            mesaCantina2_B.setX(mapaX + 1068.0);
            mesaCantina2_B.setY(mapaY + 234.5);
            mesaCantina2_B.setWidth(63.0);
            mesaCantina2_B.setHeight(200.0);

            mesaCantina3.setX(mapaX + 534.0);
            mesaCantina3.setY(mapaY + 43.5);
            mesaCantina3.setWidth(154.0);
            mesaCantina3.setHeight(134.0);

            mesaCantina4_A.setX(mapaX + 572.0);
            mesaCantina4_A.setY(mapaY + 261.5);
            mesaCantina4_A.setWidth(122.0);
            mesaCantina4_A.setHeight(159.0);
            mesaCantina4_B.setX(mapaX + 695.0);
            mesaCantina4_B.setY(mapaY + 240.5);
            mesaCantina4_B.setWidth(56.0);
            mesaCantina4_B.setHeight(180.0);

            mesaCantina5.setX(mapaX + 551.0);
            mesaCantina5.setY(mapaY + 461.5);
            mesaCantina5.setWidth(182.0);
            mesaCantina5.setHeight(176.0);

            mesaCantina6.setX(mapaX + 584.0);
            mesaCantina6.setY(mapaY + 657.5);
            mesaCantina6.setWidth(139.0);
            mesaCantina6.setHeight(170.0);

            mesaCantina7.setX(mapaX + 321.0);
            mesaCantina7.setY(mapaY + 665.5);
            mesaCantina7.setWidth(143.0);
            mesaCantina7.setHeight(164.0);

            mesaCantina8.setX(mapaX + 294.0);
            mesaCantina8.setY(mapaY + 460.5);
            mesaCantina8.setWidth(171.0);
            mesaCantina8.setHeight(187.0);

            mesaCantina9_A.setX(mapaX + 296.0);
            mesaCantina9_A.setY(mapaY + 260.5);
            mesaCantina9_A.setWidth(106.0);
            mesaCantina9_A.setHeight(160.0); // Era 175.0
            mesaCantina9_B.setX(mapaX + 403.0);
            mesaCantina9_B.setY(mapaY + 234.5);
            mesaCantina9_B.setWidth(54.0);
            mesaCantina9_B.setHeight(187.0);

            banco1.setX(mapaX + 13.0);
            banco1.setY(mapaY + 249.5);
            banco1.setWidth(50.0);
            banco1.setHeight(153.0);

            banco2.setX(mapaX + 14.0);
            banco2.setY(mapaY + 467.5);
            banco2.setWidth(50.0);
            banco2.setHeight(164.0);

            mesa1_A.setX(mapaX + 84.0);
            mesa1_A.setY(mapaY + 277.5);
            mesa1_A.setWidth(89.0);
            mesa1_A.setHeight(84.0);
            mesa1_B.setX(mapaX + 122.0);
            mesa1_B.setY(mapaY + 234.5);
            mesa1_B.setWidth(27.0);
            mesa1_B.setHeight(32.0);
            mesa1_C.setX(mapaX + 183.0);
            mesa1_C.setY(mapaY + 298.5);
            mesa1_C.setWidth(27.0);
            mesa1_C.setHeight(55.0);

            mesa2_A.setX(mapaX + 87.0);
            mesa2_A.setY(mapaY + 492.5);
            mesa2_A.setWidth(90.0);
            mesa2_A.setHeight(74.0);
            mesa2_B.setX(mapaX + 125.0);
            mesa2_B.setY(mapaY + 451.5);
            mesa2_B.setWidth(22.0);
            mesa2_B.setHeight(155.0);
            mesa2_C.setX(mapaX + 187.0);
            mesa2_C.setY(mapaY + 511.5);
            mesa2_C.setWidth(25.0);
            mesa2_C.setHeight(58.0);

            objetoCantina1.setX(mapaX + 627.0);
            objetoCantina1.setY(mapaY + 915.5);
            objetoCantina1.setWidth(87.0);
            objetoCantina1.setHeight(21.0);

            objetoCantina2.setX(mapaX + 810.0);
            objetoCantina2.setY(mapaY + 911.5);
            objetoCantina2.setWidth(130.0);
            objetoCantina2.setHeight(24.0);

            objetoCantina3.setX(mapaX + 1161.0);
            objetoCantina3.setY(mapaY + 913.5);
            objetoCantina3.setWidth(84.0);
            objetoCantina3.setHeight(23.0);


            objetoCantina4.setX(mapaX + 1175.0);
            objetoCantina4.setY(mapaY + 755.5);
            objetoCantina4.setWidth(71.0);
            objetoCantina4.setHeight(84.0);

            mesaCantina10.setX(mapaX + 947.0);
            mesaCantina10.setY(mapaY + 665.5);
            mesaCantina10.setWidth(162.0);
            mesaCantina10.setHeight(162.0);

            mesaCantina11.setX(mapaX + 1331.0);
            mesaCantina11.setY(mapaY + 781.5);
            mesaCantina11.setWidth(175.0);
            mesaCantina11.setHeight(147.0);

            mesaCantina12.setX(mapaX + 1561.0);
            mesaCantina12.setY(mapaY + 792.5);
            mesaCantina12.setWidth(38.0);
            mesaCantina12.setHeight(143.0);

            mesaCantina13.setX(mapaX + 1539.0);
            mesaCantina13.setY(mapaY + 91.5);
            mesaCantina13.setWidth(60.0);
            mesaCantina13.setHeight(139.0);

            double larguraMapa = 1600.0;
            double alturaMapa = 960.0;
            double alturaParedeTopo = 0.5;

            bordaEsquerda.setX(mapaX - 10);
            bordaEsquerda.setY(mapaY);
            bordaEsquerda.setWidth(10);
            bordaEsquerda.setHeight(alturaMapa);
            bordaDireita.setX(mapaX + larguraMapa);
            bordaDireita.setY(mapaY);
            bordaDireita.setWidth(10);
            bordaDireita.setHeight(alturaMapa);
            bordaInferior.setX(mapaX);
            bordaInferior.setY(mapaY + alturaMapa - 20);
            bordaInferior.setWidth(larguraMapa);
            bordaInferior.setHeight(30);

            paredeTopo1.setX(mapaX + 14.0);
            paredeTopo1.setY(mapaY + 0.5);
            paredeTopo1.setWidth(711.0);
            paredeTopo1.setHeight(39.0);
            paredeTopo2.setX(mapaX + 735.0);
            paredeTopo2.setY(mapaY + 2.5);
            paredeTopo2.setWidth(37.0);
            paredeTopo2.setHeight(68.0);
            paredeTopo3.setX(mapaX + 878.0);
            paredeTopo3.setY(mapaY + 0.5);
            paredeTopo3.setWidth(205.0);
            paredeTopo3.setHeight(102.0);
            paredeTopo4.setX(mapaX + 1192.0);
            paredeTopo4.setY(mapaY + 2.5);
            paredeTopo4.setWidth(405.0);
            paredeTopo4.setHeight(69.0);

            transicaoCorredor1.setX(mapaX + 777.0);
            transicaoCorredor1.setY(mapaY + 69.5);
            transicaoCorredor1.setWidth(91.0);
            transicaoCorredor1.setHeight(44.0);

            transicaoSala.setX(mapaX + 1090.0);
            transicaoSala.setY(mapaY + 64.5);
            transicaoSala.setWidth(96.0);
            transicaoSala.setHeight(47.0);

            if (playerXRel == -1) {
                if ("SALA".equals(pontoEntrada)) {
                    playerXRel = 1087.0;
                    playerYRel = 110.0;
                    ultimaDirecao = Direcao.BAIXO;
                } else {
                    playerXRel = 777.0;
                    playerYRel = 118;
                    ultimaDirecao = Direcao.BAIXO;
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