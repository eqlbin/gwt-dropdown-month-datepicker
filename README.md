ListBoxDatePicker for GWT
========================

Date picker component which allows to select the current month 
of the calendar, using the drop-down list for setting the month and year.

# Usage

```xml
<inherits name='ru.eqlbin.gwt.datepicker.ListBoxDatePicker'/>
```

```java
// Fixed range of years
ListBoxDatePicker datePickerFixed = new ListBoxDatePicker();
int minYear = 2010;
int maxYear = 2020;
datePickerFixed.setFixedYearsRange(minYear, maxYear);

// Floating range of years
ListBoxDatePicker datePickerFloating = new ListBoxDatePicker();
int currentYear = 2017;
int yearsCount = 10;
datePickerFloating.setFloatingYearsRange(currentYear, yearsCount);
```

# Screenshot

![alt tag](https://github.com/eqlbin/gwt-listbox-datepicker/raw/master/screenshots/screenshot.png)

# Note

This project was created in times of GWT 2.5.

Similar functionality appeared in default DatePicker since version 2.6:

```java
DatePicker datePicker = new DatePicker();
datePicker.setYearAndMonthDropdownVisible(true);
datePicker.setYearArrowsVisible(true);
```

So, now this project is just an example of how to create your own MonthSelector.