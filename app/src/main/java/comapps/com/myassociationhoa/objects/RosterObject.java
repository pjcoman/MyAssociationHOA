package comapps.com.myassociationhoa.objects;

/**
 * Created by me on 6/22/2016.
 */
public class RosterObject {

    private String number;                  // 0
    private String lastName;                // 1
    private String firstName;               // 2
    private String middleName;              // 3
    private String homeAddress1;            // 4
    private String homeAddress2;            // 5
    private String homeCity;                // 6
    private String homeState;               // 7
    private String HomeZip;                 // 8
    private String homePhone;               // 9
    private String mobilePhone;             // 10
    private String email;                   // 11
    private String businessName;            // 12
    private String winterAddress1;        // 13
    private String winterAddress2;        // 14
    private String winterCity;            // 15
    private String winterState;           // 16
    private String winterZip;             // 17
    private String winterPhone;          // 18
    private String winterEmail;           // 19
    private String memberNumber;      // 20
    private String status;                    // 21
    private String emergencyName;           //  22
    private String emergencyPhoneNumber;    // 23
    private String activationDate;          // 24
    private String groups;                  //  25



    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    //******************************************************************************

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;

    }

    //******************************************************************************

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    //******************************************************************************

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    //******************************************************************************

    public String getHomeAddress1() {
        return homeAddress1;
    }

    public void setHomeAddress1(String homeAddress1) {
        this.homeAddress1 = homeAddress1;
    }


    //******************************************************************************
    public String getHomeAddress2() {
        return homeAddress2;
    }

    public void setHomeAddress2(String homeAddress2) {
        this.homeAddress2 = homeAddress2;
    }

    //******************************************************************************

    public String getHomeCity() {
        return homeCity;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    //******************************************************************************

    public String getHomeState() {
        return homeState;
    }

    public void setHomeState(String homeState) {
        this.homeState = homeState;
    }

    //******************************************************************************

    public String getHomeZip() {
        return HomeZip;
    }

    public void setHomeZip(String homeZip) {
        HomeZip = homeZip;
    }

    //******************************************************************************
    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    //******************************************************************************

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    //******************************************************************************

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //******************************************************************************

    public String getWinterName() {
        return businessName;
    }

    public void setWinterName(String businessName) {
        this.businessName = businessName;
    }

    //******************************************************************************

    public String getWinterAddress2() {
        return winterAddress2;
    }

    public void setWinterAddress2(String winterAddress2) {
        this.winterAddress2 = winterAddress2;
    }

    //******************************************************************************

    public String getWinterAddress1() {
        return winterAddress1;
    }

    public void setWinterAddress1(String winterAddress1) {
        this.winterAddress1 = winterAddress1;
    }

    //********************************************************************************
    public String getWinterCity() {
        return winterCity;
    }

    public void setWinterCity(String winterCity) {
        this.winterCity = winterCity;
    }

    //******************************************************************************

    public String getWinterState() {
        return winterState;
    }

    public void setWinterState(String winterState) {
        this.winterState = winterState;
    }

    //******************************************************************************

    public String getWinterZip() {
        return winterZip;
    }

    public void setWinterZip(String winterZip) {
        this.winterZip = winterZip;
    }

    //******************************************************************************

    public String getWinterPhone() {
        return winterPhone;
    }

    public void setWinterPhone(String winterPhone) {
        this.winterPhone = winterPhone;
    }

    //******************************************************************************

    public String getWinterEmail() {
        return winterEmail;
    }

    public void setWinterEmail(String winterEmail) {
        this.winterEmail = winterEmail;
    }

    //******************************************************************************

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    //******************************************************************************

    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    //******************************************************************************

    public String getEmergencyName() {
        return emergencyName;
    }

    public void setEmergencyName(String emergencyName) {
        this.emergencyName = emergencyName;
    }

    //******************************************************************************

    public String getEmergencyPhoneNumber() {
        return emergencyPhoneNumber;
    }

    public void setEmergencyPhoneNumber(String emergencyPhoneNumber) {
        this.emergencyPhoneNumber = emergencyPhoneNumber;
    }

    //******************************************************************************

    public String getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(String activationDate) {
        this.activationDate = activationDate;
    }

    //******************************************************************************

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return "RosterObject{" +
                "number='" + number + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", homeAddress1='" + homeAddress1 + '\'' +
                ", homeAddress2='" + homeAddress2 + '\'' +
                ", homeCity='" + homeCity + '\'' +
                ", homeState='" + homeState + '\'' +
                ", HomeZip='" + HomeZip + '\'' +
                ", homePhone='" + homePhone + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", email='" + email + '\'' +
                ", businessName='" + businessName + '\'' +
                ", winterAddress1='" + winterAddress1 + '\'' +
                ", winterAddress2='" + winterAddress2 + '\'' +
                ", winterCity='" + winterCity + '\'' +
                ", winterState='" + winterState + '\'' +
                ", winterZip='" + winterZip + '\'' +
                ", winterPhone='" + winterPhone + '\'' +
                ", winterEmail='" + winterEmail + '\'' +
                ", memberNumber='" + memberNumber + '\'' +
                ", status='" + status + '\'' +
                ", emergencyName='" + emergencyName + '\'' +
                ", emergencyPhoneNumber='" + emergencyPhoneNumber + '\'' +
                ", activationDate='" + activationDate + '\'' +
                ", groups='" + groups + '\'' +
                '}';
    }
}
