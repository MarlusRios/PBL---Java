package Model.Personagens;

import Model.Eventos.Aleatorio;
import Model.Eventos.Aleatorios.Arranhao;
import Model.Jogador;

public class Gato extends Interagiveis {

    Aleatorio arranhao = new Arranhao();
    public Gato (){
        super ();
    }

    @Override // interação com o jogador e tendo um evento aleatorio modificando os atributos do jogador
    public int interacaoInt(Jogador jogador) {
        if (jogador.getEnergia() >= 0.2) {
            jogador.setMotivacao(jogador.getMotivacao() + 0.5);
            jogador.setEnergia(jogador.getEnergia() - 0.2);
            if(arranhao.acontece()) {
                arranhao.aplicarEvento(jogador);
                return 2;
            }
            return 1;
        }
        return 0;
    }
}