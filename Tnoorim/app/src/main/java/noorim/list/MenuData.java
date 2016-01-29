package noorim.list;

import noorim.navi.OnSizeChangedListener;

/**
 * Created by 쟈 on 2015-03-20.
 */
public class MenuData implements OnSizeChangedListener {
    private String menu_name;
    private String menu_img;
    private String category;
    private int price;
    private String info;

    private boolean mIsExpanded;
    private int mCollapsedHeight;
    private int mExpandedHeight;

    public MenuData(String menu_name, String category, int price, String info,
                    String menu_img, int collapsedHeight){
        this.menu_name = menu_name;
        this.category = category;
        this.price = price;
        this.info = info;
        this.menu_img = menu_img;

        mCollapsedHeight = collapsedHeight;
        mIsExpanded = false;
        mExpandedHeight = -1;
    }

    public String getMenu_name(){
        return menu_name;
    }

    public String getCategory(){
        return category;
    }

    public int getPrice(){
        return price;
    }

    public String getInfo(){
        return info;
    }

    public String getMenu_img(){
        return menu_img;
    }

    public boolean isExpanded() {
        return mIsExpanded;
    }

    public void setExpanded(boolean isExpanded) {
        mIsExpanded = isExpanded;
    }

    public int getCollapsedHeight() {
        return mCollapsedHeight;
    }

    public void setCollapsedHeight(int collapsedHeight) {
        mCollapsedHeight = collapsedHeight;
    }

    public int getExpandedHeight() {
        return mExpandedHeight;
    }

    public void setExpandedHeight(int expandedHeight) {
        mExpandedHeight = expandedHeight;
    }

    @Override
    public void onSizeChanged(int newHeight) {
        setExpandedHeight(newHeight);
    }

}
