package com.insomvic.nostalgicmelodies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class SongListActivity extends AppCompatActivity{

    // Create arraylist
    ArrayList<SongInfo> songInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_list);
        // Get game name from previous click and activity
        String gameName = MainActivity.game_franchise;
        // Back button on toolbar
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch(NullPointerException e) {
            System.out.println(e.getMessage());
        }
        // Change title of activity
        this.setTitle(gameName);
        // Create arraylists based from previous activity
        // Zelda
        if (gameName.equals(getString(R.string.zelda))) {
            // Ocarina of Time
            String[] songsOot = {"Zelda’s Lullaby", "Epona’s Song", "Saria’s Song", "Sun’s Song", "Song of Time",
                    "Song of Storms", "Minuet of Forest", "Bolero of Fire", "Serenade of Water", "Requiem of Spirit",
                    "Nocturne of Shadow", "Prelude of Light"};
            songInfoAdd(songsOot, "The Legend of Zelda - Ocarina of Time", R.drawable.ic_zelda_oot,  R.drawable.album_zelda_oot);
            // Majora's Mask
            String[] songsMm = {"Song of Healing", "Song of Soaring", "Sonata of Awakening", "Inverted Song of Time",
                    "Song of Double Time", "Oath to Order"};
            songInfoAdd(songsMm, "The Legend of Zelda - Majora's Mask", R.drawable.ic_zelda_mm,  R.drawable.album_zelda_mm);
        // Pokemon
        } else if (gameName.equals(getString(R.string.pokemon))) {
            // Red Version
            String[] songsPokeRed = {"Opening", "Palette Town Theme", "Professor Oak", "Oak Research Lab", "Rival Appears",
                    "The Road to Viridian City", "Battle (vs Wild Pokémon)", "Victory (vs Wild Pokémon)", "Victory (vs Wild Pokémon)",
                    "Pewter City's Theme", "Pokémon Center", "Pokémon Recovery", "Viridian Forest", "Guidepost", "Battle (vs Trainer)",
                    "Victory (vs Trainer)", "Mt. Moon Cave", "The Road to Cerulean", "Cerulean City's Theme", "Pokémon Gym",
                    "Jiggllypuff's Song", "Vermillion City's Theme", "The Road to Lavender Town", "Pokémon Whistle", "Battle vs Gym Leader",
                    "Victory vs Gym Leader", "Cycling", "Lavender Town's Theme", "Pokémon Tower", "Celadon City", "Casino",
                    "Team Rocket Hideout", "Sylph Company", "Ocean", "Cinnabar Islands Theme", "Pokémon Mansion", "Evolution",
                    "The Final Road", "Last Battle vs Rival", "Into the Palace", "Ending"};
            songInfoAdd(songsPokeRed, "Pokémon - Red Version", R.drawable.ic_poke_red,  R.drawable.album_poke_red);
        } else if (gameName.equals(getString(R.string.mario))) {
            // Odyssey
            String[] songsOdyssey = {"Bonneton", "Fossil Falls", "Fossil Falls (8-Bit)", "Fossil Falls: Dinosaur", "Tostarena: Ruins",
                    "Tostarena: Ruins (8-Bit)", "Tostarena: Night", "Tostarena: Night (8-Bit)", "Tostarena: Town", "Tostarena: Jaxi"};
            songInfoAdd(songsOdyssey, "Super Mario Odyssey", R.drawable.ic_mario_odyssey,  R.drawable.album_mario_odyssey);
        }
        // Create list for each item on screen (recycled view)
        SongInfoAdapter adapter = new SongInfoAdapter(this, songInfo);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
    }

    /**
     *
     * @param songs     array of strings for each song name
     * @param game      string for name of game
     * @param thumbnail id for small image before going to "now playing" activity
     * @param album     id for large album art to be displayed on "now playing"
     */
    private void songInfoAdd(String[] songs, String game, int thumbnail, int album) {
        for (int i = 0; i < songs.length; i++) {
            songInfo.add(new SongInfo(songs[i], game, thumbnail, album));
        }
    }

    // Click event method that specifies what class to return to
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(intent, 0);
        return true;
    }

}
