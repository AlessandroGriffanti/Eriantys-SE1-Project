package com.gui;


import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class GameController {

    @FXML
    BorderPane borderPane;

    public void displayGamePanel(){
        Image image = new Image("/img/board.png");

        ImageView board = new ImageView();
        board.setImage(image);

        borderPane.setLeft(board);
        borderPane.setPadding(new Insets(5));
        //BorderPane.setMargin(board, new Insets(5));
    }

}
