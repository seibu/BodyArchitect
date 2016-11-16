/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.views.listCell;

import com.gluonhq.charm.glisten.application.MobileApplication;
import com.gluonhq.charm.glisten.control.CharmListCell;
import de.seibushin.bodyArchitect.Service;
import de.seibushin.bodyArchitect.model.nutrition.BANutritionUnit;

public class NutritionUnitCell<T extends BANutritionUnit> extends CharmListCell<T> {
    private NutritionUnitCellNode nutritionUnitCellNode;

    private T current;

    public NutritionUnitCell() {
        nutritionUnitCellNode = new NutritionUnitCellNode();

        nutritionUnitCellNode.i_info.setOnMouseClicked(e -> {
            Service.getInstance().getMealInfoLayer().setMeal(current);
            MobileApplication.getInstance().showLayer("MealInfo");
        });
    }

    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        // set the new current if current is null or current differs from the new
        if (current == null || !current.equals(item)) {
            current = item;
        }

        if (item != null && !empty) {
            nutritionUnitCellNode.update(item);
            setGraphic(nutritionUnitCellNode.getNode());
        } else {
            setGraphic(null);
        }
    }
}
