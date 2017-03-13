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
  
    /**
     * Sets the floating range of years
     * 
     * @param minYear - first year of the range
     * @param maxYear - last year of the range 
     */
    public void setFixedYearsRange(int minYear, int maxYear) {
        monthSelector.setFixedYearsRange(minYear, maxYear);
        ((DefaultCalendarView) getView()).refresh();
    }
    
    public void setFixedDateRange(Date minDate, Date maxDate) {
        monthSelector.setFixedDateRange(minDate, maxDate);
        ((DefaultCalendarView) getView()).refresh();
    }


    /**
     * Sets the floating range of years
     * 
     * @param currentYear - current selected year
     * @param yearsCount - size of the years select menu
     */
    public void setFloatingYearsRange(int currentYear, int yearsCount) {
        monthSelector.setFloatingYearsRange(currentYear, yearsCount);
        ((DefaultCalendarView) getView()).refresh();
    }
    
    
    @Override
    public void setVisibleYearCount(int numberOfYears) {
        monthSelector.setFloatingYearsRange(monthSelector.getCurrentYear(), numberOfYears);
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
