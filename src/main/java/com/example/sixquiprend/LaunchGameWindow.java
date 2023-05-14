package com.example.sixquiprend;

import com.example.sixquiprend.Jeu.Game;
import com.example.sixquiprend.Jeu.Player;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class LaunchGameWindow extends Stage {

    private static final int MIN_PLAYERS = 2;
    private static final int MAX_PLAYERS = 10;

    private GridPane grid;
    private Label numPlayersLabel;
    private TextField numPlayersField;
    private Button addPlayerButton;
    private Button startButton;
    private int currentRow;

    public LaunchGameWindow() {
        grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        Label playerNameLabel = new Label("Nom du joueur :");
        Label playerTypeLabel = new Label("Type de joueur :");
        currentRow = 2;
        addPlayerRow();
        addPlayerRow();
        grid.add(playerNameLabel, 1, 0);
        grid.add(playerTypeLabel, 2, 0);


        Scene scene = new Scene(grid, 300, 450);
        setScene(scene);
        setTitle("Lancement du jeu");
    }

    private void addPlayerRow() {
        if (currentRow > MAX_PLAYERS + 1) {
            return;
        }

        TextField playerNameField = new TextField();
        playerNameField.setPromptText("Joueur " + (currentRow - 1));
        ComboBox<String> playerTypeComboBox = new ComboBox<>();
        playerTypeComboBox.getItems().addAll("Joueur humain", "Bot");
        // Valeur par défaut. Changé par bot ?
        playerTypeComboBox.setValue("Joueur humain");
        addPlayerButton = new Button("+");
        addPlayerButton.setOnAction(e -> addPlayerRow());
        startButton = new Button("Start Game");
        startButton.setOnAction(e -> startGame());


        grid.add(playerNameField, 1, currentRow);
        grid.add(playerTypeComboBox, 2, currentRow);
        grid.add(addPlayerButton, 2, currentRow + 1);
        grid.add(startButton, 1, currentRow + 1);

        currentRow++;
    }

    private void startGame() {

        // Récupérer les informations sur les joueurs et commencer le jeu
        List<Player> players = new ArrayList<>();

        for (int row = 0; row < currentRow - 2; row++) {
            if (row < 2) {
                // Car lien très etrange dans le placement des boutons
                TextField playerNameField = (TextField) grid.getChildren().get(row * 4);
                ComboBox<String> playerTypeComboBox = (ComboBox<String>) grid.getChildren().get(row * 4 + 1);
                String playerName = playerNameField.getText().isEmpty() ? playerNameField.getPromptText() : playerNameField.getText();

                String playerType = playerTypeComboBox.getValue();
                System.out.println(playerName + " " + playerType);

                Player player = new Player(playerName, playerType);
                players.add(player);
            } else {
                // Car lien très etrange dans le placement des boutons
                TextField playerNameField = (TextField) grid.getChildren().get(10 + (row - 2) * 4);
                ComboBox<String> playerTypeComboBox = (ComboBox<String>) grid.getChildren().get(11 + (row - 2) * 4);
                String playerName = playerNameField.getText().isEmpty() ? playerNameField.getPromptText() : playerNameField.getText();
                String playerType = playerTypeComboBox.getValue();
                System.out.println(playerName + " " + playerType);

                Player player = new Player(playerName, playerType);
                players.add(player);
            }
        }

        new Game(players);
        Board b = new Board();
        b.start(new Stage());

        close();
    }
}
