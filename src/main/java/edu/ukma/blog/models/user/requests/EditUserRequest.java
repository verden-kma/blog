package edu.ukma.blog.models.user.requests;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class EditUserRequest {
    @Nullable
    private String status;

    @Nullable
    private String description;
}
