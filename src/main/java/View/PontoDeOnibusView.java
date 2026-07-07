package View;

import Controller.GeralController;
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

// tela do ponto de onibus: onde o jogo comeca, da pra ir embora ou pro corredor 1
public class PontoDeOnibusView extends Application implements Observador {
    private final SalaController salaController = new SalaController();
    private final List<Rectangle> obstaculos = new ArrayList<>();
    private final long intervalo = 120_000_000;
    private final GeralController geralController = new GeralController();

    private ImageView playerView;
    private Movimento teclado;
    private Rectangle playerHitbox;
    private Rectangle gatilhoOnibus;
    private Pane caixaDialogo;
    private Label textoDialogo;
    private ProgressBar barraEnergia;
    private Label textoBarraEnergia;
    private StackPane containerEnergia;

    private Button btnStatus;
    private AtributosView painelAtributos;

    private Stage stage;
    private AnimationTimer gameLoop;
    private Rectangle transicaoCorredor1;
    public static String pontoEntrada = "NOVO_JOGO";

    private Image[] andarFrente;
    private Image[] andarCostas;
    private Image[] andarEsquerda;
    private Image[] andarDireita;

    private Label labelRelogio;

    private int frameIndex = 0;
    private long ultimoTempoAnimacao = 0;
    private Direcao ultimaDirecao = Direcao.CIMA;
    private boolean emDialogo = false;

    private ComportamentoMovimento comportamentoMovimento;

    // game loop: cuida do tempo, se formou, anda, vaza cedo ou acaba o dia
    private void loopDoJogo(long tempoAtualNano) {
        double velocidad = 1.2;
        boolean estaSeMovendo = false;
        geralController.MudarTempo();

        // ve se formou ou virou cachorro antes de rodar o loop
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

        // passa o movimento pro strategy ativo
        comportamentoMovimento.mover(teclado, playerView, velocidad, obstaculos, playerHitbox);

        if (teclado.isEsquerda()) { ultimaDirecao = Direcao.ESQUERDA; estaSeMovendo = true; }
        if (teclado.isDireita())  { ultimaDirecao = Direcao.DIREITA;  estaSeMovendo = true; }
        if (teclado.isCima())     { ultimaDirecao = Direcao.CIMA;     estaSeMovendo = true; }
        if (teclado.isBaixo())    { ultimaDirecao = Direcao.BAIXO;    estaSeMovendo = true; }

        double larguraPadrao = andarFrente[0].getWidth();
        double alturaPadrao = andarFrente[0].getHeight();

        playerHitbox.setX(playerView.getLayoutX() + (larguraPadrao - playerHitbox.getWidth()) / 2);
        playerHitbox.setY(playerView.getLayoutY() + (alturaPadrao - playerHitbox.getHeight()));

        // muda o sprite dependendo de pra onde ta andando e do frame da animacao
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

        // troca de mapa: corredor 1
        if (playerHitbox.getBoundsInParent().intersects(transicaoCorredor1.getBoundsInParent())) {
            gameLoop.stop();
            try {
                Corredor1View.pontoEntrada = "ESQUERDA";
                Corredor1View proximoMapa = new Corredor1View();
                proximoMapa.start(stage);
            } catch (Exception e) { e.printStackTrace(); }
        }

        boolean statusCiclo = geralController.Atualizador();

        // se encostar no onibus, acaba o dia mais cedo
        if (playerHitbox.getBoundsInParent().intersects(gatilhoOnibus.getBoundsInParent())) {
            if (!emDialogo) {
                emDialogo = true;
                gameLoop.stop();

                Pane containerPrincipal = (Pane) playerView.getParent();
                double larguraDoMapa = 1366.0;
                double alturaDoMapa = 768.0;

                caixaDialogo.setVisible(true);
                textoDialogo.setText("Luiza pegou o ônibus mais cedo para voltar para casa...");

                PassarVideo.tocar("/AnimacaoOnibus.mp4", containerPrincipal, gameLoop, larguraDoMapa, alturaDoMapa, () -> {
                    try {
                        geralController.EncerrarDia();
                        PontoDeOnibusView.pontoEntrada = "FIM_DO_DIA";
                        PontoDeOnibusView proximoMapa = new PontoDeOnibusView();
                        proximoMapa.start(stage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                return;
            }
        }
        // ve se o dia acabou por falta de tempo ou energia e manda o player pra casa
        else if (statusCiclo) {
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
        } else {
            if (emDialogo) {
                emDialogo = false;
                caixaDialogo.setVisible(false);
                comportamentoMovimento = new MovimentoLivre();
            }
        }

        labelRelogio.setText(Relogio.obterTempoFormatado());
    }

    // monta o mapa, hud, player e comeca o loop
    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        Image imagemMapa = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/PontoDeOnibus.png")));
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

        // atualiza a barra de energia quando o player muda (observer)
        JogoRepository.getJogoAtual().getPlayer().adicionarObservador(this);
        atualizar();

        // obstaculos invisiveis pra colisao
        criarObstaculo(53.0, 898.5, 70.0, 82.0, mundoBox);
        criarObstaculo(178.0, 768.5, 103.0, 56.0, mundoBox);
        criarObstaculo(394.0, 733.5, 83.0, 87.0, mundoBox);
        criarObstaculo(1206.0, 762.5, 31.0, 58.0, mundoBox);
        criarObstaculo(1357.0, 900.5, 63.0, 75.0, mundoBox);
        criarObstaculo(1362.0, 593.5, 16.0, 138.0, mundoBox);
        criarObstaculo(457.0, 34.5, 53.0, 83.0, mundoBox);
        criarObstaculo(994.0, 38.5, 56.0, 86.0, mundoBox);
        criarObstaculo(1109.0, 68.5, 131.0, 59.0, mundoBox);
        criarObstaculo(1355.0, 33.5, 26.0, 136.0, mundoBox);
        criarObstaculo(0, 0, 20.0, mapH, mundoBox);
        criarObstaculo(mapW - 20.0, 0, 20.0, mapH, mundoBox);
        criarObstaculo(0, 0, mapW, 20.0, mundoBox);
        criarObstaculo(0, mapH - 20.0, mapW, 20.0, mundoBox);

        // sensores invisiveis pra trocar de mapa ou dar gatilho
        gatilhoOnibus = criarTransicao(823.0, 299.5, 317.0, 137.0, mundoBox);
        transicaoCorredor1 = criarTransicao(648.0, 982.5, 143.0, 8.0, mundoBox);

        inicializarImagensAnimacao();
        playerView = new ImageView(andarFrente[0]);
        mundoBox.getChildren().add(playerView);
        playerHitbox = new Rectangle(0, 0, andarFrente[0].getWidth() * 0.8, andarFrente[0].getHeight() * 0.4);

        inicializarCaixaDialogo(mundoBox, mapW, mapH);

        // abre e fecha o painel de atributos e atualiza os valores
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

        // poe o player no lugar certo dependendo de onde ele veio
        if ("PORTA_CORREDOR".equals(pontoEntrada)) {
            playerView.setLayoutX(670.0); playerView.setLayoutY(890.0); ultimaDirecao = Direcao.CIMA;
        } else {
            playerView.setLayoutX((mapW - andarFrente[0].getWidth()) / 2); playerView.setLayoutY(500.0); ultimaDirecao = Direcao.BAIXO;
        }

        javafx.scene.transform.Scale redimensionamento = new javafx.scene.transform.Scale(1, 1, 0, 0);
        mundoGroup.getTransforms().setAll(redimensionamento);

        // recalcula o zoom pro mapa caber na tela se redimensionar
        Runnable aplicarZoom = () -> {
            double janelaW = root.getWidth();
            double janelaH = root.getHeight();
            if (janelaW <= 0 || janelaH <= 0) return;
            double zoom = Math.min(janelaW / mapW, janelaH / mapH);
            redimensionamento.setX(zoom);
            redimensionamento.setY(zoom);
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

    // cria o quadrado invisivel de colisao e poe na lista
    private Rectangle criarObstaculo(double x, double y, double w, double h, Pane root) {
        Rectangle r = new Rectangle(x, y, w, h);
        r.setFill(Color.TRANSPARENT);
        root.getChildren().add(r);
        obstaculos.add(r);
        return r;
    }

    // cria o quadrado invisivel pra mudar de mapa
    private Rectangle criarTransicao(double x, double y, double w, double h, Pane root) {
        Rectangle r = new Rectangle(x, y, w, h);
        r.setFill(Color.TRANSPARENT);
        root.getChildren().add(r);
        return r;
    }

    // carrega os frames das animacoes de andar
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

    // cria a caixa de dialogo la embaixo
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

    // atualiza a barra de energia quando o player muda (observer)
    @Override
    public void atualizar() {
        Jogador jogador = JogoRepository.getJogoAtual().getPlayer();
        barraEnergia.setProgress(jogador.getEnergia() / 100.0);
    }

    public static void main(String[] args) { launch(args); }
}