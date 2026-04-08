package Model;

public class Jogador {
    private String nome;
    private int[] aparencia = new int[2];
    private double energia;
    private double conhecimento;
    private double motivacao;
    private int saude;
    private double dinheiro;
    private int desempenho;
    private int andamento;
    private int posx;
    private int posy;
    private int loc;

    public Jogador(String nome, int cabelo, int sexo){
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

    }

}
