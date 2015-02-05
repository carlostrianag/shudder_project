package com.shiftscope.views.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shiftscope.dto.TrackDTO;
import com.shiftscope.listeners.WebSocketListener;
import com.shiftscope.netservices.TCPService;
import com.shiftscope.utils.Operation;
import com.shiftscope.utils.Sync;
import com.shiftscope.utils.adapters.LibraryAdapter;

import java.util.ArrayList;
import java.util.Objects;

import shiftscope.com.shiftscope.R;

/**
 * Created by Carlos on 2/4/2015.
 */
public class PlayListFragment extends Fragment {

    private WebSocketListener socketListener = new WebSocketListener() {
        @Override
        public void OnSync(Operation o) {
            Sync syncObject = o.getSync();
            ArrayList<TrackDTO> tracks = syncObject.getCurrentPlaylist();
            if(!tracks.isEmpty()) {
                ArrayList<Object> playList = new ArrayList<>();
                playList.addAll(tracks);
                LibraryAdapter adapter = new LibraryAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, playList);
                playListListView.setAdapter(adapter);
            }
        }
    };
    private SharedPreferences sharedPreferences;
    private ListView playListListView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_playlist, container, false);
        sharedPreferences = getActivity().getSharedPreferences("ShudderSharedPreferences", Context.MODE_PRIVATE);
        playListListView = (ListView) v.findViewById(R.id.playListListView);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        TCPService.addListener(socketListener);
        drawPlaylist();
        Log.v("FRAGMENT", "START");
    }

    @Override
    public void onResume() {
        super.onResume();
        TCPService.addListener(socketListener);
    }

    @Override
    public void onPause() {
        super.onPause();
        TCPService.removeListener(socketListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        TCPService.removeListener(socketListener);
    }

    private void drawPlaylist() {
        Gson JSONParser = new Gson();
        ArrayList<Object> playList = JSONParser.fromJson(sharedPreferences.getString("currentPlaylist", ""), new TypeToken<ArrayList<TrackDTO>>(){}.getType());
        if(playList != null) {
            LibraryAdapter adapter = new LibraryAdapter(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, playList);
            playListListView.setAdapter(adapter);
        }
    }
}