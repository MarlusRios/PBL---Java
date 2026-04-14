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

        public void executarInteracao (Jogador player, MiniMapa map,int x, int y){
            if (map.posValid(x, y)) {
                if (map.getCelula(x, y) == 3) {
                    Npc npc = map.getNpcNaPosicao(x, y);
                    if (npc != null) {
                        npc.interacao(player);
                    }
                    if (npc instanceof Gato && Math.random() < 0.04) {
                        ((Gato) npc).arranhao(player);
                    } else if (npc instanceof Cachorro && Math.random() < 0.03) {
                        ((Cachorro) npc).mordida(player);
                    } else if (npc instanceof Aluno && Math.random()<0.1) {
                        ((Aluno) npc).ensinar(player);
                    }
                }
            }
        }
    }

