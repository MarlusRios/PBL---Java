package Model.personagens;

import Model.Eventos.Aleatorio;
import Model.Eventos.Aleatorios.opressao;
import Model.Eventos.Obrigatorio;
import Model.Eventos.Obrigatorios.Aula;
import Model.Jogador;

public class Professor extends Interagiveis {

    Obrigatorio aula = new Aula();
    Aleatorio opressao = new opressao();
    public Professor (){
        super();
    }

    @Override // interação com o professor contendo dois eventos, um aleatorio e um obrigatorio modificando os atributos do jogador
    public void interacao (Jogador jogador){

        aula.aplicarEvento(jogador);
        jogador.setConhecimento(5.0);
        jogador.setEnergia(jogador.getEnergia() - 5.0);
        jogador.setMotivacao(jogador.getMotivacao() + 5.0);
        if (opressao.acontece()){
            opressao.aplicarEvento(jogador);
        }
    }
}

