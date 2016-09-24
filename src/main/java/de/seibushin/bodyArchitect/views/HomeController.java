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
import de.seibushin.bodyArchitect.Main;
import de.seibushin.bodyArchitect.Service;
import de.seibushin.bodyArchitect.model.nutrition.Day;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import jfxtras.scene.control.gauge.linear.SimpleMetroArcGauge;

import java.time.LocalDate;

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
    Label lbl_kcal_add;
    @FXML
    Label lbl_fat_add;
    @FXML
    Label lbl_carbs_add;
    @FXML
    Label lbl_protein_add;

    @FXML
    public void showNutrition() {
        MobileApplication.getInstance().switchView(Main.NUTRITION_VIEW);
        // refresh navigation set nutrition active
    }

    // @todo define globally
    private void updateGauge(SimpleMetroArcGauge gauge, double nv, Label lbl) {
        nv = Math.ceil(nv);
        if (nv > gauge.getMaxValue()) {
            double diff = nv - gauge.getMaxValue();
            lbl.setText("+" + diff);
            gauge.setValue(gauge.getMaxValue());
        } else {
            gauge.setValue(nv);
            lbl.setText("");
        }
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
                Day today = Service.getInstance().getDay(LocalDate.now());
                if (today != null) {
                    // update gauges
                    updateGauge(mag_kcal, today.getKcal(), lbl_kcal_add);
                    updateGauge(mag_carbs, today.getCarbs(), lbl_carbs_add);
                    updateGauge(mag_fat, today.getFat(), lbl_fat_add);
                    updateGauge(mag_protein, today.getProtein(), lbl_protein_add);
                }
            }
        });
    }
}
