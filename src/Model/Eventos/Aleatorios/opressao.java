package Model.Eventos.Aleatorios;

import Model.Eventos.Aleatorio;
import Model.Jogador;

public class opressao extends Aleatorio {

    public opressao(){
        super(0.1);
    }

    @Override
    public void aplicarEvento(Jogador jogador){
        jogador.setMotivacao(jogador.getMotivacao() - 20);
        jogador.setEnergia(jogador.getEnergia()- 10);
    }


}