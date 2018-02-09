package com.insomvic.nintendonews;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class NewsAdapter extends ArrayAdapter<News> {

    // Log name for easy debugging
    private static final String LOG_TAG = NewsAdapter.class.getSimpleName();

    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view can be reused
        // Otherwise, if convertView is null, then inflate a new list item layout
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }
        // Get news article at current position
        News currentNews = getItem(position);
        // Title id
        TextView titleView = listItemView.findViewById(R.id.title);
        titleView.setText(currentNews.getTitle());
        // Author id and format
        TextView authorView = listItemView.findViewById(R.id.author);
        String formattedAuthor = formatAuthor(currentNews.getAuthor());
        authorView.setText(formattedAuthor);
        // Date id and format
        TextView dateView = listItemView.findViewById(R.id.date);
        String formattedDate = formatDate(currentNews.getDate());
        dateView.setText(formattedDate);
        // Section id and format
        TextView sectionView = listItemView.findViewById(R.id.section);
        String formattedSection = formatSection(currentNews.getSection());
        sectionView.setText(formattedSection);
        // Description id and format
        TextView descriptionView = listItemView.findViewById(R.id.description);
        String formattedDescription = formatDescription(currentNews.getDescription());
        descriptionView.setText(formattedDescription);
        // Image id and format
        ImageView imageView = listItemView.findViewById(R.id.image);
        imageView.setBackgroundResource(R.drawable.bg_nintendo_news);
        formatImage(currentNews.getThumbnail(), imageView);
        // Return view
        return listItemView;
    }

    // Converts the provided API's date format to a more readable format
    private String formatDate(String date) {
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        DateFormat newDateFormat = new SimpleDateFormat("dd MMM yyyy 'at' HH:mm 'GMT'", Locale.US);
        newDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date formattedDate = simpleDateFormat.parse(date);
            return "Published: " + newDateFormat.format(formattedDate);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Problem parsing the date.", e);
            return null;
        }
    }

    private String formatAuthor(String author) {
        return "By: " + author;
    }

    private String formatSection(String section) {
        return "Section: " + section;
    }

    private String formatDescription(String description) {
        if (description != null) {
            return description;
        } else {
            return  "No context provided...";
        }
    }

    // Display image url onto image view
    // Note: Picasso is much faster and caches efficiently. This should be used in the future
    private void formatImage(Bitmap bitmap, ImageView imageView) {
        try {
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Problem getting the image.", e);
        }
    }


}
