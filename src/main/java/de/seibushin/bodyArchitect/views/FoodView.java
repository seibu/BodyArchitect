/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.views;

import com.gluonhq.charm.glisten.mvc.View;
import de.seibushin.bodyArchitect.helper.Utils;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class FoodView {

    private final String name;

    public FoodView(String name) {
        this.name = name;
    }

    public View getView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HomeView.class.getResource("food.fxml"));
            fxmlLoader.setResources(Utils.getBundle());
            View view = fxmlLoader.load();
            view.setName(name);
            return view;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException: " + e);
            return new View(name);
        }
    }
}
