package org.example;


import java.util.Map;

public class Article {
    private int id;
    private String writer;
    private int writerId;
    private String title;
    private String body;
    private String regDate;
    private String updateDate;

/*
    public Article(int id, String writer, String regDate, String updateDate, String title, String body) {
        this.id = id;
        this.writer = writer;
        this.regDate = regDate;
        this.updateDate = updateDate;
        this.title = title;
        this.body = body;
    }

 */

    public Article(Map<String, Object> articleMap) {
        this.id = (int) articleMap.get("id");
        this.writer = (String) articleMap.get("writer");
        this.writerId = (int) articleMap.get("writerId");
        this.title = (String) articleMap.get("title");
        this.body = (String) articleMap.get("body");
        this.regDate = (String) articleMap.get("regDate");
        this.updateDate = (String) articleMap.get("updateDate");
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWriter() {
        return writer;
    }

    public int getWriterId() {
        return writerId;
    }

    public void setWriter(String writer) {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                "writer=" + writer +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}