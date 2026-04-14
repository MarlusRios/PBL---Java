package Model.personagens;

import Model.Eventos.Aleatorio;
import Model.Eventos.Aleatorios.Mordida;
import Model.Jogador;

public class Cachorro extends Interagiveis {

    Aleatorio mordida = new Mordida();

    public Cachorro (int id, int posx, int posy, int loc){
        super (id, "cachorro", posx, posy, loc);
    }

    @Override
    public void interacao (Jogador jogador){
        if (jogador.getEnergia() >= 0.2) {
            jogador.setMotivacao(jogador.getMotivacao() + 0.5);
            jogador.setEnergia(jogador.getEnergia()- 0.2);
            if(mordida.acontece()){
                mordida.aplicarEvento(jogador);
            }
        }
    }
}
