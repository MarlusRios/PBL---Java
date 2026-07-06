package Controller;

import Model.Jogo;
import Repository.JogoRepository;
import Service.JogoService;

public class GeralController {
    private final JogoService jogoService = new JogoService();

    public boolean Atualizador(){
        Jogo jogo = JogoRepository.getJogoAtual();
        boolean choose = jogoService.atualizarCicloJogo(jogo);
        if (choose){
            return true;
        }
        return false;
    }

    public void EncerrarDia(){
        Jogo jogo = JogoRepository.getJogoAtual();
        jogoService.encerrarDia(jogo);
    }

    public void MudarTempo(){
        Relogio.incrementarTempo();
    }

}
