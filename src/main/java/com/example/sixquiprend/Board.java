package com.example.sixquiprend;

import com.example.sixquiprend.Jeu.Card;
import com.example.sixquiprend.Jeu.Game;
import com.example.sixquiprend.Jeu.Player;
import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Bounds;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.sixquiprend.Jeu.Game.numberOfPlayers;
import static com.example.sixquiprend.Jeu.Game.players;

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
    private int playerPlaying = 1;
private int time= 2;
private double fliptime = 0.5;
    private int round = 1;
    private VBox gameCardBarBox;
    private HBox cardBarBox;
    private final Color[] colorList = new Color[]{Color.BLUE, Color.PURPLE, Color.DEEPPINK, Color.ORANGE, Color.GREEN, Color.YELLOWGREEN, Color.BROWN, Color.GRAY, Color.MAGENTA, Color.DARKCYAN};
    private boolean canClickArrow = false;
    private Set<Polygon> arrowSet = new HashSet<>();
    private VBox playingCards;
    private String name;

    private List<String> clickedCardInfoList = new ArrayList<>();
    private StackPane cardPane;
    private int clickedIndex;

    @Override
    public void start(Stage primaryStage) {
        numberOfPlayers = players.size();
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
            int playerIndex = playerPlaying - 1; // définir une variable finale pour le joueur courant
            cardPane = new StackPane();
            cardPane.setId("cardPane_" + i); // Ajoutez un identifiant unique
            cardPane.setPrefSize(CARD_BAR_WIDTH, CARD_BAR_HEIGHT);
            cardPane.setBorder(new Border(new BorderStroke(colorList[playerIndex], BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
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
        for (int i = 0; i <= (players.size() - 1) / 3; i++) {
            HBox cardHBox = new HBox();
            cardHBox.setSpacing(10);
            playingCards.getChildren().add(cardHBox);
            cardHBox.setAlignment(Pos.CENTER_RIGHT);
            if (i == (players.size() - 1) / 3 && players.size() % 3 != 0) {
                for (int j = 0; j < players.size() % 3; j++) {
                    StackPane cardPane = new StackPane();
                    cardPane.setPrefSize(CARD_BAR_WIDTH, CARD_BAR_HEIGHT);
                    cardPane.setBorder(new Border(new BorderStroke(colorList[players.size() - 1], BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
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

        // Créer la VBox qui contiendra la barre de score des joueurs
        scoreBox = new VBox();
        scoreBox.setPadding(new Insets(10, 10, 10, 10));
        scoreBox.setSpacing(10);
        scoreBox.setAlignment(Pos.CENTER_LEFT);

        // Ajouter les labels pour le joueur courant et le tour courant
        currentRoundLabel = new Label("Tour " + round);
        currentRoundLabel.setFont(Font.font(20));
        currentRoundLabel.setTextAlignment(TextAlignment.CENTER);

        currentPlayerLabel = new Label(players.get(playerPlaying - 1).getName());
        currentPlayerLabel.setFont(Font.font(20));
        currentPlayerLabel.setTextFill(colorList[playerPlaying - 1]);
        currentPlayerLabel.setTextAlignment(TextAlignment.CENTER);


        // Ajouter les labels pour les scores de chaque joueur
        Label scoreTitleLabel = new Label("Scores :");
        scoreTitleLabel.setFont(Font.font(20));
        scoreTitleLabel.setTextAlignment(TextAlignment.CENTER);

        // Ajouter les éléments de scoreBox
        scoreBox.getChildren().addAll(currentRoundLabel, currentPlayerLabel, scoreTitleLabel);

        for (int i = 0; i < players.size(); i++) {
            HBox playerScoreBox = new HBox();
            playerScoreBox.setSpacing(10);
            playerScoreBox.setAlignment(Pos.CENTER_LEFT);

            Label playerNameLabel = new Label(players.get(i).getName() + " :");
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

        // Remplir la main du joueur
        fullHand();


        // Remplir le premier tableau (4 cartes à gauche)
        for (int i = 0; i < 4; i++) {
            addCard(i, 0, Game.getRemainingCards().get(i).getNumber() + ".png");
            //TODO les 4 premieres cartes son a enlever du jeu dcp.
        }

    }

    private void fullHand() {
        Player player = players.get(playerPlaying - 1);
        Card[] handCards = player.getHandCards();

        // Réinitialiser les emplacements de carte du joueur
        for (int i = 0; i < 10; i++) {
            StackPane cardPane = (StackPane) cardBarBox.getChildren().get(i);
            cardPane.getChildren().clear(); // Retirer les cartes précédentes
            cardPane.setBorder(new Border(new BorderStroke(colorList[playerPlaying - 1], BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        }

        // Distribuer les cartes restantes de la main initiale du joueur
        for (int i = 0; i < handCards.length; i++) {
            if (!players.get(playerPlaying - 1).getIsCardPlayed()[i]) {
                addCardInHand(i, handCards[i].getNumber() + ".png");
            }
        }
    }


    private void addCard(int i, int j, String imageName) {
        StackPane cardPane = (StackPane) ((HBox) gameCardBarBox.getChildren().get(i)).getChildren().get(j);
        URL imageUrl = Board.class.getResource("/images/cards/");
        ImageView imageView = new ImageView(new Image(imageUrl + imageName));
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
        StackPane cardPane = (StackPane) cardBarBox.getChildren().get(x);
        imageView.setOnMouseClicked(event -> {
            if (!canClickArrow) {
                if (!canClickArrow && !players.get(playerPlaying - 1).getIsCardPlayed()[x]) {
                    String cardPaneId = cardPane.getId(); // Récupérez l'identifiant de la StackPane
                    clickedIndex = Integer.parseInt(cardPaneId.substring(cardPaneId.lastIndexOf("_") + 1));
                    System.out.println("CardPane clicked: " + clickedIndex);
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
            }
        });
    }

    public void changeCardPosition(int x, int y, int newX, int newY, boolean turn) {

        StackPane cardPane = (StackPane) cardBarBox.getChildren().get(x);
        ImageView imageView = (ImageView) cardPane.getChildren().get(0);
        cardPane.getChildren().remove(imageView);
        StackPane newCardPane = (StackPane) ((HBox) playingCards.getChildren().get(newX)).getChildren().get(newY);
        // Get the start and end coordinates
        Bounds startBounds = cardPane.localToScene(cardPane.getBoundsInLocal());
        Bounds endBounds = newCardPane.localToScene(newCardPane.getBoundsInLocal());
        // Create a new ImageView to animate
        ImageView animatedImageView = new ImageView(imageView.getImage());

        // Flip the image before creating the animated ImageView
        if (turn) {
            flipImage(animatedImageView);
        }

        animatedImageView.setX(startBounds.getMinX());
        animatedImageView.setY(startBounds.getMinY());
        // Add the animated ImageView to the scene
        Pane root = (Pane) cardBarBox.getScene().getRoot();
        root.getChildren().add(animatedImageView);

        // Create the translation transition
        TranslateTransition tt = new TranslateTransition(Duration.seconds(time), animatedImageView);
        tt.setToX(endBounds.getMinX() - startBounds.getMinX());
        tt.setToY(endBounds.getMinY() - startBounds.getMinY());

        tt.setOnFinished(event -> {
            root.getChildren().remove(imageView);
            imageView.setRotate(0); // Reset rotation to 0 degrees
            newCardPane.getChildren().add(imageView);
        });

        // Start the transition
        tt.play();

        players.get(playerPlaying - 1).setIsCardPlayed(x);

        System.out.println("Moved card from (" + x + ", " + y + ") to (" + newX + ", " + newY + ")");


    }
        public void changeCardPositionBis(int cardNumber) {
        String deuxiemeElement = clickedCardInfoList.get(1);
        String[] elements = deuxiemeElement.split(" : ");
        String numeroCarte = elements[1];
        int numeroCarteInt = Integer.parseInt(numeroCarte);

        System.out.println(numeroCarteInt);
        int playerIndex = 0;
            Player player = players.get(playerIndex);
            ImageView imageView = new ImageView(new Image(Board.class.getResource("/images/cards/" + numeroCarteInt + ".png").toExternalForm()));
            cardPane.getChildren().clear();
            cardPane.getChildren().add(imageView);

            StackPane destinationPane = (StackPane) ((HBox) gameCardBarBox.getChildren().get(2)).getChildren().get(2);
            destinationPane.getChildren().clear();
            destinationPane.getChildren().add(imageView);
        }



    public void changeCardPosition(int x, int y, int newX, int newY) {
        changeCardPosition(x, y, newX, newY, false);
    }

    public void flipImage(ImageView imageView) {

        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(fliptime), imageView);
        rotateTransition.setAxis(Rotate.Y_AXIS);
        rotateTransition.setFromAngle(0);
        rotateTransition.setToAngle(180);
        rotateTransition.setInterpolator(Interpolator.EASE_BOTH);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(fliptime), imageView);
        scaleTransition.setFromX(1);
        scaleTransition.setToX(0);
        scaleTransition.setInterpolator(Interpolator.EASE_BOTH);

        ParallelTransition parallelTransition = new ParallelTransition(rotateTransition, scaleTransition);
        parallelTransition.setOnFinished(event -> {
            imageView.setScaleX(1);
            String imagePath = "/images/cards/backside.png";
            URL imageUrl = Board.class.getResource(imagePath);
            imageView.setImage(new Image(imageUrl.toExternalForm()));
            RotateTransition rotateTransition2 = new RotateTransition(Duration.seconds(fliptime), imageView);
            rotateTransition2.setAxis(Rotate.Y_AXIS);
            rotateTransition2.setFromAngle(-180);
            rotateTransition2.setToAngle(0);
            rotateTransition2.setInterpolator(Interpolator.EASE_BOTH);

            ScaleTransition scaleTransition2 = new ScaleTransition(Duration.seconds(fliptime), imageView);
            scaleTransition2.setFromX(0);
            scaleTransition2.setToX(1);
            scaleTransition2.setInterpolator(Interpolator.EASE_BOTH);

            ParallelTransition parallelTransition2 = new ParallelTransition(rotateTransition2, scaleTransition2);
            parallelTransition2.play();

            //Mise en liste de la carte cliqué
            Player player = players.get(playerPlaying - 1);
            Card[] handCards = player.getHandCards();
            System.out.println("Joueur " + (playerPlaying) + " a cliqué sur la carte " + handCards[clickedIndex].getNumber() + " (valeur : " + handCards[clickedIndex].getPoints() + ")");
            String info = "" + (playerPlaying) + " : " + handCards[clickedIndex].getNumber() + " : " + handCards[clickedIndex].getPoints() + "";
            clickedCardInfoList.add(info);
            if (clickedCardInfoList.size() == numberOfPlayers) {
                System.out.println("Message : Le nombre d'informations est égal au nombre de joueurs en cours de jeu.");
                // Supprimer l'image "backside" du nouvel emplacement
                changeCardPositionBis(103);

            }

            System.out.println(clickedCardInfoList);

            //TODO REMOVE CARD JOUé
//            StackPane cardPane = (StackPane) cardBarBox.getChildren().get(clickedIndex);
//            cardPane.getChildren().remove(imageView);
//            imageView.setDisable(true);


//            Player.removeCardFromHand(players[playerIndex], cardIndex);

            playerPlaying++;
            if (playerPlaying > players.size()) {
                playerPlaying = playerPlaying % players.size();
                round++;
                clickedCardInfoList.clear(); //Permet de reset la liste des cartes jouées (fin de tour)

            }
            fullHand();
            currentRoundLabel.setText("Tour " + round);
            currentPlayerLabel.setText(players.get(playerPlaying - 1).getName());
            currentPlayerLabel.setTextFill(colorList[playerPlaying - 1]);
        });
        parallelTransition.play();

    }

}



