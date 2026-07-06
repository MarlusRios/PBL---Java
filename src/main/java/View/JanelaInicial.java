package View;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ContentDisplay;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Pane;
import Repository.JogoRepository;

import Controller.JogoController;
import Model.Jogo;

public class JanelaInicial extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("BixoQuest");

        JogoController jogoController = new JogoController();

        Font pixelFontTitle = Font.loadFont(getClass().getResourceAsStream("/FontPixel.ttf"), 48);
        Font pixelFontMedium = Font.loadFont(getClass().getResourceAsStream("/FontPixel.ttf"), 24);
        Font pixelFontButtons = Font.loadFont(getClass().getResourceAsStream("/FontPixel.ttf"), 20);

        VBox root = new VBox();
        root.setStyle("-fx-background-color: cornflowerblue;");
        root.setAlignment(Pos.CENTER);
        root.setSpacing(25);

        Label title = new Label("BixoQuest");
        if (pixelFontTitle != null) {
            title.setFont(pixelFontTitle);
        } else {
            System.out.println("AVISO: O arquivo FontPixel.ttf não foi encontrado. Usando fonte padrão.");
            title.setStyle("-fx-font-size: 48px; -fx-font-weight: bold;");
        }

        // Criamos a cena principal logo no início para usá-la com segurança nos lambdas
        Scene scene = new Scene(root, 800, 450);

        Image imagemBotao = new Image(getClass().getResourceAsStream("/botao.png"));
        Button btnNovoJogo = new Button("Novo Jogo");
        ImageView viewNovoJogo = new ImageView(imagemBotao);
        viewNovoJogo.setFitWidth(300);
        viewNovoJogo.setPreserveRatio(true);
        btnNovoJogo.setGraphic(viewNovoJogo);
        btnNovoJogo.setContentDisplay(ContentDisplay.CENTER);
        btnNovoJogo.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-cursor: hand;");

        btnNovoJogo.setOnAction(event -> {
            StackPane telaNomeRoot = new StackPane();
            telaNomeRoot.setStyle("-fx-background-color: cornflowerblue;");

            VBox conteudoCentro = new VBox();
            conteudoCentro.setAlignment(Pos.CENTER);
            conteudoCentro.setSpacing(30);

            Label lblPergunta = new Label("Qual o nome desse save?");
            if (pixelFontMedium != null) {
                lblPergunta.setFont(pixelFontMedium);
            }

            ImageView viewCaixa = new ImageView(imagemBotao);
            viewCaixa.setFitWidth(380);
            viewCaixa.setPreserveRatio(true);

            TextField txtNome = new TextField();
            txtNome.setPromptText("Digite aqui...");
            if (pixelFontButtons != null) {
                txtNome.setFont(pixelFontButtons);
            }
            txtNome.setStyle("-fx-background-color: transparent; -fx-text-fill: black; -fx-alignment: center;");
            txtNome.setMaxWidth(320);

            txtNome.setOnAction(eventEnter -> {
                String nomeSave = txtNome.getText().trim();

                if (!nomeSave.isEmpty()) {
                    Jogo novoJogo = jogoController.novaPartida(nomeSave);
                    System.out.println("Save gerado. ID: " + novoJogo.getId());

                    // --- TRANSIÇÃO DIRETAMENTE PARA O PONTO DE ÔNIBUS (NOVO JOGO) ---
                    try {
                        // 1. Avisa o Ponto de Ônibus que é um jogo novinho do zero
                        PontoDeOnibusView.pontoEntrada = "NOVO_JOGO";

                        // 2. Instancia a View do Ponto de Ônibus
                        PontoDeOnibusView pontoDeOnibus = new PontoDeOnibusView();

                        // 3. Passa o controle do Stage atual para iniciar o mapa e o game loop
                        pontoDeOnibus.start(primaryStage);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            StackPane grupoInput = new StackPane();
            grupoInput.getChildren().addAll(viewCaixa, txtNome);
            conteudoCentro.getChildren().addAll(lblPergunta, grupoInput);

            Image imgX = new Image(getClass().getResourceAsStream("/botaoX.png"));
            ImageView viewX = new ImageView(imgX);
            viewX.setFitWidth(40);
            viewX.setPreserveRatio(true);

            Button btnVoltar = new Button();
            btnVoltar.setGraphic(viewX);
            btnVoltar.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-cursor: hand;");
            StackPane.setAlignment(btnVoltar, Pos.TOP_LEFT);
            btnVoltar.setTranslateX(20);
            btnVoltar.setTranslateY(20);

            btnVoltar.setOnAction(eventVoltar -> scene.setRoot(root));

            telaNomeRoot.getChildren().addAll(conteudoCentro, btnVoltar);
            scene.setRoot(telaNomeRoot);
        });

        Button btnCarregarJogo = new Button("Carregar Jogo");
        btnCarregarJogo.setOnAction(event -> {
            StackPane telaCarregarRoot = new StackPane();
            telaCarregarRoot.setStyle("-fx-background-color: cornflowerblue;");

            VBox conteudoCentro = new VBox();
            conteudoCentro.setAlignment(Pos.CENTER);
            conteudoCentro.setSpacing(30);

            Label lblTitulo = new Label("Saves disponíveis: ");
            if (pixelFontMedium != null) lblTitulo.setFont(pixelFontMedium);

            VBox listaSavesVBox = new VBox();
            listaSavesVBox.setAlignment(Pos.CENTER);
            listaSavesVBox.setSpacing(15);

            java.util.Collection<Jogo> partidasSalvas = jogoController.listarPartidas();

            if (partidasSalvas != null && !partidasSalvas.isEmpty()) {
                for (Jogo partida : partidasSalvas) {
                    String nomeSave = partida.getId();
                    String idPartida = partida.getId();

                    Button btnSave = new Button(nomeSave);
                    if (pixelFontButtons != null) btnSave.setFont(pixelFontButtons);

                    ImageView texturaBotao = new ImageView(imagemBotao);
                    texturaBotao.setFitWidth(360);
                    texturaBotao.setPreserveRatio(true);

                    btnSave.setGraphic(texturaBotao);
                    btnSave.setContentDisplay(ContentDisplay.CENTER);
                    btnSave.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-cursor: hand;");

                    btnSave.setOnAction(eventCliqueSave -> {
                        Jogo jogoCarregado = jogoController.carregarPartida(idPartida);
                        System.out.println("Partida carregada! ID: " + jogoCarregado.getId());

                        // --- TRANSIÇÃO PARA A SALA VIA LOADING OU DIRETA ---
                        try {
                            SalaView sala = new SalaView();
                            primaryStage.setWidth(1024);
                            primaryStage.setHeight(768);
                            sala.start(primaryStage);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });

                    listaSavesVBox.getChildren().add(btnSave);
                }
            } else {
                Label lblBandeira = new Label("Nenhum save encontrado.");
                if (pixelFontButtons != null) lblBandeira.setFont(pixelFontButtons);
                listaSavesVBox.getChildren().add(lblBandeira);
            }

            conteudoCentro.getChildren().addAll(lblTitulo, listaSavesVBox);

            Image imgX = new Image(getClass().getResourceAsStream("/botaoX.png"));
            ImageView viewX = new ImageView(imgX);
            viewX.setFitWidth(40);
            viewX.setPreserveRatio(true);

            Button btnVoltar = new Button();
            btnVoltar.setGraphic(viewX);
            btnVoltar.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-cursor: hand;");
            StackPane.setAlignment(btnVoltar, Pos.TOP_LEFT);
            btnVoltar.setTranslateX(20);
            btnVoltar.setTranslateY(20);

            // CORREÇÃO: Usa a variável 'scene' direta
            btnVoltar.setOnAction(eventVoltar -> scene.setRoot(root));

            telaCarregarRoot.getChildren().addAll(conteudoCentro, btnVoltar);
            scene.setRoot(telaCarregarRoot);
        });

        VBox caixaBotoes = new VBox();
        caixaBotoes.setAlignment(Pos.CENTER);
        caixaBotoes.setSpacing(1);
        caixaBotoes.getChildren().addAll(btnNovoJogo, btnCarregarJogo);

        root.getChildren().addAll(title, caixaBotoes);

        ImageView viewCarregar = new ImageView(imagemBotao);
        viewCarregar.setFitWidth(360);
        viewCarregar.setPreserveRatio(true);
        btnCarregarJogo.setGraphic(viewCarregar);
        btnCarregarJogo.setContentDisplay(ContentDisplay.CENTER);
        btnCarregarJogo.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-cursor: hand;");

        btnNovoJogo.setFont(pixelFontButtons);
        btnCarregarJogo.setFont(pixelFontButtons);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}