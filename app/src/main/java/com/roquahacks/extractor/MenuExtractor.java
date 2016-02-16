package com.roquahacks.extractor;


import android.content.Intent;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.util.Log;

import com.roquahacks.model.NMenu;
import com.roquahacks.model.NMenuItem;
import com.roquahacks.novus.NovusActivity;
import com.roquahacks.novus.SplashActivity;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;


public class MenuExtractor extends AsyncTask<String, Void, ArrayList<NMenu>>{

    private PDFTextStripperByArea stripper;
    private PDDocument doc;
    private PDPage page;
    private final List<Integer> itemCoors;
    private final List<Integer> menuCoors;
    private SplashActivity activity;

    private static final int WIDTH = 170;
    private static final int HEIGHT = 90;
    private static final String NOVUS_URL = "http://www.novus-mannheim.de/Wochenkarte.pdf";

    public MenuExtractor(SplashActivity activity) throws IOException {
        this.stripper = new PDFTextStripperByArea();
        this.itemCoors = Arrays.asList(70, 240, 420);
        this.menuCoors = Arrays.asList(110, 220, 320, 420, 525);
        this.activity = activity;
    }

    public void addMenuRegions() throws IOException {
        for(int day = 0; day < this.menuCoors.size(); day++) {
            for(int item = 0; item < this.itemCoors.size(); item++) {
                RectF rect;
                if(day == this.menuCoors.size()-1) {
                    rect = new RectF(this.itemCoors.get(item),
                            this.menuCoors.get(day), this.itemCoors.get(item) + WIDTH,
                            this.menuCoors.get(day) + HEIGHT-10);
                } else {
                    rect = new RectF(this.itemCoors.get(item),
                            this.menuCoors.get(day), this.itemCoors.get(item) + WIDTH,
                            this.menuCoors.get(day) + HEIGHT);
                }
                this.stripper.addRegion("day-" + day + "-item-" + item, rect);
            }
        }
    }

    public ArrayList<NMenu> extractMenus(File file) throws IOException {
        this.addMenuRegions();
        this.addPriceRegions();
        this.doc = PDDocument.load(file);
        this.page = this.doc.getPage(0);
        Log.i("MainActivity", "Before");
        this.stripper.extractRegions(this.page);
        Log.i("MainActivity", "After");
        ArrayList<NMenu> menus = new ArrayList<>();
        this.doc = PDDocument.load(file);
        this.page = this.doc.getPage(0);
        String[] prices = this.extractMenuPrices();
        this.stripper.setSortByPosition(true);
        for(int day = 0; day < this.menuCoors.size(); day++) {
            List<NMenuItem> menuItems = new ArrayList<>();
            for(int item = 0; item < this.itemCoors.size(); item++) {
                String desc = this.stripper.getTextForRegion("day-" + day + "-item-" + item);
                menuItems.add(new NMenuItem(desc, prices[item], true));
            }
            menus.add(new NMenu(menuItems, null));
        }
        return menus;
    }

    public void addPriceRegions() throws IOException {
        for(int i = 0; i < this.itemCoors.size(); i++) {
            int x = this.itemCoors.get(i);
            RectF rect = new RectF(x, 200, x + 170, 220);
            this.stripper.addRegion("p" + x, rect);
        }
    }

    public String[] extractMenuPrices() throws IOException {
        String[] prices = new String[3];
        for(int i = 0; i < this.itemCoors.size(); i++) {
            int x = this.itemCoors.get(i);
            String price = this.stripper.getTextForRegion("p" + x);
            prices[i] = price.trim();
            Log.i("MainActivity", "price: " + price);
        }
        return prices;
    }


    @Override
    protected ArrayList<NMenu> doInBackground(String... strings) {
        try {
            URL url = new URL(NOVUS_URL);
            File file = File.createTempFile("test", "pdf");
            FileUtils.copyURLToFile(url, file);
            return this.extractMenus(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    protected void onPostExecute(ArrayList<NMenu> nMenus) {
        Intent intent = new Intent(this.activity, NovusActivity.class);
        intent.putParcelableArrayListExtra(SplashActivity.MENUS, nMenus);
        this.activity.startActivity(intent);
        this.activity.finish();
    }
}
