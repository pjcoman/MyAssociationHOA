package comapps.com.myassociationhoa.objects;

/**
 * Created by me on 6/22/2016.
 */
public class AdminMBObject {

    private String postName;
    private String postDate;
    private String postText;
    private String postSort;
    private String postEmail;
    private String postComment;
    private String postNameComment;
    private String postOriginalText;




    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }





    public String getPostSort() {
        return postSort;
    }

    public void setPostSort(String postSort) {
        this.postSort = postSort;
    }

    public String getPostEmail() {
        return postEmail;
    }

    public void setPostEmail(String postEmail) {
        this.postEmail = postEmail;
    }

    public String getPostComment() {
        return postComment;
    }

    public void setPostComment(String postComment) {
        this.postComment = postComment;
    }

    public String getPostNameComment() {
        return postNameComment;
    }

    public void setPostNameComment(String postNameComment) {
        this.postNameComment = postNameComment;
    }

    public String getPostOriginalText() {
        return postOriginalText;
    }

    public void setPostOriginalText(String postOriginalText) {
        this.postOriginalText = postOriginalText;
    }



    @Override
    public String toString() {
        return "AdminMBObject{" +
                "postName='" + postName + '\'' +
                ", postDate='" + postDate + '\'' +
                ", postText='" + postText + '\'' +
                ", postSort='" + postSort + '\'' +
                ", postEmail='" + postEmail + '\'' +
                ", postComment='" + postComment + '\'' +
                ", postNameComment='" + postNameComment + '\'' +
                ", postOriginalText='" + postOriginalText + '\'' +
                '}';
    }

    public String toBeDeleted() {
        return postName + "|" +
               postDate + "|" +
               postText + "|" +
               postSort + "|" +
               postEmail + "|" +
               postComment + "|" +
               postNameComment + "|" +
               postOriginalText;
    }



}
