package com.roquahacks.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NMenuItem implements Parcelable{

    private String description;
    private String price;
    private boolean isVegetarian;

    public NMenuItem(String description, String price, boolean isVegetarian) {
        this.description = description;
        this.price = price;
        this.isVegetarian = isVegetarian;
    }

    public NMenuItem(Parcel parcel) {
        this.description = parcel.readString();
        this.price = parcel.readString();
        this.isVegetarian = parcel.readByte() != 0;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isVegetarian() {
        return isVegetarian;
    }

    public void setVegetarian(boolean isVegetarian) {
        this.isVegetarian = isVegetarian;
    }

    @Override
    public String toString() {
        return "MenuItem [description=" + description +
                ", price=" + price + ", isVegetarian=" + isVegetarian + "]";
    }

    public static final Parcelable.Creator<NMenuItem> CREATOR =
            new Parcelable.Creator<NMenuItem>(){
                @Override
                public NMenuItem createFromParcel(Parcel source) {
                    return new NMenuItem(source);
                }

                @Override
                public NMenuItem[] newArray(int size) {
                    return new NMenuItem[size];
                }
            };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.description);
        parcel.writeString(this.price);
        parcel.writeByte((byte) (this.isVegetarian ? 1 : 0));
    }
}
