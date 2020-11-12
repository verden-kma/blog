package edu.ukma.blog.controllers.blog_features;

import edu.ukma.blog.models.user.UserEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchCtrl {
    @GetMapping("/all")
    public List<UserEntity> findByKey(@RequestParam String key) {
        throw new NotImplementedException();
    }

    @GetMapping("/publishers")
    public List<UserEntity> findPublishers(@RequestParam String name) {
        throw new NotImplementedException();
    }

    @GetMapping("/records")
    public List<UserEntity> findRecords(@RequestParam String title) {
        throw new NotImplementedException();
    }
}
