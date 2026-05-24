package Model;

import java.io.Serial;
import java.io.Serializable;

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
    private int posx;
    private int posy;
    private int loc;
    private String seta;

    public Jogador() {
        this.nome = "Luiza";
        energia = 100.0;
        motivacao = 100.0;
        saude = 100;
        dinheiro = 300.0;
        posx = 15;
        posy = 1;
        loc = -1;
        seta = "cima";

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


    public int getPosx() {
        return posx;
    }

    public void setPosx(int posx) {
        this.posx = posx;
    }


    public int getPosy() {
        return posy;
    }

    public void setPosy(int posy) {
        this.posy = posy;
    }


    public int getLoc() {
        return loc;
    }

    public void setLoc(int loc) {
        this.loc = loc;
    }


    public void setSeta(String seta){
        this.seta = seta;
    }

    public String getSeta(){
        return this.seta;
    }


    public int getAndamento() {
            return andamento;
    }

    public void setAndamento(int andamento) {
        this.andamento = andamento;
    }
}

