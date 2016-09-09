/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import de.seibushin.bodyArchitect.BodyArchitect;
import de.seibushin.bodyArchitect.Main;
import de.seibushin.bodyArchitect.model.nutrition.Day;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import jfxtras.scene.control.gauge.linear.SimpleMetroArcGauge;

public class HomeController {

    @FXML
    private View home;
    @FXML
    SimpleMetroArcGauge mag_kcal;
    @FXML
    SimpleMetroArcGauge mag_protein;
    @FXML
    SimpleMetroArcGauge mag_fat;
    @FXML
    SimpleMetroArcGauge mag_carbs;

    @FXML
    public void showNutrition() {
        MobileApplication.getInstance().switchView(Main.NUTRITION_VIEW);
        // refresh navigation set nutrition active
    }

    @FXML
    public void initialize() {
        home.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> MobileApplication.getInstance().showLayer(Main.MENU_LAYER)));
                appBar.setTitleText("Home");

                // update the gauge
                // @todo how ineffective is this? we have to get the dayobject fresh from the db everytime we open this view
                Day today = BodyArchitect.getInstance().getTodayDayObject();
                if (today != null) {
                    // update gauges
                    // @todo check for maxValue and change the maxValue accordingly
                    mag_kcal.setValue(today.getKcal());
                    mag_carbs.setValue(today.getCarbs());
                    mag_fat.setValue(today.getFat());
                    mag_protein.setValue(today.getProtein());
                }
            }
        });
    }
}
