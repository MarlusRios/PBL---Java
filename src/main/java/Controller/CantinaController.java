package Controller;

import Model.Jogador;
import Model.Jogo;
import Model.Personagens.VendedorCantina;
import Repository.JogoRepository;

public class CantinaController {

    public int comprarLanche() {
        Jogo jogo = JogoRepository.getJogoAtual();
        if (jogo == null) return 0;

        Jogador jogador = jogo.getPlayer();

        VendedorCantina vendedor = new VendedorCantina();
        return vendedor.interacaoInt(jogador, jogo);
    }
}