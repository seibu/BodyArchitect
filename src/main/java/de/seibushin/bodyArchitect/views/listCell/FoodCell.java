/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.views.listCell;

import com.gluonhq.charm.glisten.control.CharmListCell;
import de.seibushin.bodyArchitect.model.nutrition.Food;
import javafx.beans.property.BooleanProperty;

import java.util.function.Consumer;

public class FoodCell extends CharmListCell<Food> {
    private SlidingListNode slidingNode;

    private FoodCellNode foodCellNode;

    private Food current;

    public FoodCell(boolean usePortion) {
        foodCellNode = new FoodCellNode(usePortion);
        slidingNode = new SlidingListNode(foodCellNode.getNode(), true);
    }

    public FoodCell(boolean usePortion, Consumer<Food> consumerLeft, Consumer<Food> consumerRight) {
        this(usePortion);
        setConsumer(consumerLeft, consumerRight);
    }

    public void setConsumer(Consumer<Food> consumerLeft, Consumer<Food> consumerRight) {
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
    }

    @Override
    public void updateItem(Food item, boolean empty) {
        super.updateItem(item, empty);
        // set the new current if current is null or current differs from the new
        if (current == null || !current.equals(item)) {
            current = item;
        }

        if (item != null && !empty) {
            foodCellNode.update(item);
            setGraphic(slidingNode);
        } else {
            setGraphic(null);
        }
    }


    public BooleanProperty slidingProperty() {
        return slidingNode.slidingProperty();
    }
}
