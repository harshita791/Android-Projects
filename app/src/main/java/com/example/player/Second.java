package com.example.player;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
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

import static android.graphics.PorterDuff.Mode.SRC_IN;

public class Second extends AppCompatActivity {

    Button b1,b2,b3;
    TextView tv;
    SeekBar sb;
    static MediaPlayer mp;
    int position;
    ArrayList<File> mySongs;
    Thread updateseekBar;
    String sname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        b1=findViewById(R.id.pause);
        b2=findViewById(R.id.previous);
        b3=findViewById(R.id.next);
        tv=findViewById(R.id.textView);
        sb=findViewById(R.id.seekbar);
        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        updateseekBar =new Thread(){
            @Override
            public void run() {
                int totalDuration=mp.getDuration();
               int currentPosition = 0;
                while(currentPosition<totalDuration){
                    try{

                        sleep(500);
                        currentPosition=mp.getCurrentPosition();
                        sb.setProgress(currentPosition);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        if(mp!=null){
            mp.stop();
            mp.release();
        }
        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        mySongs=(ArrayList) bundle.getParcelableArrayList("songs");
        sname=mySongs.get(position).getName().toString();
        String songName = i.getStringExtra("songname");
        tv.setText(String.valueOf(songName));
        tv.setSelected(true);
        position=bundle.getInt("pos",0);
        Uri u= Uri.parse(mySongs.get(position).toString());


        mp=MediaPlayer.create(getApplicationContext(),u);
        mp.start();
        Toast.makeText(this, songName, Toast.LENGTH_SHORT).show();
        sb.setMax(mp.getDuration());

        updateseekBar.start();

        sb.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.MULTIPLY);
        sb.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mp.seekTo(seekBar.getProgress());
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.setMax(mp.getDuration());
                if(mp.isPlaying()){
                    b1.setBackgroundResource(R.drawable.play);
                    mp.pause();
                }
                else{
                    b1.setBackgroundResource(R.drawable.pause);
                    mp.start();
                }
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mp.stop();
                mp.release();
                position=(position+1)%mySongs.size();

                Uri u= Uri.parse(mySongs.get(position).toString());
                mp=MediaPlayer.create(getApplicationContext(),u);

                sname=mySongs.get(position).getName().toString();
                tv.setText(sname);

                mp.start();

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                mp.release();
                position=(position-1<0)?(mySongs.size()-1):(position-1);

                Uri u=Uri.parse(mySongs.get(position).toString());
                mp=MediaPlayer.create(getApplicationContext(),u);
                sname=mySongs.get(position).getName();
                tv.setText(sname);

                mp.start();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
