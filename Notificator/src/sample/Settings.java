package sample;

import javafx.fxml.FXMLLoader;
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
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("settings.fxml"));
			Stage stage = new Stage();
			stage.setTitle("Settings");
			stage.setScene(new Scene(fxmlLoader.load(), 400, 300));

			SettingsController settingsController = fxmlLoader.getController();
			settingsController.setMainStage(mainStage);
			stage.show();

			// 設定画面を閉じる処理
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
