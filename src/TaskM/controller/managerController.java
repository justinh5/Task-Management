package TaskM.controller;

import javafx.fxml.FXML;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import TaskM.Main;
import TaskM.dialogue.aboutBox;
import TaskM.dialogue.addBox;
import TaskM.interfaces.SubController;
import java.util.List;


/**
 * @author Justin Haupt
 *
 * This is the controller for the management scene. On logging in to an account,
 * the user's saved three lists are populated into the fields. The listViews
 * allow for tasks to be inserted, deleted, and moved to and from any list.
 */

public class managerController implements SubController {

    private baseController mainController;  // reference to the base controller
    private addBox aBox = new addBox();     // an add-task alert box object

    @FXML private ListView<String> list1;   // to do
    @FXML private ListView<String> list2;   // in progress
    @FXML private ListView<String> list3;   // done

    // Add,delete, move buttons
    @FXML private Button addButton1;
    @FXML private Button addButton2;
    @FXML private Button delButton1;
    @FXML private Button delButton2;
    @FXML private Button oneTotwo;
    @FXML private Button twoToone;
    @FXML private Button twoTothree;
    @FXML private Button threeTotwo;


    // Adds a reference to the base controller
    public void setSceneParent(baseController screenParent){
        mainController = screenParent;
    }


    /*
     * Retrieve all saved tasks for a user from the database.
     * Must be retrieved one at a time.
     */
    public void loadTasks() {

        List<String> toDo = mainController.retrieveList(1);
        List<String> inProg = mainController.retrieveList(2);
        List<String> done = mainController.retrieveList(3);

        list1.getItems().addAll(toDo);
        list2.getItems().addAll(inProg);
        list3.getItems().addAll(done);
    }


    /*
     * Save the data from all three lists and send them to the database.
     */
    public void saveData() {
        ObservableList<String> one, two, three;
        one = list1.getItems();
        two = list2.getItems();
        three = list3.getItems();
        mainController.save(one, two, three);
    }


    /*
     * When an add button is clicked, this method prompts a dialogue box
     * to appear. The source of which button was clicked is retrieved so
     * that the task may be added to the correct list.
     */
    @FXML
    private void addClick(ActionEvent event) {
        String temp = aBox.alertBox();
        if(temp != null) {
            Object sourceButton = event.getSource();   // get the source button

            if(sourceButton.equals(addButton1))
                list1.getItems().add(temp);
            else if(sourceButton.equals(addButton2))
                list2.getItems().add(temp);
            else
                list3.getItems().add(temp);
        }
    }


    /*
     * Deletes a selected item from any list. Must first detect a
     * selected list item to remove.
     */
    @FXML
    private void deleteClick(ActionEvent event) {
        Object sourceButton = event.getSource();    //get the source button
        String toDelete;

        if(sourceButton.equals(delButton1)) {
            toDelete = list1.getSelectionModel().getSelectedItem();
            list1.getItems().remove(toDelete);
        }
        else if(sourceButton.equals(delButton2)) {
            toDelete = list2.getSelectionModel().getSelectedItem();
            list2.getItems().remove(toDelete);
        }
        else {
            toDelete = list3.getSelectionModel().getSelectedItem();
            list3.getItems().remove(toDelete);
        }
    }


    /*
     * Move one list item from a list to any adjacent list.
     * Copies the item to the new list and deletes from the old.
     * Prevents a null selection to be copied.
     */
    @FXML
    private void moveClick(ActionEvent event) {
        Object sourceButton = event.getSource();    //get the source button
        String toMove;

        if(sourceButton.equals(oneTotwo)) {
            toMove = list1.getSelectionModel().getSelectedItem();
            if(toMove != null) {
                list2.getItems().add(toMove);
                list1.getItems().remove(toMove);
            }
        }
        else if(sourceButton.equals(twoToone)) {
            toMove = list2.getSelectionModel().getSelectedItem();
            if(toMove != null){
                list1.getItems().add(toMove);
                list2.getItems().remove(toMove);
            }
        }
        else if(sourceButton.equals(twoTothree)){
            toMove = list2.getSelectionModel().getSelectedItem();
            if(toMove != null) {
                list3.getItems().add(toMove);
                list2.getItems().remove(toMove);
            }
        }
        else if(sourceButton.equals(threeTotwo)){
            toMove = list3.getSelectionModel().getSelectedItem();
            if(toMove != null) {
                list2.getItems().add(toMove);
                list3.getItems().remove(toMove);
            }
        }
    }


    /*
     * Removes all list items from the page.
     */
    @FXML
    private void deleteAll() {
        refresh();
    }


    /*
     * Before logging out and returning to the login screen,
     * the user's list data is saved. Then the TaskM scene is cleared.
     */
    @FXML
    private void logout() {
        saveData();
        mainController.setScreen(Main.scene1ID);    //go to the user's task TaskM
        refresh();
    }


    /*
     * Display a little 'about' box.
     */
    @FXML
    private void aboutClick() {
        aboutBox.alertBox();
    }


    /*
     * Exit the GUI by calling the close method from the base controller.
     */
    @FXML
    private void exit() {
        mainController.close();
    }


    /*
     * Clears the scene from any text in the listViews.
     */
    private void refresh() {
        list1.getItems().clear();
        list2.getItems().clear();
        list3.getItems().clear();
    }
}

