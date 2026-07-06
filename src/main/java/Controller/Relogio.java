package Controller;

public class Relogio {
    public static long segundosTotais = 0; // O tempo total acumulado do jogo
    public static long frames = 0;
    public static double tickRate;

    public static void incrementarTempo() {
        frames++;
        if (frames >= 60) { // Aproximadamente 1 segundo
            segundosTotais++;
            frames = 0;
            tickRate = 3;
        }
    }

    // Método novo para mostrar na tela!
    public static String obterTempoFormatado() {
        // Exemplo: começa as 08h00. Cada segundo real = 0.5 minuto no jogo.
        long totalMinutosNoJogo = (long) ((8 * 60) + (segundosTotais * tickRate));

        long horas = (totalMinutosNoJogo / 60) % 24; // % 24 para zerar à meia-noite
        long minutos = totalMinutosNoJogo % 60;

        // Retorna no formato "08:05", "09:12", etc.
        return String.format("%02d:%02d", horas, minutos);
    }
}