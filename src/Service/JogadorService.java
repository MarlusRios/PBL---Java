package Service;

import Model.Jogador;
import Model.maps.MiniMapa;
import Model.personagens.Aluno;
import Model.personagens.Cachorro;
import Model.personagens.Gato;
import Model.personagens.Npc;

public class JogadorService {

    public void interagir(Jogador player, MiniMapa map) {

        int x = player.getPosx();
        int y = player.getPosy();

        switch (player.getSeta()) {
            case "cima" -> {
                executarInteracao(player, map, x, y - 1);
            }
            case "baixo" -> {
                executarInteracao(player, map, x, y + 1);
            }
            case "esquerda" -> {
                executarInteracao(player, map, x - 1, y);
            }
            case "direita" -> {
                executarInteracao(player, map, x + 1, y);
            }
        }
    }

    public void executarInteracao(Jogador player, MiniMapa map, int x, int y) {
        if (map.posValid(x, y)) {
            if (map.getCelula(x, y) == 3) {
                Npc npc = map.getNpcNaPosicao(x, y);
                if (npc != null) {
                    npc.interacao(player);
                }
            }
        }
    }

    public void mover(Jogador jogador, char direcao, MiniMapa mapa){
        int x = jogador.getPosx();
        int y = jogador.getPosy();

        if (direcao == 'w' && mapa.posValid(x-1, y) && mapa.getCelula(x-1, y) == 0 ){
            jogador.setPosx(x-1);
        } else if (direcao == 's' && mapa.posValid(x+1, y) && mapa.getCelula(x+1, y) == 0 ){
            jogador.setPosx(x+1);
        }else if (direcao == 'a' && mapa.posValid(x, y-1) && mapa.getCelula(x, y-1) == 0 ){
            jogador.setPosy(y-1);
        }else if (direcao == 'd' && mapa.posValid(x, y+1) && mapa.getCelula(x, y+1) == 0 ){
            jogador.setPosy(y+1);
        }
    }
}
