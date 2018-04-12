import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.stage.*;

public class Popups
{
    static boolean answer;

    public static boolean confirm (String message)
    {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Confirmation");
        window.setMinWidth(300);
        window.setMinHeight(120);

        Label label = new Label();
        label.setText(message);

        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });

        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        window.setOnCloseRequest(e -> {
            e.consume();
            answer = false;
            window.close();
        });

        HBox layoutBool = new HBox(20);
        layoutBool.getChildren().addAll(yesButton, noButton);
        layoutBool.setAlignment(Pos.CENTER);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, layoutBool);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 100);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }

    public static void alert (String message)
    {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Attention");
        window.setMinWidth(300);
        window.setMinHeight(120);

        Label label = new Label();
        label.setText(message);

        Button closeButton = new Button("OK");

        closeButton.setOnAction(e -> {
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 100);
        window.setScene(scene);
        window.showAndWait();
    }
}
