package Model.Personagens;

import Model.Eventos.Aleatorio;
import Model.Eventos.Aleatorios.Aprender;
import Model.Jogador;
public class Aluno extends Interagiveis {

    Aleatorio aprender = new Aprender();
    public Aluno (){
        super ();
    }

    @Override //interação com o jogador e um evento aleatorio modificando os atributos do jogador
    public int interacaoInt(Jogador jogador){
        if (jogador.getEnergia() >= 5) {
            jogador.setConhecimento(jogador.getConhecimento() + 2.0);
            jogador.setEnergia(jogador.getEnergia() - 5.0);
            jogador.setMotivacao(jogador.getMotivacao() + 10.0);
            if(aprender.acontece()){
                aprender.aplicarEvento(jogador);
                return 2;
            }
            return 1;
        }
        return 0;
    }
}
