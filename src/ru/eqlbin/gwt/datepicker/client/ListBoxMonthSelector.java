package ru.eqlbin.gwt.datepicker.client;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ShowRangeEvent;
import com.google.gwt.event.logical.shared.ShowRangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.datepicker.client.MonthSelector;


/**
 * Extends 
 * {@link com.google.gwt.user.datepicker.client.MonthSelector MonthSelector}
 * from GWT library, which is set as component by default 
 * for {@link com.google.gwt.user.datepicker.client.DatePicker DatePicker}. 
 * 
 * Allows to select the current month of the
 * {@link com.google.gwt.user.datepicker.client.DatePicker DatePicker},
 * using the drop-down list 
 * {@link com.google.gwt.user.client.ui.ListBox ListBox}
 * for setting the month and year.
 * 
 * @author eqlbin
 *
 */
@SuppressWarnings("deprecation")
public class ListBoxMonthSelector extends MonthSelector {

    private static final DateTimeFormat MONTH_FORMAT = 
                             DateTimeFormat.getFormat("yyyy-MMM");

    private static final DateTimeFormat MONTH_FORMAT_LABEL = 
            DateTimeFormat.getFormat("yyyy MMM");
    
    private static final int DEFAULT_DROPDOWN_YEARS_COUNT = 31;
    
    // localized short month names
    String[] monthNames = LocaleInfo.getCurrentLocale()
            .getDateTimeFormatInfo().monthsShortStandalone();
    
    private Date minDate;
    private Date maxDate;
    
    private int dropdownYearsCount = DEFAULT_DROPDOWN_YEARS_COUNT;
    private boolean dropdownVisible = true;
    private boolean yearsButtonsVisible = true;
    
    @UiField HorizontalPanel currentMonthPanel;

    private final ListBox yearsDropdown = new ListBox();
    private final ListBox monthsDropdown = new ListBox();
    private final Label currentMonthLabel = new Label();

    @UiField Button prevMonthButton;
    @UiField Button nextMonthButton;
    
    @UiField Button prevYearButton;
    @UiField Button nextYearButton;
    
    private static ListBoxMonthSelectorUiBinder uiBinder = GWT.create(ListBoxMonthSelectorUiBinder.class);

    interface ListBoxMonthSelectorUiBinder extends UiBinder<Widget, ListBoxMonthSelector> {}

    public ListBoxMonthSelector() {
        initWidget(uiBinder.createAndBindUi(this));
    }
    
    @Override
    protected void setup() {
        initYearsDropdown();
        initMonthsDropdown();
        initButtons(); 
        
        addRangeChecksForDatepicker();
    }

    @Override
    protected void refresh() {
        updateUI();
    }

    /**
     * Initializes the {@link #yearsDropdown}
     */
    private void initYearsDropdown() {
        yearsDropdown.setMultipleSelect(false);
        yearsDropdown.setVisibleItemCount(1);
        yearsDropdown.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                updateModel();
            }
        });
    }

    /**
     * Initializes the {@link #monthsDropdown}
     */
    private void initMonthsDropdown() {
        monthsDropdown.setMultipleSelect(false);
        monthsDropdown.setVisibleItemCount(1);
        monthsDropdown.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                updateModel();
            }
        });
    }

    
    private void initButtons(){
        
        prevYearButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                prevYear();
            }
        });
        
        nextYearButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                nextYear();
            }
        });
        
        prevMonthButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                prevMonth();
            }
        });
        
        nextMonthButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent arg0) {
                nextMonth();
            }
        });
    }
    
    private void addRangeChecksForDatepicker(){
        final DatePicker datePicker = getDatePicker();

        datePicker.addShowRangeHandler(new ShowRangeHandler<Date>() {
            
            @Override
            public void onShowRange(ShowRangeEvent<Date> event) {
                
                if(!isRangeSet()) return;
                
                Date date = CalendarUtil.copyDate(event.getStart());
                
                while(!date.after(event.getEnd())) {
                    
                    if(date.before(minDate) || date.after(maxDate)) {
                        getDatePicker().setTransientEnabledOnDates(false, date);
                    }

                    CalendarUtil.addDaysToDate(date, 1);
                }
            }
        });
        
        datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
            
            @Override
            public void onValueChange(ValueChangeEvent<Date> event) {
                
                if(!isRangeSet()) return;
                
                DatePicker datePicker = getDatePicker();
                Date newDate = event.getValue();
                
                if(newDate.before(minDate)) {
                    datePicker.setValue(minDate);
                    throw new DateOutOfRangeException("The date " + newDate + 
                            " is before the minimum date " + minDate);
                }
                
                if(newDate.after(maxDate)) {
                    datePicker.setValue(maxDate);
                    throw new DateOutOfRangeException("The date " + newDate + 
                            " is after the maximum date " + maxDate);
                }
            }
        });
    }
    
    public void setDateRange(Date minDate, Date maxDate) {

        this.minDate = CalendarUtil.copyDate(minDate);
        CalendarUtil.resetTime(this.minDate);
        this.maxDate = CalendarUtil.copyDate(maxDate);
        CalendarUtil.resetTime(this.maxDate);
        
        int currentYear = getCurrentYear();
        int minYear = getMinYear();
        int maxYear = getMaxYear();
        
        if(currentYear < minYear || currentYear > maxYear) {
            currentYear = minYear + (maxYear - minYear)/2;
            setCurrentYear(currentYear);
        }
        
        refreshAll();
    }
    
    public void setDropdownYearsCount(int dropdownYearsCount) {
        this.dropdownYearsCount = dropdownYearsCount;
        refreshAll();
    }

    private int[] buildYearsList(){
        
        int startYear = -1;
        int endYear = -1;
        
        int currentYear = getCurrentYear();
        int shift = dropdownYearsCount/2;
        
        startYear = currentYear - shift;
        endYear = startYear + dropdownYearsCount - 1;
        
        if(isRangeSet()) {
            int minYear = getMinYear();
            int maxYear = getMaxYear();
            
            if(startYear < minYear) {
                startYear = minYear;
                endYear = startYear + dropdownYearsCount;
            }
            
            if(endYear > maxYear) {
                endYear = maxYear;
                startYear = endYear - dropdownYearsCount;
            }
        } 

        int size = endYear - startYear + 1;
        int[] years = new int[size];
        
        for (int year = startYear; year <= endYear; year++) {
                years[year - startYear] = year;
        }
        
        return years;
    }
    
    /**
     * Rebuilds the component {@link #yearsDropdown}
     * according with the current value of the 
     * field {@link #years}
     */
    private void rebuildYearsDropdown() {

        int[] years = buildYearsList();
        
        this.yearsDropdown.clear();
        for (int i=0; i<years.length; i++) {
            this.yearsDropdown.addItem(formatYear(years[i]));
        }
        
        int currentYear = getCurrentYear();
        boolean yearSet = false;
        
        for (int i = 0; i < yearsDropdown.getItemCount(); i++) {
            if (yearsDropdown.getItemText(i).equals(String.valueOf(currentYear))) {
                yearsDropdown.setSelectedIndex(i);
                yearSet = true;
                break;
            }
        }

        if(!yearSet)
            throw new RuntimeException("Can't select nonexistent year " 
                                        + currentYear + " in dropdown menu!");
    }
    
    private void rebuildMonthsDropdown() {
        
        int startMonth = 0;
        int endMonth = 11;
        
        int currentYear = getCurrentYear();
        
        if(currentYear == getMinYear())
            startMonth = minDate.getMonth();
        
        if(currentYear == getMaxYear())
            endMonth = maxDate.getMonth();

        monthsDropdown.clear();
        for (int i = startMonth; i <= endMonth; i++) {
            monthsDropdown.addItem(monthNames[i]);
        }
        
        Date currentMonth = getModel().getCurrentMonth();
        monthsDropdown.setSelectedIndex(currentMonth.getMonth() - startMonth);
    }
    
    /**
     * Sets the values ​​in the components {@link #yearsDropdown}
     * and {@link #monthsDropdown}, which correspond to the current month of the 
     * {@link com.google.gwt.user.datepicker.client.CalendarModel CalendarModel}
     */
    private void updateUI() { 

        Date currentMonth = CalendarUtil.copyDate(getModel().getCurrentMonth());
        
        if(isBeforeMinMonth(currentMonth)) {
            getModel().setCurrentMonth(minDate);
            refreshAll();
            return;
//            throw new MonthOutOfRangeException(
//                    "The month " + MONTH_FORMAT.format(currentMonth) 
//                     + " is before the minimum month " + MONTH_FORMAT.format(minDate));
        } else if(isAfterMaxMonth(currentMonth)) {
            getModel().setCurrentMonth(maxDate);
            refreshAll();
            return;
//            throw new MonthOutOfRangeException(
//                    "The month " + MONTH_FORMAT.format(currentMonth) 
//                     + " is after the maximum month " + MONTH_FORMAT.format(maxDate));
        }
        
        rebuildYearsDropdown();
        rebuildMonthsDropdown();
        
        currentMonthLabel.setText(MONTH_FORMAT_LABEL.format(currentMonth));

        currentMonthPanel.clear();
        if(dropdownVisible) {
            currentMonthPanel.add(monthsDropdown);
            currentMonthPanel.add(yearsDropdown);
        } else {
            currentMonthPanel.add(currentMonthLabel);
        }
        
        prevYearButton.setVisible(yearsButtonsVisible);
        nextYearButton.setVisible(yearsButtonsVisible);
        
        updateNextButtonsState();
    }
    
    /**
     * Sets the current calendar month, based on the values 
     * ​​that are specified in the components {@link #yearsDropdown}
     * и {@link #monthsDropdown}
     */
    private void updateModel() {
        
        String year = yearsDropdown.getItemText(yearsDropdown.getSelectedIndex());
        String month = monthsDropdown.getItemText(monthsDropdown.getSelectedIndex());

        getModel().setCurrentMonth(
                MONTH_FORMAT.parse(year + "-" + month));
        
        refreshAll();
    }

    
    private void nextMonth() {
        if(!hasNextMonth()) return;
        getModel().shiftCurrentMonth(1);
        refreshAll();
    }
    
    private void prevMonth() {
        if(!hasPrevMonth()) return;
        getModel().shiftCurrentMonth(-1);
        refreshAll();
    }
    
    private void nextYear() {
        if(!hasNextYear()) return;
        getModel().shiftCurrentMonth(12);
        refreshAll();
    }
    
    private void prevYear() {
        if(!hasPrevYear()) return;
        getModel().shiftCurrentMonth(-12);
        refreshAll();
    }
    
    private boolean hasPrevMonth(){
        Date prevMonth = CalendarUtil.copyDate(getModel().getCurrentMonth());
        CalendarUtil.addMonthsToDate(prevMonth, -1);

        return !isBeforeMinMonth(prevMonth);
    }
    
    private boolean hasNextMonth(){
        Date nextMonth = CalendarUtil.copyDate(getModel().getCurrentMonth());
        CalendarUtil.addMonthsToDate(nextMonth, 1);
        return !isAfterMaxMonth(nextMonth);
    }
    
    private boolean hasPrevYear(){
        return !isRangeSet() || getCurrentYear() > getMinYear();
    }
    
    private boolean hasNextYear(){
        return !isRangeSet() || getCurrentYear() < getMaxYear();
    }
    
    public void setDropdownVisible(boolean dropdownVisible) {
        this.dropdownVisible = dropdownVisible;
        refreshAll();
    }
    
    public boolean isDropdownVisible() {
        return dropdownVisible;
    }
    
    public void setYearsButtonsVisible(boolean yearsButtonsVisible) {
        this.yearsButtonsVisible = yearsButtonsVisible;
        refreshAll();
    }
    
    public boolean isYearsButtonsVisible() {
        return yearsButtonsVisible;
    }
    
    public int getDropdownYearsCount() {
        return dropdownYearsCount;
    }
    
    private void updateNextButtonsState(){

        if(!hasNextMonth()) {
            nextMonthButton.setEnabled(false);
        } else {
            nextMonthButton.setEnabled(true);
        }
        
        if(!hasNextYear()) {
            nextYearButton.setEnabled(false);
        } else {
            nextYearButton.setEnabled(true);
        }
        
        if(!hasPrevMonth()) {
            prevMonthButton.setEnabled(false);
        } else {
            prevMonthButton.setEnabled(true);
        }
        
        if(!hasPrevYear()) {
            prevYearButton.setEnabled(false);
        } else {
            prevYearButton.setEnabled(true);
        }
    }

    protected void setCurrentYear(int year){
        getModel().getCurrentMonth().setYear(year - 1900);
    }

    protected int getCurrentYear(){
        return getModel().getCurrentMonth().getYear() + 1900;
    }
    
    /**
     * Converts the value at the string so that the number 
     * of characters is always equal to 4. 
     * 
     * For example, the year of 15 will be converted to a string 0015.
     * 
     * @param year - year to translate
     * @return year as String
     */
    private static String formatYear(int year){    
        if(year < 1000) 
            return NumberFormat.getFormat("0000").format(year);
        
        return String.valueOf(year);
    }
        
    public void resetRange(){
        this.minDate = null;
        this.maxDate = null;
        this.dropdownYearsCount = DEFAULT_DROPDOWN_YEARS_COUNT;
        refreshAll();
    }
    
    public boolean isDateInRange(Date date) {
        if(!isRangeSet()) return true;
        return !(date.before(minDate) || date.after(maxDate));
    }
    
    public boolean isBeforeMinMonth(Date month) {
        if(!isRangeSet()) return false;
        
        Date minMonth = CalendarUtil.copyDate(minDate);
        CalendarUtil.setToFirstDayOfMonth(minMonth);

        return month.before(minMonth);
    }
    
    public boolean isAfterMaxMonth(Date month) {
        if(!isRangeSet()) return false;
        
        Date maxMonth = CalendarUtil.copyDate(maxDate);
        CalendarUtil.setToFirstDayOfMonth(maxMonth);
        
        return month.after(maxMonth);
    }
        
    public boolean isYearInRange(int year) {
        if(!isRangeSet()) return true;
        return !(year < getMinYear() || year > getMaxYear());
    }
    
    private int getMinYear(){
        return isRangeSet() ? minDate.getYear() + 1900 : -1;
    }
    
    private int getMaxYear(){
        return isRangeSet() ? maxDate.getYear() + 1900 : -1;
    }
    
    private boolean isRangeSet(){
        return minDate != null && maxDate != null;
    }
}
