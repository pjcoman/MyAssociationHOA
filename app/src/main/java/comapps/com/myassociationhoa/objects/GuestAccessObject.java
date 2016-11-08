package comapps.com.myassociationhoa.objects;

/**
 * Created by me on 6/17/2016.
 */
@SuppressWarnings("ALL")
public class GuestAccessObject {

    private String guestAccessOwner;
    private String guestAccessDate;
    private String guestAccessName = "";
    private String guestAccessType = "";
    private String guestAccessContactType = "";

    public String getGuestAccessOwner() {
        return guestAccessOwner;
    }

    public void setGuestAccessOwner(String guestAccessOwner) {
        this.guestAccessOwner = guestAccessOwner;
    }

    public String getGuestAccessDate() {
        return guestAccessDate;
    }

    public void setGuestAccessDate(String guestAccessDate) {
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
        return guestAccessOwner + " " + guestAccessDate + " " + guestAccessName + " " + guestAccessType + " " + guestAccessContactType + " ";

    }




}


