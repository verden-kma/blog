import React from "react"
import {IAuthProps} from "../cms_backbone/CMSNavbarRouting";
import {RouteComponentProps, withRouter} from "react-router-dom";
import axios, {AxiosResponse} from "axios";
import RecordCard from "./RecordCard";
import ReactPaginate from 'react-paginate';
import genericHandleEvaluation from "../utils/GenericHandleEvaluation";

interface IProps extends RouteComponentProps<any> {
    auth: IAuthProps,
    previewContext: RecordPreviewContext,
    targetUsername?: string
}

// user loads lazy page, publisher posts record, user loads 2nd page, gets duplicate, unable get access to the new record
interface IState {
    recordJsons: Array<IRecord>,
    recordImgs: Map<number, string>,
    currPage: number,
    numPages?: number,
    isLast?: boolean
}

interface IRecord {
    publisher: string,
    id: number,
    caption: string,
    adText: string | null,
    timestamp: string,
    isEdited: boolean,
    reaction: boolean | null,
    likes: number,
    dislikes: number,
    numOfComments: number
}


enum RecordPreviewContext {
    PUBLISHER_RECORDS,
    SEARCH,
    RECOMMENDATION
}

interface IEagerRecordsPage {
    pageItems: Array<IRecord>,
    totalPagesNum: number
}

interface ILazyRecordsPage {
    pageItems: Array<IRecord>,
    isLast: boolean
}

class RecordPreview extends React.Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);
        this.state = {
            recordJsons: [],
            recordImgs: new Map(),
            currPage: 0,
            isLast: false
        }
        this.getUrl = this.getUrl.bind(this);
        this.handleNextPage = this.handleNextPage.bind(this);
        this.handleEvaluation = this.handleEvaluation.bind(this);
        this.loadImages = this.loadImages.bind(this);
    }

    getUrl(): string {
        if (this.props.previewContext === RecordPreviewContext.PUBLISHER_RECORDS) {
            return `http://localhost:8080/users/${this.props.targetUsername}/records?page=${this.state.currPage}`;
        }
        if (this.props.previewContext === RecordPreviewContext.SEARCH) {
            const paramValue = new URLSearchParams(this.props.location.search).get("query");
            return `http://localhost:8080/search/records?title=${paramValue}&page=${this.state.currPage}`
        }
        if (this.props.previewContext === RecordPreviewContext.RECOMMENDATION) {
            // todo: implement
        }
        throw "Unknown RecordPreviewContext";
    }

    componentDidMount() {
        if (this.props.previewContext === RecordPreviewContext.PUBLISHER_RECORDS) {
            axios.get(`http://localhost:8080/users/${this.props.targetUsername}/records`, {
                params: {page: 0},
                headers: {'Authorization': `${this.props.auth.authType} ${this.props.auth.token}`}
            }).then((success: AxiosResponse<IEagerRecordsPage>) => {
                const {pageItems, totalPagesNum} = success.data;
                this.setState({recordJsons: pageItems, numPages: totalPagesNum, currPage: totalPagesNum > 0 ? 1 : 0},
                    () => this.loadImages())
            }, error => console.log(error))
        } else this.handleNextPage();
    }

    handleNextPage() {
        if (this.state.isLast) return;
        const url = this.getUrl();
        axios.get(url, {headers: {'Authorization': `${this.props.auth.authType} ${this.props.auth.token}`}})
            .then((success: AxiosResponse<ILazyRecordsPage>) => {
                    this.setState((oldState: IState) => {
                        let updRecs: Array<IRecord>;
                        if (this.props.previewContext === RecordPreviewContext.SEARCH
                            || this.props.previewContext === RecordPreviewContext.RECOMMENDATION) {
                            let recsSet = new Set(oldState.recordJsons);
                            success.data.pageItems.forEach(r => recsSet.add(r));
                            updRecs = Array.from(recsSet);
                        } else updRecs = success.data.pageItems;
                        return {
                            ...oldState,
                            isLast: success.data.isLast,
                            recordJsons: updRecs
                        }
                    }, () => this.loadImages());
//todo : handle situation: user loads record, deletes it, loads exact same record with different image
                    // (should work as IDs are different)
                },
                error => {
                    console.log(error);
                })
    }

    loadImages() {
        // filter is user in case some loading failed, an attempt will be repeated
        this.state.recordJsons.filter(({id}: IRecord) => this.state.recordImgs.get(id) === undefined)
            .forEach(({id}: IRecord) => {
                axios.get(`http://localhost:8080/users/${this.props.auth.username}/records/${id}/image-min`,
                    {
                        responseType: 'arraybuffer',
                        headers: {
                            'Authorization': `${this.props.auth.authType} ${this.props.auth.token}`
                        }
                    }).then(response => {
                    this.setState((oldState: IState) => {
                        let updImgs: Map<number, string> = new Map(oldState.recordImgs);
                        updImgs.set(id, Buffer.from(response.data, 'binary').toString('base64'));
                        return {
                            ...oldState,
                            recordImgs: updImgs
                        }
                    });
                })
            })
    }

    handleEvaluation(id: number, forLike: boolean) {
        console.log("id= " + id)

        const record = this.state.recordJsons.find((rec: IRecord) => rec.id === id);
        if (record === undefined) {
            console.log("record to like is undefined, id = " + id)
            return;
        }

        const handleStateUpdate = (updRecord: IRecord) => {
            this.setState(oldState => {
                const index: number = oldState.recordJsons.findIndex(rec => rec.publisher === updRecord.publisher && rec.id === updRecord.id);
                let updRecJsons: Array<IRecord> = [...oldState.recordJsons];
                updRecJsons[index] = updRecord;
                return {
                    ...oldState,
                    recordJsons: updRecJsons
                }
            })
        }

        genericHandleEvaluation(record, forLike, this.props.auth, handleStateUpdate);
    }

    render() {
        const records = this.state.recordJsons.map((r: IRecord) =>
            <RecordCard key={r.id} {...{
                ...r, image: this.state.recordImgs.get(r.id),
                handleEvaluation: this.handleEvaluation,
            }}/>
        )

        const pagination: any = this.props.previewContext === RecordPreviewContext.RECOMMENDATION
        || this.props.previewContext === RecordPreviewContext.SEARCH ?
            <button onClick={this.handleNextPage}>Load more</button>
            : (this.state.numPages && <ReactPaginate pageCount={this.state.numPages}
                                                     pageRangeDisplayed={3}
                                                     marginPagesDisplayed={2}
                                                     onPageChange={this.handlePageChange}
                // not implemented
                                                     containerClassName={"pagination"}
                                                     activeClassName={"active"}
                                                     breakClassName={"break-me"}/>)

        return (<div>
            {records}
            {pagination}
        </div>)
    }


    // fixme
    handlePageChange(event: { selected: number }) {
        this.setState((oldState) => {
            return {...oldState, currPage: event.selected}
        })
    }
}

export {RecordPreviewContext};
export type {IRecord};
export default withRouter(RecordPreview);