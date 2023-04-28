package com.example.sixquiprend.Jeu;

import java.util.Random;

public class Game {
    private int numberOfPlayers;
    private Player[] players;
    private Card[] deckCards;
    private int roundNumber;

    public Game(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.players = new Player[numberOfPlayers];
        this.deckCards = createDeck();
        this.roundNumber = 1;

        for (int i = 0; i < numberOfPlayers; i++) {
            players[i] = new Player("Player " + (i + 1));
            dealHandCards(players[i]);
        }
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

    private void dealHandCards(Player player) {
        for (int i = 0; i < 10; i++) {
            Card card = deckCards[i];
            player.addToHand(card);
            deckCards[i] = null;
        }
    }

    public Player[] getPlayers() {
        return players;
    }


}

