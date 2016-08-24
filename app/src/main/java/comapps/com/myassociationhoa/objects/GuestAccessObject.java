package comapps.com.myassociationhoa.objects;

import java.util.Date;

/**
 * Created by me on 6/17/2016.
 */
public class GuestAccessObject {

    private String guestAccessOwner;
    private Date guestAccessDate;
    private String guestAccessName = "";
    private String guestAccessType = "";
    private String guestAccessContactType = "";

    public String getGuestAccessOwner() {
        return guestAccessOwner;
    }

    public void setGuestAccessOwner(String guestAccessOwner) {
        this.guestAccessOwner = guestAccessOwner;
    }

    public Date getGuestAccessDate() {
        return guestAccessDate;
    }

    public void setGuestAccessDate(Date guestAccessDate) {
        this.guestAccessDate = guestAccessDate;
    }

    public String getGuestAccessName() {
        return guestAccessName;
    }

    public void setGuestAccessName(String guestAccessName) {
        this.guestAccessName = guestAccessName;
    }

    public String getGuestAccessType() {
        return guestAccessType;
    }

    public void setGuestAccessType(String guestAccessType) {
        this.guestAccessType = guestAccessType;
    }

    public String getGuestAccessContactType() {
        return guestAccessContactType;
    }

    public void setGuestAccessContactType(String guestAccessContactType) {
        this.guestAccessContactType = guestAccessContactType;
    }



    @Override
    public String toString() {
        return "GuestAccessObject{" +
                "guestAccessOwner='" + guestAccessOwner + '\'' +
                ", guestAccessDate=" + guestAccessDate +
                ", guestAccessName='" + guestAccessName + '\'' +
                ", guestAccessType='" + guestAccessType + '\'' +
                ", guestAccessContactType='" + guestAccessContactType + '\'' +
                '}';
    }


}


