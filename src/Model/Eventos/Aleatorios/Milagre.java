package Model.Eventos.Aleatorios;

import Model.Eventos.Aleatorio;
import Model.Jogador;

public class Milagre extends Aleatorio {

    public Milagre(){
        super(0.1);
    }

    @Override
    public void aplicarEvento(Jogador jogador){
        jogador.setConhecimento(jogador.getConhecimento()+50);
    }
}
