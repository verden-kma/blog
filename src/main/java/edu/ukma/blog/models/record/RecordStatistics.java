package edu.ukma.blog.models.record;

import lombok.Data;

@Data
//@Entity
public class RecordStatistics {
    int numLikes;
    int numDislikes;
    int numComments; // ... spasybi blin Gulayeva
    // does it make sense to have separate 'statistics' entity?
}
