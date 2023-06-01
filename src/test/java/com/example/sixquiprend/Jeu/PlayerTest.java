package com.example.sixquiprend.Jeu;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void constructor_SetsNameAndType() {
        // Arrange
        String expectedName = "Clément Tableau";
        String expectedType = "Joueur humain";

        // Act
        Player player = new Player(expectedName, expectedType);

        // Assert
        assertEquals(expectedName, player.getName());
        assertEquals(expectedType, player.getType());
    }

    @Test
    public void getName_ReturnsCorrectName() {
        // Arrange
        String expectedName = "Alexandre Pérou";
        String type = "Bot 1";
        Player player = new Player(expectedName, type);

        // Act
        String actualName = player.getName();

        // Assert
        assertEquals(expectedName, actualName);
    }

    @Test
    public void getType_ReturnsCorrectType() {
        // Arrange
        String name = "Alexandre LeBeau";
        String expectedType = "Bot 2";
        Player player = new Player(name, expectedType);

        // Act
        String actualType = player.getType();

        // Assert
        assertEquals(expectedType, actualType);
    }

    @Test
    public void constructor_SetsNameTypeAndInitialValues() {
        // Arrange
        String expectedName = "Alice";
        String expectedType = "Joueur humain";
        int expectedScore = 0;

        // Act
        Player player = new Player(expectedName, expectedType);

        // Assert
        assertEquals(expectedName, player.getName());
        assertEquals(expectedType, player.getType());
        assertEquals(expectedScore, player.getScore());
        assertNull(player.getHandCards()[0]);
        assertFalse(player.getIsCardPlayed()[0]);
    }

    @Test
    public void getIsCardPlayed_ReturnsCorrectIsCardPlayedArray() {
        // Arrange
        Player player = new Player("Charlie", "Bot 2");
        player.setIsCardPlayed(0);

        // Act
        boolean[] isCardPlayed = player.getIsCardPlayed();

        // Assert
        assertTrue(isCardPlayed[0]);
        assertFalse(isCardPlayed[1]);
    }

    @Test
    public void setName_SetsCorrectName() {
        // Arrange
        Player player = new Player("Alice", "Joueur humain");
        String expectedName = "Eve";

        // Act
        player.setName(expectedName);

        // Assert
        assertEquals(expectedName, player.getName());
    }

    @Test
    public void addToHand_AddsCardToHand() {
        // Arrange
        Player player = new Player("Bob", "Bot 1");
        Card card1 = new Card(1, 5);
        Card card2 = new Card(2, 3);

        // Act
        player.addToHand(card1);
        player.addToHand(card2);

        // Assert
        assertEquals(card1, player.getHandCards()[0]);
        assertEquals(card2, player.getHandCards()[1]);
    }

    @Test
    public void addToHand_NullCard_DoesNotAddCardToHand() {
        // Arrange
        Player player = new Player("Charlie", "Bot 2");
        Card card = null;

        // Act
        player.addToHand(card);

        // Assert
        assertNull(player.getHandCards()[0]);
    }

    @Test
    public void getScore_ReturnsCorrectScore() {
        // Arrange
        Player player = new Player("Alice", "Joueur humain");
        int expectedScore = 10;

        // Act
        player.setScore(expectedScore);

        // Assert
        assertEquals(expectedScore, player.getScore());
    }



}
