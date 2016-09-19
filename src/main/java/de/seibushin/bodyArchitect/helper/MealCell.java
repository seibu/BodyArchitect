/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.helper;

import com.gluonhq.charm.glisten.application.MobileApplication;
import de.seibushin.bodyArchitect.Service;
import de.seibushin.bodyArchitect.model.nutrition.Meal;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.ListCell;

import java.util.function.Consumer;

public class MealCell extends ListCell<Meal> {
    private MealCellNode mealCellNode;

    private SlidingListNode slidingNode;

    private Meal current;

    public MealCell(Consumer<Meal> consumerLeft, Consumer<Meal> consumerRight) {
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
            Service.getInstance().getMealInfoLayer().setMeal(current);
            MobileApplication.getInstance().showLayer("MealInfo");
        });
    }

    private void showPopup(Meal meal) {

    }

    @Override
    public void updateItem(Meal item, boolean empty) {
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
