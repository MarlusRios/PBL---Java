package Model.personagens;

import Model.Eventos.Aleatorio;
import Model.Eventos.Aleatorios.Arranhao;
import Model.Jogador;
public class Gato extends Interagiveis {

    Aleatorio arranhao = new Arranhao();
    public Gato (int id, int posx, int posy, int loc){
        super (id, "gato", posx, posy, loc);
    }

    @Override
    public void interacao(Jogador jogador) {
        if (jogador.getEnergia() >= 0.2) {
            jogador.setMotivacao(jogador.getMotivacao() + 0.5);
            jogador.setEnergia(jogador.getEnergia() - 0.2);
        }
        if(arranhao.acontece()){
            arranhao.aplicarEvento(jogador);
        }
    }
}