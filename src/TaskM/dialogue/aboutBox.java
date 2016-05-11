package TaskM.dialogue;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**
 * @author Justin Haupt
 * A small dialogue box object that provides some software details.
 */

public class aboutBox {

    public static void alertBox() {

        // All scene objects
        Label label1 = new Label("Task Manager 1.0");
        Label label2 = new Label("License: Free Edition");
        VBox layout = new VBox();
        Scene scene = new Scene(layout, 200, 200);
        Stage window = new Stage();

        layout.getChildren().addAll(label1, label2);
        layout.setAlignment(Pos.CENTER);

        // Display window and wait for it to be closed before returning
        window.initModality(Modality.APPLICATION_MODAL);  // Block events to other windows
        window.setTitle("About");
        window.setMinWidth(400);
        window.setScene(scene);
        window.showAndWait();
    }
}
