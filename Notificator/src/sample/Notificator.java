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

/**
 * @author Kensuke Kousaka
 */
public class Notificator {
	private double dragStartX;
	private double dragStartY;
	private volatile boolean receive = true;

	public void open (Stage mainStage) {
		System.out.println("--- open ---");

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("notificator.fxml"));

			Stage stage = new Stage();
			// fxmlLoaderのload時に，Controllerクラスのインスタンスが作成される．
			Scene scene = new Scene(fxmlLoader.load(), 280, 300, Color.TRANSPARENT);
			scene.setFill(null);

			NotificatorController notificatorController = fxmlLoader.getController();


			// サーバとのSocket通信を行うTask
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call () throws Exception {
					Parameters parameters = Parameters.getInstance();

					if (parameters.getServerIP().equals("") || parameters.getServerPort() == 0) {
						// 接続に必要なパラメータが設定されていない
						updateMessage("Parameter doesn't set correctly");
						return null;
					}

					Socket socket = null;
					BufferedReader bufferedReader = null;
					BufferedWriter bufferedWriter = null;

					try {
						// サーバへ接続
						socket = new Socket(parameters.getServerIP(), parameters.getServerPort());
						receive = true;

						// メッセージ取得オブジェクトのインスタンス化
						bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

						while (true) {
							String receivedText = bufferedReader.readLine();
							updateMessage(receivedText);

							if (receive) {
								bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
								bufferedWriter.write("received");
								bufferedWriter.flush();
							}
							else {
								bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
								bufferedWriter.write("close");
								bufferedWriter.flush();
								break;
							}
						}
					}
					catch (IOException e) {
						e.printStackTrace();
					}
					finally {
						try {
							if (bufferedReader != null) {
								bufferedReader.close();
							}
							if (bufferedWriter != null) {
								bufferedWriter.close();
							}
							if (socket != null) {
								socket.close();
							}
						}
						catch (IOException e) {
							e.printStackTrace();
						}
					}

					return null;
				}
			};

			// Taskのmessageとlabelをbindする（messageが来ればlabelに反映される）
			notificatorController.label.textProperty().bind(task.messageProperty());

			// task終了後の処理
			task.setOnSucceeded(e -> {
				notificatorController.label.textProperty().unbind();
				stage.close();
				mainStage.show();
			});
			task.setOnCancelled(e -> {
				notificatorController.label.textProperty().unbind();
				stage.close();
				mainStage.show();
			});
			task.setOnFailed(e -> {
				notificatorController.label.textProperty().unbind();
				stage.close();
				mainStage.show();
			});

			// taskをスレッドで起動する
			Thread thread = new Thread(task);
			thread.setDaemon(true);
			thread.start();

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
				}
			});

			stage.initStyle(StageStyle.TRANSPARENT);
			stage.setScene(scene);
			stage.show();
		}
		catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
}
