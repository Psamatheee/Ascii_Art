package com.example.ascii_art;

import javafx.animation.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
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
import javafx.util.Duration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;


public class HelloApplication extends Application {
    public class TextSizeTransition extends Transition {

        private Text text; // a little generic -> subclasses: ButtonBase, Cell, Label, TitledPane
        private int start, end; // initial and final size of the text

        public TextSizeTransition(Text text, int start, int end, Duration duration) {
            this.text = text;
            this.start = start;
            this.end = start - end;
            setCycleDuration(duration);
            setInterpolator(Interpolator.LINEAR);
            setCycleCount(1);
        }

        @Override
        protected void interpolate(double v) {
            int size = (int) (start - (end * v));
            if(size>= start - end) {
                text.setFont(Font.font("Monospaced", size));
            }
            if(size < start - end){
                text.setFont(Font.font("Monospaced", start - end));
            }
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        double width = Screen.getPrimary().getBounds().getWidth();
        double height = Screen.getPrimary().getBounds().getHeight();
        VBox root = getStartWindow(stage, width, height);

        Scene scene = new Scene(root, width, height);
        stage.setTitle("ASCII ART");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static String getAsciiText(File file, double width, double height){
        Image image = new Image(file.toURI().toString(),width/4,height/4 - 40 ,true,false);
        PixelReader pixelReader = image.getPixelReader();
        String scale = "`^\",:;Il!i~+_-?][}{1)(|\\/tfjrxnuvczXYUJCLQ0OZmwqpdbkhao*#MW&8%B@$";
        StringBuilder ascii = new StringBuilder();
        for(int i = 0; i < image.getHeight(); i++){
            for(int j = 0; j < image.getWidth(); j++){
                Color col = pixelReader.getColor(j,i);
                double val = 0.3 * col.getRed() + 0.59 * col.getGreen() + 0.11 * col.getBlue();
                int selector = (int) (val * scale.length());
                ascii.append((""+scale.charAt(selector)).repeat(2)); // font about 2 times tall as wide
            }
            ascii.append("\n");
        }
        return ascii.toString();
    }

    public VBox getStartWindow(Stage stage, double width, double height){
        VBox root = new VBox();
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        root.setAlignment(Pos.CENTER);
        root.setSpacing(40);

        //Text
        Text text = new Text("ASCII ART GENERATOR");
        text.setFont(Font.font("Monospaced", 80));
        text.setFill(Color.WHITE);

        //Start Button
        Button uploadButton = new Button("UPLOAD IMAGE");
        uploadButton.setStyle("-fx-background-color: white; -fx-font: 20px \"Monospaced\";");


        root.getChildren().addAll(text,uploadButton);
        uploadButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fileChooser = new FileChooser();
                FileChooser.ExtensionFilter jpgext = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg", "*.JPG");
                fileChooser.getExtensionFilters().add(jpgext);
                File file = fileChooser.showOpenDialog(null);

                if (file != null) {
                    String ascii = getAsciiText(file,width,height);
                    BorderPane window  = new BorderPane();
                    window.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

                    Text asciiText = new Text(ascii);
                    asciiText.setTextAlignment(TextAlignment.CENTER);
                    asciiText.setFont(Font.font("Monospaced", 10));
                    asciiText.setFill(Color.WHITE);

                    //Buttons
                    Button backButton = new Button("BACK");
                    backButton.setStyle("-fx-background-color: white; -fx-font: 15px \"Monospaced\";");
                    backButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            stage.getScene().setRoot(root);
                        }
                    });

                    Button saveButton = new Button("SAVE");
                    saveButton.setStyle("-fx-background-color: white; -fx-font: 15px \"Monospaced\";");
                    saveButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            try {
                                FileWriter fileWriter = new FileWriter(file.getName() + "_ascii.txt", false);
                                fileWriter.write(ascii);
                                fileWriter.close();
                            } catch (IOException e) {
                                System.out.println("An error occurred.");
                                e.printStackTrace();
                            }
                        }
                    });

                    StackPane textPane = new StackPane();
                    textPane.setAlignment(Pos.CENTER);
                    textPane.getChildren().add(asciiText);
                    textPane.setPadding(new Insets(20,20,20,20));

                    HBox buttonPane = new HBox();
                    buttonPane.getChildren().addAll(backButton,saveButton);
                    buttonPane.setAlignment(Pos.CENTER);
                    buttonPane.setSpacing(20);
                    buttonPane.setPadding(new Insets(20,20,20,20));

                    window.setBottom(buttonPane);
                    window.setCenter(textPane);

                    TextSizeTransition trans = new TextSizeTransition(asciiText, 40, 3,Duration.millis(5000));
                    stage.getScene().setRoot(window);
                    stage.getScene().setFill(Color.BLACK);

                    trans.play();
                }
            }
        });
return root;
    }

}


