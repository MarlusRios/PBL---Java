package Model.Eventos.Obrigatorios;

import Model.Eventos.Obrigatorio;
import Model.Jogador;
import Controller.Relogio;

public class Aula extends Obrigatorio {

    @Override
    public boolean aplicarEvento(Jogador jogador){
        long minutosNoJogo = (long) ((8 * 60) + (Relogio.segundosTotais * Relogio.tickRate));

        // Janelas de horário (9:30 às 10:42 e 14:00 às 14:42)
        boolean horarioManha = (minutosNoJogo >= 570 && minutosNoJogo <= 642);
        boolean horarioTarde = (minutosNoJogo >= 840 && minutosNoJogo <= 882);

        if (horarioManha || horarioTarde) {
            jogador.setConhecimento(jogador.getConhecimento() + 25);
            jogador.setEnergia(jogador.getEnergia() - 30);

            // CORREÇÃO: Faz o tempo saltar para o FIM da aula!
            if (horarioManha) {
                // Avança os segundos totais para que o relógio marque exatamente 10:45 (645 minutos)
                // Fórmula inversa: (MinutosDesejados - 480) / Velocidade
                Relogio.segundosTotais = (long) ((645 - 480) / Relogio.tickRate);
            } else {
                // Avança os segundos totais para que o relógio marque exatamente 14:45 (885 minutos)
                Relogio.segundosTotais = (long) ((885 - 480) / Relogio.tickRate);
            }

            return true; // Retorna true para abrir o diálogo de aula
        }

        return false;
    }
}