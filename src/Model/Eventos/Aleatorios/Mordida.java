package Model.Eventos.Aleatorios;

import Model.Eventos.Aleatorio;
import Model.Jogador;

public class Mordida extends Aleatorio {

    public Mordida(){
        super(0.03);
    }

    @Override//metodo de mudança de atributos do jogador ao acariciar um cachorro
    public void aplicarEvento(Jogador jogador){
        jogador.setMotivacao(jogador.getMotivacao() - 10);
        jogador.setSaude(jogador.getSaude() - 5);
        jogador.setEnergia(jogador.getEnergia()- 0.2);
    }


}
