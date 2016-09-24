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
import de.seibushin.bodyArchitect.model.nutrition.DayMeal;
import de.seibushin.bodyArchitect.model.nutrition.Meal;
import de.seibushin.bodyArchitect.model.nutrition.SimpleMeal;
import javafx.beans.property.BooleanProperty;

import java.util.function.Consumer;

public class MealCell extends CharmListCell<SimpleMeal> {
    private MealCellNode mealCellNode;

    private SlidingListNode slidingNode;

    private SimpleMeal current;

    public MealCell(Consumer<SimpleMeal> consumerLeft, Consumer<SimpleMeal> consumerRight) {
        mealCellNode = new MealCellNode();

        slidingNode = new SlidingListNode(mealCellNode.getNode(), true);

        slidingNode.swipedLeftProperty().addListener((obs, ov, nv) -> {
            if (nv && consumerRight != null) {
                consumerRight.accept(current);
            }
            slidingNode.resetTilePosition();
        });


        slidingNode.swipedRightProperty().addListener((obs, ov, nv) -> {
            if (nv && consumerLeft != null) {
                consumerLeft.accept(current);
            }
            slidingNode.resetTilePosition();
        });

        mealCellNode.i_info.setOnMouseClicked(e -> {
            if (current.isMeal()) {
                Service.getInstance().getMealInfoLayer().setMeal(((DayMeal)current).getMeal());
                MobileApplication.getInstance().showLayer("MealInfo");
            }
        });
    }

    @Override
    public void updateItem(SimpleMeal item, boolean empty) {
        super.updateItem(item, empty);

        // set the new current if current is null or current differs from the new
        if (current == null || !current.equals(item)) {
            current = item;
        }

        if (item != null) {
            mealCellNode.update(item);
            setGraphic(slidingNode);
        } else {
            setGraphic(null);
        }
    }

    public BooleanProperty slidingProperty() {
        return slidingNode.slidingProperty();
    }
}
