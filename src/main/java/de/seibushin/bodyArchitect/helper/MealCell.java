/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.helper;

import de.seibushin.bodyArchitect.model.nutrition.Meal;
import javafx.scene.control.ListCell;

public class MealCell extends ListCell<Meal> {
    private MealCellNode mealCellNode;

    public MealCell() {
        mealCellNode = new MealCellNode();
    }

    public MealCell(int type) {
        mealCellNode = new MealCellNode(type);
    }

    @Override
    public void updateItem(Meal item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            mealCellNode.update(item);
            setGraphic(mealCellNode.getNode());
        } else {
            setGraphic(null);
        }
    }
}
