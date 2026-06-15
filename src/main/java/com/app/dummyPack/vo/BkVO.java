package com.app.dummyPack.vo;

import jakarta.persistence.*;

@Entity
@Table(name="book_vo", indexes = {
        @Index(name="book_vo_auth_id", columnList = "auth_id")
})
public class BkVO {


    @Column(name="book_id")
    private Integer id;

    @Column(name="book_name")
    private String name;

    @ManyToOne
    @JoinColumn(name="auth_id")
    private AuthVO authVO;

    public AuthVO getauthVO() {
        return authVO;
    }

    public void setauthVO(AuthVO authVO) {
        this.authVO = authVO;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
