package Controller;

import Model.Jogador;
import Model.Jogo;
import Model.Personagens.Aluno;
import Repository.JogoRepository;
import Service.JogadorService;
import Service.JogoService;

public class LabController {
    private final JogadorService jogadorService = new JogadorService();
    private final JogoService jogoService = new JogoService();

    //metodo para verificar o resultado da conversa com o aluno no laboratorio
    public int baterPapo(){
        Jogo jogo = JogoRepository.getJogoAtual();
        Jogador jogador = jogo.getPlayer();
        return jogadorService.interagirInt(jogador, new Aluno()); // retorna o estado da conversa em inteiro
    }
}
