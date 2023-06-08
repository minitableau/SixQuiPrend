package com.example.sixquiprend;

import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Stage;


public class Arrow extends Application {

    private final int WIDTH = 500;
    private final int HEIGHT = 500;

    private Polygon arrow;

    private Board board;





    public Polygon getArrow(double width, double height) {
        Polygon arrow = new Polygon();
        arrow.getPoints().addAll(0.0, height / 2.0,
                height / 2.0, 0.0,
                height / 2.0, height / 4.0,
                (double) width, height / 4.0,
                (double) width, height - height / 4.0,
                height / 2.0, height - height / 4.0,
                height / 2.0, (double) height,
                0.0, height / 2.0);
        arrow.setFill(Color.TRANSPARENT);
        arrow.setStroke(Color.BLACK);



        // Gérer l'événement de clic sur la flèche
        arrow.setOnMouseClicked(event -> {
            System.out.println("Im clicked");
        });


        // Gérer l'événement de survol de la souris sur la flèche
        arrow.setOnMouseEntered(event -> {
            arrow.setFill(Color.LIGHTGRAY);
        });

        // Gérer l'événement de sortie de la souris de la flèche
        arrow.setOnMouseExited(event -> {
            arrow.setFill(Color.TRANSPARENT);
        });

        return arrow;
    }




    @Override
    public void start(Stage stage) throws Exception {
        arrow = getArrow(WIDTH, HEIGHT);
        stage.setScene(new javafx.scene.Scene(arrow.getParent()));
        stage.show();
    }

    public String getDirection() {
        return null;
    }
}

