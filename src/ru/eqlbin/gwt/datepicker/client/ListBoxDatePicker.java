package ru.eqlbin.gwt.datepicker.client;

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

    public ListBoxDatePicker() {
        super(new ListBoxMonthSelector(), new DefaultCalendarView(),
                new CalendarModel());
    }
  
    /**
     * Sets the floating range of years
     * 
     * @param minYear - first year of the range
     * @param maxYear - last year of the range 
     */
    public void setFixedYearsRange(int minYear, int maxYear) {
        ((ListBoxMonthSelector)getMonthSelector()).setFixedYearsRange(minYear, maxYear);
        ((DefaultCalendarView) getView()).refresh();
    }

    /**
     * Sets the floating range of years
     * 
     * @param currentYear - current selected year
     * @param yearsCount - size of the years select menu
     */
    public void setFloatingYearsRange(int currentYear, int yearsCount) {
        ((ListBoxMonthSelector) getMonthSelector()).setFloatingYearsRange(currentYear, yearsCount);
        ((DefaultCalendarView) getView()).refresh();
    }
    
}
