package comapps.com.myassociationhoa.objects;

/**
 * Created by me on 6/17/2016.
 */
public class AutoObject {


    private String owner = "";
    private String memberNumber = "";
    private String make = "";
    private String model = "";
    private String color = "";
    private String year = "";
    private String plate = "";
    private String tag = "";


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    @Override
    public String toString() {
        return "AutoObject{" +
                "owner='" + owner + '\'' +
                ", memberNumber='" + memberNumber + '\'' +
                ", make='" + make + '\'' +
                ", model='" + model + '\'' +
                ", color='" + color + '\'' +
                ", year='" + year + '\'' +
                ", plate='" + plate + '\'' +
                ", tag='" + tag + '}';
    }
}