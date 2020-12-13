package edu.ukma.blog.models.simple_interaction.graph_models;

import edu.ukma.blog.models.compositeIDs.RecordId;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.typeconversion.Convert;
import org.neo4j.ogm.id.UuidStrategy;
import org.neo4j.ogm.typeconversion.UuidStringConverter;

import java.util.UUID;

@NodeEntity
@NoArgsConstructor
@Data
public class RecordGraphEntity {
    @Id
    @Convert(UuidStringConverter.class)
    @GeneratedValue(strategy = UuidStrategy.class)
    private UUID uuid;

    private long publisherId;

    private int recordOwnId;

    public RecordGraphEntity(RecordId id) {
        publisherId = id.getPublisherId();
        recordOwnId = id.getRecordOwnId();
    }
}
