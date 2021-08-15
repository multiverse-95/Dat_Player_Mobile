package com.example.datplayer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;



public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_next, btn_previous, btn_pause;
    TextView songTextLabel;
    private Handler hdlr = new Handler();
    private Handler hdlr2 = new Handler();
    TextView elapsedTimeLable, remainingTimeLable;
    SeekBar songSeekBar;
    String sname;
    int sbmax=0;
    static MediaPlayer myMediaPlayer;
    int position=0;
    int totalTime;
    Uri u;

    private static int oTime =0, sTime =0, eTime =0, fTime = 5000, bTime = 5000;
    ArrayList<File> mySongs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btn_next = (Button) findViewById(R.id.next);
        btn_previous = (Button) findViewById(R.id.previous);
        btn_pause = (Button) findViewById(R.id.pause);
        songTextLabel = (TextView) findViewById(R.id.songLabel);
        songSeekBar = (SeekBar) findViewById(R.id.seekBar);


        btn_pause.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        btn_previous.setOnClickListener(this);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setDisplayShowHomeEnabled(true);



        if (myMediaPlayer != null) {
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");

        sname = mySongs.get(position).getName().toString();

        final String songName = i.getStringExtra("songname");

        songTextLabel.setText(songName);
        songTextLabel.setSelected(true);

        position = bundle.getInt("pos", 0);

        u = Uri.parse(mySongs.get(position).toString());


        myMediaPlayer = MediaPlayer.create(getApplicationContext(), u);

        myMediaPlayer.seekTo(0);
        totalTime=myMediaPlayer.getDuration();
        myMediaPlayer.start();

        myMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextSong();

            }
        });

       songSeekBar.setMax(myMediaPlayer.getDuration());


        songSeekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        songSeekBar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        sbmax=songSeekBar.getMax();

        songSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                myMediaPlayer.seekTo(seekBar.getProgress());

            }

        });

        songSeekBar.setProgress(sTime);
        hdlr.postDelayed(UpdateSongTime, 100);

    }


    @Override
    public void onClick(View v) {
        int id= v.getId();
        switch (id){
            case R.id.pause:
                if (myMediaPlayer.isPlaying()) {
                    btn_pause.setBackgroundResource(R.drawable.icon_play);
                    myMediaPlayer.pause();
                } else
                    {

                    btn_pause.setBackgroundResource(R.drawable.icon_pause);
                    myMediaPlayer.start();
                    myMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            nextSong();

                        }
                    });
                }
                break;
            case R.id.next:

                myMediaPlayer.stop();
                myMediaPlayer.release();
                position = (position + 1) % mySongs.size();
                //position = position + 1;

                u = Uri.parse(mySongs.get(position).toString());

                myMediaPlayer = MediaPlayer.create(getApplicationContext(), u);

                sname = mySongs.get(position).getName().toString();
                songTextLabel.setText(sname);
                myMediaPlayer.start();
               songSeekBar.setMax(myMediaPlayer.getDuration());
                myMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        nextSong();

                    }
                });


                break;
            case  R.id.previous:
                myMediaPlayer.stop();
                myMediaPlayer.release();

                position = (position - 1 < 0) ? mySongs.size() - 1 : position - 1;
                /*if(position-1>0){
                    position=mySongs.size()-1;
                }
                else {
                    position=position-1;
                }*/
                u = Uri.parse(mySongs.get(position).toString());
                myMediaPlayer = MediaPlayer.create(getApplicationContext(), u);

                sname = mySongs.get(position).getName().toString();
                songTextLabel.setText(sname);
                myMediaPlayer.start();
               songSeekBar.setMax(myMediaPlayer.getDuration());
                myMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        nextSong();

                    }
                });

        }
    }

    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            sTime = myMediaPlayer.getCurrentPosition();
            songSeekBar.setProgress(sTime);
            hdlr.postDelayed(this, 100);


        }
    };
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public void nextSong(){

        myMediaPlayer.stop();
        myMediaPlayer.release();
        position = (position + 1) % mySongs.size();
        //position = position + 1;

        u = Uri.parse(mySongs.get(position).toString());

        myMediaPlayer = MediaPlayer.create(getApplicationContext(), u);

        sname = mySongs.get(position).getName().toString();
        songTextLabel.setText(sname);
        myMediaPlayer.start();
        songSeekBar.setMax(myMediaPlayer.getDuration());
        myMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextSong();

            }
        });

    }


    public void tost(){
        Toast toast = Toast.makeText(getApplicationContext(), "Max: ", Toast.LENGTH_SHORT);
        toast.show();
    }

}
