package edu.ukma.blog.controllers.blog_features;

import edu.ukma.blog.models.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RestController
@RequestMapping("/")
public class MainEntryCtrl {
    @GetMapping
    public Page getMainPageData() {
        throw new NotImplementedException();
    }
}
