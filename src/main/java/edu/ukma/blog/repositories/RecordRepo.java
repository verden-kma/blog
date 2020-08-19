package edu.ukma.blog.repositories;

import edu.ukma.blog.models.Record;
import edu.ukma.blog.models.compositeIDs.RecordID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepo extends JpaRepository<Record, RecordID> {
}
