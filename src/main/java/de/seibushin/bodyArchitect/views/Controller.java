/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.bodyArchitect.views;

import de.seibushin.bodyArchitect.BodyArchitect;
import de.seibushin.bodyArchitect.Config;
import de.seibushin.bodyArchitect.helper.FxUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class Controller {

    @FXML
    TableView tv_food;

    public void showNutrition(ActionEvent actionEvent) {
        FxUtil.showFXML(Config.FXML_NUTRITION, BodyArchitect.getInstance().getRoot());

        /* Nutrition in a new Stage / Window
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main1.class.getResource(Config.FXML_NUTRITION));
            Stage stage = new Stage();
            Scene scene = null;

            scene = new Scene(loader.load());

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
    }

    public void showHome(ActionEvent actionEvent) {
        FxUtil.showFXML(Config.FXML_HOME, BodyArchitect.getInstance().getRoot());
    }
}
