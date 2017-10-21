package groupthree;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This is the controller class for Farkle. It is the class that interfaces between
 * the FXML files generated by Scene Builder and the local variables and objects in Java
 * via the @FXML annotation.
 * @author Wes Harrison
 * @version 1.0
 */
public class FarkleController implements FarkleInterface {
    
    /**
     * The java-FXML accessor variable for the first rectangle.
     */
    @FXML
   private Rectangle rect1;
    /**
     * The java-FXML accessor variable for the second rectangle.
     */
    @FXML
   private Rectangle rect2;
    /**
     * The java-FXML accessor variable for the third rectangle.
     */
    @FXML
   private Rectangle rect3;
    /**
     * The java-FXML accessor variable for the fourth rectangle.
     */
    @FXML
   private Rectangle rect4;
    /**
     * The java-FXML accessor variable for the fifth rectangle.
     */
    @FXML
   private Rectangle rect5;
    /**
     * The java-FXML accessor variable for the sixth rectangle.
     */
    @FXML
   private Rectangle rect6;
    /**
     * This is the label that keeps track of bank points.
     */
    @FXML
    private Label bankPoints;
    /**
     * This is the label that keeps track of the round points.
     */
    @FXML
    private Label roundPoints;





    /**
     * ArrayList of rectangles representing the dice on the screen.
     */
   private static ArrayList<Rectangle> rectangles = new ArrayList<>();

    private final DiceUILogic game = new DiceUILogic(this);

    public FarkleController() {


    }


    /**
     * When this method is called, it will exit the application.
     */
    public void exitHandler() {
        System.exit(0);
    }


    /**
     * When this method is called, it will change the Scene to GameScreen.
     * @param event The button push event that signals entrance
     *              into the main game screen.
     */
    public void enterGameScreenButtonPushed(final ActionEvent event)  {

        try {
            Parent gameScreenParent = FXMLLoader.load(getClass().getResource("GameScreen.fxml"));
            Scene gameScreen = new Scene(gameScreenParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(gameScreen);
            window.show();

        } catch (IOException e) {
            System.out.println("The GameScreen FXML file was not found.");
        }




    }

    /**
     * This method simply adds our rectangles into the rectangle ArrayList.
     */
    public void setRectangleArray() {
        rectangles.clear();
        rectangles.add(rect1);
        rectangles.add(rect2);
        rectangles.add(rect3);
        rectangles.add(rect4);
        rectangles.add(rect5);
        rectangles.add(rect6);
    }

    /**
     * This method handles the ActionEvent generated by pressing the "Roll Dice" button within the application.
     * It animates the current rectangles on the screen as well as utilizes the Dice class and initializes a random number
     * onto each one.
     * @param event is the ActionEvent generated by pressing the Roll Dice button.
     */
    public void rollTheDiceButtonPushed(final ActionEvent event) {

        // Adds rectangles to the rectangle arrayList.
        setRectangleArray();

        // Sets ArrayList of Dice with random values if they're not held.
        game.setHand();

        // Maps dice images to values.
        game.mapDice();

            //Animates the Dice
            Timeline diceAnimate = new Timeline(

                    new KeyFrame(Duration.ZERO,
                            ae -> game.setRectFill(game.d1)),
                    new KeyFrame(Duration.millis(111),
                            ae -> game.setRectFill(game.d2)),
                    new KeyFrame(Duration.millis(222),
                            ae -> game.setRectFill(game.d3)),
                    new KeyFrame(Duration.millis(333),
                            ae -> game.setRectFill(game.d4)),
                    new KeyFrame(Duration.millis(444),
                            ae -> game.setRectFill(game.d5)),
                    new KeyFrame(Duration.millis(555),
                            ae -> game.setRectFill(game.d6)),
                    new KeyFrame(Duration.millis(777),
                            ae -> game.getHand(rectangles)) // Calls getHand from our game instance and sets the fills.

            );

        diceAnimate.setCycleCount(1);
        diceAnimate.play();




        if (game.getRollCount() > 0 && game.isFarkle() ) {

            for (Rectangle rectangle : rectangles) {
                rectangle.setEffect(null);
            }

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initOwner(MainUI.getPrimaryStage());
            alert.setTitle("Farkle!");
            alert.setHeaderText("You have Farkled: Round Reset");
            alert.setContentText("Try again! If you farkle 3 times, you lose 1,000 from bank!");
            alert.show();
            roundPoints.setText(Integer.toString(game.getRoundScore()));
            bankPoints.setText(Integer.toString(game.getBankScore()));

        }

        game.setRolled(); // Increments the rolled variable.



    }


    /**
     * This method runs when the "Bank Points" button has been clicked.
     * @param event MouseEvent that this method takes as the input.
     */
    public void bankPointsButtonPushed( final ActionEvent event){
        game.setBankScore();
       bankPoints.setText(Integer.toString(game.getBankScore()));
        game.resetHand();
        game.setRollCount(0);
        roundPoints.setText(Integer.toString(game.getRoundScore()));



        if(game.wonGameStatus()){

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(MainUI.getPrimaryStage());
            alert.setTitle("You win!");
            alert.setHeaderText("Bank Reached 10,000!");
            alert.setContentText("You have won the game, please exit and start a new game!");
            alert.show();

        }




    }

    /**
     * This method will call methods from our game instance of DiceUILogic to
     * take our rectangles and add a glow, then hold the corresponding dice.
     * @param event The Mouse2Event generated by clicking a rectangle.
     */
    public void holdRectangles(MouseEvent event){


       Rectangle rectX = (Rectangle) event.getSource();

            game.checkRolled();
            game.setHoldStatus(rectX);
            roundPoints.setText(Integer.toString(game.getRoundScore()));



}

    /**
     * This method is to return our ArrayList of rectangles for use by other classes.
     * @return Current ArrayList<Rectangle> instantiated in FarkleController.
     */
    public ArrayList<Rectangle> getRectangles () {
        
        return rectangles;

    }




}


