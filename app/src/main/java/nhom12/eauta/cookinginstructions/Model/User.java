package nhom12.eauta.cookinginstructions.Model;


public class User {
    private String username;
    private String sex;
    private String address;
    private String avatar;
    private String birthday;
    private String favoritefood;
    private String phone;
    private String email;
    private String password;

    public User(String username, String password, String email, String sex, String favoritefood, String birthday, String avatar, String address, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.sex = sex;
        this.favoritefood = favoritefood;
        this.birthday = birthday;
        this.avatar = avatar;
        this.address = address;
        this.phone = phone;
    }

    public String getFavoritefood() {
        return favoritefood;
    }

    public void setFavoritefood(String favoritefood) {
        this.favoritefood = favoritefood;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
