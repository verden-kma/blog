package edu.ukma.blog.controllers.actors;

import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@RestController
@RequestMapping("/admins")
public class AdminCtrl {
    @PostMapping
    public void addAdmin() {
        throw new NotImplementedException();
    }

    @PutMapping
    public void updateAdmin() {
        throw new NotImplementedException();
    }

    @DeleteMapping
    public void removeAdmin() {
        throw new NotImplementedException();
    }
}
