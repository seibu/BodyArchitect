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
import de.seibushin.bodyArchitect.model.nutrition.NutriStats;

import java.util.function.Consumer;

public class NutriStatsCell<T extends NutriStats> extends CharmListCell<T> {
    private NutriStatsCellNode nutriStatsCellNode;

    private T current;

    public NutriStatsCell() {
        nutriStatsCellNode = new NutriStatsCellNode();
    }

    @Override
    public void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        // set the new current if current is null or current differs from the new
        if (current == null || !current.equals(item)) {
            current = item;
        }

        if (item != null && !empty) {
            nutriStatsCellNode.update(item);
            setGraphic(nutriStatsCellNode.getNode());
        } else {
            setGraphic(null);
        }
    }
}
