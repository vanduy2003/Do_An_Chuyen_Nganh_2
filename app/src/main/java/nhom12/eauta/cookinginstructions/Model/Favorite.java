package nhom12.eauta.cookinginstructions.Model;


public class Favorite {
    private String userId; // ID của người dùng
    private String recipeId; // ID của món ăn
    private String recipeTitle; // Tên của món ăn
    private String recipeImageUrl; // URL ảnh của món ăn

    // Constructor mặc định
    public Favorite() {
    }

    // Constructor đầy đủ
    public Favorite(String recipeId, String recipeTitle, String recipeImageUrl) {
        this.recipeId = recipeId;
        this.recipeTitle = recipeTitle;
        this.recipeImageUrl = recipeImageUrl;
    }

    // Getter và Setter cho userId
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter và Setter cho recipeId
    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    // Getter và Setter cho recipeTitle
    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    // Getter và Setter cho recipeImageUrl
    public String getRecipeImageUrl() {
        return recipeImageUrl;
    }

    public void setRecipeImageUrl(String recipeImageUrl) {
        this.recipeImageUrl = recipeImageUrl;
    }
}

