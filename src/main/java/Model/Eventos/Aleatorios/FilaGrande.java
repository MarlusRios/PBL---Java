package Model.Eventos.Aleatorios;

import Model.Eventos.Aleatorio;
import Model.Jogador;
import Model.Jogo;
import Controller.Relogio;

public class FilaGrande extends Aleatorio {

    public FilaGrande(){
        super(0.05);
    }

    @Override // Metodo para passar o tempo e deixar o jogador mais triste (fila grande na cantina)
    public void aplicarEvento(Jogador jogador, Jogo jogo){
        //avança 20 minutos
        Relogio.segundosTotais += (long) (20 / Relogio.tickRate);
        jogador.setMotivacao(jogador.getMotivacao() - 10);
    }
}