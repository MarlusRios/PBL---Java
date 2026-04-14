package Model.personagens;

import Model.Eventos.Aleatorio;
import Model.Eventos.Aleatorios.opressao;
import Model.Eventos.Obrigatorio;
import Model.Eventos.Obrigatorios.Aula;
import Model.Jogador;

public class Professor extends Interagiveis {

    Obrigatorio aula = new Aula();
    Aleatorio opressao = new opressao();
    public Professor (int id, String nome, int posx, int posy, int loc){
        super(id, nome, posx, posy, loc);
    }

    @Override
    public void interacao (Jogador jogador){
        if(getLoc() == 5){
            aula.aplicarEvento(jogador);
        }else if (jogador.getEnergia() >= 5) {
            jogador.setConhecimento(5.0);
            jogador.setEnergia(jogador.getEnergia() - 5.0);
            jogador.setMotivacao(jogador.getMotivacao() + 5.0);
            if (opressao.acontece()){
                opressao.aplicarEvento(jogador);
            }
        }
    }

}
