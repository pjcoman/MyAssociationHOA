package comapps.com.myassociationhoa.objects;

/**
 * Created by me on 6/17/2016.
 */
@SuppressWarnings("ALL")
public class MemberGroupObject {

    private String memberGroupObject_MemberNumber = "";
    private String memberGroupObject_Group = "";


    public String getMemberGroupObject_MemberNumber() {
        return memberGroupObject_MemberNumber;
    }

    public void setMemberGroupObject_MemberNumber(String memberGroupObject_MemberNumber) {
        this.memberGroupObject_MemberNumber = memberGroupObject_MemberNumber;
    }

    public String getMemberGroupObject_Group() {
        return memberGroupObject_Group;
    }

    public void setMemberGroupObject_Group(String memberGroupObject_Group) {
        this.memberGroupObject_Group = memberGroupObject_Group;
    }

    @Override
    public String toString() {
        return "MemberGroupObject{" +
                "memberGroupObject_MemberNumber='" + memberGroupObject_MemberNumber + '\'' +
                ", memberGroupObject_Group='" + memberGroupObject_Group + '\'' +
                '}';
    }













}