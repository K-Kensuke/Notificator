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

	public void NumberFormatError () {
		label.setText("数値を入力してください．");

		button.setOnAction(e -> {
			Stage stage = (Stage) button.getScene().getWindow();
			stage.close();
		});
	}
	public void IPaddressError () {
		label.setText("IPアドレスの値が不正です．");

		button.setOnAction(e -> {
			Stage stage = (Stage) button.getScene().getWindow();
			stage.close();
		});
	}

	public void PortNumError () {
		label.setText("Port番号の値が不正です．");

		button.setOnAction(e -> {
			Stage stage = (Stage) button.getScene().getWindow();
			stage.close();
		});
	}

	public void ParamNotSetError () {
		label.setText("接続に必要なパラメータが設定されていません．");

		button.setOnAction(e -> {
			Stage stage = (Stage) button.getScene().getWindow();
			stage.close();
		});
	}
}
