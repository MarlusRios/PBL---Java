package View;

import javafx.application.Application;

//essa classe serve apenas para chamar a interface grafica e contornar um bug que estava acontecendo quando eu tentava chamar ela no mainView
public class Launcher {
    public static void main(String[] args) {
        Application.launch(JanelaInicial.class, args);
    }
}