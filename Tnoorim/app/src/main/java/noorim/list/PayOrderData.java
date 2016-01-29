package noorim.list;

/**
 * Created by 쟈 on 2015-04-28.
 */
public class PayOrderData {

    private String phone;
    private String cafe_name;
    private String cafe_branch;
    private String menu_name;
    private int price;
    private String size;
    private int is_pay;

    public PayOrderData(String phone, String cafe_name, String cafe_branch, String menu_name, int price, String size, int is_pay){
        this.phone = phone;
        this.cafe_name = cafe_name;
        this.cafe_branch = cafe_branch;
        this.menu_name = menu_name;
        this.price = price;
        this.size = size;
        this.is_pay = is_pay;
    }

    public String getPhone() { return phone; }

    public String getCafe_name() { return cafe_name; }

    public String getCafe_branch() { return cafe_branch; }

    public String getMenu_name(){
        return menu_name;
    }

    public int getPrice() { return price; }

    public String getSize() { return size; }

    public int getIs_pay() { return is_pay; }
}