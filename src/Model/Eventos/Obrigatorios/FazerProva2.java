package Model.Eventos.Obrigatorios;

import Model.Eventos.Aleatorio;
import Model.Eventos.Aleatorios.Milagre;
import Model.Eventos.Obrigatorio;
import Model.Jogador;

public class FazerProva2 extends Obrigatorio {

    Aleatorio milagre = new Milagre();
    public FazerProva2(){
        super(8);
    }

    @Override // metodo ultilizado para calcular a nota do jogador considerando a nota anterior
    public void aplicarEvento(Jogador jogador) {
        if(milagre.acontece()){
            milagre.aplicarEvento(jogador);
        }
        jogador.setDesempenho((jogador.getDesempenho()+jogador.getConhecimento()/10)/2);
    }
}
