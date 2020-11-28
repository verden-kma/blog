package edu.ukma.blog.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EagerContentPage<T> {
    private List<T> pageItems;
    private int totalPagesNum;
}
