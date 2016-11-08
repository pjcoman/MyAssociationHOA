package comapps.com.myassociationhoa.objects;

/**
 * Created by me on 6/27/2016.
 */
@SuppressWarnings("ALL")
public class ServiceProviderObject {

    private String providerName;
    private String providerAddress;



    private String providerAddress2;
    private String providerCity;
    private String providerState;
    private String providerZip;
    private String providerPhoneNumber;
    private String providerNotes;


    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public String getProviderAddress() {
        return providerAddress;
    }

    public void setProviderAddress(String providerAddress) {
        this.providerAddress = providerAddress;
    }

    public String getProviderAddress2() {
        return providerAddress2;
    }

    public void setProviderAddress2(String providerAddress2) {
        this.providerAddress2 = providerAddress2;
    }

    public String getProviderCity() {
        return providerCity;
    }

    public void setProviderCity(String providerCity) {
        this.providerCity = providerCity;
    }

    public String getProviderState() {
        return providerState;
    }

    public void setProviderState(String providerState) {
        this.providerState = providerState;
    }


    public String getProviderZip() {
        return providerZip;
    }

    public void setProviderZip(String providerZip) {
        this.providerZip = providerZip;
    }

    public String getProviderPhoneNumber() {
        return providerPhoneNumber;
    }

    public void setProviderPhoneNumber(String providerPhoneNumber) {
        this.providerPhoneNumber = providerPhoneNumber;
    }

    public String getProviderNotes() {
        return providerNotes;
    }

    public void setProviderNotes(String providerNotes) {
        this.providerNotes = providerNotes;
    }


    @Override
    public String toString() {
        return "ServiceProviderObject{" +
                "providerName='" + providerName + '\'' +
                ", providerAddress='" + providerAddress + '\'' +
                ", providerAddress2='" + providerAddress2 + '\'' +
                ", providerCity='" + providerCity + '\'' +
                ", providerState='" + providerState + '\'' +
                ", providerZip='" + providerZip + '\'' +
                ", providerPhoneNumber='" + providerPhoneNumber + '\'' +
                ", providerNotes='" + providerNotes + '\'' +
                '}';
    }



}