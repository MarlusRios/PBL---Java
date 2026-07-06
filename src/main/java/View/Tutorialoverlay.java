package View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class Tutorialoverlay extends StackPane {

    private final Runnable aoFechar;

    public Tutorialoverlay(Runnable aoFechar) {
        this.aoFechar = aoFechar;

        setPickOnBounds(true);
        setStyle("-fx-background-color: rgba(0,0,0,0.75);");
        setVisible(false);
        setFocusTraversable(true);
        setMaxWidth(Double.MAX_VALUE);
        setMaxHeight(Double.MAX_VALUE);

        VBox painel = new VBox(15);
        painel.setAlignment(Pos.CENTER);
        painel.setMaxSize(500, 380); // Aumentado para acomodar melhor o texto longo
        painel.setPadding(new Insets(25));
        painel.setStyle(
                "-fx-background-color: #2b2b3d;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #f4c542;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 12;"
        );

        Label titulo = new Label("Como Jogar");
        titulo.setFont(Font.font("Verdana", FontWeight.BOLD, 22));
        titulo.setTextFill(Color.web("#f4c542"));

        Label instrucoes = new Label(
                     "W A S D — mover o personagem.\n\n" +
                        "Encoste em um NPC para iniciar uma conversa.\n\n" +
                        "Fique de olho na energia no topo da tela!\n\n" +
                        "Fique de olho nos seus status com o botão no canto inferior direito obs:eles sao atualizados a cada clique\n\n"+
                        "Cuidado com os horarios, a aula começa as 9:30 e as 14.\n\n" +
                        "Os dias de prova acabam apos ela pois luiza quer ir para casa\n\n" +
                        "As provas só podem ser feitas nas semanas 4 e 8 pela hora aula da manhã.\n\n" +
                        "se o relogio chegar em 19 horas o dia é encerrado automaticamente.\n\n" +
                        "Voce pode comprar lanche na cantina, conversar com o aluno no laboratorio, ou acariciar animais nos corredores.\n\n" +
                        "Interaja com o professor para começar as aulas e provas nos respectivos dias.\n\n"+
                        "Interaja com o onibus para terminar o dia.\n\n" +
                        "Cada semestre corresponde a 8 semanas, apos o seu andamento for suficiente voce se forma.\n\n"+
                        "Se voce ficar semestres demais na uefs voce se forma como um cachorro, então cuidado!\n\n" +
                        "Jogo salvado automaticamente ao encerra do dia, o save só é deletado quando o jogo termina (voce se forma\n\n" +
                        "Se voce ficar sem saude ou sem motivação o dia acaba imediatamente, então tome cuidado!"
        );
        instrucoes.setWrapText(true);
        instrucoes.setTextAlignment(TextAlignment.CENTER);
        instrucoes.setTextFill(Color.WHITE);
        instrucoes.setFont(Font.font("Verdana", 14));
        instrucoes.setMaxWidth(420); // Força a quebra automática do texto longo

        // Criando o ScrollPane corretamente sem misturar tipos
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(instrucoes);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Remove a barra horizontal
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Mostra a vertical só se precisar

        // Estilização transparente básica para o ScrollPane sumir no fundo escuro
        scrollPane.setStyle(
                "-fx-background: transparent;" +
                        "-fx-background-color: transparent;" +
                        "-fx-viewport-background: transparent;"
        );

        Button fechar = new Button("Entendi! (ENTER)");
        fechar.setDefaultButton(true);
        fechar.setOnAction(e -> esconder());

        painel.getChildren().addAll(titulo, scrollPane, fechar);
        getChildren().add(painel);

        setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.ESCAPE) {
                esconder();
            }
        });
    }

    public void mostrar() {
        setVisible(true);
        requestFocus();
    }

    private void esconder() {
        setVisible(false);
        if (aoFechar != null) {
            aoFechar.run();
        }
    }
}