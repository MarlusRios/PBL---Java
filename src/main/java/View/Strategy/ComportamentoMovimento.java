package View.Strategy;

import View.Movimento;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import java.util.List;

public interface ComportamentoMovimento {
    void mover(Movimento teclado, ImageView playerView, double velocidade,
               List<Rectangle> obstaculos, Rectangle playerHitbox);
}