package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

	private double dragStartX;
	private double dragStartY;

	private Settings settingView = null;

	@Override
	public void start (Stage primaryStage) throws Exception {
		System.out.println("--- start ---");

		GridPane gridPane = new GridPane();
		// これをしないと，白い背景が出て気持ち悪い
		gridPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");

		/*****   イメージビューの作成   *****/
		ImageView image = new ImageView(new Image("illust_sd/sd_eye0.png", true));

		// 自動サイズ調整
		image.fitWidthProperty().bind(primaryStage.widthProperty());
		image.fitHeightProperty().bind(primaryStage.heightProperty());

		// アスペクト比を固定する
		image.setPreserveRatio(true);
		image.setSmooth(true);
		// 必要はないかもやけど，一応…
		image.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");

		gridPane.getChildren().addAll(image);

		/*****   コンテキストメニューの作成   *****/
		ContextMenu contextMenu = new ContextMenu();
		MenuItem connect = new MenuItem("Connect");
		MenuItem settings = new MenuItem("Settings");
		MenuItem quit = new MenuItem("Quit");
		contextMenu.getItems().addAll(connect, settings, quit);

		/*****   シーンの作成   *****/
		Scene scene = new Scene(gridPane, 115, 280, Color.TRANSPARENT);
		scene.setFill(null);


		// Stageをマウスのドラッグで動かせるようにする
		scene.setOnMousePressed(e -> {
			dragStartX = e.getSceneX();
			dragStartY = e.getSceneY();
		});

		scene.setOnMouseDragged(e -> {
			primaryStage.setX(e.getScreenX() - dragStartX);
			primaryStage.setY(e.getScreenY() - dragStartY);
		});

		scene.setOnMouseClicked(e -> {
			if (e.getButton() == MouseButton.SECONDARY) {
				// 右クリックされたら，設定画面を開く
				contextMenu.show(gridPane, e.getScreenX(), e.getScreenY());
			}
		});


		connect.setOnAction(e -> {
			System.out.println("Connect");
		});

		settings.setOnAction(e -> {
			if (settingView == null) {
				settingView = new Settings();
			}
			primaryStage.hide();
			settingView.open(primaryStage);
		});

		quit.setOnAction(e -> {
			Platform.exit();
		});

		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setScene(scene);
		primaryStage.show();
	}


	public static void main (String[] args) {
		System.out.println("--- main ---");
		launch(args);
	}
}
