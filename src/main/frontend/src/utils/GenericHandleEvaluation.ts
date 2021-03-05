import axios from "axios";
import {IRecord} from "../expose_record/RecordsPreview";
import {IAuthProps} from "../cms_backbone/CMSNavbarRouting";

function genericHandleEvaluation(record: IRecord, forLike: boolean, auth: IAuthProps,
                                 updateStateCB: (updRec: IRecord) => void) {
    let updRecord: IRecord = {...record};
    if (record.reaction !== null) { // remove target eval
        axios.delete(`http://localhost:8080/users/${record.publisher}/records/${record.id}/${record.reaction ? "likers" : "dislikers"}`, {
            headers: {'Authorization': `${auth.authType} ${auth.token}`}
        }).then(success => {
            record.reaction ? updRecord.likes-- : updRecord.dislikes--;
            updRecord.reaction = null;
            updateStateCB(updRecord);
        }, error => {
            console.log(error);
        })
    }
    if (record.reaction !== forLike) {
        axios.put(`http://localhost:8080/users/${record.publisher}/records/${record.id}/${forLike ? "likers" : "dislikers"}`, {}, {
            headers: {'Authorization': `${auth.authType} ${auth.token}`}
        }).then(success => {
            forLike ? updRecord.likes++ : updRecord.dislikes++;
            updRecord.reaction = forLike;
            updateStateCB(updRecord);
        }, error => console.log(error))
    }

}

export default genericHandleEvaluation;