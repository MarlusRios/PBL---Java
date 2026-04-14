package Model.personagens;

import Model.Eventos.Aleatorio;
import Model.Eventos.Aleatorios.Aprender;
import Model.Jogador;
public class Aluno extends Interagiveis {

    Aleatorio aprender = new Aprender();
    public Aluno (int id,String nome, int posx, int posy, int loc){
        super (id, nome, posx, posy, loc);
    }

    @Override
    public void interacao(Jogador jogador){
        if (jogador.getEnergia() >= 2) {
            jogador.setConhecimento(jogador.getConhecimento() + 2.0);
            jogador.setEnergia(jogador.getEnergia() - 2.0);
            jogador.setMotivacao(jogador.getMotivacao() + 10.0);
            if(aprender.acontece()){
                aprender.aplicarEvento(jogador);
            }
        }
    }
}
