package com.insomvic.atourofclermont_ferrand;

public class PlaceOfInterest {

    private int newNameOfPlaceId;
    private int newDescriptionOfPlaceId;
    private int newImageResourceId = NO_IMAGE_PROVIDED;
    // Constant value that represents no image was provided
    private static final int NO_IMAGE_PROVIDED = -1;

    /**
     *
     * @param nameOfPlaceId         ID for name of place (i.e. cathedral)
     * @param descriptionOfPlaceId  ID for description of place
     * @param imageResourceId       ID for image of place
     */
    public PlaceOfInterest(int nameOfPlaceId, int descriptionOfPlaceId, int imageResourceId) {
        newNameOfPlaceId = nameOfPlaceId;
        newDescriptionOfPlaceId = descriptionOfPlaceId;
        newImageResourceId = imageResourceId;
    }

    public int getNameOfPlaceId() {
        return newNameOfPlaceId;
    }

    public int getDescriptionOfPlaceId() {
        return newDescriptionOfPlaceId;
    }

    public int getImageResourceId() {
        return newImageResourceId;
    }

    // Will help prevent errors if image is forgotten
    public boolean hasImage() {
        return newImageResourceId != NO_IMAGE_PROVIDED;
    }

}