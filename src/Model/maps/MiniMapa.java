//isso aqui vai virar uma classe que a gente usa herança//

package Model.maps;

import Model.personagens.Interagiveis;

import java.util.HashMap;

public class MiniMapa {
    private int id;
    private String nome;
    private int[][] matriz;
    private HashMap<String, Interagiveis> interagiveis;
    private int posInicialX;
    private int posInicialY;

    public MiniMapa(int id, int linhas, int colunas) {
        this.id = id;
        this.matriz = new int[linhas][colunas];
        this.interagiveis = new HashMap<>();
    }

    public void adicionarInter(Interagiveis interagiveis) {
        String chave = interagiveis.getPosx() + "," + interagiveis.getPosy();
        matriz[interagiveis.getPosy()][interagiveis.getPosx()] = interagiveis.getId();
        this.interagiveis.put(chave, interagiveis);
    }


    public int getCelula(int x, int y) {
        return matriz[y][x];
    }

    public int getId(){ return id; }

    public void setNome(String nome){ this.nome = nome; }

    public String getNome(){ return nome; }

    public void setCelula(int x, int y, int valor) {
        matriz[y][x] = valor;
    }

    public Interagiveis getInterNaPosicao(int x, int y) {
        return interagiveis.get(x + "," + y);
    }

    public void removerInter(int x, int y) {
        matriz[y][x] = 0;
        interagiveis.remove(x + "," + y);
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