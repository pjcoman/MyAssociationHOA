package comapps.com.myassociationhoa.objects;

/**
 * Created by me on 6/27/2016.
 */
@SuppressWarnings("ALL")
public class MaintenanceCategoryObject {

    private String maintenanceCatName;
    private String maintenanceCatEmail;

    public String getMaintenanceCatName() {
        return maintenanceCatName;
    }

    public void setMaintenanceCatName(String maintenanceCatName) {
        this.maintenanceCatName = maintenanceCatName;
    }

    public String getMaintenanceCatEmail() {
        return maintenanceCatEmail;
    }

    public void setMaintenanceCatEmail(String maintenanceCatEmail) {
        this.maintenanceCatEmail = maintenanceCatEmail;
    }



    @Override
    public String toString() {
        return "MaintenanceCategoryObject{" +
                "maintenanceCatName='" + maintenanceCatName + '\'' +
                ", maintenanceCatEmail='" + maintenanceCatEmail + '\'' +
                '}';
    }



}
