package com.roquahacks.novus;

import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.google.gson.Gson;
import com.roquahacks.extractor.MenuExtractor;
import com.roquahacks.model.NMenu;
import com.roquahacks.model.NMenuItem;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class NovusActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_novus);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        Log.i("MainActivity", "hier starten wir mit dem Novus screen");
        ArrayList<NMenu> menus = getIntent().getParcelableArrayListExtra(SplashActivity.MENUS);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), menus);
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_novus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class MenuFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String MENU_KEY = "menu";
        private NMenu menu;

        public MenuFragment() {
        }

        public void setNMenu(NMenu menu) {
            this.menu = menu;
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static MenuFragment newInstance(int sectionNumber, NMenu menu) {
            MenuFragment fragment = new MenuFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putCharSequence(MENU_KEY, new Gson().toJson(menu, NMenu.class));
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_novus, container, false);
            TextView textViewDay = (TextView) rootView.findViewById(R.id.day_label);
            this.menu = new Gson().fromJson(getArguments().getString(MENU_KEY), NMenu.class);
            /**
             * Menu item description text views
             */
            TextView textViewMenu1Desc = (TextView) rootView.findViewById(R.id.menu1_desc);
            TextView textViewMenu2Desc = (TextView) rootView.findViewById(R.id.menu2_desc);
            TextView textViewMenu3Desc = (TextView) rootView.findViewById(R.id.menu3_desc);
            textViewMenu1Desc.setText(this.menu.getItems().get(0).getDescription());
            textViewMenu2Desc.setText(this.menu.getItems().get(1).getDescription());
            textViewMenu3Desc.setText(this.menu.getItems().get(2).getDescription());
            /**
             * Menu item price text views
             */
            TextView textViewMenu1Price = (TextView) rootView.findViewById(R.id.menu1_price);
            TextView textViewMenu2Price = (TextView) rootView.findViewById(R.id.menu2_price);
            TextView textViewMenu3Price = (TextView) rootView.findViewById(R.id.menu3_price);
            textViewMenu1Price.setText(this.menu.getItems().get(0).getPrice());
            textViewMenu2Price.setText(this.menu.getItems().get(1).getPrice());
            textViewMenu3Price.setText(this.menu.getItems().get(2).getPrice());
            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1: textViewDay.setText(R.string.monday);
                    break;
                case 2: textViewDay.setText(R.string.tuesday);

                    break;
                case 3: textViewDay.setText(R.string.wednesday);

                    break;
                case 4: textViewDay.setText(R.string.thursday);

                    break;
                case 5: textViewDay.setText(R.string.friday);

                    break;
            }
            return rootView;
        }

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<NMenu> menus;

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<NMenu> menus) {
            super(fm);
            this.menus = menus;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return MenuFragment.newInstance(position + 1, this.menus.get(position));
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
                case 4:
                    return "SECTION 4";
                case 5:
                    return "SECTION 5";
            }
            return null;
        }
    }
}
