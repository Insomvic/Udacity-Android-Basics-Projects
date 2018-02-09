package com.insomvic.nintendonews;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
        // Title id and format
        TextView titleView = listItemView.findViewById(R.id.title);
        titleView.setText(currentNews.getTitle());
        // Date id and format
        TextView dateView = listItemView.findViewById(R.id.date);
        String formattedDate = formatDate(currentNews.getDate());
        dateView.setText(formattedDate);
        // Description id and format
        TextView descriptionView = listItemView.findViewById(R.id.description);
        descriptionView.setText(currentNews.getDescription());
        // Image id and format
        ImageView imageView = listItemView.findViewById(R.id.image);
        imageView.setBackgroundResource(R.drawable.bg_nintendo_news);
        formatImage(currentNews.getImage(), imageView);

        return listItemView;
    }

    // Converts the provided API's date format to a more readable format
    private String formatDate(String date) {
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        DateFormat newDateFormat = new SimpleDateFormat("dd MMMM yyyy 'at' HH:mm", Locale.US);
        newDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            Date formattedDate = simpleDateFormat.parse(date);
            return newDateFormat.format(formattedDate);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Problem parsing the date.", e);
            return null;
        }
    }

    // Display image url onto image view
    // A third party tool was used known as Picasso
    private void formatImage(String url, ImageView imageView) {
        try {
            Picasso.with(getContext()).load(url).into(imageView);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Problem getting the image.", e);
        }
    }


}
