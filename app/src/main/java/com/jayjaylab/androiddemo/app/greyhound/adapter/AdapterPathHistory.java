package com.jayjaylab.androiddemo.app.greyhound.adapter;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.inject.Inject;
import com.jayjaylab.androiddemo.Path;
import com.jayjaylab.androiddemo.R;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import roboguice.util.Ln;

/**
 * Created by jongjoo on 11/22/14.
 */
public class AdapterPathHistory extends RecyclerView.Adapter<AdapterPathHistory.ViewHolder> {
    List<Path> items = new ArrayList<Path>(20);
    DateTimeFormatter fmt;
    ReadWriteLock readWriteLock;
    @Inject Handler handler;

    public AdapterPathHistory() {
//        fmt = DateTimeFormat.forPattern("yyyy MMMM d");
        fmt = DateTimeFormat.forStyle("FF").withLocale(Locale.KOREAN);
        readWriteLock = new ReentrantReadWriteLock();
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Path path = null;
        readWriteLock.readLock().lock();
        path = items.get(position);
        readWriteLock.readLock().unlock();

        if(path != null) {
            DateTime startDatetime = DateTime.parse(path.getStartTime());
            DateTime endDatetime = DateTime.parse(path.getEndTime());
            viewHolder.textviewDateTime.setText(fmt.print(startDatetime));
            viewHolder.textviewPathInfo.setText(fmt.print(endDatetime));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recyclerview_item_greyhound_path_history, parent, false);
        ViewHolder vh = new ViewHolder(view);
        vh.textviewDateTime = (TextView)view.findViewById(R.id.textview_datetime);
        vh.textviewPathInfo = (TextView)view.findViewById(R.id.textview_pathinfo);

        return vh;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(List<Path> paths) {
        items.addAll(paths);
        // this will causes problems because this method has some bugs reported
        // in Google bug tracking site
        notifyDataSetChanged();
    }

    public void addItem(final Path path) {
        Ln.d("addItem() : path : %s", path);

        handler.post(new Runnable() {
            @Override
            public void run() {
                readWriteLock.writeLock().lock();
                items.add(0, path);
//        notifyItemInserted(0);
                notifyDataSetChanged();
                readWriteLock.writeLock().unlock();
            }
        });
    }

    public Path getItem(int position) {
        return items.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textviewDateTime;
        public TextView textviewPathInfo;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}