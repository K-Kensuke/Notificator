package sample;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class SettingsController implements Initializable {

	public TextField serverIPField;
	public TextField serverPortField;
	public Button saveBtn;
	public Button cancelBtn;

	private String serverIP = "";
	private int serverPort = 0;

	private Stage mMainStage = null;

	SettingValues settingValues;

	@Override
	public void initialize (URL location, ResourceBundle resources) {
		settingValues = SettingValues.getInstance();

		serverIP = settingValues.getServerIP();
		if (!serverIP.equals("")) {
			serverIPField.setText(serverIP);
		}

		serverPort = settingValues.getServerPort();
		if (serverPort != 0) {
			serverPortField.setText(String.valueOf(serverPort));
		}

		saveBtn.setOnAction(e -> {
			serverIP = serverIPField.getCharacters().toString();
			System.out.println("serverIP : " + serverIP);

			try {
				serverPort = Integer.valueOf(serverPortField.getCharacters().toString());
			}
			catch (NumberFormatException exception) {
				// 数値以外が入力された場合
				try {
					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("alert.fxml"));
					Stage stage = new Stage();
					stage.setTitle("Error");
					stage.setScene(new Scene(fxmlLoader.load(), 300, 100));

					AlertController alertController = fxmlLoader.getController();
					alertController.NumberFormatError();
					stage.show();
				}
				catch (IOException ioexception) {
					ioexception.printStackTrace();
				}
			}

			final String ipRegex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
			Pattern ipPattern = Pattern.compile(ipRegex);

			final String portRegex = "(6553[0-5]|655[0-2]\\d|65[0-4]\\d{2}|6[0-4]\\d{3}|[1-5]\\d{4}|[1-9]\\d{0,3})";
			Pattern portPattern = Pattern.compile(portRegex);

			if (!ipPattern.matcher(serverIP).matches()) {
				// IPアドレスの入力値が不正
				try {
					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("alert.fxml"));
					Stage stage = new Stage();
					stage.setTitle("Error");
					stage.setScene(new Scene(fxmlLoader.load(), 300, 100));

					AlertController alertController = fxmlLoader.getController();
					alertController.IPaddressError();
					stage.show();
				}
				catch (IOException exception) {
					exception.printStackTrace();
				}
			}
			else if (!portPattern.matcher(String.valueOf(serverPort)).matches()) {
				// Port番号の入力値が不正
				try {
					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("alert.fxml"));
					Stage stage = new Stage();
					stage.setTitle("Error");
					stage.setScene(new Scene(fxmlLoader.load(), 300, 100));

					AlertController alertController = fxmlLoader.getController();
					alertController.PortNumError();
					stage.show();
				}
				catch (IOException exception) {
					exception.printStackTrace();
				}
			}
			else {
				// IPアドレス，ポート番号の入力値に異常なし
				settingValues.setServerIP(serverIP);
				settingValues.setServerPort(serverPort);

				try {
					// xmlファイルに書き込み，設定画面を閉じてメイン画面を呼び出す
					settingValues.writeValues();
					Stage stage = (Stage) saveBtn.getScene().getWindow();
					stage.close();
					mMainStage.show();
				}
				catch (IOException e1) {
					// xmlファイルへの書き込みに失敗
					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("alert.fxml"));
					Stage stage = new Stage();
					stage.setTitle("Error");

					try {
						stage.setScene(new Scene(fxmlLoader.load(), 300, 100));
					}
					catch (IOException e2) {
						e2.printStackTrace();
					}

					AlertController alertController = fxmlLoader.getController();
					alertController.WriteValuesError();
					stage.show();
				}
			}
		});

		cancelBtn.setOnAction(e -> {
			Stage stage = (Stage) cancelBtn.getScene().getWindow();
			stage.close();
			mMainStage.show();
		});
	}


	public void setMainStage (Stage mainStage) {
		mMainStage = mainStage;
	}
}
