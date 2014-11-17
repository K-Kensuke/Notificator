package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by ken on 11/17/14.
 */
public class Settings {

	public void open (Stage mainStage) {
		System.out.println("--- open ---");

		try {
			Parent root = FXMLLoader.load(getClass().getResource("settings.fxml"));
			Stage stage = new Stage();
			stage.setTitle("Settings");
			stage.setScene(new Scene(root, 400, 300));
			stage.show();
			stage.setOnCloseRequest(e -> {
				stage.close();
				mainStage.show();
			});
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
