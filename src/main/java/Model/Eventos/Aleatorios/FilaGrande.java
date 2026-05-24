package Model.Eventos.Aleatorios;

import Model.Eventos.Aleatorio;
import Model.Jogador;
import Model.Jogo;


public class FilaGrande extends Aleatorio {

    public FilaGrande(){
        super(0.05);
    }

    @Override //metodo para passar o tempo e deixar o jogador mais triste (fila grande na cantina)
    public void aplicarEvento(Jogador jogador, Jogo jogo){
        jogo.setTime(jogo.getTime() + 0.33);
        jogador.setMotivacao(jogador.getMotivacao() - 10);
    }
}
