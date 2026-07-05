package Service;

import Model.Jogo;
import Model.Jogador;
import Model.Personagens.Professor;

public class JogoService {

    private final JogadorService jogadorService = new JogadorService();

    // Cria uma nova partida
    public Jogo criarJogo(String id) {
        return new Jogo(id);
    }

    //metodo para atualizar o jogo e aplicar a passagem de tempo e suas consequencias no jogador
    public void rodarJogo(Jogo jogo) throws InterruptedException{
        Jogador jogador = jogo.getPlayer();
        while (!verificarFimDoDia(jogo)) {
            atualizarJogo(jogo, jogadorService);

            Thread.sleep(30000); //1/2 minuto
             jogo.setTime(jogo.getTime()+0.5); // avança 1 unidade de tempo no jogo
        }
        encerrarDia(jogo);
    }

    // metodo para atualizar e verificar as possiveis possibilidades de eventos
    public void atualizarJogo(Jogo jogo, JogadorService jogadorService){
        Jogador jogador = jogo.getPlayer();
         jogador.setEnergia(jogador.getEnergia() - 1);
         jogador.setMotivacao(jogador.getMotivacao() - 1);
    }

    // Chamado quando o jogador entra no ônibus ou passa das 19h
    public void encerrarDia(Jogo jogo) {
        avancarSemana(jogo);
        fimDoDia(jogo.getPlayer());
        jogo.setTime(7.0); // novo dia começa às 7h
    }

    //metodo para o avanço do dia (semana)
    public void avancarSemana(Jogo jogo) {
        int semanaAtual = jogo.getSemana();

        // verifica se é semana de prova
        if (semanaAtual == 4 || semanaAtual == 8) {
            jogo.setExamTime(true);
        } else {
            jogo.setExamTime(false);
        }

        if (semanaAtual >= 8) {
            avancarSemestre(jogo);
        } else {
            jogo.setSemana(semanaAtual + 1);
        }
    }

    //metodo para resetar a energia do jogador ao fim do dia
    public void fimDoDia(Jogador jogador){
        jogador.setEnergia(100);
    }

    //metodo para avançar o semestre e verificar se o jogador se formou
    public void avancarSemestre(Jogo jogo) {
        int semestreAtual = jogo.getSemestre();
        Jogador jogador = jogo.getPlayer();

        if (jogador.getAndamento() >= 5) {
            encerrarJogo(jogo);
        } else {
            if(jogador.getDesempenho()>= 7){
                jogo.setSemestre(semestreAtual + 1);
                jogador.setAndamento(jogador.getAndamento()+1);
                jogo.setSemana(1);
                resetarAtributos(jogo.getPlayer());
            }else{
                jogo.setSemestre(semestreAtual + 1);
                jogo.setSemana(1);
                resetarAtributos(jogo.getPlayer());
            }
        }
    }

    //metodo para resetar os atributos e adicionar dinheiro no fim do semestre
    private void resetarAtributos(Jogador jogador) {
        jogador.setEnergia(100.0);
        jogador.setMotivacao(100.0);
        jogador.setSaude(100);
        jogador.setDinheiro(jogador.getDinheiro() + 300);
    }

    //metodo para verificar se passou das 19hrs
    public boolean verificarFimDoDia(Jogo jogo) {
        return jogo.getTime() >= 19.0;
    }

    //metodo para verificar se o jogador se formou
    public boolean verificarFormatura(Jogo jogo) {
        return jogo.getPlayer().getAndamento() > 5;
    }

    //encerrar o jogo
    private void encerrarJogo(Jogo jogo) {
        jogo.setSemestre(6);
    }

    public void fazerProva(Jogo jogo){
        Jogador jogador = jogo.getPlayer();

        if(jogo.getTime() >= 9.5 && jogo.getTime() <= 10.2 || jogo.getTime()>= 14 && jogo.getTime() <= 14.7) { //ver se está no horario da aula
            if (jogo.isExamTime()) {
                jogadorService.Prova(jogo);
            } else {
                jogadorService.interagirBoolean(jogador, new Professor());
            }
        }
    }
}