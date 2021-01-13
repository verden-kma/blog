import React from "react"
import {IAuthProps} from "./main/CMSMain";
import axios from "axios";
import {RouteComponentProps, withRouter} from "react-router-dom";
import PublisherCard from "./PublisherCard";

interface IProps extends IAuthProps, RouteComponentProps<any> {
    previewContext: PublisherPreviewContext
}

interface IState {
    publisherJsons: Array<IPublisher>,
    publisherAvas: { [publisher: string]: string },
    publisherBanners: { [publisher: string]: string },
    previewRecordImgs: { [publisher: string]: Array<{ id: number, img: string }> }
}

interface IPublisher {
    publisher: string,
    followers: number,
    uploads: number,
    isFollowed: boolean,
    lastRecords: Array<number>
}

enum PublisherPreviewContext {
    SEARCH,
    RECOMMENDATION
}

class PublishersPreview extends React.Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);
        this.state = {
            publisherJsons: [],
            publisherAvas: {},
            publisherBanners: {},
            previewRecordImgs: {}
        }
        this.getUrl = this.getUrl.bind(this);
        this.handleFollow = this.handleFollow.bind(this);
    }

    getUrl(): string {
        if (this.props.previewContext === PublisherPreviewContext.SEARCH) {
            const publisherPrefix = new URLSearchParams(this.props.location.search).get("query");
            return `http://localhost:8080/search/publishers?name=${publisherPrefix}&page=0`;
        }
        if (this.props.previewContext === PublisherPreviewContext.RECOMMENDATION) {
            // todo: implement
        }
        return "/404"; // mock
    }

    handleFollow(publisher: string) {
        const p = this.state.publisherJsons.find((p: IPublisher) => p.publisher === publisher);
        if (p === undefined) {
            console.log("publisher in follow target is undefined")
            return;
        }

        axios(
            {
                method: p.isFollowed ? "delete" : "put",
                url: `http://localhost:8080/users/${publisher}/followers`,
                headers: {'Authorization': `${this.props.authType} ${this.props.token}`}
            }
        ).then(success => {
            console.log("follow resp");
            console.log(success);
            let updPubl: IPublisher = {...p};
            p.isFollowed ? updPubl.followers-- : updPubl.followers++;
            updPubl.isFollowed = !p.isFollowed;
            this.setState(oldState => {
                return {
                    ...oldState,
                    publisherJsons: [...oldState.publisherJsons.filter(p => p.publisher !== publisher), updPubl]
                }
            });
        }, error => console.log(error));
    }

    componentDidMount() {
        const url = this.getUrl();

        axios.get(url, {headers: {'Authorization': `${this.props.authType} ${this.props.token}`}})
            .then(success => {
                console.log(success);
                this.setState((oldState) => {
                    return {...oldState, publisherJsons: success.data.pageItems}
                });

                success.data.pageItems.forEach((pd: IPublisher) => {
                    axios.get(`http://localhost:8080/users/${pd.publisher}/avatar`, {
                        responseType: 'arraybuffer',
                        headers: {'Authorization': `${this.props.authType} ${this.props.token}`}
                    }).then(success => {
                        if (success.data.byteLength) {
                            this.setState((oldState) => {
                                return {
                                    ...oldState,
                                    publisherAvas: {
                                        ...oldState.publisherAvas,
                                        [pd.publisher]: Buffer.from(success.data, 'binary').toString('base64')
                                    }
                                }
                            })
                        }
                    }, error => console.log(error));

                    axios.get(`http://localhost:8080/users/${pd.publisher}/top-banner`, {
                        responseType: 'arraybuffer',
                        headers: {'Authorization': `${this.props.authType} ${this.props.token}`}
                    }).then(success => {
                        if (success.data.byteLength) {
                            this.setState((oldState) => {
                                return {
                                    ...oldState,
                                    publisherBanners: {
                                        ...oldState.publisherBanners,
                                        [pd.publisher]: Buffer.from(success.data, 'binary').toString('base64')
                                    }
                                }
                            });
                        }
                    }, error => console.log(error))

                    pd.lastRecords.forEach((recId: number) => {
                        axios.get(`http://localhost:8080/users/${pd.publisher}/records/${recId}/image-icon`, {
                            responseType: 'arraybuffer',
                            headers: {'Authorization': `${this.props.authType} ${this.props.token}`}
                        }).then(success => {
                            console.log("icon")
                            console.log(success)
                            this.setState((oldState) => {
                                let updImgs = oldState.previewRecordImgs[pd.publisher]
                                    ? [...oldState.previewRecordImgs[pd.publisher]] : [];
                                updImgs.push({
                                    id: recId,
                                    img: Buffer.from(success.data, 'binary').toString('base64')
                                });

                                return {
                                    ...oldState,
                                    previewRecordImgs: {
                                        ...oldState.previewRecordImgs,
                                        [pd.publisher]: updImgs
                                    }
                                }
                            })
                        }, error => console.log(error));
                    })
                })
            }, error => console.log(error));
    }

    render() {
        const publisherCards = this.state.publisherJsons.map((pd: IPublisher) =>
            <PublisherCard publisher={pd.publisher}
                           publisherAva={this.state.publisherAvas[pd.publisher]}
                           publisherBanner={this.state.publisherBanners[pd.publisher]}
                           followers={pd.followers}
                           uploads={pd.uploads}
                           isFollowed={pd.isFollowed}
                           lastRecords={this.state.previewRecordImgs[pd.publisher]}
                           followCallback={this.handleFollow}/>
        )
        return (<div>
            publisherCards...
            <br/>
            {publisherCards}
        </div>);
    }
}

export {PublisherPreviewContext};
export default withRouter(PublishersPreview);