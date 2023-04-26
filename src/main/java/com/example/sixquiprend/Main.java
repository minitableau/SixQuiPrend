package com.example.sixquiprend;

import javafx.application.Platform;

public class Main {
    public static void main(String[] args) {
        Platform.startup(() -> {
            LaunchGameWindow launchGameWindow = new LaunchGameWindow();
            launchGameWindow.show();
        });
    }
}
