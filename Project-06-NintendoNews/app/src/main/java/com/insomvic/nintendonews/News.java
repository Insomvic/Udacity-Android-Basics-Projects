package com.insomvic.nintendonews;

/**
 * A {@link News} object contains information related to a single news article.
 */
public class News {

    private String mTitle;
    private String mDate;
    private String mDescription;
    private String mImage;
    private String mUrl;

    /**
     * Constructs a new {@link News} object.
     * @param title       is the article title
     * @param date        is the date the article was written
     * @param description is a short summary of the article
     * @param image       is a thumbnail from the article
     * @param url         is the url to the article
     */
    public News(String title, String date, String description, String image, String url) {
        mTitle = title;
        mDate = date;
        mDescription = description;
        mImage = image;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDate;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getImage() {
        return mImage;
    }

    public String getUrl() {
        return mUrl;
    }
}
