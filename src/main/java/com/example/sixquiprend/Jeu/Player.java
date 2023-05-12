package com.example.sixquiprend.Jeu;

public class Player {
    private String name;
    private Card[] handCards;

    public Player(String name) {
        this.name = name;
        this.handCards = new Card[10];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Card[] getHandCards() {
        return handCards;
    }

    public void addToHand(Card card) {
        if (card == null) {
            return;
        }
        for (int i = 0; i < handCards.length; i++) {
            if (handCards[i] == null) {
                handCards[i] = card;
                break;
            }
        }
    }

//    public static void removeCardFromHand(Player player, int index) {
//        if (index < 0 || index >= player.getHandCards().length) {
//            return;
//        }
//        Card[] hand = player.getHandCards();
//        Card[] newHand = new Card[hand.length - 1];
//
//        for (int i = 0; i < hand.length; i++) {
//            if (i == index) {
//                continue;
//            }
//            newHand[i] = hand[i];
//        }
//        handCards = newHand;
//
//    }


}
