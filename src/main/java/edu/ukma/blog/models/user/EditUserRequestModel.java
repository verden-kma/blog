package edu.ukma.blog.models.user;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class EditUserRequestModel {
    // todo: think if it is possible to get the pattern from configuration file
    @Pattern(regexp = "^[\\w\\d@$!%*#?&]{5,25}$")
    private String password;

    private String status;

    private String description;
}
