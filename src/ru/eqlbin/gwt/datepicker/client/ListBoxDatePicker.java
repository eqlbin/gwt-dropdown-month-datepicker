package ru.eqlbin.gwt.datepicker.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.datepicker.client.CalendarModel;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.datepicker.client.DefaultCalendarView;
import com.google.gwt.user.datepicker.client.MonthSelector;

public class ListBoxDatePicker extends DatePicker {

    public ListBoxDatePicker() {
        super(new ListBoxMonthSelector(), new DefaultCalendarView(),
                new CalendarModel());
        // по умолчанию устанавливаем 
        // плавающий диапазон годов
        setFloatingYearsRange(-5, 5);
    }
  
    /**
     * Метод устанавливает плавающий диапазон годов.
     * 
     * @param negativeShift - значение смещения влево относительно 
     *                        текущего выбранного года. Должно быть < 0.
     * @param positiveShift - значение смещения вправо относительно 
     *                        текущего выбранного года. Должно быть > 0.
     */
    public void setFloatingYearsRange(int negativeShift, int positiveShift) {
        ((ListBoxMonthSelector) getMonthSelector()).setYearsRange(negativeShift, positiveShift, 
                                            ListBoxMonthSelector.YearsRangeType.Floating);
    }
    
    /**
     * Метод устанавливает фиксированный диапазон годов.
     * 
     * @param first - значение первого года в списке. Должно быть > 0 и <= last.
     * @param last - значение последнего года в списке. Должно быть > 0 и >= first.
     */
    public void setFixedYearsRange(int first, int last) {
        ((ListBoxMonthSelector) getMonthSelector()).setYearsRange(first, last, 
                                            ListBoxMonthSelector.YearsRangeType.Fixed);
    }

    /**
     * Расширяет стандартный {@link com.google.gwt.user.datepicker.client.MonthSelector MonthSelector}
     * из библиотеки GWT, который устанавливается в качестве компонента для выбора месяца по умолчанию
     * для {@link com.google.gwt.user.datepicker.client.DatePicker DatePicker}. 
     * 
     * Предоставляет пользователю возможность выбора текущего мясяца календаря 
     * {@link com.google.gwt.user.datepicker.client.DatePicker DatePicker},
     * используя выпадающий список {@link com.google.gwt.user.client.ui.ListBox ListBox}
     * для установки года и месяца.
     * 
     * @author eqlbin
     *
     */
    private static class ListBoxMonthSelector extends MonthSelector {

        /**
         * 
         * Тип диапазона годов для {@link ListBoxMonthSelector}.<br><br>
         * 
         * <b>Fixed</b> - фиксированный диапазон<br>
         * <b>Floating</b> - плавающий диапазон, который может меняться в 
         * зависимости от текущего года
         * 
         */
        public static enum YearsRangeType {Fixed, Floating};
        
        public YearsRangeType yearsRangeType = YearsRangeType.Fixed;

        private static final DateTimeFormat monthFormat = 
                                 DateTimeFormat.getFormat("yyyy-MMM");
        private static final DateTimeFormat yearFormat = 
                                 DateTimeFormat.getFormat("yyyy");

        private String[] monthNames; // список названий месяцев
        private String[] years;      // список годов доступных для выбора

        private final ListBox yearsBox = new ListBox(false);
        private final ListBox monthsBox = new ListBox(false);

        private int negativeYearShift = -1, positiveYearShift = -1;

        private Grid grid;       

        @Override
        protected void setup() {
            initYearsBox();
            initMonthsBox();
            initDatepicker();   
        }

        @Override
        protected void onLoad() {
            super.onLoad();
            
            if(yearsRangeType == YearsRangeType.Floating)
                updateYearsBoxByShifts();
            
            setCurrentMonthInListBoxes();  
        }
        
        @Override
        protected void refresh() {
            setCurrentMonthInListBoxes();           
        }

        /**
         * Метод для инициализации {@link #yearsBox}
         */
        private void initYearsBox() {
            yearsBox.setVisibleItemCount(1);
            yearsBox.addChangeHandler(new ChangeHandler() {
                @Override
                public void onChange(ChangeEvent event) {
                                        
                    if(yearsRangeType == YearsRangeType.Floating)
                        updateYearsBoxByShifts();

                    updateMonth();
                }
            });
        }

        /**
         * Метод для инициализации {@link #monthsBox}
         */
        private void initMonthsBox() {
            // Заполняем названия месяцев с учетом локали
            monthNames = LocaleInfo.getCurrentLocale().
                            getDateTimeFormatInfo().monthsShortStandalone();
            for (String month : monthNames) {
                monthsBox.addItem(month);
            }
            
            monthsBox.setVisibleItemCount(1);
            monthsBox.addChangeHandler(new ChangeHandler() {
                @Override
                public void onChange(ChangeEvent event) {
                    updateMonth();
                }
            });
        }

        /**
         * Метод для инициализации Datepicker. Должен быть вызван после 
         * {@link #initYearsBox()} и {@link #initMonthsBox()}
         */
        private void initDatepicker(){
            grid = new Grid(1, 2);
            grid.setWidget(0, 0, yearsBox);
            grid.setWidget(0, 1, monthsBox);
            grid.setStyleName("ListBoxMonthSelector");
            
            initWidget(grid);
        }
        
        /**
         * Метод устанавливает и отображает текущий месяц календаря, 
         * который соответствует значениям установленным в  
         * компонентах {@link #yearsBox} и {@link #monthsBox}
         */
        private void updateMonth() {
            setCurrentMonthByListBoxes();
            refreshAll();
        }

        
        /**
         * Устанавливает диапазон годов, доступных для выбора в календаре, и его поведение. 
         * 
         * @param first - значение первого года в списке, если тип диапазона фиксированный, 
         *                или значение смещения влево от текущего выбранного года, если
         *                диапазон плавающий. При фиксированном диапазоне значение должно 
         *                быть > 0 и <= last, а при плавающем < 0
         *                
         * @param last -  значение последнего года в списке, если тип диапазона фиксированный, 
         *                или значение смещения вправо от текущего выбранного года, если
         *                диапазон плавающий. При фиксированном диапазоне значение должно 
         *                быть > 0 и >= first, а при плавающем > 0
         *                 
         * @param type -  тип диапазона, который определяет его поведение
         * 
         * @see YearsRangeType
         */
        public void setYearsRange(int first, int last, YearsRangeType type){
            yearsRangeType = type;
            
            switch (type) {
            
            case Fixed:
                
                this.negativeYearShift = -1;
                this.positiveYearShift = -1;
                
                buildYears(first, last);
                updateYearsBox();

                break;
                
            case Floating:
                
                if (first >= 0) throw 
                    new IllegalArgumentException("First year shift value must be < 0");
                if (last <= 0) throw 
                    new IllegalArgumentException("Last year shift value must be > 0");
                
                this.negativeYearShift = Math.abs(first);
                this.positiveYearShift = last;

                int currentYear = Integer.parseInt(yearFormat.format(getModel().getCurrentMonth()));
                buildYearsByShifts(currentYear, negativeYearShift, positiveYearShift);
                updateYearsBox();

                break;
            }
            
            setCurrentMonthInListBoxes();          
            updateMonth();
        }
        
        
                
        /**
         * Метод перестраивает компонент {@link #yearsBox},
         * используя в качестве базового года текущий выбраный год
         * в {@link #yearsBox} и ранее установленные значения смещений
         * {@link #negativeShiftYear} и {@link #positiveYearShift}
         * 
         */
        private void updateYearsBoxByShifts(){
            // запоминаем выбранный год
            int selectedIndex = yearsBox.getSelectedIndex();
            String selectedYear = 
                    yearsBox.getItemText(selectedIndex);

            buildYearsByShifts(Integer.parseInt(selectedYear), 
                    this.negativeYearShift, this.positiveYearShift);
            updateYearsBox();

            // устанавливаем год, который был выбран до перестроения
            for (int i = 0; i < yearsBox.getItemCount(); i++) {
                if (yearsBox.getItemText(i).equals(selectedYear)) {
                    yearsBox.setSelectedIndex(i);
                    break;
                }
            }  
        }
        
        /**         
         * 
         * Метод генерирует и сохраняет в поле {@link #years}
         * список годов. Генерация списка происходит на основе 
         * смещений negativeShiftYear и positiveShiftYear относительно 
         * базового года baseYear.<br><br>
         * 
         * Например, чтобы создать список годов от 1990 до 2005 
         * включительно, нужно выполнить метод:<br><br>
         * 
         * {@code buildYearsByShifts(2000, 10, 5);}
         * 
         * @param baseYear - базовый год. Должен быть > 0.
         * 
         * @param negativeShiftYear - смещение влево относительно базового года.
         *                            Должно быть > 0.
         * @param positiveShiftYear - смещение вправо относительно базового года. 
         *                            Должно быть > 0.
         */
        private void buildYearsByShifts(int baseYear, int negativeShiftYear, int positiveShiftYear){
            
            if (baseYear < 0 || negativeShiftYear < 0 || positiveShiftYear < 0)
                throw new IllegalArgumentException("All arguments must be positive values");
            
            // отсекаем возможные отрицательные значения годов, 
            // пересчитывая левое смещение
            if(baseYear <= negativeShiftYear)
                negativeShiftYear = 
                    negativeShiftYear - (negativeShiftYear - baseYear) - 1;

            int yearsCount = positiveShiftYear + negativeShiftYear + 1;
            int firstYear = baseYear - negativeShiftYear;
        
            this.years = new String[yearsCount];

            for (int i = firstYear; i < firstYear + yearsCount; i++) {
                    this.years[i - firstYear] = yearToString(i);
            } 
        }
        
        
        /**
         * Метод генерирует и сохраняет в поле {@link #years}
         * список годов от first до last включительно
         * 
         * @param first - первый год в списке
         * @param last - последний год в списке
         */
        private void buildYears(int first, int last){
            
            if (last < first) throw 
                new IllegalArgumentException("Last year must be >= first year");
            if (first < 1 || last < 1) throw 
                new IllegalArgumentException("Years must be > 0");

            this.years = new String[last - first + 1];

            for (int i = first; i <= last; i++) {
                    this.years[i - first] = yearToString(i);
            }
        }
        
        /**
         * Метод перестраивает компонент {@link #yearsBox}
         * в соответствии с текущим значением поля {@link #years}
         */
        private void updateYearsBox() {
            this.yearsBox.clear();
            for (String year : this.years) {
                this.yearsBox.addItem(year);
            }
        }

        /**
         * Метод устанавливает значения в компонентах {@link #yearsBox}
         * и {@link #monthsBox}, которые соответствуют текущему месяцу
         * календаря
         */
        private void setCurrentMonthInListBoxes() {

            String currentMonth = monthFormat.
                                    format(getModel().getCurrentMonth());
            
            String[] yearAndMonth = currentMonth.split("-");

            if(yearsBox.getItemCount() == 0){
                yearsBox.addItem(yearFormat.format(getModel()
                    .getCurrentMonth()));
            }
            
            if (lastYearInBox() < Integer.parseInt(yearAndMonth[0])) {
                yearsBox.setSelectedIndex(yearsBox.getItemCount() - 1);
                monthsBox.setSelectedIndex(monthsBox.getItemCount() - 1);
                setCurrentMonthByListBoxes();
                return;
            }

            for (int i = 0; i < yearsBox.getItemCount(); i++) {
                if (yearsBox.getItemText(i).equals(yearAndMonth[0])) {
                    yearsBox.setSelectedIndex(i);
                    break;
                }
            }

            for (int i = 0; i < monthsBox.getItemCount(); i++) {
                if (monthsBox.getItemText(i).equals(yearAndMonth[1])) {
                    monthsBox.setSelectedIndex(i);
                    break;
                }
            }
        }
        
        /**
         * Метод устанавливает текущий месяц календаря, основываясь на
         * значениях, которые указаны в компонентах {@link #yearsBox}
         * и {@link #monthsBox}
         */
        private void setCurrentMonthByListBoxes() {
            String year = yearsBox.getItemText(yearsBox.getSelectedIndex());
            String month = monthsBox.getItemText(monthsBox.getSelectedIndex());

            getModel().setCurrentMonth(
                    monthFormat.parse(year + "-" + month));
        }

        /**
         * Метод возвращает последний год в {@link #monthsBox}
         * 
         * @return последний год в {@link #monthsBox}
         */
        private int lastYearInBox() {
            return Integer.parseInt(yearsBox.getItemText(yearsBox
                    .getItemCount() - 1));
        }
        
        /**
         * Метод переводит значение года в строку таким образом,
         * что количество символов в строке всегда равно 4. 
         * Например, год равный 15 будет преобразован в строку 0015.
         * 
         * @param year
         * @return
         */
        private String yearToString(int year){
            
            if(year < 1000) 
                return NumberFormat.getFormat("0000").format(year);
            
            return String.valueOf(year);
        }
    }
}
