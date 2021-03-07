import axios from "axios";
import {IRecord} from "../expose_record/RecordsPreview";
import {IAuthProps} from "../cms_backbone/CMSNavbarRouting";

function genericHandleEvaluation(record: IRecord, forLike: boolean, auth: IAuthProps,
                                 updateStateCB: (updRec: IRecord) => void) {
    let updRecord: IRecord = {...record};
    if (record.reaction === forLike) {
        axios.delete(`http://localhost:8080/users/${record.publisher}/records/${record.id}/${record.reaction ? "likers" : "dislikers"}`, {
            headers: {'Authorization': `${auth.authType} ${auth.token}`}
        }).then(success => {
            forLike ? updRecord.likes-- : updRecord.dislikes--;
            updRecord.reaction = null;
            updateStateCB(updRecord);
        }, error => {
            console.log(error);
        })
    } else {
        axios.put(`http://localhost:8080/users/${record.publisher}/records/${record.id}/${forLike ? "likers" : "dislikers"}`, {}, {
            headers: {'Authorization': `${auth.authType} ${auth.token}`}
        }).then(success => {
            if ((record.reaction === true || record.reaction === false) && record.reaction !== forLike)
                record.reaction ? updRecord.likes-- : updRecord.dislikes--;
            forLike ? updRecord.likes++ : updRecord.dislikes++;
            updRecord.reaction = forLike;
            updateStateCB(updRecord);
        }, error => console.log(error))
    }
}

export default genericHandleEvaluation;