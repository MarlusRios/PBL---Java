package Service;

import Controller.Relogio;
import Model.Jogo;
import Model.Jogador;

public class JogoService {

    private final JogadorService jogadorService = new JogadorService();

    public Jogo criarJogo(String id) {
        Relogio.segundosTotais = 0;
        Relogio.frames = 0;
        return new Jogo(id);
    }

    public boolean atualizarCicloJogo(Jogo jogo) {
        if (verificarFimDoDia()) {
            encerrarDia(jogo);
            return true;
        } else {
            atualizarJogo(jogo, jogadorService);
            return false;
        }
    }

    // CORREÇÃO: Ajustada a perda passiva para rodar apenas 1 vez por segundo real (quando frames zeram)
    // Se rodar solto a cada frame (60x por segundo), a energia zera em menos de 2 minutos de jogo.
    public void atualizarJogo(Jogo jogo, JogadorService jogadorService) {
        if (Relogio.frames == 0) {
            Jogador javaJogador = jogo.getPlayer();
            // Valores balanceados para o tempo real do jogo (ajuste se achar necessário)
            javaJogador.setEnergia(javaJogador.getEnergia() - 0.2);
            javaJogador.setMotivacao(javaJogador.getMotivacao() - 0.2);
        }
    }

    public void encerrarDia(Jogo jogo) {
        avancarSemana(jogo);
        fimDoDia(jogo.getPlayer());
        Relogio.segundosTotais = 0;
        Relogio.frames = 0;
    }

    // Metodo para o avanço da semana corrigido
    public void avancarSemana(Jogo jogo) {
        int semanaAtual = jogo.getSemana();

        if (semanaAtual >= 8) {
            avancarSemestre(jogo);
        } else {
            semanaAtual = semanaAtual + 1;
            jogo.setSemana(semanaAtual);
        }
        // Toda a lógica antiga do jogo.setExamTime(...) sumiu daqui!
    }

    public void fimDoDia(Jogador jogador) {
        jogador.setEnergia(100);
    }

    public void avancarSemestre(Jogo jogo) {
        int semestreAtual = jogo.getSemestre();
        Jogador jogador = jogo.getPlayer();

        if (jogador.getAndamento() >= 5) {
            encerrarJogo(jogo);
        } else {
            if (jogador.getDesempenho() >= 7) {
                jogo.setSemestre(semestreAtual + 1);
                jogador.setAndamento(jogador.getAndamento() + 1);
                jogo.setSemana(1);
                resetarAtributos(jogo.getPlayer());
            } else {
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
        jogador.setConhecimento(0.0);
        jogador.setDinheiro(jogador.getDinheiro() + 300);
    }

    public double obterHoraAtualComoDouble() {
        long totalMinutosNoJogo = (long) ((8 * 60) + (Relogio.segundosTotais * Relogio.tickRate));
        long horas = (totalMinutosNoJogo / 60) % 24;
        long minutos = totalMinutosNoJogo % 60;
        return horas + (minutos / 60.0);
    }

    public boolean verificarFimDoDia() {
        String horaFormatada = Controller.Relogio.obterTempoFormatado();
        int horaAtual = Integer.parseInt(horaFormatada.split(":")[0]);
        return horaAtual >= 19;
    }

    public int verificarFormatura(Jogo jogo) {
        if( jogo.getPlayer().getAndamento() > 5){
            return 1;
        }else if (jogo.getSemestre()>8){
            return 0;
        }
        return -1;
    }

    private void encerrarJogo(Jogo jogo) {
        jogo.setSemestre(6);
    }

    public boolean fazerProva(Jogo jogo) {
        long minutosNoJogo = (long) ((8 * 60) + (Controller.Relogio.segundosTotais * Relogio.tickRate));
        boolean horarioManha = (minutosNoJogo >= 570 && minutosNoJogo <= 642);

        if (horarioManha) {
            // Olha direto o número da semana real
            if (jogo.getSemana() == 4 || jogo.getSemana() == 8) {
                jogadorService.Prova(jogo);
                return true;
            }
        }
        return false;
    }
}