package ru.eqlbin.gwt.datepicker.example.client;

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
import com.google.gwt.user.client.ui.Widget;

import ru.eqlbin.gwt.datepicker.client.ListBoxDatePicker;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ListBoxDatePickerExample implements EntryPoint {
    
    
    private static final DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat("yyyy-MM-dd");

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        RootPanel.get("datePickerFloating").add(createFloatingDatePickerExample());
        RootPanel.get("datePickerFixed").add(createFixedDatePickerExample());
    }
    
    @SuppressWarnings("deprecation")
    public Widget createFloatingDatePickerExample() {
        
        int currentYear = new Date().getYear() + 1900;
        int yearsCount = 10;
        
        final ListBoxDatePicker datePickerFloating = new ListBoxDatePicker();
        datePickerFloating.setFloatingYearsRange(currentYear, yearsCount);
        
        final Label currentYearLabel = new Label("Current year:"); 
        final Label yearsCountLabel = new Label("Years count:"); 
        final Label selectedDateLabel = new Label();
        
        final TextBox currentYearInputBox = new TextBox();
        currentYearInputBox.setText(String.valueOf(currentYear));
        final TextBox yearCountInput = new TextBox();
        yearCountInput.setText(String.valueOf(yearsCount));
        
        final Button setRangeButton = new Button("Set range");
        
        VerticalPanel datePickerPanel = new VerticalPanel();
        datePickerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        datePickerPanel.setWidth("300px");
        datePickerPanel.setSpacing(5);
        datePickerPanel.add(datePickerFloating);
        datePickerPanel.add(selectedDateLabel);
        datePickerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        
        VerticalPanel yearsRangePanel = new VerticalPanel();
        yearsRangePanel.setWidth("300px");
        yearsRangePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        yearsRangePanel.setSpacing(5);
        yearsRangePanel.add(currentYearLabel);
        yearsRangePanel.add(currentYearInputBox);
        yearsRangePanel.add(yearsCountLabel);
        yearsRangePanel.add(yearCountInput);
        yearsRangePanel.add(setRangeButton);
        
        HorizontalPanel mainPanel = new HorizontalPanel();
        mainPanel.setWidth("100%");
        mainPanel.setSpacing(10);
        mainPanel.add(datePickerPanel);
        mainPanel.add(yearsRangePanel);
        mainPanel.setCellHorizontalAlignment(datePickerPanel, HasHorizontalAlignment.ALIGN_RIGHT);
        mainPanel.setCellHorizontalAlignment(yearsRangePanel, HasHorizontalAlignment.ALIGN_LEFT);
                
        setRangeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                
                int currentYear = Integer.parseInt(currentYearInputBox.getValue());
                int yearsCount = Integer.parseInt(yearCountInput.getValue());
                
                if(currentYear < 0) {
                    Window.alert("Current year must be > -1!");
                    return;
                }
                
                if(yearsCount <= 0) {
                    Window.alert("Years count must be > 0");
                    return;
                }
                
                datePickerFloating.setFloatingYearsRange(currentYear, yearsCount);
            }
        });
        
        datePickerFloating.addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(ValueChangeEvent<Date> event) {
                selectedDateLabel.setText("Selected date: " +
                        DATE_FORMAT.format(event.getValue()));
            }
        });
        
        return mainPanel;
    }
    
    @SuppressWarnings("deprecation")
    private Widget createFixedDatePickerExample() {
        
        int currentYear = new Date().getYear() + 1900;
        int minYear = currentYear - 5;
        int maxYear = currentYear + 5;
        
        final ListBoxDatePicker datePickerFixed = new ListBoxDatePicker();
        datePickerFixed.setFixedYearsRange(minYear, maxYear);
        
        final Label minYearLabel = new Label("Min year:"); 
        final Label maxYearLabel = new Label("Max year:"); 
        final Label selectedDateLabel = new Label();
        
        final TextBox minYearInput = new TextBox();
        minYearInput.setText(String.valueOf(minYear));
        final TextBox maxYearInput = new TextBox();
        maxYearInput.setText(String.valueOf(maxYear));
        
        final Button setRangeButton = new Button("Set range");

        VerticalPanel datePickerPanel = new VerticalPanel();
        datePickerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        datePickerPanel.setWidth("300px");
        datePickerPanel.setSpacing(5);
        datePickerPanel.add(datePickerFixed);
        datePickerPanel.add(selectedDateLabel);
        datePickerPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

        VerticalPanel yearsRangePanel = new VerticalPanel();
        yearsRangePanel.setWidth("300px");
        yearsRangePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
        yearsRangePanel.setSpacing(5);
        yearsRangePanel.add(minYearLabel);
        yearsRangePanel.add(minYearInput);
        yearsRangePanel.add(maxYearLabel);
        yearsRangePanel.add(maxYearInput);
        yearsRangePanel.add(setRangeButton);
        
        HorizontalPanel mainPanel = new HorizontalPanel();
        mainPanel.setWidth("100%");
        mainPanel.setSpacing(10);
        mainPanel.add(datePickerPanel);
        mainPanel.add(yearsRangePanel);
        mainPanel.setCellHorizontalAlignment(datePickerPanel, HasHorizontalAlignment.ALIGN_RIGHT);
        mainPanel.setCellHorizontalAlignment(yearsRangePanel, HasHorizontalAlignment.ALIGN_LEFT);
        
        setRangeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {               
                int minYear = Integer.parseInt(minYearInput.getValue());
                int maxYear = Integer.parseInt(maxYearInput.getValue());
                
                if(minYear <= 0 || maxYear <= 0 || minYear > maxYear ) {
                    Window.alert("Wrong fixed range of years!" );
                    return;
                }
                
                datePickerFixed.setFixedYearsRange(minYear, maxYear);
            }
        });

        datePickerFixed.addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(ValueChangeEvent<Date> event) {
                selectedDateLabel.setText("Selected date: " +
                        DATE_FORMAT.format(event.getValue()));
            }
        });
        
        return mainPanel;
    }
    
}
