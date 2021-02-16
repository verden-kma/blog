import axios from "axios";
import {IRecord} from "../expose_record/RecordsPreview";
import {IAuthProps} from "../cms_backbone/CMSNavbarRouting";

function genericHandleEvaluation(record: IRecord, forLike: boolean, auth: IAuthProps,
                                 updateStateCB: (updRec: IRecord) => void) {
    // const record = this.state.recordJsons.find((rec: IRecord) => rec.id === id);
    // if (record === undefined) {
    //     console.log("record to like is undefined, id = " + id)
    //     return;
    // }
    let updRecord: IRecord = {...record};
    if (record.reaction !== null && ((forLike && record.reaction) || (!forLike && !record.reaction))) { // remove target eval
        axios.delete(`http://localhost:8080/users/${record.publisher}/records/${record.id}/${forLike ? "likers" : "dislikers"}`, {
            headers: {'Authorization': `${auth.authType} ${auth.token}`}
        }).then(success => {
            forLike ? updRecord.likes-- : updRecord.dislikes--;
            updRecord.reaction = null;

            updateStateCB(updRecord);
            // this.setState(oldState => {
            //     return {
            //         ...oldState,
            //         recordJsons: [...oldState.recordJsons.filter(rec => rec.id !== record.id), updRecord]
            //     }
            // })
        }, error => {
            console.log(error);
        })
        return;
    }
    if (record.reaction !== null && ((forLike && !record.reaction) || (!forLike && record.reaction))) { // remove opposite eval
        axios.delete(`http://localhost:8080/users/${record.publisher}/records/${record.id}/${forLike ? "dislikers" : "likers"}`, {
            headers: {'Authorization': `${auth.authType} ${auth.token}`}
        }).then(success => {
            forLike ? updRecord.dislikes-- : updRecord.likes--;
        }, error => console.log(error))
    }

    axios.put(`http://localhost:8080/users/${record.publisher}/records/${record.id}/${forLike ? "likers" : "dislikers"}`, {}, {
        headers: {'Authorization': `${auth.authType} ${auth.token}`}
    }).then(success => {
        forLike ? updRecord.likes++ : updRecord.dislikes++;
        updRecord.reaction = forLike;
        updateStateCB(updRecord);
        // this.setState((oldState) => {
        //     return {
        //         ...oldState,
        //         recordJsons: [...oldState.recordJsons.filter(rec => rec.id !== record.id), updRecord]
        //     }
        // })
    }, error => console.log(error))
}

export default genericHandleEvaluation;