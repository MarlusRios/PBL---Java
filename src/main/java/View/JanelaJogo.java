package View;

import javafx.application.Application; // Limpo o import duplicado aqui
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.animation.AnimationTimer;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.List;

import java.util.Objects;

public class JanelaJogo extends Application {
    private ImageView playerView;
    private Movimento teclado; //classe teclado
    private final List<Rectangle> obstaculos = new ArrayList<>();
    private Rectangle playerHitbox;

    // Arrays para guardar as imagens separadas de cada movimento
    private Image[] andarFrente;
    private Image[] andarCostas;
    private Image[] andarEsquerda;
    private Image[] andarDireita;

    // Variáveis de controle de animação
    private int frameIndex = 0;
    private long ultimoTempoAnimacao = 0;
    private final long intervalo = 120_000_000; // 120 milissegundos para trocar de sprite

    // Variável para lembrar qual foi a última direção (para quando ele parar)
    private Direcao ultimaDirecao = Direcao.CIMA;

    private void loopDoJogo(long tempoAtualNano) {
        double velocidade = 1.2;
        boolean estaSeMovendo = false;

        // Criamos variáveis para registrar o movimento real deste frame
        double movimentoX = 0;
        double movimentoY = 0;

        // 1. CALCULA O MOVIMENTO (Teclas opostas vão se anular numericamente)
        if (teclado.isCima())    movimentoY -= velocidade;
        if (teclado.isBaixo())   movimentoY += velocidade;
        if (teclado.isEsquerda()) movimentoX -= velocidade;
        if (teclado.isDireita())  movimentoX += velocidade;

        // 2. APLICA O MOVIMENTO E DEFINE A DIREÇÃO REAL
        if (movimentoY < 0) {
            ultimaDirecao = Direcao.CIMA;
            estaSeMovendo = true;
        } else if (movimentoY > 0) {
            ultimaDirecao = Direcao.BAIXO;
            estaSeMovendo = true;
        }

        // Usamos 'else if' aqui para o movimento horizontal respeitar o vertical ou vice-versa
        if (movimentoX < 0) {
            ultimaDirecao = Direcao.ESQUERDA;
            estaSeMovendo = true;
        } else if (movimentoX > 0) {
            ultimaDirecao = Direcao.DIREITA;
            estaSeMovendo = true;
        }

        // Atualiza a posição física do player na tela
        playerView.setLayoutX(playerView.getLayoutX() + movimentoX);
        playerView.setLayoutY(playerView.getLayoutY() + movimentoY);


        // --- 3. PARTE DA ANIMAÇÃO DOS SPRITES (Igual ao anterior, mas agora 'estaSeMovendo' é preciso) ---
        if (estaSeMovendo) {
            if (tempoAtualNano - ultimoTempoAnimacao >= intervalo) {
                frameIndex++;
                ultimoTempoAnimacao = tempoAtualNano;

                switch (ultimaDirecao) {
                    case BAIXO:    playerView.setImage(andarFrente[frameIndex % andarFrente.length]); break;
                    case CIMA:     playerView.setImage(andarCostas[frameIndex % andarCostas.length]); break;
                    case DIREITA:  playerView.setImage(andarDireita[frameIndex % andarDireita.length]); break;
                    case ESQUERDA: playerView.setImage(andarEsquerda[frameIndex % andarEsquerda.length]); break;
                    default: break;
                }
            }
        } else {
            // Se a velocidade resultante deu 0 (parou ou cancelou), reseta para o frame estático 0
            switch (ultimaDirecao) {
                case BAIXO:    playerView.setImage(andarFrente[0]); break;
                case CIMA:     playerView.setImage(andarCostas[0]); break;
                case DIREITA:  playerView.setImage(andarDireita[0]); break;
                case ESQUERDA: playerView.setImage(andarEsquerda[0]); break;
                default: break;
            }
        }
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();

        // MODIFICAÇÃO 1: Criamos a scene sem tamanho fixo inicial no construtor
        // para ela poder se expandir dinamicamente com o Stage
        Scene scene = new Scene(root);

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Style.css")).toExternalForm());

        teclado = new Movimento(scene);

        // Carrega o mapa (fundo)
        Image imagemMapa = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/salaDeAula.png")));
        ImageView mapa = new ImageView(imagemMapa);
        root.getChildren().add(mapa);

        // Carrega os sprites (mantendo suas definições)
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

        playerView = new ImageView(andarFrente[0]);
        root.getChildren().add(playerView);

        // MODIFICAÇÃO 2: Criamos um método que reposiciona o mapa e o boneco
        // baseado na largura (w) e altura (h) ATUAIS da janela.
// Modifique apenas essa parte dentro do seu método start:
        Runnable reposicionarElementos = () -> {
            double larguraAtual = primaryStage.getWidth();
            double alturaAtual = primaryStage.getHeight();

            if (larguraAtual <= 0) larguraAtual = 800;
            if (alturaAtual <= 0) alturaAtual = 600;

            // 1. Centraliza o mapa baseado no tamanho da tela
            double mapaX = (larguraAtual - imagemMapa.getWidth()) / 2;
            double mapaY = (alturaAtual - imagemMapa.getHeight()) / 2;
            mapa.setLayoutX(mapaX);
            mapa.setLayoutY(mapaY);

            // 2. CORREÇÃO: Centraliza o boneco na tela de forma dinâmica também!
            // Assim, ele vai acompanhar o centro não importa o tamanho da janela
            double playerX = (larguraAtual - andarFrente[0].getWidth()) / 2;
            double playerY = (alturaAtual - andarFrente[0].getHeight()) / 2;
            playerView.setLayoutX(playerX);
            playerView.setLayoutY(playerY);
        };

        // MODIFICAÇÃO 3: Adicionamos "escutas" na janela. Sempre que a largura ou altura mudarem
        // (pelo botão de maximizar ou arrastando a borda), o Java roda a centralização de novo.
        primaryStage.widthProperty().addListener((obs, velho, novo) -> reposicionarElementos.run());
        primaryStage.heightProperty().addListener((obs, velho, novo) -> reposicionarElementos.run());

        // Define um tamanho padrão inicial para a janela física externa
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);

        // Executa uma vez no início para ajustar o layout inicial
        reposicionarElementos.run();

        // Caso queira testar o jogo iniciando direto em Tela Cheia real (sem bordas do windows):
        // primaryStage.setFullScreen(true);

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

    // Adicionado apenas para fins de execução direta se necessário
    public static void main(String[] args) {
        launch(args);
    }
}