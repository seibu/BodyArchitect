/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.helper;

import de.seibushin.bodyArchitect.model.nutrition.Food;
import de.seibushin.bodyArchitect.model.nutrition.Meal;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;

public class FoodCell extends ListCell<Food> {
    private FoodCellNode foodCellNode;
    private ListView<Food> lv_food;

    public FoodCell(ListView lv) {
        lv_food = lv;
        foodCellNode = new FoodCellNode();
        foodCellNode.btn_action.setOnAction(e -> {
            lv_food.getItems().add(getItem());
            getListView().getItems().remove(getItem());
        });

    }

    @Override
    public void updateItem(Food item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            foodCellNode.update(item);
            setGraphic(foodCellNode.getNode());
        } else {
            setGraphic(null);
        }
    }
}
