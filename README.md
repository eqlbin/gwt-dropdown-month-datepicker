DropdownMonthDatePicker for GWT
========================

Date picker component which allows to select the current month 
of the calendar, using the drop-down list for setting the month and year.
In addition to this, it supports a fixed date range.

# Demo

http://eqlbin.github.io/gwt-dropdown-month-datepicker/demo/

# Screenshot

![Screenshot](https://github.com/eqlbin/gwt-listbox-datepicker/raw/master/screenshots/screenshot.png)

# Building

```bash
git clone https://github.com/eqlbin/gwt-dropdown-month-datepicker.git
cd gwt-dropdown-month-datepicker
mvn package
```
* JAR with DatePicker widget

  *gwt-dropdown-month-datepicker/datepicker/target/dropdown-month-datepicker-1.0-SNAPSHOT.jar*

* WAR with demo
 
  *gwt-dropdown-month-datepicker/example/target/dropdown-month-datepicker-example-1.0-SNAPSHOT.war*

# Usage

1. Put the JAR with the DropdownMonthDatePicker widget into your classpath

   For example, *WEB-INF/lib*

2. Inherit the DropdownMonthDatePicker module into your \*.gwt.xml 

   ```xml
   <inherits name='ru.eqlbin.gwt.datepicker.DropdownMonthDatePicker'/>
   ```
   
3. Write the code

   ```java
   // Floating date range
   int yearsCount = 10;
   DropdownMonthDatePicker datePickerFloating = new DropdownMonthDatePicker();
   datePickerFloating.setVisibleYearCount(yearsCount);

   // Fixed date range
   DropdownMonthDatePicker datePickerFixed = new DropdownMonthDatePicker();
   Date minDate = new Date(2010-1900, 0, 1);
   Date maxDate = new Date(2029-1900, 11, 31);
   datePickerFixed.setDateRange(minDate, maxDate);
   // yearsCount can also be used with a fixed date range
   datePickerFixed.setVisibleYearCount(yearsCount);
   ```

# Note

This project was created in times of GWT 2.5.

Similar functionality appeared in default DatePicker since version 2.6:

```java
DatePicker datePicker = new DatePicker();
datePicker.setYearAndMonthDropdownVisible(true);
datePicker.setYearArrowsVisible(true);
```

So, now this project is just an example of how to create your own MonthSelector
and how to implement a fixed date range.
