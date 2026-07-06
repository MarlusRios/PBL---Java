package View;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import java.util.Objects;

public class PassarVideo {

    public static void tocar(String caminhoVideo, Pane mundoBox, AnimationTimer gameLoop,
                             double larguraMapa, double alturaMapa, Runnable onTermino) {
        try {
            if (gameLoop != null) {
                gameLoop.stop();
            }

            String url = Objects.requireNonNull(PassarVideo.class.getResource(caminhoVideo)).toExternalForm();
            Media media = new Media(url);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);

            mediaView.setFitWidth(larguraMapa);
            mediaView.setFitHeight(alturaMapa);
            mediaView.setPreserveRatio(true);

            mediaView.layoutXProperty().bind(mundoBox.widthProperty().subtract(mediaView.fitWidthProperty()).divide(2));
            mediaView.layoutYProperty().bind(mundoBox.heightProperty().subtract(mediaView.fitHeightProperty()).divide(2));

            mundoBox.getChildren().add(mediaView);

            mediaPlayer.setOnEndOfMedia(() -> {
                Platform.runLater(() -> {
                    mediaPlayer.dispose();
                    mundoBox.getChildren().remove(mediaView);

                    // Se houver uma próxima ação (como outro vídeo), executa ela aqui!
                    if (onTermino != null) {
                        onTermino.run();
                    } else if (gameLoop != null) {
                        // Se não houver mais vídeos na fila, destrava o jogo
                        gameLoop.start();
                    }
                });
            });

            mediaPlayer.setMute(true); // Rodando no mudo como você pediu!
            mediaPlayer.play();

        } catch (Exception e) {
            System.err.println("Erro ao tentar reproduzir o vídeo: " + e.getMessage());
            if (gameLoop != null) {
                gameLoop.start();
            }
        }
    }
}