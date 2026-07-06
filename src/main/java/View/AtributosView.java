package View;

import Model.Jogador;
import Model.Jogo;
import Repository.JogoRepository;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AtributosView extends VBox {

    private final Label lblConhecimento;
    private final Label lblMotivacao;
    private final Label lblSaude;
    private final Label lblDinheiro;
    private final Label lblAndamento;
    private final Label lblSemestre;
    private final Label lblSemana;

    public AtributosView() {
        this.setPadding(new Insets(15));
        this.setSpacing(8);

        // Estilo RPG/Retro: Fundo escuro semitransparente, bordas cinzas e cantos levemente pixelados
        this.setStyle(
                "-fx-background-color: rgba(20, 20, 20, 0.85);" +
                        "-fx-border-color: #555555;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;"
        );

        Label titulo = new Label("STATUS DO JOGADOR");
        // Fonte Courier New em dourado/laranja para dar o tom de jogo retrô
        titulo.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #f39c12; -fx-font-family: 'Courier New';");

        // Inicializando os labels usando o método customizado
        this.lblConhecimento = criarLabelCustomizado();
        this.lblMotivacao = criarLabelCustomizado();
        this.lblSaude = criarLabelCustomizado();
        this.lblDinheiro = criarLabelCustomizado();
        this.lblAndamento = criarLabelCustomizado();
        this.lblSemestre = criarLabelCustomizado();
        this.lblSemana = criarLabelCustomizado();

        // Carrega os valores iniciais
        atualizarValores();

        this.getChildren().addAll(titulo, lblConhecimento, lblMotivacao, lblSaude, lblDinheiro, lblAndamento, lblSemestre, lblSemana);
    }

    // Método auxiliar para manter o padrão de fonte e cor branca nos atributos
    private Label criarLabelCustomizado() {
        Label l = new Label();
        l.setStyle("-fx-text-fill: #ffffff; -fx-font-size: 12px; -fx-font-family: 'Courier New'; -fx-font-weight: bold;");
        return l;
    }

    public void atualizarValores() {
        Jogo jogo = JogoRepository.getJogoAtual();
        Jogador jogador = jogo.getPlayer();

        this.lblConhecimento.setText("Conhecimento: " + jogador.getConhecimento());
        this.lblMotivacao.setText(String.format("Motivação: %,2f",  jogador.getMotivacao()));
        this.lblSaude.setText("Saúde: " + jogador.getSaude());
        this.lblDinheiro.setText("Dinheiro: R$ " + jogador.getDinheiro());
        this.lblAndamento.setText("Andamento: " + jogador.getAndamento());
        this.lblSemestre.setText("Semestre: " + jogo.getSemestre());
        this.lblSemana.setText("Semana: "+jogo.getSemana());
    }
}