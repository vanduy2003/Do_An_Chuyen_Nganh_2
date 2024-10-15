package nhom12.eauta.cookinginstructions.Model;

public class CatagoryItem {
    private String id;     // Sẽ gán từ khóa chính trong Firebase
    private String image;  // Khớp với trường "image" trong Firebase
    private String name;   // Khớp với trường "name" trong Firebase

    public CatagoryItem(String id, String image, String name) {
        this.id = id;
        this.image = image;
        this.name = name;
    }

    public CatagoryItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
