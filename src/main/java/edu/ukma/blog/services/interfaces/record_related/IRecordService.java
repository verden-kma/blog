package edu.ukma.blog.services.interfaces.record_related;

import edu.ukma.blog.exceptions.server_internal.ServerCriticalError;
import edu.ukma.blog.exceptions.server_internal.WrongFileFormatException;
import edu.ukma.blog.models.composite_id.RecordId;
import edu.ukma.blog.models.record.MinResponseRecord;
import edu.ukma.blog.models.record.RecordEntity;
import edu.ukma.blog.models.record.RequestRecord;
import edu.ukma.blog.models.record.ResponseRecord;
import edu.ukma.blog.utils.EagerContentPage;
import edu.ukma.blog.utils.LazyContentPage;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

// works with textual data only
public interface IRecordService {
    int addRecord(long userId, RequestRecord record, MultipartFile image)
            throws ServerCriticalError, WrongFileFormatException;

    EagerContentPage<ResponseRecord> getRecordsPage(long publisherId, long userId, Pageable pageable);

    /**
     * @param recordsChunks lists of RecordEntities posted by the same publisher
     * @param userId        user who is querying the records and who may have evaluated them
     * @return list of <code>ResponseRecord</code> for each record
     */
    List<ResponseRecord> buildRespRecs(Collection<List<RecordEntity>> recordsChunks, long userId);

    LazyContentPage<MinResponseRecord> getMinResponsePage(Pageable pageable);

    ResponseRecord getRecordCore(RecordId id, long userId);

    String getImgLocation(RecordId id);

    void editRecord(RecordId id, RequestRecord editRequest);

    void removeRecord(RecordId id);

    List<MinResponseRecord> getSelectedMinResponse(String publisher, List<Integer> rids);
}
