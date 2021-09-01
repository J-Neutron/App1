package com.apps.bildbekommen.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageSelected implements Parcelable {
    public long id;
    public String name;
    public String selectedName;
    public String path;
    public boolean isSelected = false;

    public ImageSelected(long id, String name, String selectedName, String path, boolean isSelected) {
        this.id = id;
        this.name = name;
        this.selectedName = selectedName;
        this.path = path;
        this.isSelected = isSelected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(selectedName);
        dest.writeString(path);
    }

    public static final Parcelable.Creator<ImageSelected> CREATOR = new Parcelable.Creator<ImageSelected>() {
        @Override
        public ImageSelected createFromParcel(Parcel source) {
            return new ImageSelected(source);
        }

        @Override
        public ImageSelected[] newArray(int size) {
            return new ImageSelected[size];
        }
    };

    private ImageSelected(Parcel in) {
        id = in.readLong();
        name = in.readString();
        selectedName = in.readString();
        path = in.readString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
