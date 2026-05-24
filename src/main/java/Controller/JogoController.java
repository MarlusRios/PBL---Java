package Controller;

import Model.Jogo;
import Model.maps.Map;
import Repository.JogoRepository;
import Service.InteragiveisService;
import Service.JogadorService;
import Service.JogoService;
import Service.MapService;

import java.util.Collection;

public class JogoController {

    private final JogoRepository jogoRepository = new JogoRepository();
    private final JogoService jogoService = new JogoService();

    public Jogo novaPartida() {
        Map mapa = new Map();
        String id = jogoRepository.proximoId();
        Jogo jogo = jogoService.criarJogo(id, mapa);
        jogoRepository.salvar(jogo);
        return jogo;
    }

    public Jogo carregarPartida(String id) {
        jogoRepository.inicializar();
        Map mapa = new Map();
        Jogo jogo = jogoRepository.buscarPorId(id);
        if (jogo != null){
            jogo.setMapa(mapa);
        } else {
            System.out.println("criando outra partida");
            jogo = novaPartida();
        }
        return jogo;
    }

    public void salvarPartida(Jogo jogo) {
        jogoRepository.salvar(jogo);
    }

    public void deletarPartida(String id) {
        jogoRepository.deletar(id);
    }

    public Collection<Jogo> listarPartidas() {
        return jogoRepository.buscarTodos();
    }

    public void rodarJogo(Jogo jogo) throws InterruptedException {
        jogoService.rodarJogo(jogo);
    }
}