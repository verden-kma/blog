package edu.ukma.blog.models.record;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RecordsPage {
    private List<ResponseRecord> pageRecords;
    private int totalNumPages;
}
