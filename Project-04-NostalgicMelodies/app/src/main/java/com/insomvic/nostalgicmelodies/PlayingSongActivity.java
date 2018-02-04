package com.insomvic.nostalgicmelodies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayingSongActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_song);
        // Change album cover
        ImageView imageView = findViewById(R.id.album_cover);
        imageView.setImageResource(MainActivity.playing_album_cover);
        // Change song name
        TextView textView = findViewById(R.id.playing_song_name);
        textView.setText(MainActivity.playing_song_name);
        // Change game name
        textView = findViewById(R.id.playing_game_name);
        textView.setText(MainActivity.playing_game_name);
        // Set onClickListener
        playPauseClick((ImageView)findViewById(R.id.play_pause));
        nextSongClick((ImageView)findViewById(R.id.next_song));
        previousSongClick((ImageView)findViewById(R.id.previous_song));
        // Back button on toolbar
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch(NullPointerException e) {
            System.out.println(e.getMessage());
        }
    }

    public void playPauseClick(final ImageView imageView) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MainActivity.playing) {
                    MainActivity.playing = true;
                    imageView.setImageResource(R.drawable.ic_pause);
                    //TODO: Play song method
                } else {
                    MainActivity.playing = false;
                    imageView.setImageResource(R.drawable.ic_play);
                    //TODO: Pause song method
                }
            }
        });
    }

    public void nextSongClick(final ImageView imageView) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Skip to next song method
            }
        });
    }

    public void previousSongClick(final ImageView imageView) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Skip to previous song method
            }
        });
    }

    // Click event method that specifies what class to return to
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(getApplicationContext(), SongListActivity.class);
        startActivityForResult(intent, 0);
        return true;
    }

}