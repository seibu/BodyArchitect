/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.helper;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

import static org.h2.util.Utils.getResource;

public class FxUtil {

    /**
     *  * Shows a specific fxml-files elements in the center of the given !BorderPane!
     *  @todo check ability to use other types then BorderPane
     *
     * @param path to fxml file relative to the main.class
     * @param mainPane pane to show the content of the  FXML of the given path
     * @return returns the controller specified in the fxml
     */
    public static void showFXML(String path, BorderPane mainPane) {
        try {
            // initilize a FXMLLoader
            FXMLLoader loader = new FXMLLoader();
            // set the given path to the fxml
            loader.setLocation(FxUtil.class.getResource(path));

            // load the fxml
            mainPane.setCenter(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
