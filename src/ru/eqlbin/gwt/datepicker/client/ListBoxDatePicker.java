package ru.eqlbin.gwt.datepicker.client;

import java.util.Date;

import com.google.gwt.user.datepicker.client.CalendarModel;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.datepicker.client.DefaultCalendarView;

/**
 * Date picker component which allows to select the current month of the calendar,
 * using the drop-down list for setting the month and year.
 * 
 * @author eqlbin
 *
 */
public class ListBoxDatePicker extends DatePicker {

    private ListBoxMonthSelector monthSelector;
    
    public ListBoxDatePicker() {
        super(new ListBoxMonthSelector(), new DefaultCalendarView(),
                new CalendarModel());
        monthSelector = (ListBoxMonthSelector) getMonthSelector();
    }
    
    public void setDateRange(Date minDate, Date maxDate) {
        monthSelector.setDateRange(minDate, maxDate);
    }
    
    @Override
    public void setVisibleYearCount(int numberOfYears) {
        monthSelector.setDropdownYearsCount(numberOfYears);
    }
    
    @Override
    public int getVisibleYearCount() {
        return monthSelector.getDropdownYearsCount();
    }
    
    @Override
    public void setYearAndMonthDropdownVisible(boolean dropdownVisible) {
        monthSelector.setDropdownVisible(dropdownVisible);
    }

    @Override
    public boolean isYearAndMonthDropdownVisible() {
        return monthSelector.isDropdownVisible();
    }
    
    @Override
    public void setYearArrowsVisible(boolean yearArrowsVisible) {
        monthSelector.setYearsButtonsVisible(yearArrowsVisible);
    }
    
    @Override
    public boolean isYearArrowsVisible() {
        return monthSelector.isYearsButtonsVisible();
    }
}
