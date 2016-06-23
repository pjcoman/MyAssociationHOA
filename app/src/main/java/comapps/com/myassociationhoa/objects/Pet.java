package comapps.com.myassociationhoa.objects;

/**
 * Created by me on 6/17/2016.
 */
public class Pet {

    private String name = "";
    private String type = "";
    private String breed = "";
    private String color = "";
    private String weight = "";
    private String misc = "";


    //*****************************************************


    public void SetName(String name){
        this.name = name;
    }
    public String GetName(){
        return this.name;
    }


   //*****************************************************



    public void SetType(String type){
        this.type = type;
    }
    public String GetType(){
        return this.type;
    }

    //*****************************************************




    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }


    //*****************************************************




    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }


    //*****************************************************




    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }



    //*****************************************************



    public String getMisc() {
        return misc;
    }

    public void setMisc(String misc) {
        this.misc = misc;
    }


}

