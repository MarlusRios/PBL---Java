package Model;

import Model.maps.Map;
import Service.MapService;
public class Jogo {
    private String id;
    private double time;
    private int semana;
    private int semestre;
    private Jogador player;
    private boolean examTime;
    private Map mapa;

    public Jogo(String id, String nomeJogador, int cabelo, int sexo, Map mapa) {
        this.id = id;
        this.player = new Jogador(nomeJogador, cabelo, sexo);
        this.time = 7.0;
        this.semana = 1;
        this.semestre = 1;
        this.examTime = false;
        this.mapa = mapa;
    }

    public Map getMapa(){return mapa;}

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

    public boolean isExamTime() { return examTime; }
    public void setExamTime(boolean examTime) { this.examTime = examTime; }
}