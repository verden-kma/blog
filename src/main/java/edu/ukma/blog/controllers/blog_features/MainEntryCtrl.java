package edu.ukma.blog.controllers.blog_features;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RestController
@RequestMapping("/")
public class MainEntryCtrl {
    @GetMapping
    public void getMainPageData() {
        throw new NotImplementedException();
    }
}
