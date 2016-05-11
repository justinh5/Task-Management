package TaskM.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import TaskM.Main;
import TaskM.derby.derbyDB;
import TaskM.interfaces.SubController;
import java.util.HashMap;
import java.util.List;


/**
 * @author Justin Haupt
 *
 * The base controller oversees all the scenes and the sub-controllers
 * by managing the actions of the primary stage, loading scene resources,
 * changing scenes, and establishing this object as the parent.
 * This controller also manages a database object. All saved data must flow
 * through this object to reach the database. In the event of closing
 * the GUI, there is also a method here that is called to initiate the
 * shutdown process.
 */

public class baseController {

    private derbyDB db;        // database
    private Stage window;      // the primary stage
    private HashMap<String, Scene> scenes = new HashMap<>();           // holds all the scenes
    private managerController mController = new managerController();   // manager sub-controller

    /*
     * Default constructor
     */
    public baseController(Stage primaryStage) {
        this.window = primaryStage;
        this.db = new derbyDB();
    }


    /*
     * Loads all sub-controllers with their FXML files and stores them
     * into the hashmap. Also adds the CSS stylesheet.
     */
    public boolean loadScene(String name, String resource) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Parent root = (Parent) myLoader.load();
            Scene scene = new Scene(root);
            scenes.put(name, scene);
            scene.getStylesheets().add(getClass().getResource("/TaskM/CSS/light.css").toExternalForm());
            SubController controller = ((SubController) myLoader.getController());
            controller.setSceneParent(this);

            if(name.equals(Main.scene3ID))
                mController = myLoader.getController();

            return true;
        } catch (Exception e) {        // the FXML file is not found
            System.out.println(e.getMessage());
            return false;
        }
    }


    /*
     * Switch the scene. Requires the scene ID of the new scene.
     * First checks if the TaskM is being loaded so that the user's task
     * data can be loaded before switching scenes.
     */
    public boolean setScreen(final String name) {
        if (scenes.get(name) != null) {   // screen loaded
            if(name.equals(Main.scene3ID))
                mController.loadTasks();
            window.setScene(scenes.get(name));
            return true;
        } else {
            System.out.println("Could not load screen! \n");
            return false;
        }
    }


    /*
     * These methods relay data to and from the database and the sub-controllers.
     */
    public boolean isValidLogin(String username, String password) {
        return db.isValidLogin(username, password);
    }

    public boolean isValidUsername(String username) {
        return db.isValidUsername(username);
    }

    public void addNewUser(String username, String password) {
        db.addNewUser(username, password);
    }

    public List<String> retrieveList(int type) {
        return db.retrieveList(type);
    }

    public void save(ObservableList<String> list1, ObservableList<String> list2, ObservableList<String> list3) {
        db.save(list1, list2, list3);
    }


    /*
     * Close the program. Saves the logged-in user's data and shuts down the
     * database before closing the window.
     */
    public void close() {

        mController.saveData();
        db.shutdown();
        window.close();
    }
}





