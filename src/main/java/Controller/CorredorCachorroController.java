package Controller;

import Model.Jogador;
import Model.Jogo;
import Model.Personagens.Cachorro;
import Model.Personagens.Gato;
import Repository.JogoRepository;
import Service.JogadorService;
import Service.JogoService;

public class CorredorCachorroController {
    private final JogadorService jogadorService = new JogadorService();
    private final JogoService jogoService = new JogoService();

    public int carinho(){
        Jogo jogo = JogoRepository.getJogoAtual();
        Jogador jogador = jogo.getPlayer();
        return jogadorService.interagirInt(jogador, new Cachorro());
    }
}
