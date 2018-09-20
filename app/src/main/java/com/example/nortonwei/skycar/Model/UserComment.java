package com.example.nortonwei.skycar.Model;

public class UserComment {
    private int id;
    private String nickname;
    private String headImg;
    private int star;
    private String comment;
    private String createTime;

    public UserComment(int id, String nickname, String headImg, int star, String comment, String createTime) {
        this.id = id;
        this.nickname = nickname;
        this.headImg = headImg;
        this.star = star;
        this.comment = comment;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getHeadImg() {
        return headImg;
    }

    public int getStar() {
        return star;
    }

    public String getComment() {
        return comment;
    }

    public String getCreateTime() {
        return createTime;
    }
}
