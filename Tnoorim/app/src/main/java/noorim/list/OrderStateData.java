package noorim.list;

/**
 * Created by YuJin on 2015-04-01.
 */
public class OrderStateData {
    private String cafe_name;
    private String cafe_branch;
    private String menu_name;
    private String date;
    private String buy_option;

    // item: 0, section: 1
    private int type;

    public OrderStateData (String cafe_name, String cafe_branch, String menu_name, String date, String buy_option, int type){
        this.cafe_name = cafe_name;
        this.cafe_branch = cafe_branch;
        this.menu_name = menu_name;
        this.date = date;
        this.buy_option = buy_option;
        this.type = type;
    }

    public String getCafe_name(){
        return cafe_name;
    }

    public String getCafe_branch(){
        return cafe_branch;
    }

    public String getMenu_name(){
        return menu_name;
    }

    public String getDate(){
        return date;
    }

    public String getBuy_option(){
        return buy_option;
    }

    public int getType() { return type; }

}
