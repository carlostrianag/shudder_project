package com.shiftscope.utils.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.daimajia.swipe.implments.SwipeItemMangerImpl;
import com.daimajia.swipe.interfaces.SwipeItemMangerInterface;
import com.shiftscope.dto.FolderDTO;
import com.shiftscope.dto.TrackDTO;
import com.shiftscope.netservices.TCPService;
import com.shiftscope.utils.Operation;
import com.shiftscope.utils.constants.RequestTypes;
import com.shiftscope.utils.constants.SessionConstants;
import com.shiftscope.utils.filters.LibraryFilter;

import java.util.ArrayList;
import java.util.Collections;

import shiftscope.com.shiftscope.R;

/**
 * Created by Carlos on 1/4/2015.
 */
public class LibraryAdapter extends BaseAdapter implements Filterable{

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<Object> folderContent;
    private LibraryFilter filter;
    private FolderDTO folder;
    private TrackDTO track;


    public LibraryAdapter(Context context, ArrayList<Object> objects) {
        this.context = context;
        this.folderContent = objects;
        Log.v("DEBUG", "CONSTRUCTOR");

    }

    private class FolderViewHolder {
        public TextView folderTitle;

        private FolderViewHolder(View  v) {
            folderTitle = (TextView) v.findViewById(R.id.folderTitle);
        }
    }

    private class TrackViewHolder {
        public TextView trackTitle;
        public TextView artistName;

        private TrackViewHolder(View v) {
            trackTitle = (TextView) v.findViewById(R.id.trackTitle);
            artistName =  (TextView) v.findViewById(R.id.artistName);
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return (folderContent.get(position).getClass() == FolderDTO.class)?0:1;
    }

    @Override
    public int getCount() {
        return folderContent.size();
    }

    @Override
    public Object getItem(int position) {
        return folderContent.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

/*    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }*/

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        int layoutType = getItemViewType(position);
        FolderViewHolder folderHolder;
        TrackViewHolder trackHolder;
        View v = convertView;

        switch (layoutType) {
            case 0:
                folder = (FolderDTO)folderContent.get(position);

                if ( v == null) {
                    LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v  = layoutInflater.inflate(R.layout.item_library_folder, parent, false);
                    folderHolder = new FolderViewHolder(v);
                    v.setTag(folderHolder);
                } else {
                    folderHolder = (FolderViewHolder) v.getTag();
                }
                folderHolder.folderTitle.setText(folder.getTitle());
                break;
            case 1:
                track = (TrackDTO)folderContent.get(position);
                if (v == null) {
                    LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v  = layoutInflater.inflate(R.layout.item_library_track, parent, false);
                    trackHolder = new TrackViewHolder(v);
                    v.setTag(trackHolder);
                } else {
                    trackHolder = (TrackViewHolder) v.getTag();
                }
                trackHolder.trackTitle.setText(track.getTitle());
                trackHolder.artistName.setText(track.getArtist());

                break;
        }
            return v;
    }

    @Override
    public Filter getFilter() {
        if(filter == null) {
            filter = new LibraryFilter(this);
        }
        return filter;
    }

    public ArrayList<Object> getFolderContent() {
        return folderContent;
    }
}
