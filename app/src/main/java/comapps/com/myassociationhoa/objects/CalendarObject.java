package comapps.com.myassociationhoa.objects;

/**
 * Created by me on 6/22/2016.
 */
public class CalendarObject {

    private String calendarText;
    private String calendarText2;
    private String calendarDetailText;
    private String calendarStartDate;
    private String calendarEndDate;
    private String calendarSortDate;

    @Override
    public String toString() {
        return "CalendarObject{" +
                "calendarText='" + calendarText + '\'' +
                ", calendarText2='" + calendarText2 + '\'' +
                ", calendarDetailText='" + calendarDetailText + '\'' +
                ", calendarStartDate='" + calendarStartDate + '\'' +
                ", calendarEndDate='" + calendarEndDate + '\'' +
                ", calendarSortDate='" + calendarSortDate + '\'' +
                '}';
    }


    public String toStringForDelete() {
        return calendarText + "|" +
               calendarDetailText + "|" +
               calendarStartDate + "|" +
               calendarEndDate + "|" +
               calendarSortDate + "|";

    }


    public String getCalendarText() {
        return calendarText;
    }

    public void setCalendarText(String calendarText) {
        this.calendarText = calendarText;
    }

    public String getCalendarText2() {
        return calendarText2;
    }

    public void setCalendarText2(String calendarText2) {
        this.calendarText2 = calendarText2;
    }

    public String getCalendarDetailText() {
        return calendarDetailText;
    }

    public void setCalendarDetailText(String calendarDetailText) {
        this.calendarDetailText = calendarDetailText;
    }

    public String getCalendarStartDate() {
        return calendarStartDate;
    }

    public void setCalendarStartDate(String calendarStartDate) {
        this.calendarStartDate = calendarStartDate;
    }

    public String getCalendarEndDate() {
        return calendarEndDate;
    }

    public void setCalendarEndDate(String calendarEndDate) {
        this.calendarEndDate = calendarEndDate;
    }

    public String getCalendarSortDate() {
        return calendarSortDate;
    }

    public void setCalendarSortDate(String calendarSortDate) {
        this.calendarSortDate = calendarSortDate;
    }

}
