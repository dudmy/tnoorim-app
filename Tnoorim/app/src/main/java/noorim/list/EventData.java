package noorim.list;

import noorim.navi.OnSizeChangedListener;

/**
 * Created by YuJin on 2015-04-01.
 */
public class EventData implements OnSizeChangedListener {
    private String event_name;
    private String cafe_name;
    private String event_info;
    private String startday;
    private String endday;
    private boolean is_use;

    private boolean mIsExpanded;
    private int mCollapsedHeight;
    private int mExpandedHeight;

    public EventData(String event_name, String cafe_name, String event_info, String startday, String endday,
                     boolean is_use, int collapsedHeight){
        this.event_name = event_name;
        this.cafe_name = cafe_name;
        this.event_info = event_info;
        this.startday = startday;
        this.endday = endday;
        this.is_use = is_use;

        mCollapsedHeight = collapsedHeight;
        mIsExpanded = false;
        mExpandedHeight = -1;
    }

    public String getEvent_name(){
        return event_name;
    }

    public String getCafe_name(){
        return cafe_name;
    }

    public String getEvent_info(){
        return event_info;
    }

    public String getStartday(){
        return startday;
    }

    public String getEndday(){
        return endday;
    }

    public Boolean getIs_use() { return is_use; }

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
