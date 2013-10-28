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
     * @param negativeShift - shift to the left relative to 
     *                        the selected year; must be < 0.
     * @param positiveShift - shift to the right relative to 
     *                        the selected year; must be > 0.
     */
    public void setFloatingYearsRange(int negativeShift, int positiveShift) {
        ((ListBoxMonthSelector) getMonthSelector()).setYearsRange(negativeShift, positiveShift, 
                                            ListBoxMonthSelector.YearsRangeType.Floating);
        ((DefaultCalendarView)getView()).refresh();
    }
    
    /**
     * Sets the fixed range of years
     * 
     * @param first - value of the first year in the list; 
     *                must be > 0 and <= last.
     * @param last  - value of the last year in the list; 
     *                must be > 0 and >= first.
     */
    public void setFixedYearsRange(int first, int last) {
        ((ListBoxMonthSelector) getMonthSelector()).setYearsRange(first, last, 
                                            ListBoxMonthSelector.YearsRangeType.Fixed);
        ((DefaultCalendarView)getView()).refresh();
    }
    
   

}
