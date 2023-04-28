package com.example.sixquiprend;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class LaunchGameWindow extends Stage {

    public LaunchGameWindow() {
        Label numPlayersLabel = new Label("Nombre de joueurs :");
        TextField numPlayersField = new TextField();
        Label playerTypeLabel = new Label("Type de joueur :");
        ComboBox<String> playerTypeComboBox = new ComboBox<String>();
        playerTypeComboBox.getItems().addAll("Joueur humain", "Bot");

        Button startButton = new Button("Commencer");
        startButton.setOnAction(e -> {
            int numPlayers = Integer.parseInt(numPlayersField.getText());
            String playerType = playerTypeComboBox.getValue();
            //TODO Do something with the player information
        });


        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        grid.add(numPlayersLabel, 0, 0);
        grid.add(numPlayersField, 1, 0);
        grid.add(playerTypeLabel, 0, 1);
        grid.add(playerTypeComboBox, 1, 1);
        grid.add(startButton, 1, 2);

        Scene scene = new Scene(grid, 300, 200);
        setScene(scene);
        setTitle("Lancement du jeu");
    }
}


