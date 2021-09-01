package com.apps.bildbekommen.intrfaces;

public interface ItemSelectedListener {
    void selecte();

    void unSelect();

    void deleteImage(int position);

    void onAlbumListImageClick(int position);
}
