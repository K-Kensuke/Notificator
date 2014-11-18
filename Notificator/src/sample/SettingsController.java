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

/**
 * Created by ken on 11/18/14.
 */
public class SettingsController implements Initializable {

	public TextField serverIPField;
	public TextField serverPortField;
	public Button saveBtn;
	public Button cancelBtn;

	private String serverIP = "";
	private int serverPort = 0;

	private Stage mMainStage = null;

	Parameters parameters;

	@Override
	public void initialize (URL location, ResourceBundle resources) {
		parameters = new Parameters();

		serverIP = parameters.getServerIP();
		if (!serverIP.equals("")) {
			serverIPField.setText(serverIP);
		}

		serverPort = parameters.getServerPort();
		if (serverPort != 0) {
			serverPortField.setText(String.valueOf(serverPort));
		}

		saveBtn.setOnAction(e -> {
			serverIP = serverIPField.getCharacters().toString();
			System.out.println("serverIP : " + serverIP);

			final String ipRegex = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
					"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
			Pattern ipPattern = Pattern.compile(ipRegex);

			if (ipPattern.matcher(serverIP).matches()) {
				parameters.setServerIP(serverIP);
				Stage stage = (Stage) saveBtn.getScene().getWindow();
				stage.close();
				mMainStage.show();
			}
			else {
				try {
					FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("alert.fxml"));
					Stage stage = new Stage();
					stage.setTitle("Error");
					stage.setScene(new Scene(fxmlLoader.load(), 300, 100));

					// コントローラを取得
					AlertController alertController = fxmlLoader.getController();
					alertController.IPaddressError();
					stage.show();
				}
				catch (IOException exception) {
					exception.printStackTrace();
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
