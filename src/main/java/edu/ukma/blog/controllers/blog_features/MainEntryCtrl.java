package edu.ukma.blog.controllers.blog_features;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//todo: delete this class
//todo: delete redundant username path variables
@RestController
@RequestMapping("/")
public class MainEntryCtrl {

    @GetMapping
    public void getMainPageData() {
    }
}
