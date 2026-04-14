package Model;

import java.util.ArrayList;
import java.util.List;
import Model.Eventos.Aleatorio;
import Model.Eventos.Obrigatorio;

public class Jogo {
    private String id;           // identifica a partida (para salvar/carregar)
    private double time;         // hora do dia (0.0 a 24.0)
    private int semana;          // semana atual dentro do semestre (1 a 8)
    private int semestre;        // semestre atual (1 a 5)
    private Jogador player;
    private boolean examTime;    // indica se é semana de prova
    private List<Aleatorio> eventosAleatorios;
    private List<Obrigatorio> eventosObrigatorios;

    public Jogo(String id, String nomeJogador, int cabelo, int sexo) {
        this.id = id;
        this.player = new Jogador(nomeJogador, cabelo, sexo);
        this.time = 7.0;   // jogo começa às 7h
        this.semana = 1;
        this.semestre = 1;
        this.examTime = false;
        this.eventosAleatorios = new ArrayList<>();
        this.eventosObrigatorios = new ArrayList<>();
    }

    // Getters e Setters
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

    public List<Aleatorio> getEventosAleatorios() { return eventosAleatorios; }
    public void setEventosAleatorios(List<Aleatorio> eventosAleatorios) {
        this.eventosAleatorios = eventosAleatorios;
    }

    public List<Obrigatorio> getEventosObrigatorios() { return eventosObrigatorios; }
    public void setEventosObrigatorios(List<Obrigatorio> eventosObrigatorios) {
        this.eventosObrigatorios = eventosObrigatorios;
    }
}