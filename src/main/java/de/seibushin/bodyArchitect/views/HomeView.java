/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.views;

import com.gluonhq.charm.glisten.mvc.View;
import de.seibushin.bodyArchitect.helper.MealCellNode;
import de.seibushin.bodyArchitect.helper.MsgUtil;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class HomeView {

    private final String name;

    public HomeView(String name) {
        this.name = name;
    }

    public View getView() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HomeView.class.getResource("home.fxml"));
            fxmlLoader.setResources(MsgUtil.getBundle());
            View view = fxmlLoader.load();
            view.setName(name);
            return view;
        } catch (IOException e) {
            System.out.println("IOException: " + e);
            e.printStackTrace();
            return new View(name);
        }
    }
}
