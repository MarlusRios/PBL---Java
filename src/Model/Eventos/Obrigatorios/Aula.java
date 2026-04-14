package Model.Eventos.Obrigatorios;

import Model.Eventos.Obrigatorio;
import Model.Jogador;

public class Aula extends Obrigatorio {

    @Override
    public void aplicarEvento(Jogador jogador){
        jogador.setConhecimento(jogador.getConhecimento() + 25);
    }
}
