package nhom12.eauta.cookinginstructions.Model;

public class HandBook {
    private String id;
    String img;

    public HandBook() {
    }

    public HandBook(String id, String img) {
        this.id = id;
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }
}
