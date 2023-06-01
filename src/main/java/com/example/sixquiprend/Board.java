package com.example.sixquiprend;

import com.example.sixquiprend.Jeu.Card;
import com.example.sixquiprend.Jeu.Game;
import com.example.sixquiprend.Jeu.Player;
import javafx.animation.*;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import java.util.*;

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
    private Label playerScoreLabel;
    private int playerPlaying = 1;
    private int time = 2;
    private double fliptime = 0.5;
    private int round = 1;
    private VBox gameCardBarBox;
    private HBox cardBarBox;
    private final Color[] colorList = new Color[]{Color.BLUE, Color.PURPLE, Color.DEEPPINK, Color.ORANGE, Color.GREEN, Color.YELLOWGREEN, Color.BROWN, Color.GRAY, Color.MAGENTA, Color.DARKCYAN};
    private boolean canClickArrow = false;
    private Set<Polygon> arrowSet = new HashSet<>();
    private VBox playingCards;
    private String name;

    private List<String> clickedCardInfoString = new ArrayList<>();
    private StackPane cardPane;
    private int clickedIndex;
    // Creation des tableaux pour les lignes
    private int[][] grid1 = new int[6][2];
    private int[][] grid2 = new int[6][2];
    private int[][] grid3 = new int[6][2];
    private int[][] grid4 = new int[6][2];
    private int[][] rightCardList = new int[players.size()][3];
    private int indexLastCardGrid1 = 0;
    private int indexLastCardGrid2 = 0;
    private int indexLastCardGrid3 = 0;
    private int indexLastCardGrid4 = 0;
    List<Polygon> arrowList = new ArrayList<>();
    private Polygon arrowNode;
    private boolean isGamePaused;
    private int selectedRow = -1;
    private int cardPlay;
    private int[][] rightCardListWithoutSort;
    private int delayCard = 5;
    private Music music;

    @Override
    public void start(Stage primaryStage) {
        String filePath2 = getClass().getResource("/music/Interville.wav").getFile();
        music = new Music();
        music.playMusic(filePath2);

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
            cardHBox.setId("cardHBox_" + i); // Ajoutez un identifiant unique
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
            arrowNode = arrow.getArrow(50, 40); // définit la largeur et la hauteur de la flèche
            arrowNode.setVisible(false);
            cardHBox.getChildren().add(arrowNode);
            arrowList.add(arrowNode);
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
                    cardPane.setBorder(new Border(new BorderStroke(colorList[players.size() - (players.size() % 3) + j], BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
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

            playerScoreLabel = new Label(players.get(i).getScore() + "");
            playerScoreLabel.setId("playerScoreLabel" + i);
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

//// Charger l'image du fond d'écran
        Image backgroundImage = new Image(getClass().getResource("/background/background2.png").toString());

//// Créer un objet BackgroundImage avec l'image chargée
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT
        );
//
//// Appliquer le fond d'écran à la racine (root) du BorderPane
        mainPane.setBackground(new Background(background));

// Afficher la fenêtre principale
        primaryStage.show();


        // Remplir la main du joueur
        fullHand();

        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                grid1[0][0] = Game.getRemainingCards().get(i).getNumber();
                grid1[0][1] = Game.getRemainingCards().get(i).getPoints();
            } else if (i == 1) {
                grid2[0][0] = Game.getRemainingCards().get(i).getNumber();
                grid2[0][1] = Game.getRemainingCards().get(i).getPoints();
            } else if (i == 2) {
                grid3[0][0] = Game.getRemainingCards().get(i).getNumber();
                grid3[0][1] = Game.getRemainingCards().get(i).getPoints();
            } else {
                grid4[0][0] = Game.getRemainingCards().get(i).getNumber();
                grid4[0][1] = Game.getRemainingCards().get(i).getPoints();
            }

            // Remplir le premier tableau (4 cartes à gauche)
            addCard(i, 0, Game.getRemainingCards().get(i).getNumber() + ".png");
            //TODO les 4 premieres cartes son a enlever du jeu dcp. -> ou tout redistribuer donc pas si grave.
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

        if (players.get(playerPlaying - 1).getType().equals("Bot 1")) {
            botPlay();
        }
        if (players.get(playerPlaying - 1).getType().equals("Bot 2")) {
            botPlay2();
        }
        if (players.get(playerPlaying - 1).getType().equals("Bot 3")) {
            botPlay3();
        }
    }

    private void botPlay2() {
        Card[] handCards = players.get(playerPlaying - 1).getHandCards();
        Arrays.sort(handCards, (card1, card2) -> Integer.compare(card1.getNumber(), card2.getNumber()));
        for (Card card : handCards) {
            System.out.println("Card: " + card.getNumber() + ", Points: " + card.getPoints());
        }
        int witchCardPlay = round - 1;
        if (witchCardPlay != 10) {
            StackPane cardPane = (StackPane) cardBarBox.getChildren().get(witchCardPlay);
            String cardPaneId = cardPane.getId();
            clickedIndex = Integer.parseInt(cardPaneId.substring(cardPaneId.lastIndexOf("_") + 1));
            System.out.println("CardPane clicked: " + clickedIndex);
            System.out.println("Image clicked");
            System.out.println("Card clicked: x = " + witchCardPlay + ", y = " + 0);

            // ATTENTION X = Colonnes, Y = Lignes.
            changeCardPosition(witchCardPlay, 0, (playerPlaying - 1) / 3, (playerPlaying - 1) % 3, true);
            // Désactiver les clics sur les images
            canClickArrow = true;
            // Désactiver les clics sur les flèches
            arrowSet.forEach(arrow2 -> arrow2.setOnMouseClicked(null));
            canClickArrow = false;
        }
    }

    private void botPlay3() {
        int witchCardPlay2 = 0;
        Card[] handCards = players.get(playerPlaying - 1).getHandCards();
        boolean[] handCards2 = players.get(playerPlaying - 1).getIsCardPlayed();

        int numberRow1 = grid1[indexLastCardGrid1][0];
        int numberRow2 = grid2[indexLastCardGrid2][0];
        int numberRow3 = grid3[indexLastCardGrid3][0];
        int numberRow4 = grid4[indexLastCardGrid4][0];
        Card closestCard = null;
        int closestDiff = Integer.MAX_VALUE;
        for (int i = 0; i < handCards.length; i++) {
            if (handCards2[i]) {
                continue;
            } else {
                int cardNumber = handCards[i].getNumber();
                int diff;
                if (cardNumber > numberRow1 && (diff = cardNumber - numberRow1) < closestDiff) {
                    closestCard = handCards[i];
                    closestDiff = diff;
                }
                if (cardNumber > numberRow2 && (diff = cardNumber - numberRow2) < closestDiff) {
                    closestCard = handCards[i];
                    closestDiff = diff;
                }
                if (cardNumber > numberRow3 && (diff = cardNumber - numberRow3) < closestDiff) {
                    closestCard = handCards[i];
                    closestDiff = diff;
                }
                if (cardNumber > numberRow4 && (diff = cardNumber - numberRow4) < closestDiff) {
                    closestCard = handCards[i];
                    closestDiff = diff;
                }
            }
        }

        if (closestCard != null) {
            int witchCardPlay = Arrays.asList(handCards).indexOf(closestCard);
            if (players.get(playerPlaying - 1).getIsCardPlayed()[witchCardPlay]) {
                botPlay3();
            } else {
                players.get(playerPlaying - 1).setIsCardPlayed(witchCardPlay);
                StackPane cardPane = (StackPane) cardBarBox.getChildren().get(witchCardPlay);
                String cardPaneId = cardPane.getId();
                clickedIndex = Integer.parseInt(cardPaneId.substring(cardPaneId.lastIndexOf("_") + 1));
                System.out.println("CardPane clicked: " + clickedIndex);
                System.out.println("Image clicked");
                System.out.println("Card clicked: x = " + witchCardPlay + ", y = " + 0);

                // ATTENTION X = Colonnes, Y = Lignes.
                changeCardPosition(witchCardPlay, 0, (playerPlaying - 1) / 3, (playerPlaying - 1) % 3, true);

                // Désactiver les clics sur les images
                canClickArrow = true;
                // Désactiver les clics sur les flèches
                arrowSet.forEach(arrow2 -> arrow2.setOnMouseClicked(null));
                canClickArrow = false;
            }
        } else {
//            if (handCards2[0] && handCards2[1] && handCards2[2] && handCards2[3] && handCards2[4] && handCards2[5] && handCards2[6] && handCards2[7] && handCards2[8] && handCards2[9]) {
//                return;
//            }
            //Arrays.sort(handCards, (card1, card2) -> Integer.compare(card1.getNumber(), card2.getNumber()));
            for (int i = 0; i < handCards.length; i++) {
                System.out.println("Card: " + handCards[i].getNumber() + ", Points: " + handCards[i].getPoints());
                if (handCards2[i]) {
                    continue;
                } else {
                    witchCardPlay2 = i;
                }
            }
            players.get(playerPlaying - 1).setIsCardPlayed(witchCardPlay2);
            StackPane cardPane = (StackPane) cardBarBox.getChildren().get(witchCardPlay2);
            String cardPaneId = cardPane.getId();
            clickedIndex = Integer.parseInt(cardPaneId.substring(cardPaneId.lastIndexOf("_") + 1));
            System.out.println("CardPane clicked: " + clickedIndex);
            System.out.println("Image clicked");
            System.out.println("Card clicked: x = " + witchCardPlay2 + ", y = " + 0);

            // ATTENTION X = Colonnes, Y = Lignes.
            changeCardPosition(witchCardPlay2, 0, (playerPlaying - 1) / 3, (playerPlaying - 1) % 3, true);
            // Désactiver les clics sur les images
            canClickArrow = true;
            // Désactiver les clics sur les flèches
            arrowSet.forEach(arrow2 -> arrow2.setOnMouseClicked(null));
            canClickArrow = false;

        }
    }

    private void botPlay() {
        int randomValidIndex = getRandomValidIndex();
        if (randomValidIndex != -1) {
            StackPane cardPane = (StackPane) cardBarBox.getChildren().get(randomValidIndex);
            String cardPaneId = cardPane.getId();
            clickedIndex = Integer.parseInt(cardPaneId.substring(cardPaneId.lastIndexOf("_") + 1));
            System.out.println("CardPane clicked: " + clickedIndex);
            System.out.println("Image clicked");
            System.out.println("Card clicked: x = " + randomValidIndex + ", y = " + 0);

            // ATTENTION X = Colonnes, Y = Lignes.
            changeCardPosition(randomValidIndex, 0, (playerPlaying - 1) / 3, (playerPlaying - 1) % 3, true);
            // Désactiver les clics sur les images
            canClickArrow = true;
            // Désactiver les clics sur les flèches
            arrowSet.forEach(arrow2 -> arrow2.setOnMouseClicked(null));
            canClickArrow = false;
        }
    }


    private int getRandomValidIndex() {
        List<Integer> validIndices = new ArrayList<>();

        for (int i = 0; i < cardBarBox.getChildren().size(); i++) {
            StackPane cardPane = (StackPane) cardBarBox.getChildren().get(i);
            // Vérifier si la case contient un élément (ajoutez des conditions selon vos besoins)
            if (cardPane.getChildren().size() > 0) {
                validIndices.add(i);
            }
        }

        if (validIndices.isEmpty()) {
            return -1; // Aucune case valide trouvée
        }

        Random random = new Random();
        int randomIndex = random.nextInt(validIndices.size());
        return validIndices.get(randomIndex);
    }

    private void addCard(int i, int j, String imageName) {
        //PauseTransition delay = new PauseTransition(Duration.seconds(5));
        //delay.setOnFinished(event -> {
        StackPane cardPane = (StackPane) ((HBox) gameCardBarBox.getChildren().get(i)).getChildren().get(j);
        URL imageUrl = Board.class.getResource("/images/cards/");
        ImageView imageView = new ImageView(new Image(imageUrl + imageName));
        cardPane.getChildren().add(imageView);
    }

    private void addCard(int i, int j, String imageName, int delayTime) {
        PauseTransition delay = new PauseTransition(Duration.seconds(delayTime));
        delay.setOnFinished(event -> {
            addCard(i, j, imageName);
        });
        delay.play();
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
            hideCardInHand();
            flipImage(animatedImageView);
        }

        animatedImageView.setX(startBounds.getMinX());
        animatedImageView.setY(startBounds.getMinY());
        // Add the animated ImageView to the scene
        Pane root = (Pane) cardBarBox.getScene().getRoot();
        root.getChildren().add(animatedImageView);

        TranslateTransition tt = new TranslateTransition(Duration.seconds(time), animatedImageView);
        tt.setToX(endBounds.getMinX() - startBounds.getMinX());
        tt.setToY(endBounds.getMinY() - startBounds.getMinY());

        tt.setOnFinished(event -> {
            root.getChildren().remove(animatedImageView);
            imageView.setRotate(0); // Reset rotation to 0 degrees
            String imagePath = "/images/cards/backside.png";
            URL imageUrl = Board.class.getResource(imagePath);
            imageView.setImage(new Image(imageUrl.toExternalForm()));
            newCardPane.getChildren().add(imageView);
        });

        // Start the transition
        tt.play();

        players.get(playerPlaying - 1).setIsCardPlayed(x);

        System.out.println("Moved card from (" + x + ", " + y + ") to (" + newX + ", " + newY + ")");
    }

    public void changeCardPosition2(int x, int y, int z) {
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> {
            for (int i = 0; i < rightCardList.length; i++) {
                StackPane newCardPane = (StackPane) ((HBox) playingCards.getChildren().get((i / 3))).getChildren().get(i % 3);
                ImageView imageView = (ImageView) newCardPane.getChildren().get(0);
                // va pr chaque carte a deux coordo d'ou le pb de duplication
                StackPane nCardPane = (StackPane) ((HBox) gameCardBarBox.getChildren().get(y)).getChildren().get(z);

                // Get the start and end coordinates for the current card
                Bounds startBounds = newCardPane.localToScene(newCardPane.getBoundsInLocal());
                Bounds endBounds = nCardPane.localToScene(nCardPane.getBoundsInLocal());

                // Create a new ImageView to animate for the current card
                ImageView animatedImageView = new ImageView(imageView.getImage());
                animatedImageView.setX(startBounds.getMinX());
                animatedImageView.setY(startBounds.getMinY());

                // Add the animated ImageView to the scene
                Pane root = (Pane) cardBarBox.getScene().getRoot();
                root.getChildren().add(animatedImageView);

                TranslateTransition tt = new TranslateTransition(Duration.seconds(2), animatedImageView);
                tt.setToX(endBounds.getMinX() - startBounds.getMinX());
                tt.setToY(endBounds.getMinY() - startBounds.getMinY());

                // Start the transition
                tt.play();


                tt.setOnFinished(event4 -> {
                    root.getChildren().remove(animatedImageView);
                    newCardPane.getChildren().clear();
                    suprHideCard();
                });
            }
        });
        delay.play();
    }


    public void changeCardPositionBis(int cardNumber) {
        String deuxiemeElement = clickedCardInfoString.get(1);
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
            clickedCardInfoString.add(info);
            System.out.println(clickedCardInfoString);
//            if (clickedCardInfoString.size() == numberOfPlayers) {
//                System.out.println("Message : Le nombre d'informations est égal au nombre de joueurs en cours de jeu.");
//                // Supprimer l'image "backside" du nouvel emplacement
////                changeCardPositionBis(103);
//
//            }

            // Creation du tableau equivalent a info (chaines de caracteres).
            rightCardList[playerPlaying - 1][0] = playerPlaying;
            rightCardList[playerPlaying - 1][1] = handCards[clickedIndex].getNumber();
            rightCardList[playerPlaying - 1][2] = handCards[clickedIndex].getPoints();

            playerPlaying++;

            if (playerPlaying > players.size()) {

                playerPlaying = playerPlaying % players.size();
                rightCardListWithoutSort = rightCardList.clone();
                Arrays.sort(rightCardList, Comparator.comparingInt(o -> o[1]));
                System.out.println(Arrays.deepToString(rightCardList));
                hideCardInHand();

                revealCards();
                round++;
                clickedCardInfoString.clear(); //Permet de reset la liste des cartes jouées (fin de tour)

            }
            fullHand();
            currentRoundLabel.setText("Tour " + round);
            currentPlayerLabel.setText(players.get(playerPlaying - 1).getName());
            currentPlayerLabel.setTextFill(colorList[playerPlaying - 1]);
        });

        parallelTransition.play();

    }

    public void witchGrid() {

        // Récupérer la carte jouée dans l'ordre croissant
        for (int i = 0; i < rightCardList.length; i++) {
            // Récupérer la carte jouée
            int whoPlayerPlayThisCard = rightCardList[i][0] - 1;
            int card = rightCardList[i][1];
            int point = rightCardList[i][2];

            System.out.println("Carte jouée : " + card + " par le joueur " + whoPlayerPlayThisCard);
            // Récupérer la dernière carte de chaque ligne
            int numberRow1 = grid1[indexLastCardGrid1][0];
            System.out.println("Dernière carte de la ligne 1 : " + numberRow1);
            int numberRow2 = grid2[indexLastCardGrid2][0];
            System.out.println("Dernière carte de la ligne 2 : " + numberRow2);
            int numberRow3 = grid3[indexLastCardGrid3][0];
            System.out.println("Dernière carte de la ligne 3 : " + numberRow3);
            int numberRow4 = grid4[indexLastCardGrid4][0];
            System.out.println("Dernière carte de la ligne 4 : " + numberRow4);


//            if (card < numberRow1 && card < numberRow2 && card < numberRow3 && card < numberRow4) {
//                isGamePaused = true;
//                for (int j = 0; j < arrowList.size(); j++) {
//                    Polygon arrowNode = arrowList.get(j);
//                    arrowNode.setVisible(true); // Rendre la flèche visible
//                    final int arrowIndex = j;
//                    arrowNode.setOnMouseClicked(event -> {
//                        System.out.println("Flèche cliquée : " + arrowIndex + 1);
//                    });
//                }
//                //TODO : ATTENDRE QUE LE JOUEUR CLIQUE SUR UNE FLECHE POUR CONTINUER
//                //TODO : Ajouter la carte à la ligne souhaitée + CLEAR ROW + AJOUT DES POINT
//                //TODO : Faire disparaitre les flèches
//                isGamePaused = false;
//            }

            // Trouver le placement sachant que l'on met la carte au plus proche par valeur croissante
            // Comparer la carte avec les dernières cartes de chaque ligne pour trouver la position
            int diff1 = card - numberRow1;
            int diff2 = card - numberRow2;
            int diff3 = card - numberRow3;
            int diff4 = card - numberRow4;

            // Trouver la plus petite différence positive
            int minPositiveDiff = Integer.MAX_VALUE;

            if (diff1 > 0) {
                minPositiveDiff = Math.min(minPositiveDiff, diff1);
            }
            if (diff2 > 0) {
                minPositiveDiff = Math.min(minPositiveDiff, diff2);
            }
            if (diff3 > 0) {
                minPositiveDiff = Math.min(minPositiveDiff, diff3);
            }
            if (diff4 > 0) {
                minPositiveDiff = Math.min(minPositiveDiff, diff4);
            }

            // Placer le numéro de la carte dans la bonne Grid avec la plus petite différence positive
            if (minPositiveDiff == diff1) {
                // Placer dans grid1
                grid1[indexLastCardGrid1 + 1][0] = card;
                grid1[indexLastCardGrid1 + 1][1] = point;
                //TODO Faire la transition
                //premet d'afficher dans la cardPane
                changeCardPosition2(0, 0, indexLastCardGrid1 + 1);
                addCard(0, indexLastCardGrid1 + 1, card + ".png", delayCard);
                indexLastCardGrid1++;
            } else if (minPositiveDiff == diff2) {
                // Placer dans grid2
                grid2[indexLastCardGrid2 + 1][0] = card;
                grid2[indexLastCardGrid2 + 1][1] = point;
                //TODO Faire la transition
                //premet d'afficher dans la cardPane
                changeCardPosition2(1, 1, indexLastCardGrid2 + 1);
                addCard(1, indexLastCardGrid2 + 1, card + ".png", delayCard);
                indexLastCardGrid2++;

            } else if (minPositiveDiff == diff3) {
                // Placer dans grid3
                grid3[indexLastCardGrid3 + 1][0] = card;
                grid3[indexLastCardGrid3 + 1][1] = point;
                //TODO Faire la transition
                //premet d'afficher dans la cardPane
                changeCardPosition2(2, 2, indexLastCardGrid3 + 1);
                addCard(2, indexLastCardGrid3 + 1, card + ".png", delayCard);
                indexLastCardGrid3++;
            } else if (minPositiveDiff == diff4) {
                // Placer dans grid4
                grid4[indexLastCardGrid4 + 1][0] = card;
                grid4[indexLastCardGrid4 + 1][1] = point;
                //TODO Faire la transition
                //premet d'afficher dans la cardPane
                changeCardPosition2(3, 3, indexLastCardGrid4 + 1);
                addCard(3, indexLastCardGrid4 + 1, card + ".png", delayCard);
                indexLastCardGrid4++;
            } else {
                //TODO : Pour le moment prendre la ligne avec le moins de point
                int minPointRow = Math.min(Math.min(Spoint(grid1), Spoint(grid2)), Math.min(Spoint(grid3), Spoint(grid4)));
                if (minPointRow == Spoint(grid1)) {
                    int points = Spoint(grid1);
                    System.out.println(points);
                    grid1[0][0] = card;
                    changeCardPosition2(0, 0, indexLastCardGrid1);
                    addCard(0, 0, grid1[0][0] + ".png", delayCard);
                    PauseTransition delay = new PauseTransition(Duration.seconds(delayCard));
                    delay.setOnFinished(event -> {
                        for (int z = 1; z <= 5; z++) {
                            StackPane cardPane = (StackPane) ((HBox) gameCardBarBox.getChildren().get(0)).getChildren().get(z);
                            cardPane.getChildren().clear();
                        }
                    });

                    delay.play();
                    //TODO reset la ligne
                    int score = players.get(whoPlayerPlayThisCard).getScore() + points;
                    players.get(whoPlayerPlayThisCard).setScore(score);
                    String playerScoreLabelID = "playerScoreLabel" + whoPlayerPlayThisCard;
                    Node node = scoreBox.lookup("#" + playerScoreLabelID);
                    if (node instanceof Label) {
                        Label playerScoreLabel = (Label) node;
                        playerScoreLabel.setText(String.valueOf(score));
                    }
                    //playerScoreLabel.setText(players.get(whoPlayerPlayThisCard).getScore() + "");
                    indexLastCardGrid1 = 0;
                } else if (minPointRow == Spoint(grid2)) {
                    int points = Spoint(grid2);
                    System.out.println(points);
                    grid2[0][0] = card;
                    changeCardPosition2(1, 1, indexLastCardGrid2);
                    addCard(1, 0, grid2[0][0] + ".png", delayCard);
                    PauseTransition delay = new PauseTransition(Duration.seconds(delayCard));
                    delay.setOnFinished(event -> {
                        for (int z = 1; z <= 5; z++) {
                            StackPane cardPane = (StackPane) ((HBox) gameCardBarBox.getChildren().get(1)).getChildren().get(z);
                            cardPane.getChildren().clear();
                        }
                    });

                    delay.play();
                    //TODO reset la ligne
                    int score = players.get(whoPlayerPlayThisCard).getScore() + points;
                    players.get(whoPlayerPlayThisCard).setScore(score);
                    String playerScoreLabelID = "playerScoreLabel" + whoPlayerPlayThisCard;
                    Node node = scoreBox.lookup("#" + playerScoreLabelID);
                    if (node instanceof Label) {
                        Label playerScoreLabel = (Label) node;
                        playerScoreLabel.setText(String.valueOf(score));
                    }
                    //playerScoreLabel.setText(players.get(whoPlayerPlayThisCard).getScore() + "");
                    indexLastCardGrid2 = 0;
                } else if (minPointRow == Spoint(grid3)) {
                    int points = Spoint(grid3);
                    System.out.println(points);
                    grid3[0][0] = card;
                    changeCardPosition2(2, 2, indexLastCardGrid3);
                    addCard(2, 0, grid3[0][0] + ".png", delayCard);
                    PauseTransition delay = new PauseTransition(Duration.seconds(delayCard));
                    delay.setOnFinished(event -> {
                        for (int z = 1; z <= 5; z++) {
                            StackPane cardPane = (StackPane) ((HBox) gameCardBarBox.getChildren().get(2)).getChildren().get(z);
                            cardPane.getChildren().clear();
                        }
                    });

                    delay.play();
                    //TODO reset la ligne
                    int score = players.get(whoPlayerPlayThisCard).getScore() + points;
                    players.get(whoPlayerPlayThisCard).setScore(score);
                    String playerScoreLabelID = "playerScoreLabel" + whoPlayerPlayThisCard;
                    Node node = scoreBox.lookup("#" + playerScoreLabelID);
                    if (node instanceof Label) {
                        Label playerScoreLabel = (Label) node;
                        playerScoreLabel.setText(String.valueOf(score));
                    }
                    //playerScoreLabel.setText(players.get(whoPlayerPlayThisCard).getScore() + "");
                    indexLastCardGrid3 = 0;
                } else if (minPointRow == Spoint(grid4)) {
                    int points = Spoint(grid4);
                    System.out.println(points);
                    grid4[0][0] = card;
                    changeCardPosition2(3, 3, indexLastCardGrid4);
                    addCard(3, 0, grid4[0][0] + ".png", delayCard);
                    PauseTransition delay = new PauseTransition(Duration.seconds(delayCard));
                    delay.setOnFinished(event -> {
                        for (int z = 1; z <= 5; z++) {
                            StackPane cardPane = (StackPane) ((HBox) gameCardBarBox.getChildren().get(3)).getChildren().get(z);
                            cardPane.getChildren().clear();
                        }
                    });

                    delay.play();
                    //TODO donner les points au gars et reset la ligne
                    int score = players.get(whoPlayerPlayThisCard).getScore() + points;
                    players.get(whoPlayerPlayThisCard).setScore(score);
                    String playerScoreLabelID = "playerScoreLabel" + whoPlayerPlayThisCard;
                    Node node = scoreBox.lookup("#" + playerScoreLabelID);
                    if (node instanceof Label) {
                        Label playerScoreLabel = (Label) node;
                        playerScoreLabel.setText(String.valueOf(score));
                    }
                    //playerScoreLabel.setText(players.get(whoPlayerPlayThisCard).getScore() + "");
                    indexLastCardGrid4 = 0;
                }

                //   isGamePaused = true;
                for (int j = 0; j < arrowList.size(); j++) {
                    Polygon arrowNode = arrowList.get(j);
                    arrowNode.setVisible(true); // Rendre la flèche visible
                    final int arrowIndex = j;
                    arrowNode.setOnMouseClicked(event -> {
                        System.out.println("Flèche cliquée : " + (arrowIndex + 1));
                        selectedRow = arrowIndex; // Enregistrer l'index de la ligne choisie par le joueur
//                        synchronized (this) {
//                            this.notify(); // Réveiller le thread principal en attente
//                        }
                        arrowNode.setVisible(false);
                    });

//                PauseTransition delay = new PauseTransition(Duration.seconds(2));
//                delay.setOnFinished(event -> {
//                    synchronized (this) {
//                        try {
//                            this.wait(); // Mettre le jeu en pause jusqu'à ce que le joueur fasse un choix
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//                delay.play();
                    // TODO : Ajouter la carte à la ligne choisie par le joueur + Vider la rangée + Ajouter des points
                    // TODO : Faire disparaître les flèches
                    isGamePaused = false;
                    selectedRow = -1; // Réinitialiser la ligne choisie pour la prochaine fois

                }
            }

            if (indexLastCardGrid1 == 5) {
                int points = Spoint(grid1);
                System.out.println(points);
                grid1[0][0] = grid1[5][0];
                PauseTransition delay = new PauseTransition(Duration.seconds(delayCard));
                delay.setOnFinished(event -> {
                    for (int z = 1; z <= 5; z++) {
                        StackPane cardPane = (StackPane) ((HBox) gameCardBarBox.getChildren().get(0)).getChildren().get(z);
                        cardPane.getChildren().clear();
                    }
                });

                delay.play();
                addCard(0, 0, grid1[0][0] + ".png", delayCard);
                //TODO reset la ligne
                int score = players.get(whoPlayerPlayThisCard).getScore() + points;
                players.get(whoPlayerPlayThisCard).setScore(score);
                String playerScoreLabelID = "playerScoreLabel" + whoPlayerPlayThisCard;
                Node node = scoreBox.lookup("#" + playerScoreLabelID);
                if (node instanceof Label) {
                    Label playerScoreLabel = (Label) node;
                    playerScoreLabel.setText(String.valueOf(score));
                }
                //playerScoreLabel.setText(players.get(whoPlayerPlayThisCard).getScore() + "");
                indexLastCardGrid1 = 0;
            } else if (indexLastCardGrid2 == 5) {
                int points = Spoint(grid2);
                System.out.println(points);
                grid2[0][0] = grid2[5][0];
                addCard(1, 0, grid2[0][0] + ".png", delayCard);
                PauseTransition delay = new PauseTransition(Duration.seconds(delayCard));
                delay.setOnFinished(event -> {
                    for (int z = 1; z <= 5; z++) {
                        StackPane cardPane = (StackPane) ((HBox) gameCardBarBox.getChildren().get(1)).getChildren().get(z);
                        cardPane.getChildren().clear();
                    }
                });

                delay.play();
                //TODO reset la ligne
                int score = players.get(whoPlayerPlayThisCard).getScore() + points;
                players.get(whoPlayerPlayThisCard).setScore(score);
                String playerScoreLabelID = "playerScoreLabel" + whoPlayerPlayThisCard;
                Node node = scoreBox.lookup("#" + playerScoreLabelID);
                if (node instanceof Label) {
                    Label playerScoreLabel = (Label) node;
                    playerScoreLabel.setText(String.valueOf(score));
                }
                //playerScoreLabel.setText(players.get(whoPlayerPlayThisCard).getScore() + "");
                indexLastCardGrid2 = 0;
            } else if (indexLastCardGrid3 == 5) {
                int points = Spoint(grid3);
                System.out.println(points);
                grid3[0][0] = grid3[5][0];
                addCard(2, 0, grid3[0][0] + ".png", delayCard);
                PauseTransition delay = new PauseTransition(Duration.seconds(delayCard));
                delay.setOnFinished(event -> {
                    for (int z = 1; z <= 5; z++) {
                        StackPane cardPane = (StackPane) ((HBox) gameCardBarBox.getChildren().get(2)).getChildren().get(z);
                        cardPane.getChildren().clear();
                    }
                });

                delay.play();
                //TODO reset la ligne
                int score = players.get(whoPlayerPlayThisCard).getScore() + points;
                players.get(whoPlayerPlayThisCard).setScore(score);
                String playerScoreLabelID = "playerScoreLabel" + whoPlayerPlayThisCard;
                Node node = scoreBox.lookup("#" + playerScoreLabelID);
                if (node instanceof Label) {
                    Label playerScoreLabel = (Label) node;
                    playerScoreLabel.setText(String.valueOf(score));
                }
                //playerScoreLabel.setText(players.get(whoPlayerPlayThisCard).getScore() + "");
                indexLastCardGrid3 = 0;
            } else if (indexLastCardGrid4 == 5) {
                int points = Spoint(grid4);
                System.out.println(points);
                grid4[0][0] = grid4[5][0];
                addCard(3, 0, grid4[0][0] + ".png", delayCard);
                PauseTransition delay = new PauseTransition(Duration.seconds(delayCard));
                delay.setOnFinished(event -> {
                    for (int z = 1; z <= 5; z++) {
                        StackPane cardPane = (StackPane) ((HBox) gameCardBarBox.getChildren().get(3)).getChildren().get(z);
                        cardPane.getChildren().clear();
                    }
                });

                delay.play();
                //TODO donner les points au gars et reset la ligne

                int score = players.get(whoPlayerPlayThisCard).getScore() + points;
                players.get(whoPlayerPlayThisCard).setScore(score);
                String playerScoreLabelID = "playerScoreLabel" + whoPlayerPlayThisCard;
                Node node = scoreBox.lookup("#" + playerScoreLabelID);
                if (node instanceof Label) {
                    Label playerScoreLabel = (Label) node;
                    playerScoreLabel.setText(String.valueOf(score));
                }
                //playerScoreLabel.setText(players.get(whoPlayerPlayThisCard).getScore() + "");
                indexLastCardGrid4 = 0;
            }
        }

    }


    private int Spoint(int[][] grid) {
        return grid[0][1] + grid[1][1] + grid[2][1] + grid[3][1] + grid[4][1];
    }

    private void hideCardInHand() {
        String imageName = "/images/cards/backside.png";
        for (int i = 0; i < 10; i++) {
            StackPane cardPane = (StackPane) cardBarBox.getChildren().get(i);
            URL imageUrl = Board.class.getResource(imageName);
            ImageView imageView = new ImageView(new Image(imageUrl.toString()));
            setOnClickAction(imageView, i, 0);
            cardPane.getChildren().add(imageView);
        }

    }

    private void suprHideCard() {
        String imageName = "/images/cards/backside.png";
        for (int i = 0; i < 10; i++) {
            StackPane cardPane = (StackPane) cardBarBox.getChildren().get(i);

            // Recherche de l'ImageView contenant l'image de dos
            ImageView backsideImageView = null;
            ObservableList<Node> children = cardPane.getChildren();
            for (Node child : children) {
                if (child instanceof ImageView) {
                    ImageView imageView = (ImageView) child;
                    Image image = imageView.getImage();
                    if (image != null && image.getUrl().equals(Board.class.getResource(imageName).toString())) {
                        backsideImageView = imageView;
                        break;
                    }
                }
            }

            // Suppression de l'ImageView de dos s'il a été trouvé
            if (backsideImageView != null) {
                cardPane.getChildren().remove(backsideImageView);
            }
        }
    }

    public void revealCards() {

        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        cardPlay = 0;
        delay.setOnFinished(event -> {
            hideCardInHand();
            //TODO EMEPECHER DE CLICKER TOUTES LES CARTES PENDANT CE TEMPS
            for (int x = 0; x <= (players.size() - 1) / 3; x++) {
                if (x == (players.size() - 1) / 3 && players.size() % 3 != 0) {
                    for (int y = 0; y < players.size() % 3; y++) {
                        StackPane newCardPane = (StackPane) ((HBox) playingCards.getChildren().get(x)).getChildren().get(y);
                        newCardPane.getChildren().clear();
                        ImageView imageView = new ImageView(new Image(Board.class.getResource("/images/cards/" + rightCardListWithoutSort[cardPlay][1] + ".png").toExternalForm()));
                        newCardPane.getChildren().add(imageView);
                        cardPlay++;
                    }
                } else {
                    for (int y = 0; y < 3; y++) {
                        StackPane newCardPane = (StackPane) ((HBox) playingCards.getChildren().get(x)).getChildren().get(y);
                        newCardPane.getChildren().clear();
                        ImageView imageView = new ImageView(new Image(Board.class.getResource("/images/cards/" + rightCardListWithoutSort[cardPlay][1] + ".png").toExternalForm()));
                        newCardPane.getChildren().add(imageView);
                        cardPlay++;
                    }
                }
            }
            witchGrid();
        });
        delay.play();
    }

}



