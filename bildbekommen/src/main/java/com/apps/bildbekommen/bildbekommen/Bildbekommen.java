package com.apps.bildbekommen.bildbekommen;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;

public class Bildbekommen {
    public static Activity activityss;
    public static ArrayList<String> main_list = new ArrayList<>();
    private int list_size;
    private int final_limit;
    public static boolean limitselected;

    public ListOfImages setList(ArrayList<String> list) {
        main_list = list;
        list_size = list.size();
        return new ListOfImages(list);
    }

    public void execute(Activity activity) {
        limitselected = false;
        activityss = activity;
        Intent intent = null;
        try {
            intent = new Intent(activity, Class.forName("com.apps.bildbekommen.MainActivity"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        intent.putExtra("Selection", 1);
        activity.startActivityForResult(intent, 15);
    }

    public Limit setLimit(int count) {
        list_size = 0;
        return new Limit(count);
    }

    public final class ListOfImages {
        private ListOfImages(ArrayList<String> list) {
            main_list = list;
        }

        public LimitWithSelection setLimit(int count) {
            final_limit = count;
            return new LimitWithSelection(count);
        }

        public void execute(Activity activity) {
            limitselected = false;
            activityss = activity;
            Intent intent = null;
            try {
                intent = new Intent(activity, Class.forName("com.apps.bildbekommen.MainActivity"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            intent.putExtra("Selection", 3);
            activity.startActivityForResult(intent, 15);
        }
    }

    public final class Limit {
        private Limit(int count) {
            final_limit = count;
            limitselected = true;
        }

        public Limit execute(Activity activity) {
            limitselected = true;
            activityss = activity;
            Intent intent = null;

            try {
                intent = new Intent(activity, Class.forName("com.apps.bildbekommen.MainActivity"));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            intent.putExtra("Limit", final_limit);
            intent.putExtra("Selection", 2);
            activity.startActivityForResult(intent, 15);
            return this;
        }
    }

    public final class LimitWithSelection {
        private LimitWithSelection(int count) {
            final_limit = count;
            limitselected = true;
        }

        public LimitWithSelection execute(Activity activity) {
            if (list_size >= final_limit) {
                limitselected = false;
                new FinishActivity(activity, final_limit);
            } else {
                limitselected = true;
                activityss = activity;
                Intent intent = null;
                try {
                    intent = new Intent(activity, Class.forName("com.apps.bildbekommen.MainActivity"));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                intent.putExtra("Limit", final_limit);
                intent.putExtra("Selection", 4);
                activity.startActivityForResult(intent, 15);
            }
            return this;
        }
    }

    public static final class FinishActivity {
        private FinishActivity(Activity activity, int final_limit) {
            limitselected = false;
            Toast.makeText(activity, "Limit is " + final_limit, Toast.LENGTH_SHORT).show();
            return;
        }
    }
}

