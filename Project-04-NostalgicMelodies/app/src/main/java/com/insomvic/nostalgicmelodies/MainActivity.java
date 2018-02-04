package com.insomvic.nostalgicmelodies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Global variables relating to stat of media player
    public static boolean playing = false;
    public static String game_franchise;
    public static String playing_song_name;
    public static String playing_game_name;
    public static Integer playing_album_cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Category arrays
        String[][] categoriesArray = {{getString(R.string.zelda), String.valueOf(R.drawable.bg_zelda)},
                {getString(R.string.mario), String.valueOf(R.drawable.bg_mario)},
                {getString(R.string.pokemon), String.valueOf(R.drawable.bg_pokemon)}};
        // Create layout and categories
        LinearLayout linearLayout = findViewById(R.id.categories);
        for (int i = 0; i < categoriesArray.length; i++) {
            // Adds relative layout for text and image
            RelativeLayout relativeLayout = new RelativeLayout(new ContextThemeWrapper(MainActivity.this, R.style.CategoryLayout), null, 0);
            linearLayout.addView(relativeLayout);
            // Adds image of category to relative layout
            ImageView imageView = new ImageView(new ContextThemeWrapper(MainActivity.this, R.style.CategoryBackgroundStyle), null, 0);
            imageView.setImageResource(Integer.valueOf(categoriesArray[i][1]));
            // Set height of background (60dp)
            float dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, getResources().getDisplayMetrics());
            imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Math.round(dp)));
            relativeLayout.addView(imageView);
            // Adds title of category to relative layout
            TextView textView = new TextView(new ContextThemeWrapper(MainActivity.this, R.style.CategoryTextStyle), null, 0);
            String gameName = categoriesArray[i][0];
            textView.setText(gameName);
            relativeLayout.addView(textView);
            // Provide onClickListener
            try {
                // Only provide click if class exists (prevents crashing)
                Class className = Class.forName("com.insomvic.nostalgicmelodies.SongListActivity");
                click(relativeLayout, className, gameName);
            } catch(ClassNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // Listener for TextView clicks
    public void click(RelativeLayout relativeLayout, final Class className, final String gameName) {
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                game_franchise = gameName;
                Intent intent = new Intent(view.getContext(), className);
                // Launch new activity
                view.getContext().startActivity(intent);
            }
        });
    }

}
