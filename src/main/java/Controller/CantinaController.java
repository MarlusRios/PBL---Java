package Controller;

import Model.Jogador;
import Model.Jogo;
import Model.Personagens.Gato;
import Model.Personagens.VendedorCantina;
import Repository.JogoRepository;
import Service.JogadorService;
import Service.JogoService;

public class CantinaController {
    private final JogadorService jogadorService = new JogadorService();
    private final JogoService jogoService = new JogoService();

    public int comprar(){
        Jogo jogo = JogoRepository.getJogoAtual();
        Jogador jogador = jogo.getPlayer();
        return jogadorService.interagirInt(jogador, new VendedorCantina());
    }
}
