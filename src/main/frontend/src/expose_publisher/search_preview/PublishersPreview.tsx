import React from "react"
import {IAuthProps} from "../../cms_backbone/CMSNavbarRouting";
import axios, {AxiosResponse} from "axios";
import {RouteComponentProps, withRouter} from "react-router-dom";
import PublisherCard from "./PublisherCard";
import {IMiniRecord} from "../../digest/Digest";
import handleFollow, {IPublisherFollow} from "../../utils/HandleFollow";

interface IProps extends RouteComponentProps<any> {
    auth: IAuthProps,
    previewContext: PublisherPreviewContext
}

interface IState {
    publisherJsons: Array<IPublisher>,
    publisherAvas: { [publisher: string]: string },
    publisherBanners: { [publisher: string]: string },
    previewRecordCores: { [publisher: string]: Array<IMiniRecord> }
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
            previewRecordCores: {}
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

        const pFlwData: IPublisherFollow = {
            publisherName: p.publisher,
            isFollowed: p.isFollowed,
            followers: p.followers
        }

        const updFlwState = (updPublFlw: IPublisherFollow) => {
            this.setState((oldState: IState) => {
                const publisherIndex: number = oldState.publisherJsons.findIndex(publ => publ.publisher === updPublFlw.publisherName);
                if (publisherIndex == -1) return oldState;
                let updPublishers = [...oldState.publisherJsons];
                let updTarget = {...updPublishers[publisherIndex]};
                updTarget.isFollowed = updPublFlw.isFollowed;
                updTarget.followers = updPublFlw.followers;
                updPublishers[publisherIndex] = updTarget;
                return {
                    ...oldState,
                    publisherJsons: updPublishers
                }
            });
        }

        handleFollow(pFlwData, this.props.auth, updFlwState);

        // axios(
        //     {
        //         method: p.isFollowed ? "delete" : "put",
        //         url: `http://localhost:8080/users/${publisher}/followers`,
        //         headers: {'Authorization': `${this.props.auth.authType} ${this.props.auth.token}`}
        //     }
        // ).then(success => {
        //     let updPubl: IPublisher = {...p};
        //     p.isFollowed ? updPubl.followers-- : updPubl.followers++;
        //     updPubl.isFollowed = !p.isFollowed;
        //     this.setState(oldState => {
        //         return {
        //             ...oldState,
        //             publisherJsons: [...oldState.publisherJsons.filter(p => p.publisher !== publisher), updPubl]
        //         }
        //     });
        // }, error => console.log(error));
    }

    componentDidMount() {
        const url = this.getUrl();

        axios.get(url, {headers: {'Authorization': `${this.props.auth.authType} ${this.props.auth.token}`}})
            .then(success => {
                console.log(success);
                this.setState((oldState) => {
                    return {...oldState, publisherJsons: success.data.pageItems}
                });

                success.data.pageItems.forEach((pd: IPublisher) => {
                    axios.get(`http://localhost:8080/users/${pd.publisher}/avatar`, {
                        responseType: 'arraybuffer',
                        headers: {'Authorization': `${this.props.auth.authType} ${this.props.auth.token}`}
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
                        headers: {'Authorization': `${this.props.auth.authType} ${this.props.auth.token}`}
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
                    axios.get(`http://localhost:8080/users/${pd.publisher}/records/short`, {
                        params: {
                            rids: pd.lastRecords.join(",")
                        },
                        headers: {'Authorization': `${this.props.auth.authType} ${this.props.auth.token}`}
                    }).then((success: AxiosResponse<Array<IMiniRecord>>) => {
                        this.setState((oldState) => {
                            return {
                                ...oldState,
                                previewRecordCores: {
                                    ...oldState.previewRecordCores,
                                    [pd.publisher]: success.data
                                }
                            }
                        })
                    }, error => console.log(error));
                })
            }, error => console.log(error));
    }

    render() {
        const publisherCards = this.state.publisherJsons.map((pd: IPublisher) =>
            <PublisherCard auth={this.props.auth}
                           publisher={pd.publisher}
                           publisherAva={this.state.publisherAvas[pd.publisher]}
                           publisherBanner={this.state.publisherBanners[pd.publisher]}
                           followers={pd.followers}
                           uploads={pd.uploads}
                           isFollowed={pd.isFollowed}
                           lastRecords={this.state.previewRecordCores[pd.publisher]}
                           followCallback={this.handleFollow}/>
        );
        return (<div>
            {publisherCards}
        </div>);
    }
}

export {PublisherPreviewContext};
export default withRouter(PublishersPreview);
