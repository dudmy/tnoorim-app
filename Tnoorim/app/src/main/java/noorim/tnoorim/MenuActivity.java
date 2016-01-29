package noorim.tnoorim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.rey.material.widget.SnackBar;

import java.util.ArrayList;

import noorim.server.SocketThread;
import noorim.list.PayOrderData;
import noorim.navi.CouponActivity;
import noorim.navi.EventActivity;
import noorim.navi.OptionActivity;
import noorim.navi.OrderStateActivity;

/**
 * 메뉴 액팁ㅣ
 */
public class MenuActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static ActionBarActivity MENU;
    public static Fragment[] MENUFRAG = new Fragment[5];

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private PagerSlidingTabStrip tabs;
    private ViewPager pager;
    private MyPagerAdapter adapter;
    private int currentColor = 0xFFC74B46;

    private static SnackBar mSnackBar;

    public static ArrayList<PayOrderData> cart = new ArrayList<PayOrderData>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        MENU = MenuActivity.this;

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getSupportFragmentManager());

        pager.setOffscreenPageLimit(4);
        pager.setAdapter(adapter);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        tabs.setViewPager(pager);
        tabs.setIndicatorColor(currentColor);

        cart.clear();
        mSnackBar = (SnackBar)findViewById(R.id.main_sn);
        mSnackBar.actionClickListener(new SnackBar.OnActionClickListener(){
            @Override
            public void onActionClick(SnackBar snackBar, int a) {
                for(int i=0; i<MenuActivity.cart.size(); i++){
                    PayOrderData tmp = MenuActivity.cart.get(i);
                    SocketThread.socketIO.emit("add_cart", tmp.getPhone(), tmp.getCafe_name(), tmp.getCafe_branch(), tmp.getMenu_name(),
                            tmp.getPrice(), tmp.getSize(), tmp.getIs_pay());
                }

                Intent intent = new Intent(getApplicationContext(), PayOptionActivity.class);
                intent.putExtra("beacon_event", "false");
                startActivity(intent);
            }
        });

    }

    public static SnackBar getSnackBar(){
        return mSnackBar;
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"ALL", "COFFEE", "ICE COFFEE", "NON-COFFEE", "OTHER"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            return MenuFragment.newInstance(position);
        }

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        Intent intent;

        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                intent = new Intent(getApplicationContext(), OrderStateActivity.class);
                startActivity(intent);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                intent = new Intent(getApplicationContext(), EventActivity.class);
                startActivity(intent);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                intent = new Intent(getApplicationContext(), CouponActivity.class);
                startActivity(intent);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                intent = new Intent (getApplicationContext(), OptionActivity.class);
                startActivity(intent);
                break;
            default:
                mTitle = getString(R.string.app_name);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_menu, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MenuActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}








