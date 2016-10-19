/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.views.listCell;

import com.gluonhq.charm.glisten.control.CharmListCell;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import de.seibushin.bodyArchitect.Service;
import de.seibushin.bodyArchitect.model.nutrition.plan.Plan;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;

import java.util.function.Consumer;

public class PlanCell extends CharmListCell<Plan> {
    private SlidingListNode slidingNode;

    private PlanCellNode planCellNode;

    private Plan current;

    public PlanCell() {
        planCellNode = new PlanCellNode();
        slidingNode = new SlidingListNode(planCellNode.tile, true);

        planCellNode.btn_select.setOnAction(e -> {
            if (!current.isSelected()) {
                //@fixme after inserting a new plan more than one will be selected?!
                System.out.println(current.getName());
                Service.getInstance().selectPlan(current);
            }
        });
    }

    public PlanCell(Consumer<Plan> consumerLeft, Consumer<Plan> consumerRight) {
        this();
        setConsumer(consumerLeft, consumerRight);
    }

    public void setConsumer(Consumer<Plan> consumerLeft, Consumer<Plan> consumerRight) {
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
    public void updateItem(Plan item, boolean empty) {
        super.updateItem(item, empty);
        // set the new current if current is null or current differs from the new
        if (current == null || !current.equals(item)) {
            current = item;
        }

        if (item != null && !empty) {
            //@todo check where does this belong?!?
            item.selectedProperty().addListener((obs, ov, nv) -> {
                planCellNode.update(item);
            });

            planCellNode.update(item);
            setGraphic(slidingNode);
        } else {
            setGraphic(null);
        }
    }

    public BooleanProperty slidingProperty() {
        return slidingNode.slidingProperty();
    }
}
