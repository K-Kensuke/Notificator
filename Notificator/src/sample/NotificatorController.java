package sample;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class NotificatorController implements Initializable {
	public StackPane stackPane;
	public Pane imagePane;
	public ImageView imageView;
	public Pane labelPane;
	public Label label;

	@Override
	public void initialize (URL location, ResourceBundle resources) {
		stackPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		imagePane.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		imageView.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		labelPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		label.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
		label.setTextFill(Color.web("#00ffff"));
		label.setWrapText(true);

		imageView.setImage(new Image("illust_sd/sd_eye0.png", true));

		imageView.fitWidthProperty().bind(imagePane.widthProperty());
		imageView.fitHeightProperty().bind(imagePane.heightProperty());

		imageView.setPreserveRatio(true);
		imageView.setSmooth(true);

		String test = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

		label.setText(test);
	}
}
