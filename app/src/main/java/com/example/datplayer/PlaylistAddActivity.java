package com.example.datplayer;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import java.io.File;
import java.util.ArrayList;

public class PlaylistAddActivity extends AppCompatActivity {

    ListView myListViewForSongs;
    String[] items;

    boolean flag=false;
    MenuItem myMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_add);

        myListViewForSongs=(ListView)findViewById(R.id.songsList);

        myMenuItem=(MenuItem)findViewById(R.id.action_search);



        final ArrayList<File> mySongs=findSong(Environment.getExternalStorageDirectory());

        items=new String[mySongs.size()];

        for(int i=0; i<mySongs.size(); i++){

            items[i]=mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");

        }

        final ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);


        myListViewForSongs.setAdapter(myAdapter);
        myListViewForSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String songName=myListViewForSongs.getItemAtPosition(position).toString();

                Intent intent = new Intent(PlaylistAddActivity.this, InPlayListActivity.class);
                startActivity(new Intent(getApplicationContext(),InPlayListActivity.class).putExtra("pos",position).putExtra("songs",mySongs).putExtra("songname",songName));
            }
        });



    }



    public ArrayList<File> findSong(File root){

        ArrayList<File> arrayList= new ArrayList<>();

        File[] files =root.listFiles();

        for(File singleFile: files){

            if(singleFile.isDirectory() && !singleFile.isHidden()){
                //arrayList.addAll(findSong(singleFile));
                arrayList.addAll(findSong(singleFile));
            }
            else {
                if(singleFile.getName().endsWith(".mp3") ||
                        singleFile.getName().endsWith(".wav")){

                    arrayList.add(singleFile);
                }
            }
        }

        return arrayList;
    }
}
