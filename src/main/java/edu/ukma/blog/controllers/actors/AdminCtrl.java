package edu.ukma.blog.controllers.actors;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.web.bind.annotation.*;

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
