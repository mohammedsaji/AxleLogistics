package com.app.dummyPack.vo;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="auth_vo", indexes = {
        @Index(name="auth_vo_name", columnList = "auth_name")
})
public class AuthVO {

    @Column(name="auth_id")
    private Integer id;

    @Column(name="auth_name")
    private String name;

    @OneToMany(mappedBy = "authorVO", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BkVO> bkVOList = new ArrayList<>();

    public List<BkVO> getBkVOList() {
        return bkVOList;
    }

    public void setBkVOList(BkVO bkVO) {
        bkVOList.add(bkVO);
        bkVO.setauthVO(this);
    }

    public void removeBkVO(BkVO bkVO){
        bkVOList.remove(bkVO);
        bkVO.setauthVO(null);
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
