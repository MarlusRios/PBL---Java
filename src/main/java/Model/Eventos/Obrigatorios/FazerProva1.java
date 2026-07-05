package Model.Eventos.Obrigatorios;

import Model.Eventos.Aleatorio;
import Model.Eventos.Aleatorios.Milagre;
import Model.Eventos.Obrigatorio;
import Model.Jogador;

public class FazerProva1 extends Obrigatorio {

    Aleatorio milagre = new Milagre();
    public FazerProva1(){
        super(4);
    }

    @Override // metodo ultilizado para calcular a nota do jogador
    public boolean aplicarEvento(Jogador jogador) {
        if(milagre.acontece()){
            milagre.aplicarEvento(jogador);
        }
        jogador.setDesempenho(jogador.getConhecimento()/10);
        jogador.setConhecimento(Math.min(10, jogador.getConhecimento())); //isso apenas pra verificar se o conhecimento atual é menor que 10
        return false;
    }
}
