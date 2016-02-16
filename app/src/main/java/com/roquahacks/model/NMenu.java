package com.roquahacks.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NMenu implements Parcelable{

    private List<NMenuItem> items;
    private Date date;

    public NMenu(List<NMenuItem> items, Date date) {
        this.items = items;
        this.date = date;
    }

    public NMenu(Parcel parcel) {
        int numOfItem = parcel.readInt();
        this.items = new ArrayList<>();
        for(int i = 0; i < numOfItem; i++) {
            this.items.add((NMenuItem)parcel.readParcelable(NMenu.class.getClassLoader()));
        }
    }

    public NMenu() {

    }

    public List<NMenuItem> getItems() {
        return items;
    }

    public void setItems(List<NMenuItem> items) {
        this.items = items;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Menu [items=" + items; //+ ", date=" + date + "]";
    }

    public static final Parcelable.Creator<NMenu> CREATOR =
            new Parcelable.Creator<NMenu>(){
                @Override
                public NMenu createFromParcel(Parcel source) {
                    return new NMenu(source);
                }

                @Override
                public NMenu[] newArray(int size) {
                    return new NMenu[size];
                }
            };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(items.size());
        for(NMenuItem item : items) {
            parcel.writeParcelable(item, 0);
        }
    }
}