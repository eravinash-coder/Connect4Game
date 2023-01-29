package com.connect4games;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {

    private static  final int COLUMNS=7;
    private static  final int ROWS=6;
    private static  final int CIRCLE_DIAMETER=80;
    private static  final String discColor1="#24303E";
    private static  final String discColor2="#4CAA88";

    private  boolean isplayeroneturn =true;
    private boolean isallowedtoinsert=true;
    private Disc[][] inserteddiscarray=new Disc[ROWS][COLUMNS];
    @FXML
    GridPane rootGridPane;
    @FXML
    Pane insertedDiscsPane;
    @FXML
    Label playerNameLabel;
    @FXML
   public  TextField playeArOneTextField,playerTwoTextField;
    @FXML
    Button setNamesButton;
    public  void createplayground(){
        Shape rectangleWithHoles=createGameStructuralGrid();
        rootGridPane.add(rectangleWithHoles,0,1);
        List<Rectangle> rectangleList=createclickable();
        for(Rectangle rectangle:rectangleList){
            rootGridPane.add(rectangle,0,1);

        }
    setNamesButton.setOnAction(event->{
       if(isplayeroneturn){
        playerNameLabel.setText(String.valueOf(playeArOneTextField.getText()));}
       else{
           playerNameLabel.setText(String.valueOf(playerTwoTextField.getText()));}

    });
    }
    private Shape createGameStructuralGrid(){
        Shape rectangleWithHoles=new javafx.scene.shape.Rectangle((COLUMNS+1)*CIRCLE_DIAMETER,(ROWS+1)*CIRCLE_DIAMETER);
        for(int row=0;row<ROWS;row++){
            for(int col=0;col<COLUMNS;col++) {
                Circle circle = new Circle();
                circle.setRadius(CIRCLE_DIAMETER / 2);
                circle.setCenterX(CIRCLE_DIAMETER / 2);
                circle.setCenterY(CIRCLE_DIAMETER / 2);
                circle.setSmooth(true);

                circle.setTranslateX(col * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4); //+5 for padding between circles
                circle.setTranslateY(row * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);

                rectangleWithHoles = Shape.subtract(rectangleWithHoles, circle);
            }}
        rectangleWithHoles.setFill(Color.WHITE);
        return rectangleWithHoles;
    }

    private List<Rectangle> createclickable(){
        java.util.List<Rectangle> rectangleList=new ArrayList<>();
        for(int col=0;col<COLUMNS;col++){
            Rectangle rectangle=new Rectangle(CIRCLE_DIAMETER,(ROWS+1)*CIRCLE_DIAMETER);
            rectangle.setFill(Color.TRANSPARENT);
            rectangle.setTranslateX(col * (CIRCLE_DIAMETER + 5) +CIRCLE_DIAMETER / 4);
            //hover
            rectangle.setOnMouseEntered(event->rectangle.setFill(Color.valueOf("#eeeeee26")));
            rectangle.setOnMouseExited(event->rectangle.setFill(Color.TRANSPARENT));
            //each rectangle is clickable
            final int column=col;
            rectangle.setOnMouseClicked(event->{
                if(isallowedtoinsert) {
                    isallowedtoinsert=false; //when more than 1 disc is inserted it would not allow

                    insertDisc(new Disc(isplayeroneturn), column);
                }});
            rectangleList.add(rectangle);
        }
        return rectangleList;
    }
    private void insertDisc(Disc disc,int column){

        int row=ROWS-1;
        while(row>=0){
            if (inserteddiscarray[row][column]==null)
                break;
            row--;
            if(row<0)
                return;
        }

        inserteddiscarray[row][column]=disc; //structural change ,inserting disc object
        insertedDiscsPane.getChildren().add(disc);
        disc.setTranslateX(column * (CIRCLE_DIAMETER + 5) +CIRCLE_DIAMETER / 4);
        TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),disc);
        translateTransition.setToY(row * (CIRCLE_DIAMETER + 5) + CIRCLE_DIAMETER / 4);
        int current_row=row;
        //change from player one to player 2 also change disc color
        translateTransition.setOnFinished(event->{
            isallowedtoinsert=true;
            if(gameEnded(current_row,column)){
                gameOver();
            }
            isplayeroneturn=!isplayeroneturn; //changing color
            playerNameLabel.setText(isplayeroneturn?playeArOneTextField.getText():playerTwoTextField.getText());

        });
        translateTransition.play();
    }

    private void gameOver() {

        String winner=isplayeroneturn?playeArOneTextField.getText():playerTwoTextField.getText();
        System.out.println("Winner is :"+winner);
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Winner");
        alert.setHeaderText("Winner is "+winner);
        ButtonType yesbtn=new ButtonType("Yes");
        ButtonType nobtn =new ButtonType("NO,Exit");
        alert.getButtonTypes().setAll(yesbtn,nobtn);
        //In order to display result after animation is complete
        Platform.runLater(()->{
            Optional<ButtonType>btnClicked=alert.showAndWait();
            if(btnClicked.isPresent() && btnClicked.get()==yesbtn){
                resetGame();
            }
            else{
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public void resetGame() {
        insertedDiscsPane.getChildren().clear(); //clear all inserted discs from pane
        //clear discs from stucture
        for(int row=0;row< inserteddiscarray.length;row++){
            for(int col=0;col<inserteddiscarray[row].length;col++){
                inserteddiscarray[row][col]=null;
            }
        }
        isplayeroneturn=true; //since player one will start the game
        playerNameLabel.setText(playeArOneTextField.getText());
        createplayground(); //prepare new playground
    }

    private boolean gameEnded(int row,int column){
        //for vertical Points
        List<Point2D>verticalPoints= IntStream.rangeClosed(row-3,row+3) //in vertical to make a match of 4,we have to compare the element
                //with above 3 and below 3 elements
                .mapToObj(r->new Point2D(r,column)) //Point2D class has (x,y) coordinates
                .collect(Collectors.toList());

        //for horizontal Points
        List<Point2D>horizontalPoints= IntStream.rangeClosed(column-3,column+3) //in vertical to make a match of 4,we have to compare the element
                //with above 3 and below 3 elements
                .mapToObj(c->new Point2D(row,c)) //Point2D class has (x,y) coordinates
                .collect(Collectors.toList());


        Point2D startPoint1=new Point2D(row-3,column+3);
        List<Point2D> diagonal1Points=IntStream.rangeClosed(0,6).mapToObj(i->(startPoint1.add(i,-i))).collect(Collectors.toList());

        Point2D startPoint2=new Point2D(row-3,column-3);
        List<Point2D> diagonal2Points=IntStream.rangeClosed(0,6).mapToObj(i->(startPoint2.add(i,i))).collect(Collectors.toList());

        boolean isEnded=checkCombinations(verticalPoints)||checkCombinations(horizontalPoints)||checkCombinations(diagonal1Points)
                ||checkCombinations(diagonal2Points);
        return isEnded;

    }
    private  boolean checkCombinations(List<Point2D> points){
        int chain=0;
        for(Point2D point:points){
            int rowIndexForArray= (int) point.getX();
            int columnIndexForArray= (int) point.getY();
            Disc disc=getDiscifPresent(rowIndexForArray,columnIndexForArray);
            if(disc!=null && disc.isPlayerOneMove==isplayeroneturn) {
                chain++;
                if (chain == 4)
                    return true;
            }
            else
                chain=0;

        }
        return false;
    }

    private Disc getDiscifPresent(int row,int column){
        if(row>=ROWS || row<0||column>=COLUMNS|| column<0)
            return null;
        return inserteddiscarray[row][column];

    }
    private  static class Disc extends Circle{
        private final boolean isPlayerOneMove;

        public Disc(boolean isPlayerOneMove){
            this.isPlayerOneMove=isPlayerOneMove;
            setRadius(CIRCLE_DIAMETER/2);
            setFill(isPlayerOneMove?Color.valueOf(discColor1):Color.valueOf(discColor2));
            setCenterX(CIRCLE_DIAMETER/2);
            setCenterY(CIRCLE_DIAMETER/2);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
