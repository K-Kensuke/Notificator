package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Created by ken on 11/18/14.
 */
public class Notificator {
	private double dragStartX;
	private double dragStartY;

	public void open (Stage mainStage) {
		System.out.println("--- open ---");

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("notificator.fxml"));
			NotificatorController notificatorController = fxmlLoader.getController();


			Stage stage = new Stage();
			// fxmlLoaderのload時に，Controllerクラスのインスタンスが作成される．
			Scene scene = new Scene(fxmlLoader.load(), 280, 300, Color.TRANSPARENT);
			scene.setFill(null);

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
					stage.close();
					mainStage.show();
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
