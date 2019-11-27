
package sys_enchere;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author BLHA
 */
public class Sys_enchere extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXML.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("sys_enchere");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
        stage.setOnCloseRequest((WindowEvent e) -> {
            Interface_utilController.autoRefresh_shutdown();
            Platform.exit();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
