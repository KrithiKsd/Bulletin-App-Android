package edu.uncc.finalexam;
/*
File Name: SubNews.java
Full Name of author: Krithika Kasaragod
*/
public class SubNews {

    String subId, docId, uId, newsItem, title, author, source, publishedAt, image;

    public SubNews() {
    }

    public SubNews(String subId, String docId, String uId,
                   String newsItem, String title, String author,
                   String source, String publishedAt, String image) {
        this.subId = subId;
        this.docId = docId;
        this.uId = uId;
        this.newsItem = newsItem;
        this.title = title;
        this.author = author;
        this.source = source;
        this.publishedAt = publishedAt;
        this.image = image;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getNewsItem() {
        return newsItem;
    }

    public void setNewsItem(String newsItem) {
        this.newsItem = newsItem;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "SubNews{" +
                "subId='" + subId + '\'' +
                ", docId='" + docId + '\'' +
                ", uId='" + uId + '\'' +
                ", newsItem='" + newsItem + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", source='" + source + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
