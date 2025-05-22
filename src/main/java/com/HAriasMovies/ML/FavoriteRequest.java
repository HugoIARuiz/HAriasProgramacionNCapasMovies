
package com.HAriasMovies.ML;


public class FavoriteRequest {
    private String media_type = "movie";
    private Long media_id;
    private boolean favorite;

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public Long getMedia_id() {
        return media_id;
    }

    public void setMedia_id(Long media_id) {
        this.media_id = media_id;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public FavoriteRequest() {
    }

    public FavoriteRequest(Long media_id, boolean favorite) {
        this.media_id = media_id;
        this.favorite = favorite;
    }
    
     
    
}
