package com.shiftscope.views.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shiftscope.controllers.FolderController;
import com.shiftscope.controllers.LibraryController;
import com.shiftscope.controllers.PlaylistController;
import com.shiftscope.listeners.WebSocketListener;
import com.shiftscope.netservices.TCPService;
import com.shiftscope.utils.Operation;
import com.shiftscope.utils.Sync;
import com.shiftscope.utils.adapters.DrawerAdapter;
import com.shiftscope.utils.constants.Constants;
import com.shiftscope.utils.constants.RequestTypes;
import com.shiftscope.utils.constants.SessionConstants;
import com.shiftscope.views.dialogs.VolumeDialog;
import com.shiftscope.views.fragments.LibraryFragment;
import com.shiftscope.views.fragments.PlayListFragment;

import shiftscope.com.shiftscope.R;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, SearchView.OnQueryTextListener, MenuItem.OnMenuItemClickListener{


    private LibraryFragment libraryFragment;
    private PlayListFragment playListFragment;
    private ListView navListView;
    private TextView currentSongText;
    private SharedPreferences sharedPreferences;

    private WebSocketListener socketListener = new WebSocketListener() {
        @Override
        public void OnSync(Operation o) {
            switch (o.getOperationType()) {
                case RequestTypes.SYNC:
                    Sync syncObject = o.getSync();
                    if (syncObject != null) {
                        sharedPreferences = getSharedPreferences("ShudderSharedPreferences", Context.MODE_PRIVATE);

                        PlaylistController.setPlaylist(syncObject.getCurrentPlaylist());
                        LibraryController.queueChanged(syncObject.getAddedTrack(), syncObject.getDeletedTrack());

                        SessionConstants.PLAYER_VOLUME = syncObject.getCurrentVolume();
                        if(syncObject.getCurrentSongName() != null && syncObject.getCurrentSongArtist() != null) {
                            currentSongText.setText(syncObject.getCurrentSongName() + " - " + syncObject.getCurrentSongArtist());
                        } else {
                            currentSongText.setText("");
                        }

                    }
                    break;
                case RequestTypes.SET_VOLUME:
                    SessionConstants.PLAYER_VOLUME = o.getValue();
                    SessionConstants.VOLUME_FROM_USER = false;
                    if(volumeDialog != null) {
                        volumeDialog.updateVolume();
                    }
                    SessionConstants.VOLUME_FROM_USER = true;
                    break;
            }
        }
    };


    private AdapterView.OnItemClickListener navDrawerOnClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            navListView.setItemChecked(position, true);
            switch(position) {
                case 0:
                    libraryFragment = new LibraryFragment();
                    drawerLayout.closeDrawers();
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainContent, libraryFragment).commit();
                    break;
                case 1:
                    playListFragment = new PlayListFragment();
                    drawerLayout.closeDrawers();
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainContent, playListFragment).commit();
                    break;
            }
        }
    };

    private ImageView nextBtn;
    private ImageView playBtn;
    private ImageView backBtn;
    private ImageView stopBtn;
    private ImageView volumeBtn;
    private SearchView searchView;
    private VolumeDialog volumeDialog;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setLogo(R.drawable.logo_shudder);

        TCPService.addListener(socketListener);
        Constants.init();
        DrawerAdapter navListAdapter = new DrawerAdapter(this, android.R.layout.simple_list_item_1, Constants.ENTRIES,  getLayoutInflater());
        navListView = (ListView)findViewById(R.id.drawerList);
        navListView.setAdapter(navListAdapter);
        navListView.setOnItemClickListener(navDrawerOnClickListener);
        currentSongText = (TextView) findViewById(R.id.currentSongTextView);
        nextBtn = (ImageView) findViewById(R.id.nextBtn);
        backBtn = (ImageView) findViewById(R.id.backBtn);
        playBtn = (ImageView) findViewById(R.id.pauseBtn);
        stopBtn = (ImageView) findViewById(R.id.stopBtn);
        volumeBtn = (ImageView) findViewById(R.id.volBtn);
        nextBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        playBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);
        volumeBtn.setOnClickListener(this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerListener = new ActionBarDrawerToggle(this, drawerLayout, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout.setDrawerListener(drawerListener);
        libraryFragment = new LibraryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainContent, libraryFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(drawerListener.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        MenuItem orderByArtist = menu.findItem(R.id.action_order_by_artist);
        orderByArtist.setOnMenuItemClickListener(this);
        MenuItem orderByTitle = menu.findItem(R.id.action_order_by_title);
        orderByTitle.setOnMenuItemClickListener(this);
        return true;
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerListener.syncState();
    }


    @Override
    public void onClick(View v) {
        Operation operation = new Operation();
        operation.setUserId(SessionConstants.USER_ID);
        operation.setTo(SessionConstants.DEVICE_ID);
        switch(v.getId()) {
            case R.id.backBtn:
                operation.setOperationType(RequestTypes.BACK);
                break;
            case R.id.nextBtn:
                operation.setOperationType(RequestTypes.NEXT);
                break;
            case R.id.stopBtn:
                operation.setOperationType(RequestTypes.STOP);
                break;
            case R.id.pauseBtn:
                operation.setOperationType(RequestTypes.PAUSE);
                break;
            case R.id.volBtn:
                volumeDialog = new VolumeDialog();
                volumeDialog.show(getSupportFragmentManager(), "VolumeDialog");
                break;
        }
        TCPService.send(operation);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        FolderController.search(newText);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_order_by_artist:
                FolderController.orderByArtist();
                break;
            case R.id.action_order_by_title:
                FolderController.orderByTitle();
                break;
        }
        return false;
    }
}