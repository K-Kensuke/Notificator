package sample;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Notificator {
	private double dragStartX;
	private double dragStartY;

	public static volatile boolean receive = true;
	private SocketReceiver socketReceiver = null;
	private ExecutorService service = Executors.newSingleThreadExecutor();

	public void open (Stage mainStage) throws IOException {
		System.out.println("--- open ---");

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("notificator.fxml"));

		Stage stage = new Stage();
		// fxmlLoaderのload時に，Controllerクラスのインスタンスが作成される．
		Scene scene = new Scene(fxmlLoader.load(), 280, 300, Color.TRANSPARENT);
		scene.setFill(null);

		NotificatorController notificatorController = fxmlLoader.getController();

		socketReceiver = new SocketReceiver();

		// Taskのmessageとlabelをbindする（messageが来ればlabelに反映される）
		notificatorController.label.textProperty().bind(socketReceiver.messageProperty());

		// task終了後の処理
		socketReceiver.setOnSucceeded(e -> {
			notificatorController.label.textProperty().unbind();
			stage.close();
			mainStage.show();
		});
		socketReceiver.setOnCancelled(e -> {
			notificatorController.label.textProperty().unbind();
			stage.close();
			mainStage.show();
		});
		socketReceiver.setOnFailed(e -> {
			notificatorController.label.textProperty().unbind();
			stage.close();
			mainStage.show();
		});

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
				// 次回データ受信後にSocket通信を終了する
				receive = false;
				socketReceiver = null;
			}
		});

		stage.initStyle(StageStyle.TRANSPARENT);
		stage.setScene(scene);
		stage.show();

		service.submit(socketReceiver);
	}


	public void close () {
		service.shutdown();
	}
}


class SocketReceiver extends Task {

	@Override
	protected Object call () throws Exception {
		SettingValues settingValues = SettingValues.getInstance();

		if (settingValues.getServerIP().equals("") || settingValues.getServerPort() == 0) {
			// 接続に必要なパラメータが設定されていない
			updateMessage("Parameter doesn't set correctly");
			return null;
		}

		Socket socket = new Socket(settingValues.getServerIP(), settingValues.getServerPort());
		Notificator.receive = true;

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

		while (true) {
			String receivedText = bufferedReader.readLine();
			updateMessage(receivedText);

			if (Notificator.receive) {
				bufferedWriter.write("received");
				bufferedWriter.flush();
			}
			else {
				bufferedWriter.write("close");
				bufferedWriter.flush();

				bufferedReader.close();
				bufferedWriter.close();
				socket.close();
				break;
			}
		}
		return null;
	}
}
