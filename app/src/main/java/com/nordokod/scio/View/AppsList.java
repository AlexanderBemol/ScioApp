package com.nordokod.scio.View;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.nordokod.scio.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class AppsList extends AppCompatActivity {
    private ListView AppList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_list);
        final PackageManager pm = getPackageManager();
        AppList= findViewById(R.id.appList);
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        ArrayList<AppData> data=new ArrayList<>();
        for (ApplicationInfo p : packages) {
            AppData ad=new AppData();
            ad.Name=p.loadLabel(pm).toString();
            ad.Package=p.packageName;
            ad.Estado=false;
            ad.icon=p.loadIcon(pm);
            if((p.flags&ApplicationInfo.FLAG_SYSTEM)==1){
                continue;
            }
            data.add(ad);
        }
        AdapterApps adApps= new AdapterApps(this,data);
        AppList.setAdapter(adApps);

        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
        String activityOnTop = ar.topActivity.getClassName();

    }
    public  class AppData{
        public String Name,Package;
        public  boolean Estado;
        public Drawable icon;
    }
    public class AdapterApps extends BaseAdapter {
        protected Activity activity;
        protected ArrayList<AppData> items;

        public AdapterApps(Activity activity, ArrayList<AppData> items) {
            this.activity = activity;
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        public void clear() {
            items.clear();
        }

        public void addAll(ArrayList<AppData> category) {
            for (int i =0; i < category.size(); i++) {
                items.add(category.get(i));
            }
        }
        @Override
        public Object getItem(int arg0) {
            return items.get(arg0);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (convertView == null) {
                LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inf.inflate(R.layout.item_app, null);
            }
            AppData dir = items.get(position);
            TextView title = v.findViewById(R.id.AppName);
            title.setText(dir.Name);
            TextView pack =  v.findViewById(R.id.packageName);
            pack.setText(dir.Package);
            Switch edoApp = v.findViewById(R.id.switchApp);
            edoApp.setChecked(dir.Estado);
            ImageView icono = v.findViewById(R.id.appIcon);
            icono.setImageDrawable(dir.icon);
            return v;
        }
    }
}
