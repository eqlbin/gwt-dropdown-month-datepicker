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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
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
    private static final DateTimeFormat YEAR_FORMAT = 
                             DateTimeFormat.getFormat("yyyy");

    private String[] years;      // list of years available for selection

    private int minYear;
    private int maxYear;
    
    private int yearsCount = 20;
    
    private boolean fixedRange = false;
    
    @UiField ListBox yearsSelect;
    @UiField ListBox monthsSelect;

    @UiField Button prevMonthButton;
    @UiField Button nextMonthButton;
    
    @UiField Button prevYearButton;
    @UiField Button nextYearButton;
    
    private static ListBoxMonthSelectorUiBinder uiBinder = GWT.create(ListBoxMonthSelectorUiBinder.class);

    interface ListBoxMonthSelectorUiBinder extends UiBinder<Widget, ListBoxMonthSelector> {
    }

    public ListBoxMonthSelector() {
        initWidget(uiBinder.createAndBindUi(this));

    }
    
    @Override
    protected void setup() {
        initYearsSelect();
        initMonthsSelect();
        initButtons(); 
    }

    @Override
    protected void refresh() {
        setListBoxesByModel();
    }

    /**
     * Initializes the {@link #yearsSelect}
     */
    private void initYearsSelect() {
        yearsSelect.setVisibleItemCount(1);
        yearsSelect.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                if(!fixedRange) {
                    buildYearsByCount(Integer.parseInt(getSelectedYear()));
                    updateYearsBox();
                }
                setModelByListBoxes();
            }
        });
        
        buildYearsByCount(getModel().getCurrentMonth().getYear() + 1900);
        updateYearsBox();
    }

    /**
     * Initializes the {@link #monthsSelect}
     */
    private void initMonthsSelect() {
        
        // localized short month names
        String[] monthNames = LocaleInfo.getCurrentLocale()
                                .getDateTimeFormatInfo().monthsShortStandalone();
        for (String monthName : monthNames) {
            monthsSelect.addItem(monthName);
        }
        
        monthsSelect.setVisibleItemCount(1);
        monthsSelect.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                setModelByListBoxes();
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
        this.minYear = minYear;
        this.maxYear = maxYear;
        buildYearsByRange();
        fixedRange = true;
        updateYearsBox();
        setModelByListBoxes();
    }
    
    public void setFloatingYearsRange(int currentYear, int yearsCount) {
        this.yearsCount = yearsCount;
        buildYearsByCount(currentYear);
        fixedRange = false;
        updateYearsBox();
        setModelByListBoxes();
    }

    /**
     * Rebuilds the component {@link #yearsSelect}
     * according with the current value of the 
     * field {@link #years}
     */
    private void updateYearsBox() {
        
        // save the selected year
        String selectedYear = getSelectedYear();
        boolean selectedFound = false;

        this.yearsSelect.clear();
        for (int i=0; i<this.years.length; i++) {
            String year = this.years[i];
            this.yearsSelect.addItem(year);
            
            if(year.equals(selectedYear)) {
                this.yearsSelect.setSelectedIndex(i);
                selectedFound = true;
            }
        }
        
        if(selectedFound) return;
        
        // select the middle item if the previous selected year is not found in yearsBox
        this.yearsSelect.setSelectedIndex(this.yearsSelect.getItemCount()/2);
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
                this.years[i - firstYear] = yearToString(i);
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
                this.years[year - minYear] = yearToString(year);
        }
    }

    /**
     * Sets the values ​​in the components {@link #yearsSelect}
     * and {@link #monthsSelect}, which correspond to the current month of the 
     * {@link com.google.gwt.user.datepicker.client.CalendarModel CalendarModel}
     */
    private void setListBoxesByModel() { 

        Date currentMonth = getModel().getCurrentMonth();
        
        int year = currentMonth.getYear() + 1900;
        int month = currentMonth.getMonth();
        
        boolean yearSetted = false;
        boolean monthSetted = false;
        
        for (int i = 0; i < yearsSelect.getItemCount(); i++) {
            if (yearsSelect.getItemText(i).equals(String.valueOf(year))) {
                yearsSelect.setSelectedIndex(i);
                yearSetted = true;
                break;
            }
        }
    
        monthsSelect.setSelectedIndex(month);
        monthSetted = true;
    
        if(!yearSetted || !monthSetted)
            throw new RuntimeException("Can't set month " + MONTH_FORMAT.format(currentMonth) + 
                                        " in " + getClass().getName());
    }
    
    /**
     * Sets the current calendar month, based on the values 
     * ​​that are specified in the components {@link #yearsSelect}
     * и {@link #monthsSelect}
     */
    private void setModelByListBoxes() {
        String year = yearsSelect.getItemText(yearsSelect.getSelectedIndex());
        String month = monthsSelect.getItemText(monthsSelect.getSelectedIndex());
    
        getModel().setCurrentMonth(
                MONTH_FORMAT.parse(year + "-" + month));
        refreshAll();
    }

    
    private void nextMonth() {
        
        if(monthsSelect.getSelectedIndex() == monthsSelect.getItemCount() - 1) {
            
            if(hasNextMonth()) {
                nextYear();
                monthsSelect.setSelectedIndex(0);
            }
            
            return;
        }
        
        monthsSelect.setSelectedIndex(monthsSelect.getSelectedIndex() + 1);
        
        if(!fixedRange) {
            buildYearsByCount(Integer.parseInt(getSelectedYear())); 
            updateYearsBox();
        }
        setModelByListBoxes();
    }
    
    
    private void prevMonth() {
        
        if(monthsSelect.getSelectedIndex() == 0) {
            
            if(hasPrevMonth()) {
                prevYear();
                monthsSelect.setSelectedIndex(monthsSelect.getItemCount() - 1);
            }
            
            return;
        }
        
        monthsSelect.setSelectedIndex(monthsSelect.getSelectedIndex() - 1);
        
        if(!fixedRange) {
            buildYearsByCount(Integer.parseInt(getSelectedYear())); 
            updateYearsBox();
        }
        setModelByListBoxes();
    }
    
    
    private void nextYear() {
        
        if(fixedRange && !hasNextYear()) return;
        
        yearsSelect.setSelectedIndex(yearsSelect.getSelectedIndex() + 1);
        
        if(!fixedRange) {
            buildYearsByCount(Integer.parseInt(getSelectedYear())); 
            updateYearsBox();
        }
        setModelByListBoxes();
    }
    
    private void prevYear() {
        
        if(fixedRange && !hasPrevYear()) return;
        
        yearsSelect.setSelectedIndex(yearsSelect.getSelectedIndex() - 1);
        
        if(!fixedRange) {
            buildYearsByCount(Integer.parseInt(getSelectedYear())); 
            updateYearsBox();
        }
        setModelByListBoxes();
    }
    
    private boolean hasPrevMonth(){
        return hasPrevYear() && (!fixedRange || monthsSelect.getSelectedIndex() > 0);
    }
    
    private boolean hasNextMonth(){
        return hasNextYear() && (!fixedRange || monthsSelect.getSelectedIndex() <  monthsSelect.getItemCount() - 1);
    }
    
    private boolean hasPrevYear(){
        return yearsSelect.getSelectedIndex() > 0;
    }
    
    private boolean hasNextYear(){
        return yearsSelect.getSelectedIndex() < yearsSelect.getItemCount() - 1;
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
    }
    
    
    /**
     * Returns current selected year in 
     * the {@link #yearsSelect} as String
     * 
     * @return current selected year
     */
    private String getSelectedYear(){
        String selectedYear = null;
        int selectedIndex = yearsSelect.getSelectedIndex();
        
        if(selectedIndex >= 0)
            selectedYear = yearsSelect.getItemText(selectedIndex);
        
        return selectedYear;
    }
    
    /**
     * Returns current year of the 
     * {@link com.google.gwt.user.datepicker.client.CalendarModel CalendarModel}
     * as String
     * 
     * @return current year of the DatePicker model
     */
    private String getCurrentModelYear(){
        return YEAR_FORMAT.format(getModel().getCurrentMonth());
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
    private static String yearToString(int year){      
        if(year < 1000) 
            return NumberFormat.getFormat("0000").format(year);
        
        return String.valueOf(year);
    }
}
