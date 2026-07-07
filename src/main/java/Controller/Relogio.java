package Controller;

//nossa classe que implementa o singleton
public class Relogio {
    public static long segundosTotais = 0;
    public static long frames = 0;
    public static double tickRate = 0.5; //velocidade do jogo

    public static void incrementarTempo() {
        frames++;
        if (frames >= 60) { // Aproximadamente 1 segundo
            segundosTotais++;
            frames = 0;
        }
    }

    // Metodo para mostrar na tela
    public static String obterTempoFormatado() {
        // começa as 08h00. Cada segundo real = 0.5 minuto no jogo.
        long totalMinutosNoJogo = (long) ((8 * 60) + (segundosTotais * tickRate));

        long horas = (totalMinutosNoJogo / 60) % 24;
        long minutos = totalMinutosNoJogo % 60;

        // Retorna no formato de relogio
        return String.format("%02d:%02d", horas, minutos);
    }
}