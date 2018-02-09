package com.insomvic.nintendonews;

import android.graphics.Bitmap;

/**
 * A {@link News} object contains information related to a single news article.
 */
public class News {

    private String mTitle;
    private String mAuthor;
    private String mDate;
    private String mSection;
    private String mDescription;
    private Bitmap mThumbnail;
    private String mUrl;

    /**
     * Constructs a new {@link News} object.
     * @param title       is the article title
     * @param author      is the article author
     * @param date        is the date the article was written
     * @param section     is the section name of the article (i.e. Games)
     * @param description is a short summary of the article
     * @param thumbnail       is a thumbnail from the article
     * @param url         is the url to the article
     */
    public News(String title, String author, String date, String section, String description, Bitmap thumbnail, String url) {
        mTitle = title;
        mAuthor = author;
        mSection = section;
        mDate = date;
        mDescription = description;
        mThumbnail = thumbnail;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getDate() {
        return mDate;
    }

    public String getSection() {
        return mSection;
    }

    public String getDescription() {
        return mDescription;
    }

    public Bitmap getThumbnail() {
        return mThumbnail;
    }

    public String getUrl() {
        return mUrl;
    }
}
