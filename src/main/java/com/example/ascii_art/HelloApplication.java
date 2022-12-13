package com.example.ascii_art;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        double width = Screen.getPrimary().getBounds().getWidth();
        double height = Screen.getPrimary().getBounds().getHeight();
        VBox root = new VBox();
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        //elements
        Text text = new Text("ASCII Art Generator");
        text.setFont(Font.font("Monospaced", 80));
        text.setFill(Color.WHITE);
        Button uploadButton = new Button("Upload Image");
        root.getChildren().addAll(text,uploadButton);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(40);
        Scene scene = new Scene(root, width, height);
        stage.setTitle("ASCII ART");
        stage.setScene(scene);

        //Control
        uploadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter jpgext = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg", "*.JPG");
                fileChooser.getExtensionFilters().add(jpgext);
                File file = fileChooser.showOpenDialog(null);

                if (file != null) {

                    String ascii = getAsciiText(file,width,height);
                    StackPane window = new StackPane();

                    window.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

                    Text text1 = new Text(ascii);
                    text1.setTextAlignment(TextAlignment.CENTER);
                    text1.setFont(Font.font("Monospaced", 3));
                    text1.setFill(Color.WHITE);

                    window.getChildren().add(text1);
                    window.setAlignment(Pos.CENTER);
                    
                    stage.getScene().setRoot(window);
                    stage.getScene().setFill(Color.BLACK);
                    stage.show();
                }
            }
        });


        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static String getAsciiText(File file, double width, double height){
        Image image = new Image(file.toURI().toString(),width/4,height/4,true,false);
        PixelReader pixelReader = image.getPixelReader();
        String scale = "`^\",:;Il!i~+_-?][}{1)(|\\/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$";
        int mapping = 100/scale.length();
        StringBuilder ascii = new StringBuilder();
        for(int i = 0; i < image.getHeight(); i++){
            for(int j = 0; j < image.getWidth(); j++){
                Color col = pixelReader.getColor(j,i);
                double val = 0.3 * col.getRed() + 0.59 * col.getGreen() + 0.11 * col.getBlue();
                int selector = (int) (val * scale.length());
                ascii.append((""+scale.charAt(selector)).repeat(3));
            }
            ascii.append("\n");
        }
        return ascii.toString();
    }
}