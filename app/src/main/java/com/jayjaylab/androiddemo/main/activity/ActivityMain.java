package com.jayjaylab.androiddemo.main.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.inject.Inject;
import com.jayjaylab.androiddemo.R;
import com.jayjaylab.androiddemo.main.adapter.AdapterMain;
import com.jayjaylab.androiddemo.main.model.App;

import java.util.ArrayList;
import java.util.List;

import roboguice.activity.RoboActionBarActivity;
import roboguice.activity.event.OnPauseEvent;
import roboguice.context.event.OnCreateEvent;
import roboguice.event.Observes;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_main)
public class ActivityMain extends RoboActionBarActivity {
//    @Inject ImageViewThreadPool  imageViewThreadPool;
    @Inject
AdapterMain adapter;

    // views
    @InjectView(R.id.toolbar) Toolbar toolbar;
    @InjectView(R.id.recycler_view) RecyclerView recyclerView;

    public void onCreateEvent(@Observes OnCreateEvent event) {
        setSupportActionBar(toolbar);

        setViews();
    }

    public void onPauseEvent(@Observes OnPauseEvent event) {

    }

    protected void setViews() {
        setRecyclerView();
    }

    protected void setRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2,
                GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter.addItems(getApps());
        recyclerView.setAdapter(adapter);
//        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent event) {
//                Ln.d("onInterceptTouchEvent() : event : %s", event);
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(RecyclerView recyclerView, MotionEvent event) {
//                Ln.d("onTouchEvent() : event : %s", event);
//            }
//        });
    }

    protected List<App> getApps() {
        List<App> apps = new ArrayList<App>(10);
        apps.add(new App(null, "Grey Hound", "This native app records your walking path"));
        apps.add(new App(null, "Collie", "This webapp offers new mobile shopping experiences"));
        apps.add(new App(null, "Wild Dog", "This webapp provides hotels, inns info for people to stay"));

        return apps;
    }
}