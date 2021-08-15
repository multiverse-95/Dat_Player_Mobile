package com.example.datplayer;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

/*import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;*/

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "";
    LinearLayout refr_lay;
    ListView myListViewForSongs;
    String[] items;
    String[] items2;
    Button Se;
    Button refresh_but;
    String search="";
    String search2="";
    EditText searchE;
    String strSearchtName="";

    boolean flag=false;
    MenuItem myMenuItem;

    static final String[] _requestPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean isAccess=isStoragePermissionGranted();
        myListViewForSongs=(ListView)findViewById(R.id.mySongListView);

        myMenuItem=(MenuItem)findViewById(R.id.action_search);
        searchE=(EditText)findViewById(R.id.searchEd);
        refresh_but=(Button)findViewById(R.id.refresh_b);
        refr_lay=(LinearLayout)findViewById(R.id.refresh_l);

        refresh_but.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               getSongs();
            }
        });
        if (isAccess){
            getSongs();
        }

    }

    public void getSongs() {
        refr_lay.setVisibility(View.GONE);
            final ArrayList<File> mySongs=findSong(Environment.getExternalStorageDirectory());

            items=new String[mySongs.size()];

            for(int i=0; i<mySongs.size(); i++){

                items[i]=mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");

            }

            searchE.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


                }

                @Override
                public void afterTextChanged(Editable s) {

                    OnSearchView();

                }
            });

            searchE.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(keyCode == KeyEvent.KEYCODE_ENTER)
                    {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    }
                    return false;
                }
            });


            final ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);

            // Toast toast = Toast.makeText(getApplicationContext(), strSearchtName, Toast.LENGTH_SHORT);
            // toast.show();

            myListViewForSongs.setAdapter(myAdapter);
            myListViewForSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String songName=myListViewForSongs.getItemAtPosition(position).toString();
                    startActivity(new Intent(getApplicationContext(),PlayerActivity.class).putExtra("pos",position).putExtra("songs",mySongs).putExtra("songname",songName));
                }
            });

    }



    public void OnSearchView() {

        strSearchtName = searchE.getText().toString();
        strSearchtName=strSearchtName.toLowerCase();

        final ArrayList<File> mySongs=findSong2(Environment.getExternalStorageDirectory());

        items2=new String[mySongs.size()];

        for(int i=0; i<mySongs.size(); i++){

            items2[i]=mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");

        }
        final ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items2);
        myListViewForSongs.setAdapter(myAdapter);
        myListViewForSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String songName=myListViewForSongs.getItemAtPosition(position).toString();
                startActivity(new Intent(getApplicationContext(),PlayerActivity.class).putExtra("pos",position).putExtra("songs",mySongs).putExtra("songname",songName));
            }
        });


    }

    public void createPlst()
    {
        final ArrayList<File> mySongs=findSong(Environment.getExternalStorageDirectory());

        items=new String[mySongs.size()];

        for(int i=0; i<mySongs.size(); i++){

            items[i]=mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");

        }
        final ArrayAdapter<String> myAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice, items);
        myListViewForSongs.setAdapter(myAdapter);
        myListViewForSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                myListViewForSongs.setItemChecked(position,false);
              String prompt = "Вы выбрали: " + myListViewForSongs.getItemAtPosition(position).toString() + "\n";
                prompt += "Выбранные элементы: \n";
                int count = myListViewForSongs.getCount();

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

        File[] files =root.listFiles();

        for(File singleFile: files){


            if(singleFile.isDirectory() && !singleFile.isHidden()){

                arrayList.addAll(findSong2(singleFile));
            }
            else {
                if((singleFile.getName().endsWith(".mp3") ||
                        singleFile.getName().endsWith(".wav")) && (singleFile.getName().toLowerCase().contains(strSearchtName))){

                    arrayList.add(singleFile);
                }
            }
        }

        return arrayList;
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search:

                return true;
           case R.id.action_pllist:

                Intent intent = new Intent(MainActivity.this, InPlayListActivity.class);
                startActivity(intent);


            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {

        openQuitDialog();
    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                MainActivity.this);
        quitDialog.setTitle("Выход: Вы уверены?");

        quitDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        quitDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
            }
        });

        quitDialog.show();
    }


}
