package comapps.com.myassociationhoa.objects;

/**
 * Created by me on 6/17/2016.
 */
@SuppressWarnings("ALL")
public class GuestObject {

    private String guestOwner = "";
    private String guestOwnerMemberNumber = "";
    private String guestType = "";
    private String guestStartdate = "";
    private String guestEnddate = "";

    private String mondayAccess = "";
    private String tuesdayAccess = "";
    private String wednesdayAccess = "";
    private String thursdayAccess = "";
    private String fridayAccess = "";
    private String saturdayAccess = "";
    private String sundayAccess = "";

    private String guestDescription = "";
    private String guestName = "";
    private String ownerContactNumberType = "";
    private String ownerContactNumber = "";


    public String getGuestOwner() {
        return guestOwner;
    }

    public void setGuestOwner(String guestOwner) {
        this.guestOwner = guestOwner;
    }

    public String getGuestOwnerMemberNumber() {
        return guestOwnerMemberNumber;
    }

    public void setGuestOwnerMemberNumber(String guestOwnerMemberNumber) {
        this.guestOwnerMemberNumber = guestOwnerMemberNumber;
    }

    public String getGuestType() {
        return guestType;
    }

    public void setGuestType(String guestType) {
        this.guestType = guestType;
    }

    public String getGuestStartdate() {
        return guestStartdate;
    }

    public void setGuestStartdate(String guestStartdate) {
        this.guestStartdate = guestStartdate;
    }

    public String getGuestEnddate() {
        return guestEnddate;
    }

    public void setGuestEnddate(String guestEnddate) {
        this.guestEnddate = guestEnddate;
    }

    public String getMondayAccess() {
        return mondayAccess;
    }

    public void setMondayAccess(String mondayAccess) {
        this.mondayAccess = mondayAccess;
    }

    public String getTuesdayAccess() {
        return tuesdayAccess;
    }

    public void setTuesdayAccess(String tuesdayAccess) {
        this.tuesdayAccess = tuesdayAccess;
    }

    public String getWednesdayAccess() {
        return wednesdayAccess;
    }

    public void setWednesdayAccess(String wednesdayAccess) {
        this.wednesdayAccess = wednesdayAccess;
    }

    public String getThursdayAccess() {
        return thursdayAccess;
    }

    public void setThursdayAccess(String thursdayAccess) {
        this.thursdayAccess = thursdayAccess;
    }

    public String getFridayAccess() {
        return fridayAccess;
    }

    public void setFridayAccess(String fridayAccess) {
        this.fridayAccess = fridayAccess;
    }

    public String getSaturdayAccess() {
        return saturdayAccess;
    }

    public void setSaturdayAccess(String saturdayAccess) {
        this.saturdayAccess = saturdayAccess;
    }

    public String getSundayAccess() {
        return sundayAccess;
    }

    public void setSundayAccess(String sundayAccess) {
        this.sundayAccess = sundayAccess;
    }

    public String getGuestDescription() {
        return guestDescription;
    }

    public void setGuestDescription(String guestDescription) {
        this.guestDescription = guestDescription;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public String getOwnerContactNumberType() {
        return ownerContactNumberType;
    }

    public void setOwnerContactNumberType(String ownerContactNumberType) {
        this.ownerContactNumberType = ownerContactNumberType;
    }

    public String getOwnerContactNumber() {
        return ownerContactNumber;
    }

    public void setOwnerContactNumber(String ownerContactNumber) {
        this.ownerContactNumber = ownerContactNumber;
    }




    @Override
    public String toString() {
        return "GuestObject{" +
                "guestOwner='" + guestOwner + '\'' +
                ", guestOwnerMemberNumber='" + guestOwnerMemberNumber + '\'' +
                ", guestType='" + guestType + '\'' +
                ", guestStartdate='" + guestStartdate + '\'' +
                ", guestEnddate='" + guestEnddate + '\'' +
                ", mondayAccess='" + mondayAccess + '\'' +
                ", tuesdayAccess='" + tuesdayAccess + '\'' +
                ", wednesdayAccess='" + wednesdayAccess + '\'' +
                ", thursdayAccess='" + thursdayAccess + '\'' +
                ", fridayAccess='" + fridayAccess + '\'' +
                ", saturdayAccess='" + saturdayAccess + '\'' +
                ", sundayAccess='" + sundayAccess + '\'' +
                ", guestDescription='" + guestDescription + '\'' +
                ", guestName='" + guestName + '\'' +
                ", ownerContactNumberType='" + ownerContactNumberType + '\'' +
                ", ownerContactNumber='" + ownerContactNumber + '\'' +
                '}';

    }

    public String toStringForDelete() {
        return guestOwner + "^" +
                guestOwnerMemberNumber + "^" +
                guestType + "^" +
                guestStartdate + "^" +
                guestEnddate + "^" +
                mondayAccess + "^" +
                tuesdayAccess + "^" +
                wednesdayAccess + "^" +
                thursdayAccess + "^" +
                fridayAccess + "^" +
                saturdayAccess + "^" +
                sundayAccess + "^" +
                guestDescription + "^" +
                guestName + "^" +
                ownerContactNumberType + "^" +
                ownerContactNumber + "|";

    }
}


