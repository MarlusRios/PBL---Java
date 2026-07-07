package View;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

// gerenciador de entradas do teclado para controlar os estados de movimento do jogador
public class Movimento {
    // estados das teclas de movimentação (true se pressionada, false se solta)
    private boolean cima, baixo, esquerda, direita;

    // configura os ouvintes de evento (listeners) para capturar o pressionamento das teclas na cena
    public Movimento (Scene scene){
        scene.setOnKeyPressed(e -> getTecla(e.getCode(), true));
        scene.setOnKeyReleased(e-> getTecla(e.getCode(), false));
    }

    // mapeia as teclas WASD para os respectivos estados booleanos de direção
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

    // metodos getters para consulta do loop de jogo
    public boolean isCima() { return cima; }
    public boolean isBaixo() { return baixo; }
    public boolean isEsquerda() { return esquerda; }
    public boolean isDireita() { return direita; }

    // verifica se o jogador está completamente parado (nenhuma tecla de movimento ativa)
    public boolean isParado() {
        return !cima && !baixo && !esquerda && !direita;
    }
}