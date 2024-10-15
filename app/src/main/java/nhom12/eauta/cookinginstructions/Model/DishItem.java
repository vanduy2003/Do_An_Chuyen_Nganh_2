package nhom12.eauta.cookinginstructions.Model;

public class DishItem {
    private String id;
    private String title;
    private String img;
    private String description;

    public DishItem() {}

    public DishItem(String id, String title, String img) {
        this.id = id;
        this.title = title;
        this.img = img;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
