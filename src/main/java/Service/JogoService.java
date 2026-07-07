package Service;

import Controller.Relogio;
import Model.Jogo;
import Model.Jogador;
import Repository.JogoRepository;

public class JogoService {

    private final JogadorService jogadorService = new JogadorService();

    //metodo de criação do jogo
    public Jogo criarJogo(String id) {
        Relogio.segundosTotais = 0;
        Relogio.frames = 0;
        return new Jogo(id);
    }

    //metodo para atualizar em tempo real
    public boolean atualizarCicloJogo(Jogo jogo) {
        if (verificarFimDoDia()) {
            encerrarDia(jogo);
            return true;
        } else {
            atualizarJogo(jogo, jogadorService);
            return false;
        }
    }

    // metodo para mudar a energia e motivação automaticamente
    public void atualizarJogo(Jogo jogo, JogadorService jogadorService) {
        if (Relogio.frames == 0) {//muda cada framerate
            Jogador javaJogador = jogo.getPlayer();
            javaJogador.setEnergia(javaJogador.getEnergia() - 0.2);
            javaJogador.setMotivacao(javaJogador.getMotivacao() - 0.2);
        }
    }

    //metodo para encerrar o dia
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
    }

    //metodo que reinicia os atributos a cada dia
    public void fimDoDia(Jogador jogador) {
        jogador.setEnergia(100.0);
        jogador.setMotivacao(100.0);
        jogador.setSaude(100);
    }

    //metodo para avançar um semestre
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

    //metodo de reset de atributos a cada semestre
    private void resetarAtributos(Jogador jogador) {
        jogador.setEnergia(100.0);
        jogador.setMotivacao(100.0);
        jogador.setSaude(100);
        jogador.setConhecimento(0.0);
        jogador.setDinheiro(jogador.getDinheiro() + 300);
    }

    //metodo para verificar se o dia ja acabou (horario >= 19)
    public boolean verificarFimDoDia() {
        Jogador jogador = JogoRepository.getJogoAtual().getPlayer();
        String horaFormatada = Controller.Relogio.obterTempoFormatado();
        int horaAtual = Integer.parseInt(horaFormatada.split(":")[0]);//pega so a hora, sem os minutos
        return horaAtual >= 19 || jogador.getMotivacao()<= 0 || jogador.getSaude()<= 0;
    }

    //metodo para verificar se luiza se formou, virou cachorro ou ainda é aluna
    public int verificarFormatura(Jogo jogo) {
        if( jogo.getPlayer().getAndamento() >= 5){
            return 1;
        }else if (jogo.getSemestre()>8){
            return 2;
        }
        return 0;
    }

    //metodo que encerra o jogo (coloca o semestre em um maior que o aceitavel
    public void encerrarJogo(Jogo jogo) {
        jogo.setSemestre(9);
    }

    //metodo de verificação do estado da prova
    public boolean fazerProva(Jogo jogo) {
        long minutosNoJogo = (long) ((8 * 60) + (Controller.Relogio.segundosTotais * Relogio.tickRate));
        boolean horarioManha = (minutosNoJogo >= 570 && minutosNoJogo <= 642);

        if (horarioManha) {
            // olha se é semana de prova
            if (jogo.getSemana() == 4 || jogo.getSemana() == 8) {
                jogadorService.Prova(jogo);
                return true;
            }
        }
        return false;
    }
}