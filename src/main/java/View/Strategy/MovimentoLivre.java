package View.Strategy;

import View.Movimento;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import java.util.List;

public class MovimentoLivre implements ComportamentoMovimento {

    @Override
    public void mover(Movimento teclado, ImageView playerView, double velocidade,
                      List<Rectangle> obstaculos, Rectangle playerHitbox) {

        double movimentoX = 0;
        double movimentoY = 0;

        if (teclado.isCima())    movimentoY -= velocidade;
        if (teclado.isBaixo())   movimentoY += velocidade;
        if (teclado.isEsquerda()) movimentoX -= velocidade;
        if (teclado.isDireita())  movimentoX += velocidade;

        double larguraPadrao = playerView.getImage().getWidth();
        double alturaPadrao = playerView.getImage().getHeight();

        // Lógica de colisão X
        double proximoX = playerView.getLayoutX() + movimentoX;
        playerHitbox.setX(proximoX + (larguraPadrao - playerHitbox.getWidth()) / 2);
        playerHitbox.setY(playerView.getLayoutY() + (alturaPadrao - playerHitbox.getHeight()));

        boolean colidiuX = false;
        for (Rectangle obs : obstaculos) {
            if (playerHitbox.getBoundsInParent().intersects(obs.getBoundsInParent())) {
                colidiuX = true; break;
            }
        }
        if (!colidiuX && movimentoX != 0) playerView.setLayoutX(proximoX);

        // Lógica de colisão Y
        double proximoY = playerView.getLayoutY() + movimentoY;
        playerHitbox.setX(playerView.getLayoutX() + (larguraPadrao - playerHitbox.getWidth()) / 2);
        playerHitbox.setY(proximoY + (alturaPadrao - playerHitbox.getHeight()));

        boolean colidiuY = false;
        for (Rectangle obs : obstaculos) {
            if (playerHitbox.getBoundsInParent().intersects(obs.getBoundsInParent())) {
                colidiuY = true; break;
            }
        }
        if (!colidiuY && movimentoY != 0) playerView.setLayoutY(proximoY);
    }
}