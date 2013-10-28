package ru.eqlbin.gwt.datepicker.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ListBoxDatePickerExample implements EntryPoint {
    

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        
        
        final ListBoxDatePicker datePicker = new ListBoxDatePicker();

        final Label firstYearLabel = new Label("First year or left shift:"); 
        final Label lastYearLabel = new Label("Last year or right shift:"); 
        final Label selectedDateLabel = new Label();
        final TextBox firstYearBox = new TextBox();
        final TextBox lastYearBox = new TextBox();
        final Button fixedRangeButton = new Button("Set fixed range");
        final Button floatingRangeButton = new Button("Set floating range");

        final DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd");

        VerticalPanel datePickerPanel = new VerticalPanel();
        datePickerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        datePickerPanel.setWidth("100%");
        datePickerPanel.setSpacing(5);
        datePickerPanel.add(datePicker);
        datePickerPanel.add(selectedDateLabel);
        datePickerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        
        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(5);
        buttonPanel.add(fixedRangeButton);
        buttonPanel.add(floatingRangeButton);
        
        VerticalPanel yearsRangeChanger = new VerticalPanel();
        yearsRangeChanger.setWidth("100%");
        yearsRangeChanger.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        yearsRangeChanger.setSpacing(2);
        yearsRangeChanger.add(firstYearLabel);
        yearsRangeChanger.add(firstYearBox);
        yearsRangeChanger.add(lastYearLabel);
        yearsRangeChanger.add(lastYearBox);
        yearsRangeChanger.add(buttonPanel);
        
        HorizontalPanel mainPanel = new HorizontalPanel();
        mainPanel.setWidth("100%");
        mainPanel.setSpacing(10);
        mainPanel.add(datePickerPanel);
        mainPanel.add(yearsRangeChanger);

        RootPanel.get("datePicker").add(mainPanel);

        fixedRangeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {               
                int first = Integer.parseInt(firstYearBox.getValue());
                int last = Integer.parseInt(lastYearBox.getValue());
                
                if(first <= 0 || last <= 0 || first > last ) {
                    Window.alert("Wrong fixed range of years!" );
                    return;
                }
                
                datePicker.setFixedYearsRange(first, last);
            }
        });
        
        floatingRangeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                
                int leftShift = Integer.parseInt(firstYearBox.getValue());
                int rightShift = Integer.parseInt(lastYearBox.getValue());

                if(leftShift >= 0) {
                    Window.alert("Wrong left shift value! It must be < 0.");
                    return;
                }
                
                if(rightShift <= 0) {
                    Window.alert("Wrong right shift value! It must be > 0.");
                    return;
                }
                
                datePicker.setFloatingYearsRange(leftShift, rightShift);
            }
        });

        datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(ValueChangeEvent<Date> event) {
                selectedDateLabel.setText("Selected date: " +
                        dateFormat.format(event.getValue()));
            }
        });
    }
}
