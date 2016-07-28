package comapps.com.myassociationhoa.objects;

/**
 * Created by me on 6/27/2016.
 */
public class ProviderObject {

    private String providerType;
    private String providerCount;
    private String providerList;




    @Override
    public String toString() {
        return "ProviderObject{" +
                "providerType='" + providerType + '\'' +
                ", providerCount='" + providerCount + '\'' +
                ", providerList='" + providerList + '\'' +
                ", providerAddress='" + '}';
    }


    public String getProviderType() {
        return providerType;
    }

    public void setProviderType(String providerType) {
        this.providerType = providerType;
    }


    public String getProviderCount() {
        return providerCount;
    }

    public void setProviderCount(String providerCount) {
        this.providerCount = providerCount;
    }



    public String getProviderList() {
        return providerList;
    }

    public void setProviderList(String providerList) {
        this.providerList = providerList;
    }


}