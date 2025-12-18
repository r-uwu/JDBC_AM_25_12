package org.example;

import java.util.Map;

public class Member {

    private int id;
    private String regDate;
    private String updateDate;
    private String userId;
    private String userPw;
    private String name;

    public Member(Map<String, Object> row) {
        this.id = (int) row.get("id");
        this.regDate = (String) row.get("regDate");
        this.updateDate = (String) row.get("updateDate");
        this.userId = (String) row.get("loginId");
        this.userPw = (String) row.get("loginPw");
        this.name = (String) row.get("name");
    }

    public int getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPw() {
        return userPw;
    }

    public String getName() {
        return name;
    }

    public String getRegDate() {
        return regDate;
    }
}
