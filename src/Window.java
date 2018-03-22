import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Window extends Application
{
    public static void main (String[] args)
    {
        launch(args);
    }

    Button button;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("Library View");

        button = new Button();
        button.setText("Log In");

        button.setOnAction(e -> {
            System.out.println("15:44?");
            System.out.println("ANTONIO!");
        });

        StackPane layout = new StackPane();
        layout.getChildren().add(button);

        Scene scene = new Scene(layout, 960, 540);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
