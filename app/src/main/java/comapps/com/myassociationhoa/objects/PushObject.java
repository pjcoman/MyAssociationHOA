package comapps.com.myassociationhoa.objects;

/**
 * Created by me on 6/29/2016.
 */
public class PushObject {


    private String date;
    private String pushNotifacation;

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
                "date='" + date + '\'' +
                ", pushNotifacation='" + pushNotifacation + '\'' +
                '}';
    }


}
