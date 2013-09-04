package ru.eqlbin.gwt.datepicker.client;

import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ListBoxDatePickerProject implements EntryPoint {
    

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        
        
        final ListBoxDatePicker datePicker = new ListBoxDatePicker();
        
        final TextBox currentDateBox = new TextBox();       
        final TextBox firstYearBox = new TextBox();
        final TextBox lastYearBox = new TextBox();
        final Button fixedRangeButton = new Button("Fixed range");
        final Button floatingRangeButton = new Button("Floating range");
        
        final DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd");
        
        
       // Window.alert(NumberFormat.getFormat("0000").format(5));
        
        
        VerticalPanel datePickerPanel = new VerticalPanel();
        datePickerPanel.setSpacing(5);
        datePickerPanel.add(datePicker);
        datePickerPanel.add(currentDateBox);
        
        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(5);
        buttonPanel.add(fixedRangeButton);
        buttonPanel.add(floatingRangeButton);
        
        VerticalPanel yearsRangeChanger = new VerticalPanel();
        yearsRangeChanger.setSpacing(2);
        yearsRangeChanger.add(firstYearBox);
        yearsRangeChanger.add(lastYearBox);
        yearsRangeChanger.add(buttonPanel);
        
        HorizontalPanel mainPanel = new HorizontalPanel();
        mainPanel.setSpacing(10);
        mainPanel.add(datePickerPanel);
        mainPanel.add(yearsRangeChanger);
        
        RootPanel.get("datePicker").add(mainPanel);
        
       // RootPanel.get("yearsRangeChanger").add(yearsRangeChanger);
        
        fixedRangeButton.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                int first = Integer.parseInt(firstYearBox.getValue());
                int last = Integer.parseInt(lastYearBox.getValue());
                
                datePicker.setFixedYearsRange(first, last);
            }
        });
        
        floatingRangeButton.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                int first = Integer.parseInt(firstYearBox.getValue());
                int last = Integer.parseInt(lastYearBox.getValue());

                datePicker.setFloatingYearsRange(first, last);
            }
        });
        
        datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(ValueChangeEvent<Date> event) {
                currentDateBox.setValue(dateFormat.format(event.getValue()));     
            }
        });
        
    }
}
