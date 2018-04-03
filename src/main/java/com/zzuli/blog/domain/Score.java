package com.zzuli.blog.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author: 李正浩
 * @Date: 2018/4/3 9:01
 * @Description: 打分
 */
@Entity
public class Score implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long socre;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", nullable = true)
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "blogId", nullable = true)
    private Blog blog;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSocre() {
        return socre;
    }

    public void setSocre(Long socre) {
        this.socre = socre;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    @Override
    public String toString() {
        return "Score{" +
                "id=" + id +
                ", socre=" + socre +
                ", user=" + user +
                ", blog=" + blog +
                '}';
    }
}
