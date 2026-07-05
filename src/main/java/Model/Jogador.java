package Model;

import View.Observador;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Jogador implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String nome;
    private double energia;
    private static final int MAX = 100;
    private double conhecimento;
    private double motivacao;
    private int saude;
    private double dinheiro;
    private double desempenho;
    private int andamento;
    private transient List<Observador> observadores = new ArrayList<>();

    public Jogador() {
        this.nome = "Luiza";
        energia = 100.0;
        motivacao = 100.0;
        saude = 100;
        dinheiro = 300.0;
    }

    //getters e setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getEnergia() {
        return energia;
    }

    public void setEnergia(double energia) {
        this.energia = energia;
        notificar();
    }

    public double getConhecimento() { return conhecimento; }

    public void setConhecimento(double conhecimento){
        this.conhecimento = Math.max(0, Math.min(conhecimento, MAX));
    }

    public double getMotivacao() {
        return motivacao;
    }

    public void setMotivacao(double motivacao) {
        this.motivacao = Math.max(0, Math.min(motivacao, MAX));
    }


    public int getSaude() {
        return saude;
    }

    public void setSaude(int vida) {
        this.saude = Math.max(0, Math.min(vida, MAX));
    }


    public double getDinheiro() {
        return dinheiro;
    }

    public void setDinheiro(double dinheiro) { this.dinheiro = dinheiro; }


    public double getDesempenho() {
        return desempenho;
    }

    public void setDesempenho(double desempenho) {
        this.desempenho = desempenho;
    }

    public int getAndamento() {
            return andamento;
    }

    public void setAndamento(int andamento) {
        this.andamento = andamento;
    }

    private void notificar() {
        if (observadores != null) {
            for (Observador o : observadores) {
                o.atualizar();
            }
        }
    }

    public void adicionarObservador(Observador o) {
        // Evita que a lista fique nula caso o jogador seja carregado de um arquivo binário
        if (this.observadores == null) {
            this.observadores = new ArrayList<>();
        }
        this.observadores.add(o);
    }
}

