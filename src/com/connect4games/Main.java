package com.connect4games;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private  Controller controller;
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Connect4games.fxml"));
        GridPane rootnode=loader.load();
        controller=loader.getController();
        controller.createplayground();

        MenuBar menuBar=createMenu();
        Pane menuPane = (Pane) rootnode.getChildren().get(0);
        menuPane.getChildren().add(menuBar);
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty()); //menubar equal to size of primary stage
        Scene scene = new Scene(rootnode);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect 4");
        primaryStage.show();
    }
    private MenuBar createMenu(){
        Menu fileMenu=new Menu("File");
        MenuItem newGame=new MenuItem("New game");
        newGame.setOnAction(event->controller.resetGame());

        MenuItem resetGame=new MenuItem("Reset game");
        resetGame.setOnAction(actionEvent -> controller.resetGame());
        MenuItem exitGame=new MenuItem("Exit game");
        exitGame.setOnAction(actionEvent -> { ExitGame();

        });
        fileMenu.getItems().addAll(newGame,resetGame,exitGame);


        Menu helpMenu=new Menu("Help");
        MenuItem aboutGame=new MenuItem("About game");
        aboutGame.setOnAction(actionEvent -> AboutConnect4());
        MenuItem aboutme=new MenuItem("About me");
        aboutme.setOnAction(actionEvent -> Aboutme());
        helpMenu.getItems().addAll(aboutGame,aboutme);
        MenuBar menuBar=new MenuBar();
        menuBar.getMenus().addAll(fileMenu,helpMenu);
        return menuBar;
    }

    private void Aboutme() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Me");
        alert.setResizable(true);
        alert.setHeaderText("Sakshi");
        alert.setContentText("I am new to this.Currently learning more.This is my first game");
        alert.show();
    }

    private void AboutConnect4() {

        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Connect Four");
        alert.setHeaderText("How to play?");
        alert.setResizable(true);
        alert.setContentText("Connect is a two-player connection board game, in which the players choose a color" +
                " and then take turns dropping colored discs into a seven-column, six-row vertically suspended grid." +
                " The pieces fall straight down, occupying the lowest available space within the column. " +
                "The objective of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's" +
                " own discs. Connect Four is a solved game. The first player can always win by playing the right moves.");
        alert.show();
    }

    private void ExitGame() {
        Platform.exit();
        System.exit(0);
    }

    
}
