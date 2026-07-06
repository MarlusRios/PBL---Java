package Model.Personagens;

import Model.Eventos.Aleatorio;
import Model.Eventos.Aleatorios.FilaGrande;
import Model.Jogador;
import Model.Jogo;
import Repository.JogoRepository;

public class VendedorCantina extends Interagiveis {

    Aleatorio filaGrande = new FilaGrande();
    public VendedorCantina (){
        super();
    }

    @Override // interação com o vendedor da cantina modificando os atributos do jogador
    public int interacaoInt(Jogador jogador){
        Jogo jogo = JogoRepository.getJogoAtual();

        if(jogador.getDinheiro() >= 20) {
            jogador.setSaude(jogador.getSaude() + 10);
            jogador.setEnergia(jogador.getEnergia() + 10);
            jogador.setDinheiro(jogador.getDinheiro() - 20);
            jogador.setMotivacao(jogador.getMotivacao() + 10);
            if (filaGrande.acontece()) {
                filaGrande.aplicarEvento(jogador, jogo);
                return 2;
            }
            return 1;
        }
    return 0;
    }

}
