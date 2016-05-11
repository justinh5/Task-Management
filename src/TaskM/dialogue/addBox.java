package TaskM.dialogue;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * @author Justin Haupt
 * An object that controls a small dialogue box that reads in user input
 * for a task to add. Verifies that the task is between 1-30 characters.
 */

public class addBox {

    private String toAdd;


    /*
     * Produces an alert box that reads in a string from the user.
     * The input is verified, then once it is valid, the string will
     * be returned to the method caller. Returns null if the string
     * is not valid.
     */
    public String alertBox() {

        toAdd = null;

        // All scene objects
        Label prompt = new Label("Please enter a short task description");
        Label error = new Label("Must be between 1-30 characters!");
        TextField txtField = new TextField();
        Button addButton = new Button("Add");
        VBox root = new VBox();
        Scene scene = new Scene(root, 250, 100);
        Stage window = new Stage();

        error.setTextFill(Color.RED);
        error.setVisible(false);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(prompt, error, txtField, addButton);

        window.setTitle("Add");
        window.setScene(scene);
        addButton.setOnAction(e -> check(window, error, txtField));
        window.showAndWait();

        return toAdd;
    }


    /*
     * Checks if the input from the textfield is between 1-30 characters.
     * Displays an error message if it is not. Upon the input of a valid
     * task, the window will close.
     */
    private void check(Stage window, Label error, TextField txtField) {
        int length = txtField.getText().length();
        if(length > 0 && length < 30) {
            this.toAdd = txtField.getText();
            window.close();
        }
        else error.setVisible(true);
    }
}
