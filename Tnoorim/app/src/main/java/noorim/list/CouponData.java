package noorim.list;

/**
 * Created by YuJin on 2015-04-09.
 */
public class CouponData {
    private String cafe_name;
    private int num;

    public CouponData(String cafe_name, int num) {
        this.cafe_name = cafe_name;
        this.num = num;
    }

    public String getCafe_name() {
        return cafe_name;
    }

    public int getNum() {
        return num;
    }
}
