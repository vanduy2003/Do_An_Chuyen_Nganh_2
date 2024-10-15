package nhom12.eauta.cookinginstructions.Model;

public class Step {
    private int stepNumber;
    private String description;
    private String imageUrl;

    public Step() {
        // Default constructor required for calls to DataSnapshot.getValue(Step.class)
    }

    public Step(int stepNumber, String description, String imageUrl) {
        this.stepNumber = stepNumber;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
