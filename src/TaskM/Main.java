package TaskM;

import javafx.application.Application;
import javafx.stage.Stage;
import TaskM.controller.baseController;


/**
 * @author Justin Haupt
 *
 * The Main class starts the main JavaFX application. The base controller
 * object is created here and all the scenes are loaded into it.
 */


public class Main extends Application {

    public static final String scene1ID = "login";
    public static final String scene1File = "/TaskM/view/login.fxml";
    public static final String scene2ID = "register";
    public static final String scene2File = "/TaskM/view/register.fxml";
    public static final String scene3ID = "manager";
    public static final String scene3File = "/TaskM/view/manager.fxml";


    @Override
    public void start(Stage primaryStage) {

        baseController mainController = new baseController(primaryStage);
        mainController.loadScene(Main.scene1ID, Main.scene1File);
        mainController.loadScene(Main.scene2ID, Main.scene2File);
        mainController.loadScene(Main.scene3ID, Main.scene3File);

        mainController.setScreen(Main.scene1ID);    // load the login screen
        primaryStage.setTitle("Task Manager");      // set the stage title
        primaryStage.show();                        // show the stage

        // Run this when the stage's red X is clicked.
        primaryStage.setOnCloseRequest( e -> mainController.close());
    }


    public static void main(String[] args) {
        launch(args);
    }
}






