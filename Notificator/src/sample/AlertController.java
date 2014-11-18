package sample;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Created by ken on 11/18/14.
 */
public class AlertController {
	public Label label;
	public Button button;

	public void IPaddressError () {
		label.setText("IPアドレスの値が不正です．");

		button.setOnAction(e -> {
			Stage stage = (Stage) button.getScene().getWindow();
			stage.close();
		});
	}
}
