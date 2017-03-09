package ru.eqlbin.gwt.datepicker.client;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
    
    private String[] years;      // list of years available for selection

    private int minYear;
    private int maxYear;
    
    private int yearsCount = 20;
    
    private boolean fixedRange = false;
    
//    private boolean dropdownVisible = true;
//    private boolean yearsButtonsVisible = true;
    
    
    @UiField HorizontalPanel yearMonthSelectPanel;
    @UiField ListBox yearsDropdown;
    @UiField ListBox monthsDropdown;

    @UiField Button prevMonthButton;
    @UiField Button nextMonthButton;
    
    @UiField Button prevYearButton;
    @UiField Button nextYearButton;
    
//    @UiField Label currentMonthLabel;
    
    
    private static ListBoxMonthSelectorUiBinder uiBinder = GWT.create(ListBoxMonthSelectorUiBinder.class);

    interface ListBoxMonthSelectorUiBinder extends UiBinder<Widget, ListBoxMonthSelector> {
    }

    public ListBoxMonthSelector() {
        initWidget(uiBinder.createAndBindUi(this));

    }
    
    @Override
    protected void setup() {
        initYearsDropdown();
        initMonthsDropdown();
        initButtons(); 
    }

    @Override
    protected void refresh() {
        updateUI();
    }

    /**
     * Initializes the {@link #yearsDropdown}
     */
    private void initYearsDropdown() {
        yearsDropdown.setVisibleItemCount(1);
        yearsDropdown.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                updateModel();
            }
        });
        
        buildYearsByCount(getCurrentYear());
        rebuildYearsDropdown();
    }

    /**
     * Initializes the {@link #monthsDropdown}
     */
    private void initMonthsDropdown() {
        
        // localized short month names
        String[] monthNames = LocaleInfo.getCurrentLocale()
                                .getDateTimeFormatInfo().monthsShortStandalone();
        for (String monthName : monthNames) {
            monthsDropdown.addItem(monthName);
        }
        
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
    
    public void setFixedYearsRange(int minYear, int maxYear) {
        
        resetYears();
        
        int currentYear = getCurrentYear();
        
        if(currentYear < minYear || currentYear > maxYear) {
            currentYear = minYear + (maxYear - minYear)/2;
            setCurrentYear(currentYear);
        }
        
        this.minYear = minYear;
        this.maxYear = maxYear;
        fixedRange = true;
        updateUI();
    }
    
    public void setFloatingYearsRange(int currentYear, int yearsCount) {
        
        resetYears();
        
        this.yearsCount = yearsCount;
        setCurrentYear(currentYear);
        fixedRange = false;
        updateUI();
    }

    /**         
     * Generates and stores in a field {@link #years}
     * the list of years with the size of {@link #yearsCount}.
     * <br><br>
     * @param baseYear - base year; must be > 0.
     */
    private void buildYearsByCount(int baseYear){
        
        if (baseYear < 0)
            throw new IllegalArgumentException("Base year must be > 0!");
        
        int shift = yearsCount/2;
        int firstYear = baseYear - shift;
        this.years = new String[yearsCount];

        for (int i = firstYear; i < firstYear + yearsCount; i++) {
                this.years[i - firstYear] = formatYear(i);
        } 
    }
    
    /**
     * Generates and stores in a field {@link #years}
     * a list of years from first to last, inclusive
     * 
     * @param minYear - first year in the list
     * @param maxYear - last year in the list
     */
    private void buildYearsByRange(){
        
        if (maxYear < minYear) throw 
            new IllegalArgumentException("Max year must be >= min year");
        if (minYear < 1 || maxYear < 1) throw 
            new IllegalArgumentException("Years must be > 0");

        
        this.years = new String[maxYear - minYear + 1];
        for (int year = minYear; year <= maxYear; year++) {
                this.years[year - minYear] = formatYear(year);
        }
    }

    
    
    /**
     * Rebuilds the component {@link #yearsDropdown}
     * according with the current value of the 
     * field {@link #years}
     */
    private void rebuildYearsDropdown() {

        if(fixedRange) {
            if(!yearsBuilded()) buildYearsByRange();
        } else {
            buildYearsByCount(getCurrentYear());
        }

        this.yearsDropdown.clear();
        for (int i=0; i<this.years.length; i++) {
            String year = this.years[i];
            this.yearsDropdown.addItem(year);
        }
    }
    
    
    /**
     * Sets the values ​​in the components {@link #yearsDropdown}
     * and {@link #monthsDropdown}, which correspond to the current month of the 
     * {@link com.google.gwt.user.datepicker.client.CalendarModel CalendarModel}
     */
    private void updateUI() { 
        
        rebuildYearsDropdown();
        
        Date currentMonth = getModel().getCurrentMonth();
        
        int year = currentMonth.getYear() + 1900;
        int month = currentMonth.getMonth();
        
        boolean yearSetted = false;
        boolean monthSetted = false;
        
        for (int i = 0; i < yearsDropdown.getItemCount(); i++) {
            if (yearsDropdown.getItemText(i).equals(String.valueOf(year))) {
                yearsDropdown.setSelectedIndex(i);
                yearSetted = true;
                break;
            }
        }
                
        monthsDropdown.setSelectedIndex(month);
        monthSetted = true;
        
        if(!yearSetted || !monthSetted)
            throw new RuntimeException("Can't set month " + MONTH_FORMAT.format(currentMonth) + 
                                        " in " + getClass().getName());
        
//        currentMonthLabel.setText(MONTH_FORMAT_LABEL.format(currentMonth));
        
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
        updateUI();
    }
    
    
    private void prevMonth() {
        if(!hasPrevMonth()) return;
        getModel().shiftCurrentMonth(-1);
        updateUI();
    }
    
    private void nextYear() {
        if(!hasNextYear()) return;
        getModel().shiftCurrentMonth(12);
        updateUI();
    }
    
    private void prevYear() {
        if(!hasPrevYear()) return;
        getModel().shiftCurrentMonth(-12);
        updateUI();
    }
    
    private boolean hasPrevMonth(){
        return getCurrentMonth() > 1 || hasPrevYear();
    }
    
    private boolean hasNextMonth(){
        return getCurrentMonth() < 12 || hasNextYear();
    }
    
    private boolean hasPrevYear(){
        return !fixedRange || getCurrentYear() > minYear;
    }
    
    private boolean hasNextYear(){
        return !fixedRange || getCurrentYear() < maxYear;
    }
    
//    private void updateNextButtonsState(){
//
//        if(!hasNextMonth()) {
//            nextMonthButton.setEnabled(false);
//        } else {
//            nextMonthButton.setEnabled(true);
//        }
//        
//        if(!hasNextYear()) {
//            nextYearButton.setEnabled(false);
//        } else {
//            nextYearButton.setEnabled(true);
//        }
//    }

    protected void setCurrentYear(int year){
        getModel().getCurrentMonth().setYear(year - 1900);
    }
    
    protected void setCurrentMonth(int year, int month){
        Date date = getModel().getCurrentMonth();
        date.setYear(year);
        date.setMonth(month-1);
    }
    
    protected int getCurrentYear(){
        return getModel().getCurrentMonth().getYear() + 1900;
    }
    
    protected int getCurrentMonth(){
        return getModel().getCurrentMonth().getMonth() + 1;
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
    
    protected int getYearsCount() {
        return yearsCount;
    }
    
    private boolean yearsBuilded(){
        return years != null && years.length > 0;
    }
    
    private void resetYears(){
        years = null;
        this.minYear = 0;
        this.maxYear = 0;
        this.yearsCount = 0;
    }
    
    
}
