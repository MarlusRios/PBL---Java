package Service;

import Model.Jogo;
import Model.Jogador;

public class JogoService {

    // Cria uma nova partida
    public Jogo criarJogo(String id, String nomeJogador, int cabelo, int sexo) {
        return new Jogo(id, nomeJogador, cabelo, sexo);
    }

    // Chamado quando o jogador entra no ônibus ou passa das 19h
    public void encerrarDia(Jogo jogo) {
        avancarSemana(jogo);
        resetarAtributos(jogo.getPlayer());
        jogo.setTime(7.0); // novo dia começa às 7h
    }

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

    public void avancarSemestre(Jogo jogo) {
        int semestreAtual = jogo.getSemestre();
        Jogador jogador = jogo.getPlayer();

        if (jogador.getAndamento() >= 5) {
            // jogador passou no ultimo semestre
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

    private void resetarAtributos(Jogador jogador) {
        jogador.setEnergia(100.0);
        jogador.setMotivacao(100.0);
        jogador.setSaude(100);
        // dinheiro não reseta
    }

    public boolean verificarFimDoDia(Jogo jogo) {
        return jogo.getTime() >= 19.0;
    }

    public boolean verificarFormatura(Jogo jogo) {
        return jogo.getPlayer().getAndamento() > 5;
    }

    private void encerrarJogo(Jogo jogo) {
        jogo.setSemestre(6);
    }
}