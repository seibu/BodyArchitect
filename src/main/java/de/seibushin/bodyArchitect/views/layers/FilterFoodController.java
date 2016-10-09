/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.views.layers;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.mvc.View;
import de.seibushin.bodyArchitect.Main;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.function.Predicate;

public class FilterFoodController {
    @FXML
    private TextField tf_search;
    @FXML
    private View filter;

    @FXML
    public void initialize() {
        System.out.println(filter);

        filter.onShowingProperty().addListener(((observable, oldValue, newValue) -> {
            System.out.println(observable);
        }));

        filter.showingProperty().addListener((obs, ov, nv) -> {
            System.out.println(ov);
            if (ov && !nv) {
                System.out.println("showing");
            }
        });
    }

    @FXML
    private void search() {
        MobileApplication.getInstance().hideLayer(Main.FILTER_FOOD);
    }

    @FXML
    private void close() {
        tf_search.setText("");
        MobileApplication.getInstance().hideLayer(Main.FILTER_FOOD);
    }

    public Predicate<? super Food> getPredicate() {
        return n -> n.getName().toLowerCase().contains(tf_search.getText().toLowerCase());
    }
}
