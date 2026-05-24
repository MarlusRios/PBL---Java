package Controller;

import Model.maps.Map;
import Model.personagens.Interagiveis;
import Service.InteragiveisService;
import Service.MapService;

//main para testar se o controller ta funcionando
public class MainController {
    public static void main (String[] args){
        final JogoController jogo = new JogoController();
        String  id = "1";
        Map mapa = new Map();

        // carrega e verifica
        Model.Jogo jogoCarregado = jogo.carregarPartida(id);
        System.out.println("Partida carregada: " + jogoCarregado.getId());
        System.out.println("Jogador: " + jogoCarregado.getPlayer().getNome());
        System.out.println("Semana: " + jogoCarregado.getSemana());
        System.out.println("Semestre: " + jogoCarregado.getSemestre());
    }
}
