package comapps.com.myassociationhoa.objects;

/**
 * Created by me on 6/17/2016.
 */
@SuppressWarnings("ALL")
public class PetObject {


    private String owner = "";
    private String memberNumber = "";
    private String name = "";
    private String type = "";
    private String breed = "";
    private String color = "";
    private String weight = "";
    private String misc = "";




    @Override
    public String toString() {
        return "PetObject{" +
                "owner='" + owner + '\'' +
                ", memberNumber='" + memberNumber + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", breed='" + breed + '\'' +
                ", color='" + color + '\'' +
                ", weight='" + weight + '\'' +
                ", misc='" + misc + '\'' +
                '}';
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getMisc() {
        return misc;
    }

    public void setMisc(String misc) {
        this.misc = misc;
    }


}

