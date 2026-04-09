//isso aqui vai virar uma classe que a gente usa herança

package Model.maps;

import Model.personagens.Npc;

import java.util.HashMap;

public class MiniMapa {
    private int id;
    private String nome;
    private int[][] matriz;
    private HashMap<String, Npc> npcs;
    private int posInicialX;
    private int posInicialY;

    public MiniMapa(int id, String nome, int linhas, int colunas) {
        this.id = id;
        this.nome = nome;
        this.matriz = new int[linhas][colunas];
        this.npcs = new HashMap<>();
    }

    public void adicionarNpc(Npc npc) {
        String chave = npc.getPosx() + "," + npc.getPosy();
        matriz[npc.getPosy()][npc.getPosx()] = npc.getId();
        npcs.put(chave, npc);
    }


    public int getCelula(int x, int y) {
        return matriz[y][x];
    }

    public void setCelula(int x, int y, int valor) {
        matriz[y][x] = valor;
    }

    public Npc getNpcNaPosicao(int x, int y) {
        return npcs.get(x + "," + y);
    }

    public void removerNpc(int x, int y) {
        matriz[y][x] = 0;
        npcs.remove(x + "," + y);
    }

    public boolean posValid(int x, int y) {
        if (y < 0) {
            return false;
        }
        if (y >= matriz.length) {
            return false;
        }
        if (x < 0) {
            return false;
        }
        if (x >= matriz[0].length) {
            return false;
        }
        return true;
    }
}