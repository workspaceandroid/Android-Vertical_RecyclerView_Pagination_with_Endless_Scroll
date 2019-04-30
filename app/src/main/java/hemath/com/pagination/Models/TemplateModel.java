package hemath.com.pagination.Models;

public class TemplateModel {

    private String imageurl = "FullImageFile";
    private String title = "Title";

    public TemplateModel(String imageurl,String title) {
        this.imageurl = imageurl;
        this.title = title;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
