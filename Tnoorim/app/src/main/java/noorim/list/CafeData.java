package noorim.list;

/**
 * Created by 쟈 on 2015-03-19.
 */
public class CafeData {
    private String cafe_name;
    private String cafe_branch;
    private String time;
    private String cafe_tel;
    private String cafe_img;

    public CafeData(String cafe_name, String cafe_branch, String time, String cafe_tel, String cafe_img) {
        this.cafe_name = cafe_name;
        this.cafe_branch = cafe_branch;
        this.time = time;
        this.cafe_tel = cafe_tel;
        this.cafe_img = cafe_img;
    }

    public String getCafe_name(){
        return cafe_name;
    }

    public String getCafe_branch(){
        return cafe_branch;
    }

    public String getTime(){
        return time;
    }

    public String getCafe_tel(){
        return cafe_tel;
    }

    public String getCafe_img(){
        return cafe_img;
    }
}
