package com.apps.bildbekommen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.apps.bildbekommen.adapter.ImageAdapter;
import com.apps.bildbekommen.adapter.ImageAlbumAdapter;
import com.apps.bildbekommen.adapter.SelectedImageListAdapter;
import com.apps.bildbekommen.bildbekommen.Bildbekommen;
import com.apps.bildbekommen.intrfaces.ItemSelectedListener;
import com.apps.bildbekommen.model.ImageAlbum;
import com.apps.bildbekommen.model.ImageSelected;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import static com.apps.bildbekommen.bildbekommen.Bildbekommen.activityss;

public class Bekommen extends AppCompatActivity implements ItemSelectedListener {
    private ArrayList<ImageAlbum> buckets;
    ImageAlbumAdapter imagealbumadapter;
    SelectedImageListAdapter selectedImageListAdapter;
    ImageAdapter imageAdapter;
    private RecyclerView recyclerAlbumList, recyclerImageList, selectedimages;
    private ArrayList<ImageSelected> previewActivities;
    private ArrayList<ImageSelected> temp;
    Handler handler;
    String name, album_name;
    public static TextView iv_image_count;
    private HashSet<Long> albumSet;
    public static ArrayList<String> main_list_copy = new ArrayList<>();
    private final String[] projection = new String[]{
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA};
    ImageView btn_done, btn_back;
    public int final_limit,selection;
    private boolean album_exists = false;
    private KProgressHUD kProgressHUD;
    private RelativeLayout main_layout;
    Activity activitys;

    public Bekommen(Activity activity) {
        activitys = activity;
    }

    public Bekommen() {

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_done = findViewById(R.id.btn_done);
        btn_back = findViewById(R.id.btn_back);
        main_layout = findViewById(R.id.main_layout);
        selectedimages = findViewById(R.id.selectedimages);
        iv_image_count = findViewById(R.id.iv_image_count);
        albumSet = new HashSet<>();
        handler = new Handler();
        buckets = new ArrayList<>();
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        String rationale = "Please provide Storage permission so that you can ...";
        Permissions.Options options = new Permissions.Options()
                .setRationaleDialogTitle("Info")
                .setSettingsDialogTitle("Warning");

        activitys = activityss;

        final_limit = getIntent().getIntExtra("Limit", 0);
        selection = getIntent().getIntExtra("Selection",0);
        if (selection == 1)
        {
            createCopyList();
            Bildbekommen.main_list.clear();
        }
        else if (selection == 2)
        {
            createCopyList();
            Bildbekommen.main_list.clear();
        }
        else if (selection == 3 || selection == 4)
        {
            createCopyList();
        }


        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Bildbekommen.main_list != null && Bildbekommen.main_list.size() != 0) {
                    Intent i = new Intent(Bekommen.this, activitys.getClass());
                    i.putStringArrayListExtra("album_images_layout", Bildbekommen.main_list);
                    setResult(RESULT_OK, i);
                    finish();
                } else {
                    Toast.makeText(Bekommen.this, "Please select some previewActivities", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        Permissions.check(this, permissions, rationale, options, new PermissionHandler() {
            @Override
            public void onGranted() {
                new getData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                main_layout.setVisibility(View.GONE);
                Intent i = new Intent(Bekommen.this, activitys.getClass());
                startActivity(i);
                finish();
            }
        });
    }

    public void createCopyList()
    {
        main_list_copy.clear();
        for (int j = 0; j< Bildbekommen.main_list.size(); j++)
        {
            main_list_copy.add(Bildbekommen.main_list.get(j));
        }
    }

    @Override
    public void selecte() {
        if (Bildbekommen.main_list != null) {

            selectedimages.setHasFixedSize(true);
            LinearLayoutManager HorizontalLayout = new LinearLayoutManager(Bekommen.this, LinearLayoutManager.HORIZONTAL, false);
            selectedimages.setLayoutManager(HorizontalLayout);
            selectedImageListAdapter = new SelectedImageListAdapter(Bekommen.this, Bildbekommen.main_list);
            selectedimages.setAdapter(selectedImageListAdapter);
            iv_image_count.setText(String.valueOf(Bildbekommen.main_list.size()));
        } else {
            selectedImageListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void unSelect() {
        if (Bildbekommen.main_list != null) {
            selectedimages.setHasFixedSize(true);
            LinearLayoutManager HorizontalLayout = new LinearLayoutManager(Bekommen.this, LinearLayoutManager.HORIZONTAL, false);
            selectedimages.setLayoutManager(HorizontalLayout);
            selectedImageListAdapter = new SelectedImageListAdapter(Bekommen.this, Bildbekommen.main_list);
            selectedimages.setAdapter(selectedImageListAdapter);
            iv_image_count.setText(String.valueOf(Bildbekommen.main_list.size()));
        } else {
            selectedImageListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void deleteImage(int position) {
        iv_image_count.setText(String.valueOf(Bildbekommen.main_list.size()));
        imageAdapter.notifyItemChanged(position);
        imageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAlbumListImageClick(int ColorClock) {
        name = buckets.get(ColorClock).getTitle();
        if (name == null)
        {
            name = "internal";
        }
        album_name = name;
        Toast.makeText(this, "" + name, Toast.LENGTH_SHORT).show();
        //albumImages();
        new getImages().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class getData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            kProgressHUD = KProgressHUD.create(Bekommen.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setDetailsLabel("Fetching data")
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
            if (buckets != null) {
                buckets.clear();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Cursor cursor = getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, MediaStore.Images.Media.DATE_ADDED);

            File file;
            if (cursor.moveToLast()) {
                do {
                    long albumId = cursor.getLong(cursor.getColumnIndex(projection[0]));
                    String album = cursor.getString(cursor.getColumnIndex(projection[1]));
                    String image = cursor.getString(cursor.getColumnIndex(projection[2]));
                    if (!albumSet.contains(albumId)) {
                        file = new File(image);
                        if (file.exists()) {
                            Log.e("asdsdqdqwidj",""+album+"\t\t"+image);
                            if (album == null)
                            {
                                album = "internal";
                            }
                            buckets.add(new ImageAlbum(album, image));
                            albumSet.add(albumId);
                        }
                    } else {
                    }
                } while (cursor.moveToPrevious());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            kProgressHUD.dismiss();
            if (buckets != null && buckets.size() != 0) {
                main_layout.setVisibility(View.VISIBLE);
                album_exists = true;
                recyclerAlbumList = findViewById(R.id.albumlist);
                recyclerAlbumList.setHasFixedSize(true);
                recyclerAlbumList.setLayoutManager(new LinearLayoutManager(Bekommen.this));
                imagealbumadapter = new ImageAlbumAdapter(Bekommen.this, buckets);
                recyclerAlbumList.setAdapter(imagealbumadapter);
                recyclerAlbumList.setVisibility(View.VISIBLE);

            } else {
                main_layout.setVisibility(View.GONE);
                Intent i = new Intent(Bekommen.this, activitys.getClass());
                startActivity(i);
                finish();
                album_exists = false;
                Toast.makeText(Bekommen.this, "Album not found", Toast.LENGTH_LONG).show();
            }

            if (album_exists == true)
            {
                if (Bildbekommen.main_list != null) {
                    selectedimages.setHasFixedSize(true);
                    LinearLayoutManager HorizontalLayout = new LinearLayoutManager(Bekommen.this, LinearLayoutManager.HORIZONTAL, false);
                    selectedimages.setLayoutManager(HorizontalLayout);
                    selectedImageListAdapter = new SelectedImageListAdapter(Bekommen.this, Bildbekommen.main_list);
                    selectedimages.setAdapter(selectedImageListAdapter);
                    iv_image_count.setText(String.valueOf(Bildbekommen.main_list.size()));
                }
                else if (main_list_copy != null)
                {
                    selectedimages.setHasFixedSize(true);
                    LinearLayoutManager HorizontalLayout = new LinearLayoutManager(Bekommen.this, LinearLayoutManager.HORIZONTAL, false);
                    selectedimages.setLayoutManager(HorizontalLayout);
                    selectedImageListAdapter = new SelectedImageListAdapter(Bekommen.this, main_list_copy);
                    selectedimages.setAdapter(selectedImageListAdapter);
                    iv_image_count.setText(String.valueOf(main_list_copy.size()));
                }
            }
        }
    }


    private class getImages extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            kProgressHUD = KProgressHUD.create(Bekommen.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setDetailsLabel("Fetching data")
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            File file;
            HashSet<Long> selectedImages = new HashSet<>();
            if (previewActivities != null) {
                ImageSelected imageSelected;
                for (int i = 0, l = previewActivities.size(); i < l; i++) {
                    imageSelected = previewActivities.get(i);
                    file = new File(imageSelected.path);
                    if (file.exists() && imageSelected.isSelected) {
                        selectedImages.add(imageSelected.id);
                    }
                }
            }
            Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " =?", new String[]{name}, MediaStore.Images.Media.DATE_ADDED);
            temp = new ArrayList<>(cursor.getCount());
            if (cursor.moveToLast()) {
                do {
                    if (Thread.interrupted()) {
                        return null;
                    }
                    long id = cursor.getLong(cursor.getColumnIndex(projection[0]));
                    String selectedName = cursor.getString(cursor.getColumnIndex(projection[1]));
                    String path = cursor.getString(cursor.getColumnIndex(projection[2]));
                    boolean isSelected = selectedImages.contains(id);
                    File file1 = new File(path);
                    album_name = file1.getName();
                    file = new File(path);
                    if (file.exists()) {
                        temp.add(new ImageSelected(id, album_name, selectedName, path, isSelected));
                    }

                } while (cursor.moveToPrevious());
            }
            cursor.close();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            kProgressHUD.dismiss();
            if (previewActivities == null) {
                previewActivities = new ArrayList<>();
            }
            previewActivities.clear();
            previewActivities.addAll(temp);

            if (previewActivities != null) {
                recyclerImageList = findViewById(R.id.imagelist);
                recyclerImageList.setHasFixedSize(true);
                recyclerImageList.setLayoutManager(new GridLayoutManager(Bekommen.this, 2));
                imageAdapter = new ImageAdapter(Bekommen.this, previewActivities, final_limit);
                recyclerImageList.setAdapter(imageAdapter);
                recyclerImageList.setVisibility(View.VISIBLE);

            } else {
                imageAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Bildbekommen.main_list.clear();
        for (int j=0;j<main_list_copy.size();j++)
        {
            Bildbekommen.main_list.add(main_list_copy.get(j));
        }
        Intent i = new Intent(Bekommen.this, activitys.getClass());
        i.putStringArrayListExtra("album_images_layout", main_list_copy);
        setResult(RESULT_OK, i);
        finish();
    }
}
