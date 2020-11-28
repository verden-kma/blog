package edu.ukma.blog.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LazyContentPage<T> {
    private List<T> pageItems;
    private boolean isLast;
}
