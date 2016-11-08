package comapps.com.myassociationhoa.objects;

/**
 * Created by me on 6/27/2016.
 */
@SuppressWarnings("ALL")
public class MaintenanceObject {

    private String maintenanceName;
    private String maintenanceDesc;
    private String maintenanceDate;
    private String maintenanceNotes;
    private String maintenanceCategory;
    private String maintenanceNumber;

    public String getMaintenanceName() {
        return maintenanceName;
    }

    public void setMaintenanceName(String maintenanceName) {
        this.maintenanceName = maintenanceName;
    }

    public String getMaintenanceDesc() {
        return maintenanceDesc;
    }

    public void setMaintenanceDesc(String maintenanceDesc) {
        this.maintenanceDesc = maintenanceDesc;
    }

    public String getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(String maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public String getMaintenanceNotes() {
        return maintenanceNotes;
    }

    public void setMaintenanceNotes(String maintenanceNotes) {
        this.maintenanceNotes = maintenanceNotes;
    }

    public String getMaintenanceCategory() {
        return maintenanceCategory;
    }

    public void setMaintenanceCategory(String maintenanceCategory) {
        this.maintenanceCategory = maintenanceCategory;
    }

    public String getMaintenanceNumber() {
        return maintenanceNumber;
    }

    public void setMaintenanceNumber(String maintenanceNumber) {
        this.maintenanceNumber = maintenanceNumber;
    }

    @Override
    public String toString() {
        return "MaintenanceObject{" +
                "maintenanceName='" + maintenanceName + '\'' +
                ", maintenanceDesc='" + maintenanceDesc + '\'' +
                ", maintenanceDate='" + maintenanceDate + '\'' +
                ", maintenanceNotes='" + maintenanceNotes + '\'' +
                ", maintenanceCategory='" + maintenanceCategory + '\'' +
                ", maintenanceNumber='" + maintenanceNumber + '\'' +
                '}';
    }
}
