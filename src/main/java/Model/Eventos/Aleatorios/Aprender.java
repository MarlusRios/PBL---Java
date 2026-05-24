package Model.Eventos.Aleatorios;

import Model.Eventos.Aleatorio;
import Model.Jogador;

public class Aprender extends Aleatorio {

    public Aprender(){
        super(0.1);
    }

    @Override //metodo de mudança de atributos do jogador ao conversar com um aluno
    public void aplicarEvento(Jogador jogador){
        jogador.setConhecimento(jogador.getConhecimento()+ 10);
        jogador.setMotivacao(jogador.getMotivacao() + 20.0);
        jogador.setEnergia(jogador.getEnergia() - 4.0);
    }
}
