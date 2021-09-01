package com.apps.bildbekommen.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageAlbum implements Parcelable {
    String title;
    String images;


    public ImageAlbum(String title, String images) {
        this.title = title;
        this.images = images;
    }

    protected ImageAlbum(Parcel in) {
        title = in.readString();
        images = in.readString();
    }

    public static final Creator<ImageAlbum> CREATOR = new Creator<ImageAlbum>() {
        @Override
        public ImageAlbum createFromParcel(Parcel in) {
            return new ImageAlbum(in);
        }

        @Override
        public ImageAlbum[] newArray(int size) {
            return new ImageAlbum[size];
        }
    };

    public String getImages() {
        return images;
    }
    public void setImages(String images) {
        this.images = images;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.title);
        parcel.writeString(this.images);
    }
}
