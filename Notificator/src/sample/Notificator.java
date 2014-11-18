package sample;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by ken on 11/18/14.
 */
public class Notificator {
	private double dragStartX;
	private double dragStartY;
	private volatile boolean receive = false;

	public void open (Stage mainStage) {
		System.out.println("--- open ---");

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("notificator.fxml"));

			Stage stage = new Stage();
			// fxmlLoaderのload時に，Controllerクラスのインスタンスが作成される．
			Scene scene = new Scene(fxmlLoader.load(), 280, 300, Color.TRANSPARENT);
			scene.setFill(null);

			NotificatorController notificatorController = fxmlLoader.getController();

			scene.setOnMousePressed(e -> {
				dragStartX = e.getSceneX();
				dragStartY = e.getSceneY();
			});

			scene.setOnMouseDragged(e -> {
				stage.setX(e.getScreenX() - dragStartX);
				stage.setY(e.getScreenY() - dragStartY);
			});

			scene.setOnMouseClicked(e -> {
				if (e.getButton() == MouseButton.SECONDARY) {
					receive = false;
					stage.close();
					mainStage.show();
				}
			});

			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setScene(scene);
			stage.show();

			// これを用いることで，別スレッドを立てて処理を行い，UIを触ることも出来る．
			Platform.runLater(() -> {
				receive = true;
				receiveMessage(receive, notificatorController);
			});
		}
		catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}


	public void receiveMessage (boolean receive, NotificatorController notificatorController) {
		Parameters parameters = new Parameters();

		if (parameters.getServerIP().equals("") || parameters.getServerPort() == 0) {
			// 接続に必要なパラメータが設定されていない
			notificatorController.setText("Parameter doesn't set correctly");
		}

		Socket socket = null;
		BufferedReader bufferedReader = null;

		try {
			// サーバへ接続
			socket = new Socket(parameters.getServerIP(), parameters.getServerPort());

			// メッセージ取得オブジェクトのインスタンス化
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			while (receive) {
				String receivedText = bufferedReader.readLine();
				notificatorController.setText(receivedText);
			}
		}
		catch (UnknownHostException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (socket != null) {
					socket.close();
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
