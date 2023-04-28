package com.example.sixquiprend;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

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
    private int numberOfPlayers = 5;

    private int playerPlaying = 3;
    private int round = 1;
    private VBox gameCardBarBox;
    private HBox cardBarBox;
    private final Color[] colorList = new Color[]{Color.BLUE, Color.PURPLE, Color.DEEPPINK, Color.ORANGE, Color.GREEN, Color.YELLOWGREEN, Color.BROWN, Color.GRAY, Color.MAGENTA, Color.DARKCYAN};
    private boolean canClickArrow = false;
    private Set<Polygon> arrowSet = new HashSet<>();
    private VBox playingCards;
    private String name;

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
        cardBarBox = new HBox();
        cardBarBox.setPadding(new Insets(10, 10, 10, 10));
        cardBarBox.setSpacing(10);
        cardBarBox.setAlignment(Pos.CENTER);

        gameCardBarBox = new VBox();
        gameCardBarBox.setPadding(new Insets(10, 10, 10, 10));
        gameCardBarBox.setSpacing(10);

        // Ajouter les HBox des emplacements des cartes au BorderPane principal
        mainPane.setBottom(cardBarBox);
        mainPane.setLeft(gameCardBarBox);

        // Ajouter les emplacements de carte à la HBox
        for (int i = 0; i < 10; i++) {
            StackPane cardPane = new StackPane();
            cardPane.setPrefSize(CARD_BAR_WIDTH, CARD_BAR_HEIGHT);
            cardPane.setBorder(new Border(new BorderStroke(colorList[playerPlaying - 1], BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
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

        playingCards = new VBox();
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
                    cardPane.setBorder(new Border(new BorderStroke(colorList[numberOfPlayers - 1], BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
                    cardHBox.getChildren().add(cardPane);
                }
            } else {
                for (int j = 0; j < 3; j++) {
                    StackPane cardPane = new StackPane();
                    cardPane.setPrefSize(CARD_BAR_WIDTH, CARD_BAR_HEIGHT);
                    cardPane.setBorder(new Border(new BorderStroke(colorList[(i * 3) + j], BorderStrokeStyle.SOLID, null, new BorderWidths(1))))
                    ;
                    cardHBox.getChildren().add(cardPane);
                }
            }
        }

        // Créer la HBox qui contiendra les boutons d'action du joueur
        playerActionsBox = new HBox();
        playerActionsBox.setPadding(new Insets(10, 10, 10, 10));
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
        scoreBox = new VBox();
        scoreBox.setPadding(new Insets(10, 10, 10, 10));
        scoreBox.setSpacing(10);
        scoreBox.setAlignment(Pos.CENTER_LEFT);

        // Ajouter les labels pour le joueur courant et le tour courant
        currentRoundLabel = new Label("Tour " + round);
        currentRoundLabel.setFont(Font.font(20));
        currentRoundLabel.setTextAlignment(TextAlignment.CENTER);

        currentPlayerLabel = new Label("Joueur " + playerPlaying);
        currentPlayerLabel.setFont(Font.font(20));
        currentPlayerLabel.setTextFill(colorList[playerPlaying - 1]);
        currentPlayerLabel.setTextAlignment(TextAlignment.CENTER);


        // Ajouter les labels pour les scores de chaque joueur
        Label scoreTitleLabel = new Label("Scores :");
        scoreTitleLabel.setFont(Font.font(20));
        scoreTitleLabel.setTextAlignment(TextAlignment.CENTER);

        // Ajouter les éléments de scoreBox
        scoreBox.getChildren().addAll(currentRoundLabel, currentPlayerLabel, scoreTitleLabel);

        for (int i = 0; i < numberOfPlayers; i++) {
            HBox playerScoreBox = new HBox();
            playerScoreBox.setSpacing(10);
            playerScoreBox.setAlignment(Pos.CENTER_LEFT);

            Label playerNameLabel = new Label("Joueur " + (i + 1) + " :");
            playerNameLabel.setFont(Font.font(16));
            playerNameLabel.setTextFill(colorList[i]);

            Label playerScoreLabel = new Label("0");
            playerScoreLabel.setFont(Font.font(16));
            playerScoreLabel.setTextFill(colorList[i]);

            playerScoreBox.getChildren().addAll(playerNameLabel, playerScoreLabel);
            scoreBox.getChildren().add(playerScoreBox);
        }

        // Ajouter la VBox de scores au BorderPane principal
        mainPane.setRight(scoreBox);

        // Créer la scène principale avec le BorderPane et l'afficher
        Scene scene = new Scene(mainPane, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setTitle("6 Qui Prend");
        primaryStage.setScene(scene);
        primaryStage.show();

        // TODO Remplir le debut du plateau
        addCard(0, 0, "1.png");
        addCard(1, 0, "55.png");
        addCard(2, 0, "32.png");
        addCard(3, 0, "19.png");


        //TODO Remplir la main du joueur
//        Game game = new Game(numberOfPlayers);
//        Player player = game.getPlayers()[playerPlaying - 1];
//        Card[] handCards = player.getHandCards();
        for (int i = 0; i < 10; i++) {
            addCardInHand(i, i + 20 + ".png");
//            addCardInHand(i, handCards[i].getNumber() + ".png");
        }


    }

    private void addCard(int i, int j, String imageName) {
        StackPane cardPane = (StackPane) ((HBox) gameCardBarBox.getChildren().get(i)).getChildren().get(j);
        URL imageUrl = Board.class.getResource("/images/cards/");
        ImageView imageView = new ImageView(new Image(imageUrl + imageName));
        //        setOnClickAction(imageView, i, j);
        cardPane.getChildren().add(imageView);
    }

    private void addCardInHand(int i, String imageName) {
        StackPane cardPane = (StackPane) cardBarBox.getChildren().get(i);
        URL imageUrl = Board.class.getResource("/images/cards/");
        ImageView imageView = new ImageView(new Image(imageUrl + imageName));
        setOnClickAction(imageView, i, 0);
        cardPane.getChildren().add(imageView);
    }

    public void setOnClickAction(ImageView imageView, int x, int y) {
        imageView.setOnMouseClicked(event -> {
            if (!canClickArrow) {
                System.out.println("Image clicked");
                System.out.println("Card clicked: x = " + x + ", y = " + y);
                // ATTENTION X = Colonnes, Y = Lignes.
                changeCardPosition(x, 0, (playerPlaying - 1) / 3, (playerPlaying - 1) % 3, true);
                // Désactiver les clics sur les images
                canClickArrow = true;
                // Désactiver les clics sur les flèches
                arrowSet.forEach(arrow2 -> arrow2.setOnMouseClicked(null));
                canClickArrow = false;
            }
            ;

        });
    }

    public void changeCardPosition(int x, int y, int newX, int newY, boolean turn) {

        // Obtient la StackPane à la position x, y
//        StackPane cardPane = (StackPane) ((HBox) playingCards.getChildren().get(x)).getChildren().get(y);
        StackPane cardPane = (StackPane) cardBarBox.getChildren().get(x);
        // Récupère l'image à partir de la StackPane
        ImageView imageView = (ImageView) cardPane.getChildren().get(0);
        if (turn) {
            flipImage(imageView);
        }
        // Calcule la distance horizontale et verticale à parcourir
        double deltaX = (newX - x) * CARD_BAR_WIDTH;
        double deltaY = (newY - y) * CARD_BAR_HEIGHT;

        // Crée une transition de translation pour déplacer l'image
        TranslateTransition transition = new TranslateTransition(Duration.millis(500), imageView);
        // - car la carte va vers le haut
        transition.setToX(imageView.getTranslateX() - deltaX);
        transition.setToY(imageView.getTranslateY() - deltaY);
        transition.setInterpolator(Interpolator.EASE_BOTH);

        // Ajoute un événement pour déplacer l'image à la fin de la transition
        transition.setOnFinished(event -> {
            // Supprime l'image de la StackPane
            cardPane.getChildren().remove(imageView);
            // Obtient la nouvelle StackPane où l'image doit être placée
            StackPane newCardPane = (StackPane) ((HBox) playingCards.getChildren().get(newX)).getChildren().get(newY);
            // Ajoute l'image à la nouvelle StackPane
            newCardPane.getChildren().add(imageView);
            // Réinitialise les propriétés de translation de l'image
            imageView.setTranslateX(0);
            imageView.setTranslateY(0);
            System.out.println("Moved card from (" + x + ", " + y + ") to (" + newX + ", " + newY + ")");
        });
        transition.play();
        System.out.println("Moving card from (" + x + ", " + y + ") to (" + newX + ", " + newY + ")");
    }

    public void changeCardPosition(int x, int y, int newX, int newY) {
        changeCardPosition(x, y, newX, newY, false);
    }

    public void flipImage(ImageView imageView) {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(0.5), imageView);
        rotateTransition.setAxis(Rotate.Y_AXIS);
        rotateTransition.setFromAngle(0);
        rotateTransition.setToAngle(180);
        rotateTransition.setInterpolator(Interpolator.EASE_BOTH);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), imageView);
        scaleTransition.setFromX(1);
        scaleTransition.setToX(0);
        scaleTransition.setInterpolator(Interpolator.EASE_BOTH);

        ParallelTransition parallelTransition = new ParallelTransition(rotateTransition, scaleTransition);
        parallelTransition.setOnFinished(event -> {
            imageView.setScaleX(1);
            String imagePath = "/images/cards/backside.png";
            URL imageUrl = Board.class.getResource(imagePath);
            imageView.setImage(new Image(imageUrl.toExternalForm()));
            RotateTransition rotateTransition2 = new RotateTransition(Duration.seconds(0.5), imageView);
            rotateTransition2.setAxis(Rotate.Y_AXIS);
            rotateTransition2.setFromAngle(-180);
            rotateTransition2.setToAngle(0);
            rotateTransition2.setInterpolator(Interpolator.EASE_BOTH);

            ScaleTransition scaleTransition2 = new ScaleTransition(Duration.seconds(0.5), imageView);
            scaleTransition2.setFromX(0);
            scaleTransition2.setToX(1);
            scaleTransition2.setInterpolator(Interpolator.EASE_BOTH);

            ParallelTransition parallelTransition2 = new ParallelTransition(rotateTransition2, scaleTransition2);
            parallelTransition2.play();
        });
        parallelTransition.play();
    }

}
