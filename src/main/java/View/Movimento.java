package View;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class Movimento {
    private boolean cima, baixo, esquerda, direita;

    public Movimento (Scene scene){
        scene.setOnKeyPressed(e -> getTecla(e.getCode(), true));
        scene.setOnKeyReleased(e-> getTecla(e.getCode(), false));
    }

    public void getTecla(KeyCode tecla, boolean state){
        switch (tecla){
            case W:
                cima = state;
                break;

            case S:
                baixo = state;
                break;

            case D:
                direita = state;
                break;

            case A:
                esquerda = state;
                break;
        }
    }

    public boolean isCima() { return cima; }
    public boolean isBaixo() { return baixo; }
    public boolean isEsquerda() { return esquerda; }
    public boolean isDireita() { return direita; }

    public boolean isParado() {
        return !cima && !baixo && !esquerda && !direita;
    }
}
