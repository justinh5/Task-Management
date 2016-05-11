package TaskM.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import TaskM.Main;
import TaskM.interfaces.SubController;
import java.util.InputMismatchException;
import java.util.regex.Pattern;


/**
 * @author Justin Haupt
 *
 * The register controller controls the account registration scene, where
 * the user may sign up. A valid sign-up requires both the username and
 * password to be between 5 and 15 characters long, and containing only
 * numbers and letters.
 */

public class registerController implements SubController {

    // Reference to the base controller
    private baseController mainController;

    // Text/password fields
    @FXML private TextField usernameTxtField;
    @FXML private TextField passwordTxtField;
    @FXML private Label errorLabel;

    // Adds a reference to the base controller
    public void setSceneParent(baseController screenParent){
        mainController = screenParent;
    }


    /*
     * The back button switches the scene back to the login screen.
     */
    @FXML
    private void backButtonClick() {
        mainController.setScreen(Main.scene1ID);
        refresh();
    }


    /*
     * The back button switches the scene back to the login screen.
     */
    @FXML
    private void regButtonClick() {

        String username = usernameTxtField.getText();
        String password = passwordTxtField.getText();

        // Checks if the username and password are valid
        if(isValidSignUp(username, password)) {

            // Add the user's new account to the database and go to the user's task TaskM
            mainController.addNewUser(username, password);
            mainController.setScreen(Main.scene3ID);
            refresh();
        }
        else errorLabel.setVisible(true);    // Show error message
    }


    /*
     * Verifies if the the registration is valid. The username and password must
     * be between 5 and 15 characters long and contain only numbers and letters.
     * Also checks if the username is already in the database.
     */
    private boolean isValidSignUp(String username, String password) {

        int nameLength = username.length();
        int passwordLength = password.length();

        // Check if all the conditions are met
        try{
            if(nameLength < 5 || nameLength > 15) throw new InputMismatchException();
            if(!Pattern.matches("^[a-zA-Z0-9_.-]*$", username)) throw new InputMismatchException();
            if(passwordLength < 5 || passwordLength > 15) throw new InputMismatchException();
            if(!Pattern.matches("^[a-zA-Z0-9_.-]*$", password)) throw new InputMismatchException();
        } catch (InputMismatchException e) {
            errorLabel.setVisible(true);
            System.out.println("Invalid username or password!");
            return false;
        }
        // Check if the username is already in the database
        return mainController.isValidUsername(username);
    }


    /*
     * Clears the scene from any text in the input fields, and
     * hides the error label if it ever became visible.
     */
    private void refresh() {
        usernameTxtField.clear();
        passwordTxtField.clear();
        errorLabel.setVisible(false);
    }
}
