/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.helper;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.css.PseudoClass;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.IOException;

// check gluon Comments2.0 sample for reference
// https://bitbucket.org/gluon-oss/samples/src/ffe48d13300f7638e31223965ad261de098dc91c/Comments2.0/src/main/java/com/gluonhq/comments20/views/SlidingListTile.java

public class SlidingListNode extends StackPane {
    private final Node node;

    // position of the node
    private double x = 0;
    private double y = 0;

    // Color
    private Color leftColor = Color.RED;
    private Color rightColor = Color.BLUE;

    /**
     * Minimun distance that listTile has to be slid to trigger swipe fake effect
     */
    // @todo check other way taking into consideration the width of the node
    private final DoubleProperty threshold = new SimpleDoubleProperty(150);

    /**
     * Sliding the tile horizontally or scrolling the listview vertically
     */
    private final BooleanProperty scrolling = new SimpleBooleanProperty();
    private final BooleanProperty sliding = new SimpleBooleanProperty();

    /**
     * fake swipe events, in terms of boolean properties
     */
    private final BooleanProperty swipedLeft = new SimpleBooleanProperty();
    private final BooleanProperty swipedRight = new SimpleBooleanProperty();

    // @todo change VBox to StackPane????
    public SlidingListNode(VBox frontPane, boolean slideable) {
        // BackPane
        HBox backPane;
        FXMLLoader fxmlLoader = new FXMLLoader(SlidingListNode.class.getResource("SlidingBackPane.fxml"));
        fxmlLoader.setController(this);
        fxmlLoader.setResources(Utils.getBundle());
        try {
            backPane = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        backPane.getStyleClass().add("back");

        // set a PseudoCss class for sliding to the left
        // we dont need one for right, we take right as the default for styling, since you only see the backPane
        // if you are sliding
        // sliding from right to left means we see the right content of the backPane
        PseudoClass pseudoLeft = PseudoClass.getPseudoClass("left");
        frontPane.translateXProperty().addListener((obs, ov, nv) -> {
            backPane.pseudoClassStateChanged(pseudoLeft, nv.doubleValue() < 0);
        });

        // add a PseudoClass when the sliding was enough to trigger the swipe event
        PseudoClass pseudoSwiped = PseudoClass.getPseudoClass("swiped");
        frontPane.translateXProperty().addListener((obs, ov, nv) -> {
            backPane.pseudoClassStateChanged(pseudoSwiped, Math.abs(nv.doubleValue()) >= threshold.get());
        });

        /*
        Front node
         */
        this.node = frontPane;
        this.node.getStyleClass().add("front");

        if (slideable) {
            frontPane.setOnMousePressed(e -> {
                x = e.getSceneX();
                y = e.getSceneY();
                swipedLeft.set(false);
                swipedRight.set(false);
            });

            frontPane.setOnMouseDragged(e -> {
                // once sliding or scrolling have started, no change can be done
                // until release and start again
                if (scrolling.get() || (!sliding.get() && Math.abs(e.getSceneY() - y) > 10)) {
                    e.consume();
                    scrolling.set(true);
                }

                if (sliding.get() || (!scrolling.get() && Math.abs(e.getSceneX() - x) > 10)) {
                    sliding.set(true);
                    if (e.getSceneX() - x >= -frontPane.getWidth() + 20 &&
                            e.getSceneX() - x <= frontPane.getWidth() - 20) {
                        translateNode(e.getSceneX() - x);
                    }
                }
            });

            frontPane.setOnMouseReleased(e -> {
                // do nothing
                if (scrolling.get()) {
                    e.consume();
                }

                if (sliding.get()) {
                    // on sliding, swipe event after slid distance greater than threshold
                    if (e.getSceneX() - x > this.threshold.get()) {
                        swipedRight.set(true);
                    } else if (e.getSceneX() - x < -this.threshold.get()) {
                        swipedLeft.set(true);
                    } else {
                        // reset without transition
                        translateNode(0);
                    }
                }

                // reset
                scrolling.set(false);
                sliding.set(false);
            });
        }

        // this is a StackPane since we are extending from it
        this.getChildren().addAll(backPane, frontPane);
    }

    private void translateNode(double posX) {
        node.setTranslateX(posX);
    }

    /**
     * Reset position of node with smooth transition
     */
    public void resetTilePosition() {
        TranslateTransition transition = new TranslateTransition(Duration.millis(300), node);
        transition.setInterpolator(Interpolator.EASE_OUT);
        transition.setFromX(node.getTranslateX());
        transition.setToX(0);
        transition.playFromStart();
    }

    public BooleanProperty swipedLeftProperty() {
        return swipedLeft;
    }

    public BooleanProperty swipedRightProperty() {
        return swipedRight;
    }

    public DoubleProperty thresholdProperty() {
        return threshold;
    }

    public BooleanProperty slidingProperty() {
        return sliding;
    }

    public BooleanProperty scrollingProperty() {
        return scrolling;
    }
}
