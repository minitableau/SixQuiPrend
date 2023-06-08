package com.example.sixquiprend.Jeu;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardTest {

    @Test
    public void constructor_SetsNumberAndPoints() {
        // Arrange
        int expectedNumber = 5;
        int expectedPoints = 10;

        // Act
        Card card = new Card(expectedNumber, expectedPoints);

        // Assert
        assertEquals(expectedNumber, card.getNumber());
        assertEquals(expectedPoints, card.getPoints());
    }

    @Test
    public void getNumber_ReturnsCorrectNumber() {
        // Arrange
        int expectedNumber = 7;
        int points = 5;
        Card card = new Card(expectedNumber, points);

        // Act
        int actualNumber = card.getNumber();

        // Assert
        assertEquals(expectedNumber, actualNumber);
    }

    @Test
    public void getPoints_ReturnsCorrectPoints() {
        // Arrange
        int number = 3;
        int expectedPoints = 15;
        Card card = new Card(number, expectedPoints);

        // Act
        int actualPoints = card.getPoints();

        // Assert
        assertEquals(expectedPoints, actualPoints);
    }
}
