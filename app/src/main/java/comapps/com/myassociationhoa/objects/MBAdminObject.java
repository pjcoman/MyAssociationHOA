package comapps.com.myassociationhoa.objects;

/**
 * Created by me on 6/22/2016.
 */
public class MBAdminObject {

    private String adminMessageName;
    private String adminMessageDate;
    private String adminMessageText;
    private String adminMessageSort;
    private String adminMessageEmail;
    private String adminMessageComment;
    private String adminMessageType;
    private String adminMessageNameComment;
    private String adminMessageOriginalText;

    public String getAdminMessageName() {
        return adminMessageName;
    }

    public void setAdminMessageName(String adminMessageName) {
        this.adminMessageName = adminMessageName;
    }

    public String getAdminMessageDate() {
        return adminMessageDate;
    }

    public void setAdminMessageDate(String adminMessageDate) {
        this.adminMessageDate = adminMessageDate;
    }

    public String getAdminMessageText() {
        return adminMessageText;
    }

    public void setAdminMessageText(String adminMessageText) {
        this.adminMessageText = adminMessageText;
    }

    public String getAdminMessageSort() {
        return adminMessageSort;
    }

    public void setAdminMessageSort(String adminMessageSort) {
        this.adminMessageSort = adminMessageSort;
    }

    public String getAdminMessageEmail() {
        return adminMessageEmail;
    }

    public void setAdminMessageEmail(String adminMessageEmail) {
        this.adminMessageEmail = adminMessageEmail;
    }

    public String getAdminMessageComment() {
        return adminMessageComment;
    }

    public void setAdminMessageComment(String adminMessageComment) {
        this.adminMessageComment = adminMessageComment;
    }

    public String getAdminMessageType() {
        return adminMessageType;
    }

    public void setAdminMessageType(String adminMessageType) {
        this.adminMessageType = adminMessageType;
    }

    public String getAdminMessageNameComment() {
        return adminMessageNameComment;
    }

    public void setAdminMessageNameComment(String adminMessageNameComment) {
        this.adminMessageNameComment = adminMessageNameComment;
    }

    public String getAdminMessageOriginalText() {
        return adminMessageOriginalText;
    }

    public void setAdminMessageOriginalText(String adminMessageOriginalText) {
        this.adminMessageOriginalText = adminMessageOriginalText;
    }

    @Override
    public String toString() {
        return "MBAdminObject{" +
                "adminMessageName='" + adminMessageName + '\'' +
                ", adminMessageDate='" + adminMessageDate + '\'' +
                ", adminMessageText='" + adminMessageText + '\'' +
                ", adminMessageSort='" + adminMessageSort + '\'' +
                ", adminMessageEmail='" + adminMessageEmail + '\'' +
                ", adminMessageComment='" + adminMessageComment + '\'' +
                ", adminMessageType='" + adminMessageType + '\'' +
                ", adminMessageNameComment='" + adminMessageNameComment + '\'' +
                ", adminMessageOriginalText='" + adminMessageOriginalText + '\'' +
                '}';
    }






}
