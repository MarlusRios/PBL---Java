package Controller;

import Model.Jogador;
import Model.Jogo;
import Model.Personagens.Gato;
import Repository.JogoRepository;
import Service.JogadorService;
import Service.JogoService;

public class CorredorGatoController {
    private final JogadorService jogadorService = new JogadorService();
    private final JogoService jogoService = new JogoService();

    //metodo para verificar o resultado no carinho do gato
    public int carinho(){
        Jogo jogo = JogoRepository.getJogoAtual();
        Jogador jogador = jogo.getPlayer();
        return jogadorService.interagirInt(jogador, new Gato()); // retorna o estado em inteiro
    }
}