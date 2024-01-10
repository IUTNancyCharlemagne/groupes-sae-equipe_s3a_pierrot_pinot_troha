package tralleno;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

public class TaskManager extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Task Manager");

        // Creating root item for TreeView
        TreeItem<String> rootItem = new TreeItem<>("Root");

        // Creating sample sections
        TreeItem<String> section1 = new TreeItem<>("Section 1");
        TreeItem<String> section2 = new TreeItem<>("Section 2");

        // Adding tasks to section1
        TreeItem<String> task1 = new TreeItem<>("Task 1");
        TreeItem<String> task2 = new TreeItem<>("Task 2");

        // Adding subtasks to task1
        TreeItem<String> subtask1 = new TreeItem<>("Subtask 1");
        task1.getChildren().add(subtask1);

        // Adding sections and tasks to rootItem
        rootItem.getChildren().addAll(section1, section2);
        section1.getChildren().addAll(task1, task2);

        // Creating TreeView and setting root
        TreeView<String> treeView = new TreeView<>(rootItem);
        treeView.setShowRoot(false); // Hide the root node

        // Show the scene
        Scene scene = new Scene(treeView, 300, 250);
        primaryStage.setScene(scene);


        scene.getStylesheets().add(getClass().getResource("/tralleno/css/style.css").toExternalForm());
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);

    }
}