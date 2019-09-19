/*
 *	[CS213] Assignment 1
 *	by Greg Melillo and Eric Kim
*/

package songlib.app;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SongLib extends Application {
	@Override
	public void start(Stage primaryStage) 
	throws IOException {
		
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(
				getClass().getResource("SongLibController.fxml"));
		
		
		BorderPane root = (BorderPane)loader.load();
		
		SongLibController listController = loader.getController();
		listController.start(primaryStage);

		Scene scene = new Scene(root, 600, 500);
		primaryStage.setScene(scene);
		primaryStage.show(); 
	}
	


	public static void main(String[] args) {
		launch(args);
	}

}
