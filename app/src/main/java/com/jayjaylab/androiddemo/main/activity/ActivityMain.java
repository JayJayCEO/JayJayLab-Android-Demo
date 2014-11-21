package com.jayjaylab.androiddemo.main.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.google.inject.Inject;
import com.jayjaylab.androiddemo.R;
import com.jayjaylab.androiddemo.event.OnClickEvent;
import com.jayjaylab.androiddemo.main.adapter.AdapterMain;
import com.jayjaylab.androiddemo.main.model.App;

import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboActionBarActivity;
import roboguice.activity.event.OnPauseEvent;
import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import roboguice.util.Ln;

@ContentView(R.layout.activity_main)
public class ActivityMain extends RoboActionBarActivity {
//    @Inject ImageViewThreadPool  imageViewThreadPool;
    @Inject AdapterMain adapter;

    @InjectResource(R.string.app_description_greyhound) String appDescriptionGreyHound;
    @InjectResource(R.string.app_description_collie) String appDescriptionCollie;
    @InjectResource(R.string.app_description_wilddog) String appDescriptionWildDog;

    // views
    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.recycler_view) RecyclerView recyclerView;

    public void onCreateEvent(@Observes OnCreateEvent event) {
        setSupportActionBar(toolbar);

        setViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void handleOnGridItemClickEvent(@Observes OnClickEvent event) {
        Ln.d("handleOnGridItemClickEvent() : event : %s", event);
        if(event != null) {
            Intent intent = null;

            switch(event.getWhich()) {
                case 0:
                    intent = new Intent(this, com.jayjaylab.androiddemo.app.greyhound.
                            activity.ActivityMain.class);
                    break;
                case 1:
                    break;
                case 2:
                    break;
            }
            startActivity(intent);
        }
    }

    public void onPauseEvent(@Observes OnPauseEvent event) {

    }

    protected void setViews() {
        setToolbar();
        setRecyclerView();
    }

    protected void setToolbar() {
//        toolbar.inflateMenu(R.menu.menu_activity_main);
        toolbar.setNavigationIcon(R.drawable.ic_launcher);
    }

    protected void setRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter.addItems(getApps());
        recyclerView.setAdapter(adapter);
    }

    protected List<App> getApps() {
        List<App> apps = new ArrayList<App>(10);
        apps.add(new App(null, "Grey Hound", appDescriptionGreyHound));
        apps.add(new App(null, "Collie", appDescriptionCollie));
        apps.add(new App(null, "Wild Dog", appDescriptionWildDog));

        return apps;
    }
}
