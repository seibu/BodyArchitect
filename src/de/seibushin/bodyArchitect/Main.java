/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect;

import de.seibushin.bodyArchitect.helper.FxUtil;
import de.seibushin.bodyArchitect.helper.MsgUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // init BodyArchitect + Hibernate
        BodyArchitect.init();

        primaryStage.setTitle(MsgUtil.getString("title"));
        primaryStage.setOnCloseRequest(event -> {
            // stop the propagation of the event
            event.consume();
            close();
        });

        // load the fxml and show HOME
        try {
            // init FXMLLoader
            FXMLLoader loader = new FXMLLoader();
            // set location to home.fxml
            loader.setLocation(getClass().getResource(Config.FXML_ROOT));

            // load
            BorderPane root = loader.load();
            // load the root for ba
            BodyArchitect.getBa().setRoot(root);

            primaryStage.setScene(new Scene(root));
            FxUtil.showFXML(Config.FXML_HOME, root);

            // show the stage
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        close();
    }

    /**
     * Terminate all threads and close the app
     */
    private void close() {
        BodyArchitect.getBa().close();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
