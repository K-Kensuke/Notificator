package sample;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

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

	public void WriteValuesError () {
		label.setText("設定値のファイルへの書き込みに失敗しました．");

		button.setOnAction(e -> {
			Stage stage = (Stage) button.getScene().getWindow();
			stage.close();
		});
	}
}
