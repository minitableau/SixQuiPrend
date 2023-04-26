package com.example.sixquiprend;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class Board extends Application {


    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 700;

    private static final int CARD_BAR_HEIGHT = 100;
    private static final int CARD_BAR_WIDTH = 80;

    private BorderPane mainPane;
    private GridPane cardGridPane;
    private HBox playerActionsBox;
    private VBox scoreBox;
    private Label currentPlayerLabel;
    private Label currentRoundLabel;
    private int numberOfPlayers = 4;

    @Override
    public void start(Stage primaryStage) {
        // Créer le BorderPane principal qui contiendra les éléments du jeu
        mainPane = new BorderPane();
        mainPane.setPadding(new Insets(10, 10, 10, 10));

        // Créer le GridPane qui contiendra les cartes jouées par les joueurs
        cardGridPane = new GridPane();
        cardGridPane.setPadding(new Insets(10, 10, 10, 10));
        cardGridPane.setAlignment(Pos.CENTER);
        cardGridPane.setVgap(10);
        cardGridPane.setHgap(10);

        // Créer la HBox qui contiendra les emplacements pour les cartes du joueur
        HBox cardBarBox = new HBox();
        cardBarBox.setPadding(new Insets(10, 10, 10, 10));
        cardBarBox.setSpacing(10);
        cardBarBox.setAlignment(Pos.CENTER);

        VBox gameCardBarBox = new VBox();
        gameCardBarBox.setPadding(new Insets(10, 10, 10, 10));
        gameCardBarBox.setSpacing(10);

        // Ajouter les HBox des emplacements des cartes au BorderPane principal
        mainPane.setBottom(cardBarBox);
        mainPane.setLeft(gameCardBarBox);

        // Ajouter les emplacements de carte à la HBox
        for (int i = 0; i < 10; i++) {
            StackPane cardPane = new StackPane();
            cardPane.setPrefSize(CARD_BAR_WIDTH, CARD_BAR_HEIGHT);
            cardPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
            cardBarBox.getChildren().add(cardPane);
        }

        for (int i = 0; i < 4; i++) {
            HBox cardHBox = new HBox();
            cardHBox.setSpacing(10);
            gameCardBarBox.getChildren().add(cardHBox);
            for (int j = 0; j < 6; j++) {
                StackPane cardPane = new StackPane();
                cardPane.setPrefSize(CARD_BAR_WIDTH, CARD_BAR_HEIGHT);
                if (j == 5) {
                    cardPane.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                } else {
                    cardPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                }
                cardHBox.getChildren().add(cardPane);
            }
            Arrow arrow = new Arrow();
            Polygon arrowNode = arrow.getArrow(50, 40); // définit la largeur et la hauteur de la flèche
            cardHBox.getChildren().add(arrowNode);
            cardHBox.setAlignment(Pos.CENTER);

        }


        //TODO
        VBox playingCards = new VBox();
        playingCards.setPadding(new Insets(10, 10, 10, 10));
        playingCards.setSpacing(10);

        // Ajouter les HBox des emplacements des cartes au BorderPane principal
        mainPane.setCenter(playingCards);

        // On soustrait 1 au nombre de joueur on divise par le  de case que l'on souhaite par ligne et on ajoute 1
        for (int i = 0; i <= (numberOfPlayers - 1) / 3; i++) {
            HBox cardHBox = new HBox();
            cardHBox.setSpacing(10);
            playingCards.getChildren().add(cardHBox);
            cardHBox.setAlignment(Pos.CENTER_RIGHT);
            if (i == (numberOfPlayers - 1) / 3 && numberOfPlayers % 3 != 0) {
                for (int j = 0; j < numberOfPlayers % 3; j++) {
                    StackPane cardPane = new StackPane();
                    cardPane.setPrefSize(CARD_BAR_WIDTH, CARD_BAR_HEIGHT);
                    cardPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                    cardHBox.getChildren().add(cardPane);
                }
            } else {
                for (int j = 0; j < 3; j++) {
                    StackPane cardPane = new StackPane();
                    cardPane.setPrefSize(CARD_BAR_WIDTH, CARD_BAR_HEIGHT);
                    cardPane.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                    cardHBox.getChildren().add(cardPane);
                }
            }
        }

        // Créer la HBox qui contiendra les boutons d'action du joueur
        playerActionsBox = new

                HBox();
        playerActionsBox.setPadding(new

                Insets(10, 10, 10, 10));
        playerActionsBox.setSpacing(10);
        playerActionsBox.setAlignment(Pos.CENTER);

//        // Ajouter les boutons d'action au HBox
//        Button playCardButton = new Button("Jouer une carte");
//        Button passButton = new Button("Passer");
//        Button takeRowButton = new Button("Prendre une rangée");
//
//        playerActionsBox.getChildren().addAll(playCardButton, passButton, takeRowButton);
//
//        // Ajouter la HBox des actions de joueur au BorderPane principal
//        mainPane.setTop(playerActionsBox);

        // Créer la VBox qui contiendra la barre de score des joueurs
        scoreBox = new

                VBox();
        scoreBox.setPadding(new

                Insets(10, 10, 10, 10));
        scoreBox.setSpacing(10);
        scoreBox.setAlignment(Pos.CENTER_LEFT);

        // Ajouter les labels pour le joueur courant et le tour courant
        currentPlayerLabel = new

                Label("Joueur 1");
        currentPlayerLabel.setFont(Font.font(20));
        currentPlayerLabel.setTextAlignment(TextAlignment.CENTER);

        currentRoundLabel = new

                Label("Tour 1");
        currentRoundLabel.setFont(Font.font(20));
        currentRoundLabel.setTextAlignment(TextAlignment.CENTER);

        // Ajouter les labels pour les scores de chaque joueur
        Label scoreTitleLabel = new Label("Scores :");
        scoreTitleLabel.setFont(Font.font(20));
        scoreTitleLabel.setTextAlignment(TextAlignment.CENTER);

        HBox playerScoreBox1 = new HBox();
        playerScoreBox1.setSpacing(10);
        playerScoreBox1.setAlignment(Pos.CENTER_LEFT);

        Label player1NameLabel = new Label("Joueur 1 :");
        player1NameLabel.setFont(Font.font(16));
        player1NameLabel.setTextFill(Color.BLUE);

        Label player1ScoreLabel = new Label("0");
        player1ScoreLabel.setFont(Font.font(16));
        player1ScoreLabel.setTextFill(Color.BLUE);

        playerScoreBox1.getChildren().

                addAll(player1NameLabel, player1ScoreLabel);

        HBox playerScoreBox2 = new HBox();
        playerScoreBox2.setSpacing(10);
        playerScoreBox2.setAlignment(Pos.CENTER_LEFT);

        Label player2NameLabel = new Label("Joueur 2 :");
        player2NameLabel.setFont(Font.font(16));
        player2NameLabel.setTextFill(Color.RED);

        Label player2ScoreLabel = new Label("0");
        player2ScoreLabel.setFont(Font.font(16));
        player2ScoreLabel.setTextFill(Color.RED);

        playerScoreBox2.getChildren().

                addAll(player2NameLabel, player2ScoreLabel);

        // Ajouter les éléments de scoreBox
        scoreBox.getChildren().

                addAll(currentPlayerLabel, currentRoundLabel, scoreTitleLabel, playerScoreBox1, playerScoreBox2);

        // Ajouter la VBox de scores au BorderPane principal
        mainPane.setRight(scoreBox);

        // Créer la scène principale avec le BorderPane et l'afficher
        Scene scene = new Scene(mainPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setTitle("6 Qui Prend");
        primaryStage.setScene(scene);
        primaryStage.show();


    }

//    private void addCardToBar(int index, Card card) {
//        StackPane cardPane = (StackPane) cardBarBox.getChildren().get(index);
//        // Créer un Label pour afficher le numéro de la carte
//        Label cardLabel = new Label(String.valueOf(card.getNumber()));
//        cardLabel.setFont(Font.font(20));
//        // Ajouter le Label au StackPane
//        cardPane.getChildren().add(cardLabel);
//    }


}
