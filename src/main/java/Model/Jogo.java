package Model;


import java.io.Serial;
import java.io.Serializable;

public class Jogo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String id;
    private double time;
    private int semana;
    private int semestre;
    private Jogador player;

    public Jogo(String id) {
        this.id = id;
        this.player = new Jogador();
        this.time = 7.0;
        this.semana = 8;
        this.semestre = 8;
    }


    //getters e setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public double getTime() { return time; }
    public void setTime(double time) { this.time = time; }

    public int getSemana() { return semana; }
    public void setSemana(int semana) { this.semana = semana; }

    public int getSemestre() { return semestre; }
    public void setSemestre(int semestre) { this.semestre = semestre; }

    public Jogador getPlayer() { return player; }
    public void setPlayer(Jogador player) { this.player = player; }
}