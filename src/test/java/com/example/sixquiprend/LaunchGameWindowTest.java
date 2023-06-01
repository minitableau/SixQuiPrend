//package com.example.sixquiprend;
//
//import javafx.embed.swing.JFXPanel;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.TextField;
//import javafx.stage.Stage;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//public class LaunchGameWindowTest {
//
//    @BeforeAll
//    public static void setup() {
//        // Initialisation de l'environnement JavaFX
//        JFXPanel panel = new JFXPanel();
//    }
//
//    @Test
//    public void addPlayerRow_MaxPlayersReached_NoNewRowAdded() {
//        // Arrange
//        LaunchGameWindow window = new LaunchGameWindow();
//        int expectedNumberOfRows = LaunchGameWindow.MAX_PLAYERS + 2;
//
//        // Act
//        for (int i = 0; i < expectedNumberOfRows; i++) {
//            window.addPlayerRow();
//        }
//
//        // Assert
//        assertEquals(expectedNumberOfRows, window.getCurrentRow());
//        window.addPlayerRow(); // Tentative d'ajouter une nouvelle ligne
//        assertEquals(expectedNumberOfRows, window.getCurrentRow());
//    }
//
//    @Test
//    public void addPlayerRow_NewRowAdded() {
//        // Arrange
//        LaunchGameWindow window = new LaunchGameWindow();
//        int expectedNumberOfRows = 3;
//
//        // Act
//        window.addPlayerRow();
//
//        // Assert
//        assertEquals(expectedNumberOfRows, window.getCurrentRow());
//    }
//
//    @Test
//    public void addPlayerRow_PlayerNameFieldAndComboBoxInitialized() {
//        // Arrange
//        LaunchGameWindow window = new LaunchGameWindow();
//
//        // Act
//        window.addPlayerRow();
//
//        // Assert
//        TextField playerNameField = (TextField) window.getGrid().getChildren().get(1);
//        ComboBox<String> playerTypeComboBox = (ComboBox<String>) window.getGrid().getChildren().get(2);
//        assertNull(playerNameField.getText());
//        assertEquals("Joueur humain", playerTypeComboBox.getValue());
//    }
//
//    @Test
//    public void startGame_CreatesPlayersList() {
//        // Arrange
//        LaunchGameWindow window = new LaunchGameWindow();
//        TextField playerNameField1 = new TextField("Joueur 1");
//        TextField playerNameField2 = new TextField("Joueur 2");
//        ComboBox<String> playerTypeComboBox1 = new ComboBox<>();
//        ComboBox<String> playerTypeComboBox2 = new ComboBox<>();
//        playerTypeComboBox1.setValue("Joueur humain");
//        playerTypeComboBox2.setValue("Bot 1");
//        window.getGrid().add(playerNameField1, 1, 2);
//        window.getGrid().add(playerTypeComboBox1, 2, 2);
//        window.getGrid().add(playerNameField2, 1, 3);
//        window.getGrid().add(playerTypeComboBox2, 2, 3);
//
//        // Act
//        window.startGame();
//
//        // Assert
//        List<Player> players = window.getGame().getPlayers();
//        assertEquals(2, players.size());
//        assertEquals("Joueur 1", players.get(0).getName());
//        assertEquals("Joueur humain", players.get(0).getType());
//        assertEquals("Joueur 2", players.get(1).getName());
//        assertEquals("Bot 1", players.get(1).getType());
//    }
//
//    @Test
//    public void stopMusic_MusicStopped() {
//        // Arrange
//        LaunchGameWindow window = new LaunchGameWindow();
//        Music music = new Music();
//        window.setMusic(music);
//
//        // Act
//        window.stopMusic();
//
//        // Assert
//        assertNull(music.getMediaPlayer());
//    }
//}
