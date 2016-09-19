/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.helper;

import de.seibushin.bodyArchitect.model.nutrition.MealFood;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.ListCell;

import java.util.function.Consumer;

public class MealFoodCell extends ListCell<MealFood> {
    private MealFoodCellNode mealFoodCellNode;

    private MealFood current;

    public MealFoodCell() {
        mealFoodCellNode = new MealFoodCellNode();
    }

    @Override
    public void updateItem(MealFood item, boolean empty) {
        super.updateItem(item, empty);
        // set the new current if current is null or current differs from the new
        if (current == null || !current.equals(item)) {
            current = item;
        }

        if (item != null && !empty) {
            mealFoodCellNode.update(item);
            setGraphic(mealFoodCellNode.getNode());
        } else {
            setGraphic(null);
        }
    }
}
