import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Window extends Application implements EventHandler<ActionEvent>
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
        button.setOnAction(this);

        StackPane layout = new StackPane();
        layout.getChildren().add(button);

        Scene scene = new Scene(layout, 960, 540);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void handle(ActionEvent event)
    {
        if (event.getSource() == button)
        {
            System.out.println("ANTONIO!");
        }
    }
}
