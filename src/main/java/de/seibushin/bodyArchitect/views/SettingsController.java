/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

package de.seibushin.bodyArchitect.views;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.TextField;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import de.seibushin.bodyArchitect.BodyArchitect;
import de.seibushin.bodyArchitect.helper.Utils;
import javafx.fxml.FXML;

public class SettingsController {
    @FXML
    TextField tf_kcal;
    @FXML
    TextField tf_fat;
    @FXML
    TextField tf_carbs;
    @FXML
    TextField tf_protein;

    @FXML
    View settings;

    @FXML
    private void initialize() {
        settings.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = MobileApplication.getInstance().getAppBar();

                appBar.setNavIcon(MaterialDesignIcon.ARROW_BACK.button(e -> {
                    MobileApplication.getInstance().switchToPreviousView();
                }));

                appBar.getActionItems().add(MaterialDesignIcon.SAVE.button(e -> {
                    try {
                        BodyArchitect.getInstance().settingsProperty().get().setTargetKcal(Utils.getNumberFormat().parse(tf_kcal.getText()).intValue());
                        BodyArchitect.getInstance().settingsProperty().get().setTargetCarbs(Utils.getNumberFormat().parse(tf_carbs.getText()).intValue());
                        BodyArchitect.getInstance().settingsProperty().get().setTargetFat(Utils.getNumberFormat().parse(tf_fat.getText()).intValue());
                        BodyArchitect.getInstance().settingsProperty().get().setTargetProtein(Utils.getNumberFormat().parse(tf_protein.getText()).intValue());

                        BodyArchitect.getInstance().storeSettings();
                    } catch (Exception ex) {
                        // this exception this thrown, if the new targetValue is smaller then the value of the gauge
                        // this happens coz the property is bound to the gauge value
                        // ex.printStackTrace();
                    }
                }));

                // set Title
                appBar.setTitleText("Nutrition - Settings");
            }
        });

        tf_kcal.setText(Utils.getNumberFormat().format(BodyArchitect.getInstance().settingsProperty().get().getTargetKcal()));
        tf_fat.setText(Utils.getNumberFormat().format(BodyArchitect.getInstance().settingsProperty().get().getTargetFat()));
        tf_carbs.setText(Utils.getNumberFormat().format(BodyArchitect.getInstance().settingsProperty().get().getTargetCarbs()));
        tf_protein.setText(Utils.getNumberFormat().format(BodyArchitect.getInstance().settingsProperty().get().getTargetProtein()));
    }
}
