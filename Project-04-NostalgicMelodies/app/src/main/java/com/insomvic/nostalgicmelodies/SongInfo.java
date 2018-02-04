package com.insomvic.nostalgicmelodies;

public class SongInfo {

    private String newSongName;
    private String newGameName;
    private int newThumbnailResourceId;
    private int newAlbumResourceId;

    /**
     *
     * @param SongName name of song
     * @param GameName name of game where song comes from
     */
    public SongInfo(String SongName, String GameName, int thumbnailResourceId, int albumResourceId) {
        newSongName = SongName;
        newGameName = GameName;
        newThumbnailResourceId = thumbnailResourceId;
        newAlbumResourceId = albumResourceId;
    }

    public String getSongName() {
        return newSongName;
    }

    public String getGameName() {
        return newGameName;
    }

    public int getThumbnailResourceId() {
        return newThumbnailResourceId;
    }

    public int getAlbumResourceId() {
        return newAlbumResourceId;
    }

}
