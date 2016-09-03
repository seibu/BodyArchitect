/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.views;

import com.gluonhq.charm.glisten.mvc.View;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class NutritionView {

    private final String name;

    public NutritionView(String name) {
        this.name = name;
    }

    public View getView() {
        try {
            View view = FXMLLoader.load(NutritionView.class.getResource("nutrition.fxml"));
            view.setName(name);
            return view;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException: " + e);
            return new View(name);
        }
    }
}
