package nhom12.eauta.cookinginstructions.Model;

public class VideoCook {
    String id;
    String title;
    private String urlVideo;

    public VideoCook() {
        // Default constructor required for calls to DataSnapshot.getValue(Recipe.class)
    }

    public VideoCook(String id, String title, String urlVideo) {
        this.id = id;
        this.title = title;
        this.urlVideo = urlVideo;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
