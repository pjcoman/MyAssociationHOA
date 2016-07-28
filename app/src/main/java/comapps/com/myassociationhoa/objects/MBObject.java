package comapps.com.myassociationhoa.objects;

/**
 * Created by me on 6/22/2016.
 */
public class MBObject {

    private String mbName;                  // 0
    private String mbPostDate;             // 1
    private String mbPost;
    private String mbPostDate2;    // 2
    private String mbPosterEmailAddress;

    @Override
    public String toString() {
        return "MBObject{" +
                "mbName='" + mbName + '\'' +
                ", mbPostDate='" + mbPostDate + '\'' +
                ", mbPost='" + mbPost + '\'' +
                ", mbPostDate2='" + mbPostDate2 + '\'' +
                ", mbPosterEmailAddress='" + mbPosterEmailAddress + '\'' +
                '}';
    }

    public String getMbPostDate2() {
        return mbPostDate2;
    }

    public void setMbPostDate2(String mbPostDate2) {
        this.mbPostDate2 = mbPostDate2;
    }


    public String getMbPost() {
        return mbPost;
    }

    public void setMbPost(String mbPost) {
        this.mbPost = mbPost;
    }

    public String getMbName() {
        return mbName;
    }

    public void setMbName(String mbName) {
        this.mbName = mbName;
    }

    public String getMbPostDate() {
        return mbPostDate;
    }

    public void setMbPostDate(String mbPostDate) {
        this.mbPostDate = mbPostDate;
    }

    public String getMbPosterEmailAddress() {
        return mbPosterEmailAddress;
    }

    public void setMbPosterEmailAddress(String mbPosterEmailAddress) {
        this.mbPosterEmailAddress = mbPosterEmailAddress;
    }


}
