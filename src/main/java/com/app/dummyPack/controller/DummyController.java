package com.app.dummyPack.controller;

import com.app.dummyPack.dto.CompSaveDto;
import com.app.dummyPack.service.dummyservice;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("dummy")
public class DummyController {

    private dummyservice ds;

    public DummyController(dummyservice ds){
        this.ds = ds;
    }

    @PostMapping("/save")
    public void dummy(@RequestBody CompSaveDto compSaveDto){
        ds.saveAuthor(compSaveDto);
    }

}
