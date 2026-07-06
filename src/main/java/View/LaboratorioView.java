package View;

import Controller.GeralController;
import Controller.LabController;
import Controller.Relogio;
import Controller.SalaController;
import Model.Jogador;
import Repository.JogoRepository;
import View.Strategy.ComportamentoMovimento;
import View.Strategy.MovimentoLivre;
import View.Strategy.MovimentoParado;
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
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LaboratorioView extends Application implements Observador {
    private final LabController labController = new LabController();
    private final List<Rectangle> obstaculos = new ArrayList<>();
    private final long intervalo = 120_000_000;
    private final GeralController geralController = new GeralController();

    private ImageView playerView;
    private Movimento teclado;
    private Rectangle playerHitbox;
    private Pane caixaDialogo;
    private Label textoDialogo;
    private ProgressBar barraEnergia;
    private Label textoBarraEnergia;
    private StackPane containerEnergia;

    private Button btnStatus;
    private AtributosView painelAtributos;

    private Rectangle blocoNpc;

    private Image[] andarFrente;
    private Image[] andarCostas;
    private Image[] andarEsquerda;
    private Image[] andarDireita;

    private Label labelRelogio;

    private int frameIndex = 0;
    private long ultimoTempoAnimacao = 0;
    private Direcao ultimaDirecao = Direcao.CIMA;
    private boolean emDialogo = false;

    public static String pontoEntrada = "CORREDOR2";
    private Stage stage;
    private AnimationTimer gameLoop;
    private Rectangle transicaoCorredor2;
    private Rectangle transicaoColegiado;

    private ComportamentoMovimento comportamentoMovimento;

    private void loopDoJogo(long tempoAtualNano) {
        double velocidad = 1.2;
        boolean estaSeMovendo = false;
        geralController.MudarTempo();

        int statusFormatura = geralController.Formatura();

        if (statusFormatura != 0) {
            gameLoop.stop();

            Pane containerPrincipal = (Pane) playerView.getParent();
            double larguraDoMapa = 1366.0;
            double alturaDoMapa = 768.0;

            if (statusFormatura == 1) {
                PassarVideo.tocar("/formaturaNormal.mp4", containerPrincipal, gameLoop, larguraDoMapa, alturaDoMapa, () -> {
                    geralController.EncerrarJogo();
                    try {
                        TelaFimDeJogoView telaFinal = new TelaFimDeJogoView();
                        telaFinal.start(stage);
                    } catch (Exception e) { e.printStackTrace(); }
                });
            } else if (statusFormatura == 2) {
                PassarVideo.tocar("/formaturaCachorro.mp4", containerPrincipal, gameLoop, larguraDoMapa, alturaDoMapa, () -> {
                    geralController.EncerrarJogo();
                    try {
                        TelaFimDeJogoView telaFinal = new TelaFimDeJogoView();
                        telaFinal.start(stage);
                    } catch (Exception e) { e.printStackTrace(); }
                });
            }
            return;
        }

        comportamentoMovimento.mover(teclado, playerView, velocidad, obstaculos, playerHitbox);

        if (teclado.isEsquerda()) { ultimaDirecao = Direcao.ESQUERDA; estaSeMovendo = true; }
        if (teclado.isDireita())  { ultimaDirecao = Direcao.DIREITA;  estaSeMovendo = true; }
        if (teclado.isCima())     { ultimaDirecao = Direcao.CIMA;     estaSeMovendo = true; }
        if (teclado.isBaixo())    { ultimaDirecao = Direcao.BAIXO;    estaSeMovendo = true; }

        double larguraPadrao = andarFrente[0].getWidth();
        double alturaPadrao = andarFrente[0].getHeight();

        playerHitbox.setX(playerView.getLayoutX() + (larguraPadrao - playerHitbox.getWidth()) / 2);
        playerHitbox.setY(playerView.getLayoutY() + (alturaPadrao - playerHitbox.getHeight()));

        if (estaSeMovendo) {
            if (tempoAtualNano - ultimoTempoAnimacao >= intervalo) {
                frameIndex++; ultimoTempoAnimacao = tempoAtualNano;
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

        if (playerHitbox.getBoundsInParent().intersects(blocoNpc.getBoundsInParent())) {
            if (!emDialogo) {
                emDialogo = true;
                caixaDialogo.setVisible(true);
                int chat = labController.baterPapo();
                if(chat == 1){
                    textoDialogo.setText("aluno: Olá! Como vai o andamento do semestre?\n\nPerde energia\nGanha conhecimnento e motivação");
                }else if(chat == 2){
                    textoDialogo.setText("Luiza: você é muito inteligente, obrigado pela dica!!\n\nGanha mais conhecimento e motivação\nPerde energia");
                }else if(chat == 0){
                    textoDialogo.setText("Energia insuficiente.");
                }
            }
        } else {
            if (emDialogo) {
                emDialogo = false;
                caixaDialogo.setVisible(false);
            }
        }

        if (playerHitbox.getBoundsInParent().intersects(transicaoCorredor2.getBoundsInParent())) {
            gameLoop.stop();
            try {
                Corredor2View.pontoEntrada = "LABORATORIO";
                Corredor2View mapaAnterior = new Corredor2View();
                mapaAnterior.start(stage);
            } catch (Exception e) { e.printStackTrace(); }
        }
        if (playerHitbox.getBoundsInParent().intersects(transicaoColegiado.getBoundsInParent())) {
            gameLoop.stop();
            try {
                ColegiadoView proximoMapa = new ColegiadoView();
                proximoMapa.start(stage);
            } catch (Exception e) { e.printStackTrace(); }
        }
        boolean statusCiclo = geralController.Atualizador();

        if (statusCiclo) {
            gameLoop.stop();

            Pane containerPrincipal = (Pane) playerView.getParent();
            double larguraDoMapa = 1366.0;
            double alturaDoMapa = 768.0;

            caixaDialogo.setVisible(true);
            textoDialogo.setText("O dia acabou! Luiza está pegando o ônibus de volta para casa...");

            PassarVideo.tocar("/AnimacaoOnibus.mp4", containerPrincipal, gameLoop, larguraDoMapa, alturaDoMapa, () -> {
                try {
                    PontoDeOnibusView.pontoEntrada = "FIM_DO_DIA";
                    PontoDeOnibusView proximoMapa = new PontoDeOnibusView();
                    proximoMapa.start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return;
        }
        labelRelogio.setText(Relogio.obterTempoFormatado());
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        Image imagemMapa = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/laboratorio.png")));
        double mapW = imagemMapa.getWidth();
        double mapH = imagemMapa.getHeight();

        Pane mundoBox = new Pane();
        mundoBox.setPrefSize(mapW, mapH);
        mundoBox.setMinSize(mapW, mapH);
        mundoBox.setMaxSize(mapW, mapH);

        Group mundoGroup = new Group(mundoBox);
        StackPane root = new StackPane(mundoGroup);
        root.setStyle("-fx-background-color: #000000;");

        root.setAlignment(javafx.geometry.Pos.CENTER);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Style.css")).toExternalForm());
        teclado = new Movimento(scene);

        comportamentoMovimento = new MovimentoLivre();

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

        criarObstaculo(693.0, 300.0, 73.0, 103.0, mundoBox);

        criarObstaculo(131.0, 169.0, 159.0, 124.0, mundoBox);
        criarObstaculo(371.0, 167.0, 190.0, 131.0, mundoBox);
        criarObstaculo(621.0, 180.0, 223.0, 87.0, mundoBox);
        criarObstaculo(69.0, 883.0, 178.0, 101.0, mundoBox);
        criarObstaculo(323.0, 464.0, 178.0, 96.0, mundoBox);
        criarObstaculo(667.0, 461.0, 178.0, 100.0, mundoBox);
        criarObstaculo(978.0, 460.0, 178.0, 99.0, mundoBox);
        criarObstaculo(325.0, 724.0, 173.0, 100.0, mundoBox);
        criarObstaculo(667.0, 723.0, 173.0, 104.0, mundoBox);
        criarObstaculo(981.0, 724.0, 169.0, 104.0, mundoBox);
        criarObstaculo(43.0, 76.0, 20.0, 411.0, mundoBox);
        criarObstaculo(44.0, 613.0, 17.0, 390.0, mundoBox);
        criarObstaculo(1429.0, 243.0, 13.0, 230.0, mundoBox);
        criarObstaculo(1427.0, 607.0, 15.0, 391.0, mundoBox);
        criarObstaculo(64.0, 993.0, 1358.0, 30.0, mundoBox);
        criarObstaculo(68.0, 75.0, 970.0, 166.0, mundoBox);
        criarObstaculo(1047.0, 75.0, 367.0, 248.0, mundoBox);

        blocoNpc = new Rectangle(670.0, 403.0, 120.0, 42.0);
        blocoNpc.setFill(Color.TRANSPARENT);
        mundoBox.getChildren().add(blocoNpc);

        transicaoCorredor2 = criarTransicao(34.0, 501.0, 34.0, 100.0, mundoBox);
        transicaoColegiado = criarTransicao(1408.0, 494.0, 31.0, 97.0, mundoBox);

        inicializarImagensAnimacao();
        playerView = new ImageView(andarFrente[0]);
        mundoBox.getChildren().add(playerView);
        playerHitbox = new Rectangle(0, 0, andarFrente[0].getWidth() * 0.8, andarFrente[0].getHeight() * 0.4);

        inicializarCaixaDialogo(mundoBox, mapW, mapH);

        Pane hud = new Pane();
        hud.setPickOnBounds(false);

        painelAtributos = new AtributosView();
        painelAtributos.setPickOnBounds(true);
        painelAtributos.setVisible(false);
        painelAtributos.setLayoutX(mapW - 250);
        painelAtributos.setLayoutY(mapH - 320);
        painelAtributos.setPrefWidth(220);

        btnStatus = new Button("Status 📊");
        btnStatus.setPickOnBounds(true);
        btnStatus.setLayoutX(mapW - 150);
        btnStatus.setLayoutY(mapH - 80);
        btnStatus.setFocusTraversable(false);

        btnStatus.setStyle(
                "-fx-background-color: #2c3e50;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Courier New';" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;" +
                        "-fx-border-color: #7f8c8d;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;" +
                        "-fx-cursor: hand;"
        );

        btnStatus.setOnAction(e -> {
            if (!painelAtributos.isVisible()) {
                painelAtributos.atualizarValores();
            }
            painelAtributos.setVisible(!painelAtributos.isVisible());
        });

        hud.getChildren().addAll(btnStatus, painelAtributos);
        mundoBox.getChildren().add(hud);

        if ("COLEGIADO".equals(pontoEntrada)) {
            playerView.setLayoutX(1300.0); playerView.setLayoutY(494); ultimaDirecao = Direcao.ESQUERDA;
        } else {
            playerView.setLayoutX(90.0); playerView.setLayoutY(494.0); ultimaDirecao = Direcao.DIREITA;
        }

        javafx.scene.transform.Scale redimensionamento = new javafx.scene.transform.Scale(1, 1);
        mundoGroup.getTransforms().setAll(redimensionamento);

        Runnable aplicarZoom = () -> {
            double janelaW = root.getWidth();
            double javaH = root.getHeight();
            if (janelaW <= 0 || javaH <= 0) return;
            double zoom = Math.min(janelaW / mapW, javaH / mapH);

            redimensionamento.setX(zoom);
            redimensionamento.setY(zoom);

            redimensionamento.setPivotX(mapW / 2.0);
            redimensionamento.setPivotY(mapH / 2.0);
        };

        root.widthProperty().addListener((obs, velho, novo) -> aplicarZoom.run());
        root.heightProperty().addListener((obs, velho, novo) -> aplicarZoom.run());

        primaryStage.setScene(scene);

        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");

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
        r.setFill(Color.TRANSPARENT);
        root.getChildren().add(r);
        obstaculos.add(r);
        return r;
    }

    private Rectangle criarTransicao(double x, double y, double w, double h, Pane root) {
        Rectangle r = new Rectangle(x, y, w, h);
        r.setFill(Color.TRANSPARENT);
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