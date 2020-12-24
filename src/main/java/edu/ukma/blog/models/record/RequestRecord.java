package edu.ukma.blog.models.record;

import edu.ukma.blog.constants.ValidationConstants;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Size;


@Data
public class RequestRecord {
    @Size(min = ValidationConstants.MIN_CAPTION_LEN, max = ValidationConstants.MAX_CAPTION_LEN)
    private String caption;

    @Nullable
    private String adText;
}
