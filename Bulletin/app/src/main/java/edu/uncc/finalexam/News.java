package edu.uncc.finalexam;
/*
File Name: News.java
Full Name of author: Krithika Kasaragod
*/
import java.io.Serializable;

public class News implements Serializable {
    String title, author, published_at, source_name, image, url;

    public News() {
    }

    public News(String title, String author, String published_at, String source_name, String image, String url) {
        this.title = title;
        this.author = author;
        this.published_at = published_at;
        this.source_name = source_name;
        this.image = image;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublished_at() {
        return published_at;
    }

    public void setPublished_at(String published_at) {
        this.published_at = published_at;
    }

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", published_at='" + published_at + '\'' +
                ", source_name='" + source_name + '\'' +
                ", image='" + image + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
