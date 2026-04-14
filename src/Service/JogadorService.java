package Service;

import Model.Eventos.Obrigatorios.FazerProva1;
import Model.Eventos.Obrigatorios.FazerProva2;
import Model.Jogo;
import Model.Eventos.Obrigatorio;
import Model.Jogador;
import Model.maps.MiniMapa;
import Model.personagens.Interagiveis;

import java.security.PublicKey;

public class JogadorService {
    //
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
    //metodo para interagir com os Interagiveis e aplicar suas consequencias
    public void executarInteracao(Jogador player, MiniMapa map, int x, int y) {
        if (map.posValid(x, y)) {
            if (map.getCelula(x, y) == 3) {
                Interagiveis interagiveis = map.getInterNaPosicao(x, y);
                if (interagiveis != null) {
                    interagiveis.interacao(player);
                }
            }
        }
    }

    //metodo para movimentação do jogador
    public void mover(Jogador jogador, char direcao, MiniMapa mapa){
        int x = jogador.getPosx();
        int y = jogador.getPosy();

        if (direcao == 'w' && mapa.posValid(x-1, y) && mapa.getCelula(x-1, y) == 0 ){
            jogador.setPosx(x-1);
            jogador.setSeta("cima");
            if(mapa.getCelula(x-1, y) >= 30){
                jogador.setLoc(mapa.getCelula(x-1, y) - 29);
                jogador.setPosx(3);
                jogador.setPosy(3);
            }
        } else if (direcao == 's' && mapa.posValid(x+1, y) && mapa.getCelula(x+1, y) == 0 ){
            jogador.setPosx(x+1);
            jogador.setSeta("baixo");
            if(mapa.getCelula(x-1, y) >= 30){
                jogador.setLoc(mapa.getCelula(x+1, y) - 29);
                jogador.setPosx(3);
                jogador.setPosy(3);
            }
        }else if (direcao == 'a' && mapa.posValid(x, y-1) && mapa.getCelula(x, y-1) == 0 ){
            jogador.setPosy(y-1);
            jogador.setSeta("esquerda");
            if(mapa.getCelula(x-1, y) >= 30){
                jogador.setLoc(mapa.getCelula(x, y-1) - 29);
                jogador.setPosx(3);
                jogador.setPosy(3);
            }
        }else if (direcao == 'd' && mapa.posValid(x, y+1) && mapa.getCelula(x, y+1) == 0 ){
            jogador.setPosy(y+1);
            jogador.setSeta("direita");
            if(mapa.getCelula(x-1, y) >= 30){
                jogador.setLoc(mapa.getCelula(x, y-1) - 29);
                jogador.setPosx(3);
                jogador.setPosy(3);
            }
        }
    }

    //metodo para fazer a prova
    public void Prova(Jogo jogo){
        if(jogo.getSemana() == 4)   {
            Obrigatorio prova = new FazerProva1();
            prova.aplicarEvento(jogo.getPlayer());
        }else if(jogo.getSemana() == 8){
            Obrigatorio prova = new FazerProva2();
            prova.aplicarEvento(jogo.getPlayer());
        }
    }

    public boolean naSala(Jogador jogador){
        return jogador.getLoc() == 4;
    }
}
