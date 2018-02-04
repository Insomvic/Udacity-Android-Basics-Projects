package com.insomvic.nostalgicmelodies;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SongInfoAdapter extends ArrayAdapter<SongInfo> {

    public SongInfoAdapter(Activity context, ArrayList<SongInfo> words) {
        super(context, 0, words);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        final SongInfo currentSong = getItem(position);
        // Set song name
        TextView songTextView = listItemView.findViewById(R.id.song_text_view);
        songTextView.setText(currentSong.getSongName());
        // Set game name
        TextView gameTextView = listItemView.findViewById(R.id.game_text_view);
        gameTextView.setText(currentSong.getGameName());
        // Set icon for row
        ImageView imageView = listItemView.findViewById(R.id.song_image_view);
        imageView.setImageResource(currentSong.getThumbnailResourceId());
        // Make entire row clickable
        LinearLayout linearLayout = listItemView.findViewById(R.id.song_row);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.playing = true;
                MainActivity.playing_song_name = currentSong.getSongName();
                MainActivity.playing_game_name = currentSong.getGameName();
                MainActivity.playing_album_cover = currentSong.getAlbumResourceId();
                // Go to playing song activity
                Intent i = new Intent(view.getContext(), PlayingSong.class);
                view.getContext().startActivity(i);
            }
        });
        return listItemView;
    }

}
