package View;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class TelaFimDeJogoView extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("BixoQuest - Fim de Jogo");

        Font pixelFontTitle = Font.loadFont(getClass().getResourceAsStream("/FontPixel.ttf"), 36);

        Label lblMensagem = new Label("Fim, Obrigado por jogar");

        if (pixelFontTitle != null) {
            lblMensagem.setFont(pixelFontTitle);
            lblMensagem.setStyle("-fx-text-fill: white;");
        } else {
            lblMensagem.setStyle("-fx-font-size: 36px; -fx-font-family: 'Courier New'; -fx-font-weight: bold; -fx-text-fill: white;");
        }

        StackPane root = new StackPane(lblMensagem);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #000000;");

        Scene scene = new Scene(root, 800, 600);

        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitHint("");

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
