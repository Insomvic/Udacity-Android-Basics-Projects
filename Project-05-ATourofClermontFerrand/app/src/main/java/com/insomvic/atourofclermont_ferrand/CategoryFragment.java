package com.insomvic.atourofclermont_ferrand;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Locale;

// This is the actual fragment that manages shown content
public class CategoryFragment extends Fragment implements TextToSpeech.OnInitListener {

    // Audio management
    private TextToSpeech textToSpeech;
    private AudioManager newAudioManager;
    // Get audio state and stop if something overrides it
    private AudioManager.OnAudioFocusChangeListener NewOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                textToSpeech.stop();
            } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                textToSpeech.stop();
            }
        }
    };

    public CategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.description_list, container, false);
        // Create text to speech
        textToSpeech = new TextToSpeech(getActivity(), CategoryFragment.this);
        newAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        // ArrayList of each category
        final ArrayList<PlaceOfInterest> placeOfInterests = new ArrayList<>();
        int categoryColor = R.color.colorPrimary;
        try {
            String category = this.getArguments().getString("Category");
            // Attractions
            if (category.equals(getString(R.string.attractions))) {
                categoryColor = R.color.colorAttractions;
                placeOfInterests.add(new PlaceOfInterest(R.string.vulcania, R.string.vulcania_description, R.drawable.attractions_vulcania));
                placeOfInterests.add(new PlaceOfInterest(R.string.cathedral, R.string.cathedral_description, R.drawable.attractions_cathedral));
                placeOfInterests.add(new PlaceOfInterest(R.string.placedejaude, R.string.placedejaude_description, R.drawable.attractions_placedejaude));
            // Restaurants
            } else if (category.equals(getString(R.string.restaurants))) {
                categoryColor = R.color.colorRestaurants;
                placeOfInterests.add(new PlaceOfInterest(R.string.chatlounge, R.string.chatlounge_description, R.drawable.restaurants_chatlounge));
                placeOfInterests.add(new PlaceOfInterest(R.string.lezinc, R.string.lezinc_description, R.drawable.restaurants_lezinc));
                placeOfInterests.add(new PlaceOfInterest(R.string.chezcecile, R.string.chezcecile_description, R.drawable.restaurants_chezcecile));
            // Parks
            } else if (category.equals(getString(R.string.parks))) {
                categoryColor = R.color.colorParks;
                placeOfInterests.add(new PlaceOfInterest(R.string.lejardinlecoq, R.string.lejardinlecoq_description, R.drawable.parks_lejardinlecoq));
                placeOfInterests.add(new PlaceOfInterest(R.string.leparcdemontjuzet, R.string.leparcdemontjuzet_description, R.drawable.parks_leparcdemontjuzet));
                placeOfInterests.add(new PlaceOfInterest(R.string.placedu1ermai, R.string.placedu1ermai_description, R.drawable.parks_placedu1ermai));
            // Events
            } else if (category.equals(getString(R.string.events))) {
                categoryColor = R.color.colorEvents;
                placeOfInterests.add(new PlaceOfInterest(R.string.festivalducourtmetrage, R.string.festivalducourtmetrage_description, R.drawable.events_festivalducourtmetrage));
                placeOfInterests.add(new PlaceOfInterest(R.string.europavoxfestival, R.string.europavoxfestival_description, R.drawable.events_europavoxfestival));
                placeOfInterests.add(new PlaceOfInterest(R.string.marchedenoel, R.string.marchedenoel_description, R.drawable.events_marchedenoel));
            }
        } catch (NullPointerException e) {
            Log.i("CategoryFragment.java", "Failed to find category");
        }
        // Put arraylist into adapter to display list
        PlaceOfInterestAdapter adapter = new PlaceOfInterestAdapter(getActivity(), placeOfInterests, categoryColor);
        ListView listView = rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);
        // Set onclick listener per list
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Stop previous playing
                textToSpeech.stop();
                // Get instance of click
                PlaceOfInterest placeOfInterest = placeOfInterests.get(position);
                int result = newAudioManager.requestAudioFocus(NewOnAudioFocusChangeListener,
                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    // Start text to speech with name of place of interest
                    String place = getString(placeOfInterest.getNameOfPlaceId());
                    textToSpeech.speak(place, TextToSpeech.QUEUE_FLUSH, null);
                    // Then provide description in text to speech
                    String description = getString(placeOfInterest.getDescriptionOfPlaceId());
                    textToSpeech.speak(description, TextToSpeech.QUEUE_ADD, null);
                }
            }
        });
        return rootView;
    }

    // Init text to speech and log failures
    // Needed for onInitListener for text to speech
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.i("Text to Speech", "Language is not supported");
            }
        } else {
            Log.i("Text to Speech", "Initialization Failed");
        }
    }

    // Re-initialize text to speech on resume
    @Override
    public void onResume() {
        if (textToSpeech != null) {
            onInit(TextToSpeech.SUCCESS);
        } else {
            textToSpeech = new TextToSpeech(getActivity(), CategoryFragment.this);
        }
        super.onResume();
    }

    // Destroy text to speech to free up memory
    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

}
