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

public class SimpleMealCell extends CharmListCell<SimpleMeal> {
    private SimpleMealCellNode cellNode;

    private SlidingListNode slidingNode;

    private SimpleMeal current;

    public SimpleMealCell(boolean isMeal, boolean usePortion, boolean slideLeft, boolean slideRight) {
        cellNode = new SimpleMealCellNode();
        slidingNode = new SlidingListNode(cellNode.getNode(), true);

        cellNode.i_info.setOnMouseClicked(e -> {
            if (current.isMeal()) {
                Service.getInstance().getMealInfoLayer().setMeal(current.getMeal());
                MobileApplication.getInstance().showLayer("MealInfo");
            }
        });

        if (isMeal) {

        } else {

            if (usePortion) {

            }
        }

    }

    public SimpleMealCell(boolean isMeal, boolean usePortion, boolean slideLeft, boolean slideRight, Consumer<SimpleMeal> consumerLeft, Consumer<SimpleMeal> consumerRight) {
        this(isMeal, usePortion, slideLeft, slideRight);
        setConsumer(consumerLeft, consumerRight);
    }

    public void setConsumer(Consumer<SimpleMeal> consumerLeft, Consumer<SimpleMeal> consumerRight) {
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
    public void updateItem(SimpleMeal item, boolean empty) {
        super.updateItem(item, empty);

        // set the new current if current is null or current differs from the new
        if (current == null || !current.equals(item)) {
            current = item;
        }

        if (item != null) {
            cellNode.update(item);
            setGraphic(slidingNode);
        } else {
            setGraphic(null);
        }
    }

    public BooleanProperty slidingProperty() {
        return slidingNode.slidingProperty();
    }
}
