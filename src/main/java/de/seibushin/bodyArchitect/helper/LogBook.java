/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 * 
 */

package de.seibushin.bodyArchitect.helper;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LogBook {
    @FXML
    VBox vbox_logBook;
    @FXML
    Label lbl_month;
    @FXML
    GridPane gp_calendar;

    private List<Button> days = new ArrayList<>();

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM yyyy");
    private LocalDate month;
    private int today = LocalDate.now().getDayOfMonth() - 1;
    private ObjectProperty<LocalDate> selectedDay = new SimpleObjectProperty<>(LocalDate.now());

    public LogBook() {
        FXMLLoader fxmlLoader = new FXMLLoader(LogBook.class.getResource("LogBook.fxml"));
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        init();
    }

    private void init() {
        createDays();

        month = LocalDate.of(selectedDay.get().getYear(), selectedDay.get().getMonth(), 1);

        // populate Calendar with current Month
        populateCalendar();
    }

    private void createDays() {
        for (int i = 1; i <= 31; i++) {
            Button day = new Button(String.valueOf(i));
            int finalI = i;
            day.setOnAction(e -> {
                selectedDay.set(month.withDayOfMonth(finalI));
            });
            days.add(day);
        }
    }

    public ObjectProperty<LocalDate> getSelectedDayProperty() {
        return selectedDay;
    }

    public LocalDate getSelectedDay() {
        return selectedDay.getValue();
    }

    private void populateCalendar() {
        lbl_month.setText(month.format(dtf));

        // check for current month to highlight todays
        if (LocalDate.now().withDayOfMonth(1).equals(month)) {
            days.get(today).getStyleClass().add("today");
        }

        // highlight Days with data
        // @todo

        gp_calendar.getChildren().removeAll(days);

        // add the days to the correct cell
        int col = month.getDayOfWeek().getValue() - 1;
        int row = 1;
        for (int i = 0; i < month.lengthOfMonth(); i++) {
            gp_calendar.add(days.get(i), col, row);
            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }

    @FXML
    private void nextMonth() {
        days.get(today).getStyleClass().remove("today");
        month = month.plusMonths(1);
        populateCalendar();
    }

    @FXML
    private void prevMonth() {
        days.get(today).getStyleClass().remove("today");
        month = month.minusMonths(1);
        populateCalendar();
    }

    public VBox getWrapper() {
        return vbox_logBook;
    }
}
