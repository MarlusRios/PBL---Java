package Model.Personagens;

import Model.Eventos.Aleatorio;
import Model.Eventos.Aleatorios.Mordida;
import Model.Jogador;

public class Cachorro extends Interagiveis {

    Aleatorio mordida = new Mordida();

    public Cachorro (){
        super ();
    }

    @Override // interação com o jogador e um evento aleatorio modificando os atributos do jogador
    public boolean interacaoBoolean (Jogador jogador){
        if (jogador.getEnergia() >= 0.2) {
            jogador.setMotivacao(jogador.getMotivacao() + 0.5);
            jogador.setEnergia(jogador.getEnergia()- 0.2);
            if(mordida.acontece()){
                mordida.aplicarEvento(jogador);
                return true;
            }
        }
    return false;
    }
}
