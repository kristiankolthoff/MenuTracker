package com.roquahacks.novus;

import android.content.Intent;
import android.os.Handler;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.mingle.widget.LoadingView;
import com.roquahacks.extractor.MenuExtractor;
import com.roquahacks.model.NMenu;
import com.roquahacks.model.NMenuItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SplashActivity extends AppCompatActivity {

    private Handler mHandler;
    private Runnable myRunnable;
    private ArrayList<NMenu> menus;
    private static final int SPLASH_TIME_OUT = 5;
    public static final String MENUS = "menus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.abc_grow_fade_in_from_bottom, R.anim.fadeout);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        // Just create simple XML layout with i.e a single ImageView or a custom layout
        setContentView(R.layout.activity_splash);
        LoadingView loadingView = (LoadingView)findViewById(R.id.loadView);
        MenuExtractor menuExtractor = null;
        try {
            menuExtractor = new MenuExtractor(this);
            menuExtractor.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("MainActivity", "hier am ende des SplashScreens");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public List<NMenu> getMenus() {
        return this.menus;
    }
}
