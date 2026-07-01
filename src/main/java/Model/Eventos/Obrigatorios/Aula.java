package Model.Eventos.Obrigatorios;

import Model.Eventos.Obrigatorio;
import Model.Jogador;
import Model.Jogo;

public class Aula extends Obrigatorio {

    @Override
    public void aplicarEvento(Jogador jogador, Jogo jogo){
        if (jogo.getTime() > 8.5 && jogo.getTime() < 10.5){
            jogador.setConhecimento(jogador.getConhecimento() + 25);
            jogador.setEnergia(jogador.getEnergia() - 30);
        }
    }
}
