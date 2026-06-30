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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import Controller.JogoController;
import Model.Jogo;


public class MainView extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("BixoQuest");

        JogoController jogoController = new JogoController();

        //carrega a fonte que eu coloquei
        Font pixelFontTitle = Font.loadFont(getClass().getResourceAsStream("/FontPixel.ttf"), 48);
        Font pixelFontMedium = Font.loadFont(getClass().getResourceAsStream("/FontPixel.ttf"), 24);
        Font pixelFontButtons = Font.loadFont(getClass().getResourceAsStream("/FontPixel.ttf"), 20);

        VBox root = new VBox();
        root.setStyle("-fx-background-color: cornflowerblue;");

        root.setAlignment(Pos.CENTER);

        root.setSpacing(25);
//        Label title = new Label("BixoQuest");
//        title.setFont(pixelFontTitle);

        Label title = new Label("BixoQuest");

        //a fonte ta dando erro, então fiz uma verificação pra caso n ache o arquivo abrir com a fotne padrão
        if (pixelFontTitle != null) {
            title.setFont(pixelFontTitle);
        } else {
            // Se cair aqui, o jogo abre normalmente com a fonte padrão e te avisa o porquê
            System.out.println("AVISO: O arquivo FontPixel.ttf não foi encontrado. Usando fonte padrão.");
            title.setStyle("-fx-font-size: 48px; -fx-font-weight: bold;");
        }






        //botoes
        Image imagemBotao = new Image(getClass().getResourceAsStream("/botao.png"));
        Button btnNovoJogo = new Button("Novo Jogo");

        //pra adicionar a imagem no fundo do botao
        ImageView viewNovoJogo = new ImageView(imagemBotao);

        viewNovoJogo.setFitWidth(300);
        viewNovoJogo.setPreserveRatio(true);
        btnNovoJogo.setGraphic(viewNovoJogo);
        btnNovoJogo.setContentDisplay(ContentDisplay.CENTER);

        btnNovoJogo.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-cursor: hand;");

//        btnNovoJogo.setOnAction(event -> {
//            VBox telaNome = new VBox();
//            telaNome.setAlignment(Pos.CENTER);
//            telaNome.setSpacing(30);
//
//            telaNome.setStyle("-fx-background-color: #149ca8;");
//
//            Label lblPergunta = new Label("Qual o nome desse save?");
//            lblPergunta.setStyle("-fx-font-size: 36px; -fx-font-family: 'Courier New'; -fx-font-weight: bold; -fx-text-fill: black;");
//
//            TextField txtNome = new TextField();
//            txtNome.setPromptText("Digite aqui...");
//            txtNome.setMaxWidth(400);
//            txtNome.setStyle("-fx-font-size: 20px; -fx-font-family: 'Courier New'; -fx-background-radius: 25; -fx-border-radius: 25; -fx-border-color: #0c575e; -fx-border-width: 3; -fx-alignment: center;");
//
//            telaNome.getChildren().addAll(lblPergunta, txtNome);
//
//
//            root.getScene().setRoot(telaNome);
//        });


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
            txtNome.setStyle(
                    "-fx-background-color: transparent; " +
                            "-fx-text-fill: black; " +
                            "-fx-alignment: center;"
            );
            txtNome.setMaxWidth(320);


            txtNome.setOnAction(eventEnter -> {
                String nomeSave = txtNome.getText().trim();


                if (!nomeSave.isEmpty()) {
                    //novoJogo.getPlayer().setNome(nomeSave);
                    Jogo novoJogo = jogoController.novaPartida(nomeSave);
                    //jogoController.salvarPartida(novoJogo);

                    System.out.println("Save gerado. ID: " + novoJogo.getId());

                    VBox telaSucesso = new VBox();
                    telaSucesso.setAlignment(Pos.CENTER);
                    telaSucesso.setStyle("-fx-background-color: cornflowerblue;");

                    Label lblSucesso = new Label("Save '" + nomeSave + "' criado com sucesso!");
                    if (pixelFontMedium != null) lblSucesso.setFont(pixelFontMedium);

                    telaSucesso.getChildren().add(lblSucesso);
                    root.getScene().setRoot(telaSucesso);
                }
            });


            StackPane grupoInput = new StackPane();
            grupoInput.getChildren().addAll(viewCaixa, txtNome);

            conteudoCentro.getChildren().addAll(lblPergunta, grupoInput);


            // logica do botão de voltar (X)
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

            btnVoltar.setOnAction(eventVoltar -> {
                telaNomeRoot.getScene().setRoot(root);
            });

            telaNomeRoot.getChildren().addAll(conteudoCentro, btnVoltar);

            root.getScene().setRoot(telaNomeRoot);
        });


        //BOTAO CARREGAR JOGO

        Button btnCarregarJogo = new Button("Carregar Jogo");
        btnCarregarJogo.setOnAction(event -> {
            StackPane telaCarregarRoot = new StackPane();
            telaCarregarRoot.setStyle("-fx-background-color: cornflowerblue;");

            VBox conteudoCentro = new VBox();
            conteudoCentro.setAlignment(Pos.CENTER);
            conteudoCentro.setSpacing(30);

            Label lblTitulo = new Label("Saves disponíveis: ");
            if (pixelFontMedium != null) {
                lblTitulo.setFont(pixelFontMedium);
            }

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

                        VBox telaJogo = new VBox();
                        telaJogo.setAlignment(Pos.CENTER);
                        telaJogo.setStyle("-fx-background-color: cornflowerblue;");

                        Label lblJogando = new Label("Entrando no mundo com: " + nomeSave);
                        if (pixelFontMedium != null) lblJogando.setFont(pixelFontMedium);

                        telaJogo.getChildren().add(lblJogando);
                        telaCarregarRoot.getScene().setRoot(telaJogo);
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

            btnVoltar.setOnAction(eventVoltar -> {
                telaCarregarRoot.getScene().setRoot(root);
            });

            telaCarregarRoot.getChildren().addAll(conteudoCentro, btnVoltar);
            root.getScene().setRoot(telaCarregarRoot);
        });








        //teste pra ver a distancia dos botoes
        VBox caixaBotoes = new VBox();
        caixaBotoes.setAlignment(Pos.CENTER);

        caixaBotoes.setSpacing(1);

        caixaBotoes.getChildren().addAll(btnNovoJogo, btnCarregarJogo);

        root.getChildren().addAll(title, caixaBotoes);

        //pra botar a imagem no fundo do botao
        ImageView viewCarregar = new ImageView(imagemBotao);

        viewCarregar.setFitWidth(360);
        viewCarregar.setPreserveRatio(true);
        btnCarregarJogo.setGraphic(viewCarregar);
        btnCarregarJogo.setContentDisplay(ContentDisplay.CENTER);
        btnCarregarJogo.setStyle("-fx-background-color: transparent; -fx-padding: 0; -fx-cursor: hand;");


        btnNovoJogo.setFont(pixelFontButtons);
        btnCarregarJogo.setFont(pixelFontButtons);

        Scene scene = new Scene(root, 800, 450);
        primaryStage.setScene(scene);
        primaryStage.show();


    }
}
