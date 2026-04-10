package Service;

import Model.Jogador;
import Model.maps.MiniMapa;

public class JogadorService {

    public void interagir(Jogador player, MiniMapa map){

        switch (player.getSeta()) {
            case "cima" -> {
                if (map.getCelula(player.getPosx(), player.getPosy() - 1) == 3) {
                    map.getNpcNaPosicao(player.getPosx(), player.getPosy() - 1).interacao(player);
                }
                else if (map.getCelula(player.getPosx(), player.getPosy() - 1) == 2){
                    //colocar local fixo (cantina ou colegiado)//
                }
            }
            case "baixo" -> {
                if (map.getCelula(player.getPosx(), player.getPosy()+ 1) == 3) {
                    map.getNpcNaPosicao(player.getPosx(), player.getPosy() + 1).interacao(player);
                }
            }
            case "esquerda" -> {
                if (map.getCelula(player.getPosx() - 1, player.getPosy()) == 3) {
                    map.getNpcNaPosicao(player.getPosx() - 1, player.getPosy()).interacao(player);
                }
            }
            case "direita" -> {
                if (map.getCelula(player.getPosx() + 1, player.getPosy()) == 3) {
                    map.getNpcNaPosicao(player.getPosx() + 1, player.getPosy()).interacao(player);
                }
            }
        }
    }

}

