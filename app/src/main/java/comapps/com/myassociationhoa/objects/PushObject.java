package comapps.com.myassociationhoa.objects;

/**
 * Created by me on 6/29/2016.
 */
@SuppressWarnings("ALL")
public class PushObject {




    private String month;
    private String hoaAndGroup;
    private String date;
    private String pushNotifacation;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getHoaAndGroup() {
        return hoaAndGroup;
    }

    public void setHoaAndGroup(String hoaAndGroup) {
        this.hoaAndGroup = hoaAndGroup;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPushNotifacation() {
        return pushNotifacation;
    }

    public void setPushNotifacation(String pushNotifacation) {
        this.pushNotifacation = pushNotifacation;
    }

    @Override
    public String toString() {
        return "PushObject{" +
                "month='" + month + '\'' +
                ", hoaAndGroup='" + hoaAndGroup + '\'' +
                ", date='" + date + '\'' +
                ", pushNotifacation='" + pushNotifacation + '\'' +
                '}';
    }


}
