package Controller;

import Model.Jogo;
import Repository.JogoRepository;
import Service.JogoService;

public class GeralController {
    private final JogoService jogoService = new JogoService();
    private final JogoController jogoController = new JogoController();

    public boolean Atualizador(){
        Jogo jogo = JogoRepository.getJogoAtual();
        boolean choose = jogoService.atualizarCicloJogo(jogo);
        if (choose){
            jogoController.salvarPartida(jogo);
            return true;
        }
        return false;
    }

    public void EncerrarDia(){
        Jogo jogo = JogoRepository.getJogoAtual();
        jogoService.encerrarDia(jogo);
        jogoController.salvarPartida(jogo);
    }

    public void MudarTempo(){
        Relogio.incrementarTempo();
    }

    public int Formatura(){
        Jogo jogo = JogoRepository.getJogoAtual();
        return jogoService.verificarFormatura(jogo);
    }

    public void EncerrarJogo(){
        Jogo jogo = JogoRepository.getJogoAtual();
        jogoService.encerrarJogo(jogo);
        jogoController.deletarPartida(jogo.getId());
    }

}
