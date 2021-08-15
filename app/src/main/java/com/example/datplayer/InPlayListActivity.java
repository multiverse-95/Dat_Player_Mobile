package com.example.datplayer;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class InPlayListActivity extends AppCompatActivity {

    Button addtr;
    ArrayList<File> mySongs;
    ArrayList<String> songss = new ArrayList();
    int position=0;
    String sname;
    ListView songsList;
    String[] items;
    ArrayList<Integer>flagpos=new ArrayList();

    String songName;
    int count2=0;

    ArrayList<String> myChecked2=new ArrayList<String>();
    ArrayAdapter<String> songadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_play_list);
        addtr=(Button)findViewById(R.id.addtrack);
        songsList = (ListView) findViewById(R.id.songsList);


        final ArrayList<File> mySongs=findSong(Environment.getExternalStorageDirectory());

        items=new String[mySongs.size()];

        for(int i=0; i<mySongs.size(); i++){

            items[i]=mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");

        }

        for(int i=0; i<mySongs.size(); i++)
        {
            flagpos.add(0);
        }
        final ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice, items);
        songsList.setAdapter(myAdapter);

        songsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               int count = songsList.getCount();
              ArrayList<String> myChecked=new ArrayList<String>();

                SparseBooleanArray sparseBooleanArray = songsList.getCheckedItemPositions();
                for (int i = 0; i < count; i++) {
                    if (sparseBooleanArray.get(i)) {
                        myChecked.add(songsList.getItemAtPosition(i).toString());
                        //Toast toast = Toast.makeText(getApplicationContext(), "pos "+myChecked, Toast.LENGTH_SHORT);
                        //toast.show();


                    }
                        else
                            {
                                ArrayList<String> myChecked2=new ArrayList<String>();
                               // myChecked2=myChecked;


                        }


                }
                myChecked2=myChecked;



            }
        });



    }

    public ArrayList<File> findSong(File root){

        ArrayList<File> arrayList= new ArrayList<>();

        File[] files =root.listFiles();

        for(File singleFile: files){

            if(singleFile.isDirectory() && !singleFile.isHidden()){

                arrayList.addAll(findSong(singleFile));
            }
            else {
                if(singleFile.getName().endsWith(".mp3") ||
                        singleFile.getName().endsWith(".wav")) {

                    arrayList.add(singleFile);
                }
            }
        }

        return arrayList;
    }

   public ArrayList<File> findSong2(File root){

        ArrayList<File> arrayList= new ArrayList<>();
        int count=0;
        File[] files =root.listFiles();

        for(File singleFile: files){


            if(singleFile.isDirectory() && !singleFile.isHidden()){

                arrayList.addAll(findSong2(singleFile));
            }
            else {
                for (int i=0; i<myChecked2.size(); i++){
                    if((singleFile.getName().endsWith(".mp3") ||
                            singleFile.getName().endsWith(".wav")) && (singleFile.getName().equals(myChecked2.get(i)+".mp3"))){
                        arrayList.add(singleFile);
                }

                }
            }

        }

        return arrayList;
    }

    public void OnSearchView() {

        addtr.setVisibility(View.GONE);
        final ArrayList<File> mySongs=findSong2(Environment.getExternalStorageDirectory());

        items=new String[mySongs.size()];

        for(int i=0; i<mySongs.size(); i++){

            items[i]=mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");

        }
        final ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);
        songsList.setAdapter(myAdapter);
        songsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String songName=songsList.getItemAtPosition(position).toString();
                startActivity(new Intent(getApplicationContext(),PlayerActivity.class).putExtra("pos",position).putExtra("songs",mySongs).putExtra("songname",songName));
            }
        });


    }

    public void addThis(View view)
    {

        OnSearchView();
    }


}
