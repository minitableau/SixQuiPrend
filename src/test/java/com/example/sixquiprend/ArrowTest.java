package com.example.sixquiprend;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class ArrowTest {

    @Test
    public void getArrow_ReturnsPolygonWithCorrectPointsAndStyling() {
        // Arrange
        double width = 100;
        double height = 200;
        Arrow arrow = new Arrow();

        // Act
        Polygon polygon = arrow.getArrow(width, height);

        // Assert
        assertEquals(8, polygon.getPoints().size());
        assertEquals(0.0, polygon.getPoints().get(0));
        assertEquals(height / 2.0, polygon.getPoints().get(1));
        assertEquals(height / 2.0, polygon.getPoints().get(4));
        assertEquals(width, polygon.getPoints().get(6));
        assertEquals(Color.TRANSPARENT, polygon.getFill());
        assertEquals(Color.BLACK, polygon.getStroke());
    }



    @Test
    public void getDirection_ReturnsNull() {
        // Arrange
        Arrow arrow = new Arrow();

        // Act
        String direction = arrow.getDirection();

        // Assert
        assertNull(direction);
    }
}
