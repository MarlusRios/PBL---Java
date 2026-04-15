package Model.Eventos.Aleatorios;

import Model.Eventos.Aleatorio;
import Model.Jogador;
public class Arranhao extends Aleatorio {

    public Arranhao(){
        super(0.04);
    }

    @Override //metodo de mudança de atributos do jogador ao acariciar um gato
    public void aplicarEvento(Jogador jogador){
        jogador.setMotivacao(jogador.getMotivacao() - 10);
        jogador.setSaude(jogador.getSaude() - 5);
        jogador.setEnergia(jogador.getEnergia()- 0.2);
    }
}
