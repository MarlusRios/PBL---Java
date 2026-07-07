package Model.Personagens;

import Model.Eventos.Aleatorio;
import Model.Eventos.Aleatorios.opressao;
import Model.Eventos.Obrigatorios.Aula;
import Model.Jogador;

public class Professor extends Interagiveis {

    private final Aula aula = new Aula();
    private final Aleatorio opressao = new opressao();
    public Professor (){
        super();
    }

    @Override // interação com o professor contendo dois eventos, um aleatorio e um obrigatorio modificando os atributos do jogador
    public int interacaoInt(Jogador jogador){

        boolean isClass = aula.aplicarEvento(jogador);
        if (isClass){
            return 3;
        }
        if(jogador.getEnergia() >= 5 ){
        jogador.setEnergia(jogador.getEnergia() - 5.0);
        jogador.setMotivacao(jogador.getMotivacao() + 5.0);
        if (opressao.acontece()) {
            opressao.aplicarEvento(jogador);
            return 2;
        }
        return 1;
        }
    return 0;
    }
}

