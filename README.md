DropdownMonthDatePicker for GWT
========================

Date picker component which allows to select the current month 
of the calendar, using the drop-down list for setting the month and year.
In addition to this, it supports a fixed date range.

# Demo

http://eqlbin.github.io/gwt-dropdown-month-datepicker/demo/

# Building

```bash
git clone https://github.com/eqlbin/gwt-dropdown-month-datepicker.git
cd gwt-dropdown-month-datepicker
mvn package
```
## JAR with DatePicker widget

* gwt-dropdown-month-datepicker/datepicker/target/dropdown-month-datepicker-1.0-SNAPSHOT.jar

## WAR with demo
 
* gwt-dropdown-month-datepicker/example/target/dropdown-month-datepicker-example-1.0-SNAPSHOT.war


# Usage

```xml
<inherits name='ru.eqlbin.gwt.datepicker.DropdownMonthDatePicker'/>
```

```java
// Floating range of years
int yearsCount = 10;
DropdownMonthDatePicker datePickerFloating = new DropdownMonthDatePicker();
datePickerFloating.setVisibleYearCount(yearsCount);

// Fixed range of years
DropdownMonthDatePicker datePickerFixed = new DropdownMonthDatePicker();
Date minDate = new Date(2010-1900, 0, 1);
Date maxDate = new Date(2029-1900, 11, 31);
int yearsCount = 10;
datePickerFixed.setDateRange(minDate, maxDate);
datePickerFixed.setVisibleYearCount(yearsCount);
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

So, now this project is just an example of how to create your own MonthSelector
and how to implement a fixed date range.
