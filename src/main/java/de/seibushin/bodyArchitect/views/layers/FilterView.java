/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.views.layers;

import com.gluonhq.charm.glisten.mvc.View;
import de.seibushin.bodyArchitect.helper.Utils;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class FilterView {

    private final String name;
    private FilterFoodController c;
    private View view;

    public FilterView(String name) {
        this.name = name;

        FXMLLoader fxmlLoader = new FXMLLoader(FilterView.class.getResource("filter_food.fxml"));
        fxmlLoader.setResources(Utils.getBundle());
        try {
            view = fxmlLoader.load();
            view.setName(name);
            c = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public FilterFoodController getController() {
        return c;
    }

    public View getView() {
       return view;
    }
}
