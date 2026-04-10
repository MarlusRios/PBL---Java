package Model;

import Model.maps.MiniMapa;
public class Jogador {
    private String nome;
    private int[] aparencia = new int[2];
    private double energia;
    private double conhecimento;
    private double motivacao;
    private int saude;
    private double dinheiro;
    private int desempenho;
    private int posx;
    private int posy;
    private int loc;
    private String seta;

    public Jogador(String nome, int cabelo, int sexo) {
        this.nome = nome;
        aparencia[0] = cabelo;
        aparencia[1] = sexo;
        energia = 100.0;
        conhecimento = 100.0;
        motivacao = 100.0;
        saude = 100;
        dinheiro = 2000.0;
        posx = -1;
        posy = -1;
        loc = -1;
        seta = "cima";

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public int[] getAparencia() {
        return aparencia;
    }

    public void setAparencia(int cabelo, int sexo) {
        aparencia[0] = cabelo;
        aparencia[1] = sexo;
    }

    public double getEnergia() {
        return energia;
    }

    public void setEnergia(double energia) {
        this.energia = energia;
    }


    public double getConhecimento() {
        return conhecimento;
    }

    public void setConhecimento(double conhecimento) {
        this.conhecimento = conhecimento;
    }

    public double getMotivacao() {
        return motivacao;
    }

    public void setMotivacao(double motivacao) {
        this.motivacao = motivacao;
    }


    public int getSaude() {
        return saude;
    }

    public void setSaude(int saude) {
        this.saude = saude;
    }


    public double getDinheiro() {
        return dinheiro;
    }

    public void setDinheiro(double dinheiro) {
        this.dinheiro = dinheiro;
    }


    public int getDesempenho() {
        return desempenho;
    }

    public void setDesempenho(int desempenho) {
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

}

