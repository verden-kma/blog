import React from "react"
import {IAuthProps} from "./main/CMSMain";
import {RouteComponentProps, withRouter} from "react-router-dom";
import axios from "axios";
import RecordCard from "./main/RecordCard";
import ReactPaginate from 'react-paginate';

interface IProps extends RouteComponentProps<any>, IAuthProps {
    previewContext: number
}

interface IState {
    recordJsons: Array<IRecord>,
    recordImgs: { [id: number]: string }
    currPage: number,
    numPages?: number,
    isLast?: boolean
}

interface IRecord {
    publisher: string,
    id: number,
    caption: string,
    adText?: string,
    timestamp: string,
    isEdited: boolean,
    reaction: boolean | null,
    likes: number,
    dislikes: number,
    numOfComments: number
}

const PreviewContextEnum = Object.freeze({
    PUBLISHER_RECORDS: 1,
    SEARCH: 2
});

class RecordPreview extends React.Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);
        this.state = {
            recordJsons: [],
            recordImgs: {},
            currPage: 0
        }
        this.getUrl = this.getUrl.bind(this);
        this.handlePageChange = this.handlePageChange.bind(this);
        this.handleEvaluation = this.handleEvaluation.bind(this);
    }

    getUrl(): string {
        if (this.props.previewContext === PreviewContextEnum.PUBLISHER_RECORDS) {
            return `http://localhost:8080/users/${this.props.username}/records?page=${this.state.currPage}`;
        } else if (this.props.previewContext === PreviewContextEnum.SEARCH) {
            const paramValue = new URLSearchParams(this.props.location.search).get("query");
            return `http://localhost:8080/users/${this.props.username}/records?title=${paramValue}&page=${this.state.currPage}`
        }
        return "/404"; // mock
    }

    componentDidMount() {
        const url = this.getUrl();
        axios.get(url, {
            headers: {'Authorization': `${this.props.authType} ${this.props.token}`}
        }).then(success => {
                console.log(success.data)
                this.setState((oldState: IState) => {
                    return {
                        ...oldState,
                        numPages: success.data.totalPagesNum,
                        isLast: success.data.isLast,
                        recordJsons: success.data.pageItems
                    }
                });
//todo : handle situation: user loads record, deletes it, loads exact same record with different image
                // (should work as IDs are different)
                this.state.recordJsons.filter(({id}: IRecord) => this.state.recordImgs[id] === undefined)
                    .forEach(({id}: IRecord) => {
                        axios.get(`http://localhost:8080/users/${this.props.username}/records/${id}/image-min`,
                            {
                                responseType: 'arraybuffer',
                                headers: {
                                    'Authorization': `${this.props.authType} ${this.props.token}`
                                }
                            }).then(response => {
                            this.setState((oldState: IState) => {
                                return {
                                    ...oldState,
                                    recordImgs: {
                                        ...oldState.recordImgs,
                                        [id]: Buffer.from(response.data, 'binary').toString('base64')
                                    }
                                }
                            });
                        })
                    })
            },
            error => {
                console.log(error)
            })
    }

    handleEvaluation(id: number, forLike: boolean) {
        const record = this.state.recordJsons.find((rec: IRecord) => rec.id === id);
        if (record === undefined) {
            console.log("record to like is undefined, id = " + id)
            return;
        }
        let updRecord = {...record};
        if (record.reaction !== null && ((forLike && record.reaction) || (!forLike && !record.reaction))) { // remove target eval
            axios.delete(`http://localhost:8080/users/${record.publisher}/records/${record.id}/${forLike ? "likers" : "dislikers"}`, {
                headers: {'Authorization': `${this.props.authType} ${this.props.token}`}
            }).then(success => {
                forLike ? updRecord.likes-- : updRecord.dislikes--;
                updRecord.reaction = null;
                this.setState((oldState) => {
                    return {
                        ...oldState,
                        recordJsons: [...oldState.recordJsons.filter(rec => rec.id !== record.id), updRecord]
                    }
                })
                console.log(success);
            }, error => {
                console.log(error);
            })
            return;
        }
        if (record.reaction !== null && ((forLike && !record.reaction) || (!forLike && record.reaction))) { // remove opposite eval
            axios.delete(`http://localhost:8080/users/${record.publisher}/records/${record.id}/${forLike ? "dislikers" : "likers"}`, {
                headers: {'Authorization': `${this.props.authType} ${this.props.token}`}
            }).then(success => {
                forLike ? updRecord.dislikes-- : updRecord.likes--;
                console.log(success);
            }, error => console.log(error))
        }

        axios.put(`http://localhost:8080/users/${record.publisher}/records/${record.id}/${forLike ? "likers" : "dislikers"}`, {}, {
            headers: {'Authorization': `${this.props.authType} ${this.props.token}`}
        }).then(success => {
            forLike ? updRecord.likes++ : updRecord.dislikes++;
            updRecord.reaction = forLike;
            this.setState((oldState) => {
                return {
                    ...oldState,
                    recordJsons: [...oldState.recordJsons.filter(rec => rec.id !== record.id), updRecord]
                }
            })
            console.log(success)
        }, error => console.log(error))
    }

    render() {
        const records = this.state.recordJsons.map((r: IRecord) =>
            <RecordCard key={r.id} {...{
                ...r, image: this.state.recordImgs[r.id],
                handleEvaluation: this.handleEvaluation,
            }}/>
        )
        return (<div>
            {records}
            {this.state.numPages && <ReactPaginate pageCount={this.state.numPages}
                                                   pageRangeDisplayed={3}
                                                   marginPagesDisplayed={2}
                                                   onPageChange={this.handlePageChange}
                // not implemented
                                                   containerClassName={"pagination"}
                                                   activeClassName={"active"}
                                                   breakClassName={"break-me"}/>}
        </div>)
    }

    handlePageChange(event: { selected: number }) {
        this.setState((oldState) => {
            return {...oldState, currPage: event.selected}
        })
    }
}

export {PreviewContextEnum};
export type {IRecord};
export default withRouter(RecordPreview);