package Model.personagens;

import Model.Jogador;
import Model.Jogo;
import Service.JogoService;

public abstract class Interagiveis {
    private int  id;
    private String nome;
    private int posx;
    private int posy;
    private int loc;

    public Interagiveis(int id, String nome, int posx, int posy, int loc ){
        this.id = id;
        this.nome = nome;
        this.posx = posx;
        this.posy = posy;
        this.loc = loc;
    }

    public void interacao(Jogo jogo, JogoService jogoService){}

    public  void interacao (Jogador jogador){}

    public void interacao (Jogador jogador, Jogo jogo){}

    public String getNome() { return nome; }
    public int getPosx() { return posx; }
    public int getPosy() { return posy; }
    public int getLoc() { return loc; }
    public void setPosx(int posx) { this.posx = posx; }
    public void setPosy(int posy) { this.posy = posy; }
    public void setLoc(int loc) { this.loc = loc; }
    public int getId() { return id; }

}
