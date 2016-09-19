/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.helper;

import de.seibushin.bodyArchitect.model.nutrition.Food;
import javafx.beans.property.BooleanProperty;
import javafx.scene.control.ListCell;

import java.util.function.Consumer;

public class FoodCell extends ListCell<Food> {

    private SlidingListNode slidingNode;
    private FoodCellNode foodCellNode;

    private Food current;

    public FoodCell() {
        foodCellNode = new FoodCellNode();
        slidingNode = new SlidingListNode(foodCellNode.getNode(), true);
    }

    public FoodCell(Consumer<Food> consumerLeft, Consumer<Food> consumerRight) {
        foodCellNode = new FoodCellNode();

        slidingNode = new SlidingListNode(foodCellNode.getNode(), true);
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

    public void collapse() {
        System.out.println("collapse");
    }

    public BooleanProperty slidingProperty() {
        return slidingNode.slidingProperty();
    }
}
