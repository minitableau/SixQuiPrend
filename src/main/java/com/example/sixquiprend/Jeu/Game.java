package com.example.sixquiprend.Jeu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.security.cert.PolicyNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
    private int numberOfPlayers;
    private Player[] players;
    private static Card[] deckCards;
    private int roundNumber;
    private VBox gameCardBarBox;



    public Game(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.players = new Player[numberOfPlayers];
        this.deckCards = createDeck();
        this.roundNumber = 1;

        for (int i = 0; i < numberOfPlayers; i++) {
            players[i] = new Player("Player " + (i + 1));
        }
        dealHandCards();
        printPlayersCards();
        printRemainingCards();
    }


    private Card[] createDeck() {
        Card[] deck = new Card[104];
        int index = 0;
        for (int i = 1; i <= 104; i++) {
            int points = calculatePoints(i);
            deck[index] = new Card(i, points);
            index++;
        }
        shuffleDeck(deck);
        return deck;
    }

    private void shuffleDeck(Card[] deck) {
        Random random = new Random();
        for (int i = deck.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            Card temp = deck[i];
            deck[i] = deck[j];
            deck[j] = temp;
        }
    }

    private int calculatePoints(int cardNumber) {
        if (cardNumber == 55) {
            return 7;
        } else if (cardNumber % 11 == 0) {
            return 5;
        } else if (cardNumber % 10 == 0) {
            return 3;
        } else if (cardNumber % 10 == 5) {
            return 2;
        } else {
            return 1;
        }
    }

    private void dealHandCards() {
        int numberOfCardsPerPlayer = 10;
        int cardIndex = 0;
        for (int i = 0; i < numberOfPlayers; i++) {
            for (int j = 0; j < numberOfCardsPerPlayer; j++) {
                Card card = deckCards[cardIndex];
                players[i].addToHand(card);
                deckCards[cardIndex] = null;
                cardIndex++;
            }

        }
    }


    public Player[] getPlayers() {
        return players;
    }

    public void printPlayersCards() {
        for (int i = 0; i < numberOfPlayers; i++) {
            Player player = players[i];
            System.out.println("Joueur " + (i + 1) + " :");
            Card[] handCards = player.getHandCards();
            for (int j = 0; j < handCards.length; j++) {
                Card card = handCards[j];
                System.out.println("- Carte " + (j + 1) + " : " + card.getNumber() + " (valeur : " + card.getPoints() + ")");
            }
            System.out.println();

        }

    }
    public void printRemainingCards() {
        System.out.println("Cartes restantes dans le paquet :");
        int remainingCards = 0;
        for (int i = 0; i < deckCards.length; i++) {
            if (deckCards[i] != null) {
                remainingCards++;
                System.out.println("- Carte " + remainingCards + " : " + deckCards[i].getNumber() + " (valeur : " + deckCards[i].getPoints() + ")");
            }
        }
    }

    public static List<Card> getRemainingCards() {
        List<Card> remainingCards = new ArrayList<>();
        for (int i = 0; i < deckCards.length; i++) {
            if (deckCards[i] != null) {
                remainingCards.add(deckCards[i]);
            }
        }
        return remainingCards;
    }



}
