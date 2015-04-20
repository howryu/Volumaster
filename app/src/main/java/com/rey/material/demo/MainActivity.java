package com.rey.material.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.rey.material.app.ToolbarManager;
import com.rey.material.util.ThemeUtil;
import com.rey.material.widget.SnackBar;
import com.rey.material.widget.TabPageIndicator;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements ToolbarManager.OnToolbarGroupChangedListener {

    private DrawerLayout dl_navigator;
    private FrameLayout fl_drawer;
    private ListView lv_drawer;
    private static CustomViewPager vp;
    private TabPageIndicator tpi;

    private DrawerAdapter mDrawerAdapter;
    private PagerAdapter mPagerAdapter;

    private Toolbar mToolbar;
    private ToolbarManager mToolbarManager;
    private SnackBar mSnackBar;

    //    private Tab[] mItems = new Tab[]{Tab.PROGRESS, Tab.BUTTONS, Tab.FAB, Tab.SWITCHES, Tab.SLIDERS,
//            Tab.SPINNERS, Tab.TEXTFIELDS, Tab.SNACKBARS, Tab.DIALOGS, Tab.SETRULE, Tab.SETTING,
//            Tab.CALENDARVIEW, Tab.LISTVIEW};
    private Tab[] mItems = new Tab[]{
            Tab.CALENDARVIEW, Tab.LISTVIEW,Tab.SETRULE, Tab.SETTING};

    public static MyDB myDB;

    public final static String EVENTSLIST = "com.rey.material.demo.EVENTSLIST";
    public final static String CHECKED = "com.rey.material.demo.CHECKED";
    public final static int SYNC_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        myDB = MyDB.getInstance(this);
        try {
            myDB.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //AudioManager am =  (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //Log.d("Volume", "Max Vol" + new Integer(am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)).toString());

        dl_navigator = (DrawerLayout)findViewById(R.id.main_dl);
        fl_drawer = (FrameLayout)findViewById(R.id.main_fl_drawer);
        lv_drawer = (ListView)findViewById(R.id.main_lv_drawer);
        mToolbar = (Toolbar)findViewById(R.id.main_toolbar);
        vp = (CustomViewPager)findViewById(R.id.main_vp);
        tpi = (TabPageIndicator)findViewById(R.id.main_tpi);
        mSnackBar = (SnackBar)findViewById(R.id.main_sn);

        mToolbarManager = new ToolbarManager(this, mToolbar, 0, R.style.ToolbarRippleStyle, R.anim.abc_fade_in, R.anim.abc_fade_out);
        mToolbarManager.setNavigationManager(new ToolbarManager.BaseNavigationManager(R.style.NavigationDrawerDrawable, this, mToolbar, dl_navigator) {
            @Override
            public void onNavigationClick() {
                if(mToolbarManager.getCurrentGroup() != 0)
                    mToolbarManager.setCurrentGroup(0);
                else
                    dl_navigator.openDrawer(Gravity.START);
            }

            @Override
            public boolean isBackState() {
                return super.isBackState() || mToolbarManager.getCurrentGroup() != 0;
            }

            @Override
            protected boolean shouldSyncDrawerSlidingProgress() {
                return super.shouldSyncDrawerSlidingProgress() && mToolbarManager.getCurrentGroup() == 0;
            }

        });
        mToolbarManager.registerOnToolbarGroupChangedListener(this);

        mDrawerAdapter = new DrawerAdapter();
        lv_drawer.setAdapter(mDrawerAdapter);

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), mItems);
        vp.setAdapter(mPagerAdapter);
        tpi.setViewPager(vp);
        tpi.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mDrawerAdapter.setSelected(mItems[position]);
                mSnackBar.dismiss();
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {}

            @Override
            public void onPageScrollStateChanged(int state) {}

        });

//        mDrawerAdapter.setSelected(Tab.PROGRESS);
        mDrawerAdapter.setSelected(Tab.SETRULE);
        vp.setCurrentItem(0);

        getChecked();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mToolbarManager.createMenu(R.menu.menu_main);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mToolbarManager.onPrepareMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.tb_contextual:
                mToolbarManager.setCurrentGroup(R.id.tb_group_contextual);
                break;
            case R.id.tb_done:
            case R.id.tb_done_all:
                mToolbarManager.setCurrentGroup(0);
                break;
            case R.id.action_today:
                return false;
            case R.id.action_day_view:
                return false;
            case R.id.action_three_day_view:
                return false;
            case R.id.action_week_view:
                return false;
        }
        return true;
    }

    @Override
    public void onToolbarGroupChanged(int oldGroupId, int groupId) {
        mToolbarManager.notifyNavigationStateChanged();
    }

    public SnackBar getSnackBar(){
        return mSnackBar;
    }

    public enum Tab {
        //	    PROGRESS ("Progresses"),
//	    BUTTONS ("Buttons"),
//        FAB ("FABs"),
//	    SWITCHES ("Switches"),
//        SLIDERS ("Sliders"),
//        SPINNERS ("Spinners"),
//	    TEXTFIELDS ("TextFields"),
//	    SNACKBARS ("SnackBars"),
//        DIALOGS ("Dialogs"),
        SETRULE("SetRule"),
        SETTING("Setting"),
        CALENDARVIEW("CalendarView"),
        LISTVIEW("ListView");
        private final String name;

        private Tab(String s) {
            name = s;
        }

        public boolean equalsName(String otherName){
            return (otherName != null) && name.equals(otherName);
        }

        public String toString(){
            return name;
        }


    }

    class DrawerAdapter extends BaseAdapter implements View.OnClickListener {

        private Tab mSelectedTab;

        public void setSelected(Tab tab){
            if(tab != mSelectedTab){
                mSelectedTab = tab;
                notifyDataSetInvalidated();
            }
        }

        public Tab getSelectedTab(){
            return mSelectedTab;
        }

        @Override
        public int getCount() {
            return mItems.length;
        }

        @Override
        public Object getItem(int position) {
            return mItems[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if(v == null) {
                v = LayoutInflater.from(MainActivity.this).inflate(R.layout.row_drawer, null);
                v.setOnClickListener(this);
            }

            v.setTag(position);
            Tab tab = (Tab)getItem(position);
            ((TextView)v).setText(tab.toString());

            if(tab == mSelectedTab) {
                v.setBackgroundColor(ThemeUtil.colorPrimary(MainActivity.this, 0));
                ((TextView)v).setTextColor(0xFFFFFFFF);
            }
            else {
                v.setBackgroundResource(0);
                ((TextView)v).setTextColor(0xFF000000);
            }

            return v;
        }

        @Override
        public void onClick(View v) {
            int position = (Integer)v.getTag();
            vp.setCurrentItem(position);
            dl_navigator.closeDrawer(fl_drawer);
        }
    }

    private static class PagerAdapter extends FragmentStatePagerAdapter {

        Fragment[] mFragments;
        Tab[] mTabs;

        private static final Field sActiveField;

        static {
            Field f = null;
            try {
                Class<?> c = Class.forName("android.support.v4.app.FragmentManagerImpl");
                f = c.getDeclaredField("mActive");
                f.setAccessible(true);
            } catch (Exception e) {}

            sActiveField = f;
        }

        public PagerAdapter(FragmentManager fm, Tab[] tabs) {
            super(fm);
            mTabs = tabs;
            mFragments = new Fragment[mTabs.length];


            //dirty way to get reference of cached fragment
            try{
                ArrayList<Fragment> mActive = (ArrayList<Fragment>)sActiveField.get(fm);
                if(mActive != null){
                    for(Fragment fragment : mActive){
//    					if(fragment instanceof ProgressFragment)
//    						setFragment(Tab.PROGRESS, fragment);
//    					else if(fragment instanceof ButtonFragment)
//    						setFragment(Tab.BUTTONS, fragment);
//                        else if(fragment instanceof FabFragment)
//                            setFragment(Tab.FAB, fragment);
//    					else if(fragment instanceof SwitchesFragment)
//    						setFragment(Tab.SWITCHES, fragment);
//                        else if(fragment instanceof SliderFragment)
//                            setFragment(Tab.SLIDERS, fragment);
//                        else if(fragment instanceof SpinnersFragment)
//                            setFragment(Tab.SPINNERS, fragment);
//    					else if(fragment instanceof TextfieldFragment)
//    						setFragment(Tab.TEXTFIELDS, fragment);
//    					else if(fragment instanceof SnackbarFragment)
//    						setFragment(Tab.SNACKBARS, fragment);
//                        else if(fragment instanceof DialogsFragment)
//                            setFragment(Tab.DIALOGS, fragment);
//                        else if(fragment instanceof SetRuleFragment)
                        if(fragment instanceof SetRuleFragment)
                            setFragment(Tab.SETRULE, fragment);
                        else if(fragment instanceof SettingFragment)
                            setFragment(Tab.SETTING, fragment);
                        else if(fragment instanceof CalendarViewFragment)
                            setFragment(Tab.CALENDARVIEW, fragment);
                        else if(fragment instanceof ListViewFragment)
                            setFragment(Tab.LISTVIEW, fragment);
                    }
                }
            }
            catch(Exception e){}
        }

        private void setFragment(Tab tab, Fragment f){
            for(int i = 0; i < mTabs.length; i++)
                if(mTabs[i] == tab){
                    mFragments[i] = f;
                    break;
                }
        }

        @Override
        public Fragment getItem(int position) {
            if(mFragments[position] == null){
                switch (mTabs[position]) {
//					case PROGRESS:
//						mFragments[position] = ProgressFragment.newInstance();
//						break;
//					case BUTTONS:
//						mFragments[position] = ButtonFragment.newInstance();
//						break;
//                    case FAB:
//                        mFragments[position] = FabFragment.newInstance();
//                        break;
//					case SWITCHES:
//						mFragments[position] = SwitchesFragment.newInstance();
//						break;
//                    case SLIDERS:
//                        mFragments[position] = SliderFragment.newInstance();
//                        break;
//                    case SPINNERS:
//                        mFragments[position] = SpinnersFragment.newInstance();
//                        break;
//					case TEXTFIELDS:
//						mFragments[position] = TextfieldFragment.newInstance();
//						break;
//					case SNACKBARS:
//						mFragments[position] = SnackbarFragment.newInstance();
//						break;
//                    case DIALOGS:
//                        mFragments[position] = DialogsFragment.newInstance();
//                        break;
                    case SETRULE:
                        mFragments[position] = SetRuleFragment.newInstance();
                        ((SetRuleFragment) mFragments[position]).setViewPager(vp);
                        break;
                    case SETTING:
                        mFragments[position] = SettingFragment.newInstance();
                        break;
                    case CALENDARVIEW:
                        mFragments[position] = CalendarViewFragment.newInstance();
                        ((CalendarViewFragment) mFragments[position]).setViewPager(vp);
                        ((CalendarViewFragment) mFragments[position]).setSRFragment(SetRuleFragment.newInstance());
                        break;
                    case LISTVIEW:
                        mFragments[position] = ListViewFragment.newInstance();
                        ((ListViewFragment) mFragments[position]).setViewPager(vp);
                        ((ListViewFragment) mFragments[position]).setSRFragment(SetRuleFragment.newInstance());
                        break;
                }
            }

            return mFragments[position];
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabs[position].toString().toUpperCase();
        }

        @Override
        public int getCount() {
            return mFragments.length;
        }

    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Calendar", "activityresult" + "request " + requestCode + "result " + resultCode);
        if (requestCode == SYNC_CODE && resultCode == RESULT_OK) {
            Log.d("Calendar", "sync and ok");
            ArrayList<String> eventsToBeInserted = data.getStringArrayListExtra(EVENTSLIST);
            Log.d("check size", "size = " + eventsToBeInserted.size());
            for (int i=0; i<eventsToBeInserted.size(); i++) {
                String eventInfo = eventsToBeInserted.get(i);
                String[] info = eventInfo.split("#");
                String summary = info[0];
                String date = info[1].substring(0, 10);
                String start = info[1].substring(11,16);
                String end = info[2].substring(11,16);
                Rule r = new Rule(summary, date, start, end, "0");
                myDB.insert(r);
            }

        }
    }*/

    private void getChecked() {
        Intent intent = getIntent();
        ArrayList<String> eventsToBeInserted = intent.getStringArrayListExtra(CHECKED);
        if (eventsToBeInserted == null){
            return;
        }
        Log.d("check size", "size = " + eventsToBeInserted.size());
        for (int i = 0; i < eventsToBeInserted.size(); i++) {
            String eventInfo = eventsToBeInserted.get(i);
            String[] info = eventInfo.split("#");
            String summary = info[0];
            String date = info[1].substring(0, 10);
            String start = info[1].substring(11, 16);
            String end = info[2].substring(11, 16);
            Rule r = new Rule(summary, date, start, end, "0");
            myDB.insert(r);
        }
    }
}