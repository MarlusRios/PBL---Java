package View;

import Controller.GeralController;
import Controller.SalaController;
import Controller.Relogio;
import Model.Jogador;
import Repository.JogoRepository;
import View.Strategy.ComportamentoMovimento;
import View.Strategy.MovimentoLivre;
import View.Strategy.MovimentoParado;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SalaView extends Application implements Observador {
    private final SalaController salaController = new SalaController();
    private final List<Rectangle> obstaculos = new ArrayList<>();
    private final long intervalo = 120_000_000;
    private final GeralController geralController = new GeralController();

    private ImageView playerView;
    private Movimento teclado;
    private Rectangle playerHitbox;
    private Rectangle blocoProfessor;
    private Pane caixaDialogo;
    private Label textoDialogo;
    private ProgressBar barraEnergia;
    private Label textoBarraEnergia;
    private StackPane containerEnergia;

    private Image[] andarFrente;
    private Image[] andarCostas;
    private Image[] andarEsquerda;
    private Image[] andarDireita;

    private Label labelRelogio;

    private int frameIndex = 0;
    private long ultimoTempoAnimacao = 0;
    private Direcao ultimaDirecao = Direcao.CIMA;
    private boolean emDialogo = false;

    public static String pontoEntrada = "CANTINA";
    private Stage stage;
    private AnimationTimer gameLoop;
    private Rectangle transicaoCantina;
    private Rectangle transicaoCorredor2;

    // Atributo do Padrão Strategy
    private ComportamentoMovimento comportamentoMovimento;

    private void loopDoJogo(long tempoAtualNano) {
        double velocidade = 1.2;
        boolean estaSeMovendo = false;
        geralController.MudarTempo();

        // 1. Delega a movimentação para a estratégia ativa (Strategy)
        comportamentoMovimento.mover(teclado, playerView, velocidade, obstaculos, playerHitbox);

        // 2. Atualiza a direção da animação baseada no teclado
        if (teclado.isEsquerda()) { ultimaDirecao = Direcao.ESQUERDA; estaSeMovendo = true; }
        if (teclado.isDireita())  { ultimaDirecao = Direcao.DIREITA;  estaSeMovendo = true; }
        if (teclado.isCima())     { ultimaDirecao = Direcao.CIMA;     estaSeMovendo = true; }
        if (teclado.isBaixo())    { ultimaDirecao = Direcao.BAIXO;    estaSeMovendo = true; }

        double larguraPadrao = andarFrente[0].getWidth();
        double alturaPadrao = andarFrente[0].getHeight();

        // Sincroniza a hitbox com a posição atual após o movimento
        playerHitbox.setX(playerView.getLayoutX() + (larguraPadrao - playerHitbox.getWidth()) / 2);
        playerHitbox.setY(playerView.getLayoutY() + (alturaPadrao - playerHitbox.getHeight()));

        // 3. Interação com o Professor (Com trava de repetição automática)
        if (playerHitbox.getBoundsInParent().intersects(blocoProfessor.getBoundsInParent())) {
            // SÓ fala com o professor se a caixa de diálogo NÃO estiver ativa na tela
            if (!emDialogo) {
                int chat = salaController.conversar();
                emDialogo = true;
                caixaDialogo.setVisible(true);
                //necessarias para passar o video
                Pane containerPrincipal = (Pane) playerView.getParent();
                double larguraDoMapa = 1366.0;
                double alturaDoMapa = 768.0;

                if (chat == 1) {
                    textoDialogo.setText("Professor: Luiza, que bom que chegou! Pronto para apresentar o projeto? \n\nEnergia - 5\n motivação +5");
                } else if (chat == 2) {
                    textoDialogo.setText("Professor: Luiza, voce é muito burra e vai repetir a materia! \n\nEnergia -10\nMotivação -20");
                } else if (chat == 3) {
                    textoDialogo.setText("Professor: Bom dia luiza, prota pra aula? \n\nO tempo passou\nconhecimento +25\nenergia -30");
                } else if (chat == 0) {
                    textoDialogo.setText("Energia insuficiente");
                } else {
                    textoDialogo.setText("Professor: Bom luiza, prova iniciada...");

// 1. Pausa o movimento mudando a estratégia para ela não andar durante o vídeo
                    comportamentoMovimento = new MovimentoParado();

                    // 2. Chama o vídeo
                    PassarVideo.tocar("/FazendoProva.mp4", containerPrincipal, gameLoop, larguraDoMapa, alturaDoMapa, () -> {

                        // ADAPTAÇÃO: Altera o cálculo para ir direto para as 19:00 (Fim do dia)
                        // 660 minutos totais a partir das 08:00 do início do dia
                        Controller.Relogio.segundosTotais = (long) (660 / Relogio.tickRate);

                        // 3. Depois que o tempo mudou, o jogo checa se passou ou perdeu
                        if (salaController.passou()) {
                            textoDialogo.setText("Professor: Bom luiza, prova concluida, parabens!!");
                            PassarVideo.tocar("/Passando.mp4", containerPrincipal, gameLoop, larguraDoMapa, alturaDoMapa, null);

                            // ADAPTAÇÃO: Restaura o movimento mantendo o 'null' que seu método exige
                            comportamentoMovimento = new MovimentoLivre();
                        } else {
                            textoDialogo.setText("Professor: Bom luiza, prova concluida, que pena que voce perdeu...");
                            PassarVideo.tocar("/Perdendo.mp4", containerPrincipal, gameLoop, larguraDoMapa, alturaDoMapa, null);

                            // ADAPTAÇÃO: Faz o mesmo aqui caso ela perca
                            comportamentoMovimento = new MovimentoLivre();
                        }
                    });
                }
            }
        } else {
            //dialogo sai se o jogador sair de perto
            if (emDialogo) {
                emDialogo = false;
                caixaDialogo.setVisible(false);
                comportamentoMovimento = new MovimentoLivre();
            }
        }

        //Animação do Sprite
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

        //Transição de Cenário
        if (playerHitbox.getBoundsInParent().intersects(transicaoCantina.getBoundsInParent())) {
            gameLoop.stop();
            try {
                CantinaView.pontoEntrada = "SALA";
                CantinaView mapaAnterior = new CantinaView();
                mapaAnterior.start(stage);
            } catch (Exception e) { e.printStackTrace(); }
        }

        if (playerHitbox.getBoundsInParent().intersects(transicaoCorredor2.getBoundsInParent())) {
            gameLoop.stop();
            try {
                Corredor2View.pontoEntrada = "SALA";
                Corredor2View proximoMapa = new Corredor2View();
                proximoMapa.start(stage);
            } catch (Exception e) { e.printStackTrace(); }
        }

        boolean statusCiclo = geralController.Atualizador();

        if (statusCiclo) {
            gameLoop.stop(); // Para tudo imediatamente!

            Pane containerPrincipal = (Pane) playerView.getParent();
            double larguraDoMapa = 1366.0;
            double alturaDoMapa = 768.0;

            caixaDialogo.setVisible(true);
            textoDialogo.setText("O dia acabou! Luiza está pegando o ônibus de volta para o campus...");

            // Toca o vídeo do ônibus e te joga para o Ponto de Ônibus ao terminar
            PassarVideo.tocar("/AnimacaoOnibus.mp4", containerPrincipal, gameLoop, larguraDoMapa, alturaDoMapa, () -> {
                try {
                    PontoDeOnibusView.pontoEntrada = "FIM_DO_DIA";
                    PontoDeOnibusView proximoMapa = new PontoDeOnibusView();
                    proximoMapa.start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            return; // Corta a execução do frame para evitar bugs visuais
        }
        labelRelogio.setText(Relogio.obterTempoFormatado());
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        Image imagemMapa = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/salaDeAula.png")));
        double mapW = imagemMapa.getWidth();
        double mapH = imagemMapa.getHeight();

        Pane mundoBox = new Pane();
        mundoBox.setPrefSize(mapW, mapH);
        mundoBox.setMinSize(mapW, mapH);
        mundoBox.setMaxSize(mapW, mapH);

        Group mundoGroup = new Group(mundoBox);
        StackPane root = new StackPane(mundoGroup);
        root.setStyle("-fx-background-color: #000000;");

        // Alinha todo o conteúdo no Topo-Esquerdo da janela
        root.setAlignment(javafx.geometry.Pos.TOP_LEFT);

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Style.css")).toExternalForm());
        teclado = new Movimento(scene);

        // Inicializa a estratégia de movimento padrão (Livre)
        comportamentoMovimento = new MovimentoLivre();

        ImageView mapa = new ImageView(imagemMapa);
        mundoBox.getChildren().add(mapa);

        blocoProfessor = new Rectangle(775, 55, 100, 120);
        blocoProfessor.setFill(Color.TRANSPARENT);
        mundoBox.getChildren().add(blocoProfessor);

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

        criarObstaculo(136, 214.5, 481, 112, mundoBox);
        criarObstaculo(798, 216.5, 467, 111, mundoBox);
        criarObstaculo(136, 380, 481, 112, mundoBox);
        criarObstaculo(798, 380, 467, 111, mundoBox);
        criarObstaculo(136, 550, 481, 112, mundoBox);
        criarObstaculo(798, 550, 467, 111, mundoBox);
        criarObstaculo(0, 0, 48, 800, mundoBox);
        criarObstaculo(1360, 0, 100, 800, mundoBox);
        criarObstaculo(0, 0, 656, 133, mundoBox);
        criarObstaculo(749, 0, 751, 133, mundoBox);
        criarObstaculo(0, 680, 658, 200, mundoBox);
        criarObstaculo(748, 680, 752, 200, mundoBox);

        transicaoCantina = criarTransicao(658.0, 690.0, 90.0, 36.0, mundoBox);
        transicaoCorredor2 = criarTransicao(656.0, 103.5, 93.0, 31.0, mundoBox);

        inicializarImagensAnimacao();
        playerView = new ImageView(andarFrente[0]);
        mundoBox.getChildren().add(playerView);
        playerHitbox = new Rectangle(0, 0, andarFrente[0].getWidth() * 0.8, andarFrente[0].getHeight() * 0.4);

        inicializarCaixaDialogo(mundoBox, mapW, mapH);

        if ("CORREDOR2".equals(pontoEntrada)) {
            playerView.setLayoutX(660.0); playerView.setLayoutY(140.0); ultimaDirecao = Direcao.BAIXO;
        } else {
            playerView.setLayoutX(660.0); playerView.setLayoutY(580.0); ultimaDirecao = Direcao.CIMA;
        }

        // Cria uma transformação de escala explícita com pivô cravado em (0,0)
        javafx.scene.transform.Scale redimensionamento = new javafx.scene.transform.Scale(1, 1, 0, 0);
        mundoGroup.getTransforms().setAll(redimensionamento);

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