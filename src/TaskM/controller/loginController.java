package TaskM.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import TaskM.Main;
import TaskM.interfaces.SubController;


/**
 * @author Justin Haupt
 *
 * The login controller controls the login scene, where the user must input
 * a username and password or choose to register a new account.
 */

public class loginController implements SubController {

    // Reference to the base controller
    private baseController mainController;

    // Text fields and one error label
    @FXML private TextField usernameTxtField;
    @FXML private TextField passwordTxtField;
    @FXML private Label errorLabel;


    // Adds a reference to the base controller
    public void setSceneParent(baseController screenParent){
        mainController = screenParent;
    }


    /*
     * Once the login button is clicked, the input from the text fields is
     * verified. Access is granted if the username and password are correct.
     */
    @FXML
    private void loginButtonClick() {
        String username = usernameTxtField.getText();
        String password = passwordTxtField.getText();

        //Checks if the username and password are valid
        if(mainController.isValidLogin(username, password)) {
            mainController.setScreen(Main.scene3ID);    //go to the user's task TaskM
            refresh();
        }
        else {
            errorLabel.setVisible(true);
            System.out.println("Invalid login!");
        }
    }


    /*
     * Once the login button is clicked, the input from the text fields is
     * verified. Access is granted if the username and password are correct.
     */
    @FXML
    private void signupButtonClick() {
        mainController.setScreen(Main.scene2ID);    //go to the registration scene
        refresh();
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