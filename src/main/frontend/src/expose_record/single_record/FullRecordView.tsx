import React from 'react';
import {IAuthProps, monthNames} from "../../cms_backbone/CMSNavbarRouting";
import {Redirect, RouteComponentProps, withRouter} from "react-router";
import axios from "axios";
import store from "store2"
import {IRecord} from "../multiple_records/RecordsPreviewPage";
import Comment from "./Comment";
import genericHandleEvaluation from "../../utils/GenericHandleEvaluation";
import UserStats from "../../expose_publisher/UserStats";
import {Link} from "react-router-dom";
import {Button, Modal, ModalBody, ModalFooter, ModalTitle} from "react-bootstrap";
import ModalHeader from "react-bootstrap/ModalHeader";
import RecordTargetRecom from "./RecordTargetRecom";


interface IProps extends RouteComponentProps<any> {
    auth: IAuthProps
}

interface ICommDel {
    targetId: number,
    targetBlock: number
}

interface IState {
    recordJson?: IRecord,
    image?: string,
    comments: Map<number, Array<IComment>>,
    nextCommentPage: number,
    hasMoreCommentPages: boolean,
    newCommentText: string,
    deleteRequested: boolean,
    deleteAccomplished: boolean,

    // deleteCommentRequested : boolean

    commDel?: ICommDel
}

interface IComment {
    commentId: number,
    commentator: string,
    text: string,
    timestamp: string,
    commenterAva?: string
}

class FullRecordView extends React.Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);
        this.state = {
            recordJson: undefined,
            image: undefined,
            comments: new Map(),
            nextCommentPage: 0,
            hasMoreCommentPages: true,
            newCommentText: "",
            deleteRequested: false,
            deleteAccomplished: false,
            // deleteCommentRequested : false
        }
        this.handleEvaluation = this.handleEvaluation.bind(this);
        this.loadNextComments = this.loadNextComments.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.sendComment = this.sendComment.bind(this);
        this.handleDelete = this.handleDelete.bind(this);
        this.handleCommentDeleteRequest = this.handleCommentDeleteRequest.bind(this);
        this.doCommentDelete = this.doCommentDelete.bind(this);
    }


    handleEvaluation(forLike: boolean) {
        if (this.state.recordJson === undefined) {
            console.log("record json data has not yet been loaded");
            return;
        }
        const handleStateUpdate = (updRec: IRecord) => {
            this.setState((oldState) => {
                return {
                    ...oldState,
                    recordJson: updRec
                }
            })
        };

        genericHandleEvaluation(this.state.recordJson, forLike, this.props.auth, handleStateUpdate)
    }

    loadNextComments() {
        if (!this.state.hasMoreCommentPages) return;
        const {publisher, recordId} = this.props.match.params;
        axios.get(`http://localhost:8080/users/${publisher}/records/${recordId}/comments`, {
            headers: {'Authorization': `Bearer ${this.props.auth.token}`},
            params: {block: this.state.nextCommentPage}
        }).then(success => {
            this.setState((oldState: IState) => {
                let comments: Map<number, Array<IComment>> = new Map(oldState.comments);
                comments.set(oldState.nextCommentPage, success.data.pageItems || []);
                return {
                    ...oldState,
                    nextCommentPage: oldState.nextCommentPage + 1,
                    hasMoreCommentPages: !success.data.isLast,
                    comments: comments
                }
            }, () => {
                success.data.pageItems.forEach((comment: IComment, index: number) => {
                    axios.get(`http://localhost:8080/users/${comment.commentator}/avatar`, {
                        responseType: 'arraybuffer',
                        headers: {'Authorization': `Bearer ${this.props.auth.token}`}
                    }).then(success => {
                        if (success.data !== null) {
                            this.setState(oldState => {
                                let comments: Map<number, Array<IComment>> = new Map(oldState.comments);
                                // @ts-ignore
                                comments.get(oldState.nextCommentPage - 1)[index].commenterAva =
                                    Buffer.from(success.data, 'binary').toString('base64');
                                return {
                                    ...oldState,
                                    comments: comments
                                }
                            })
                        }
                    }, error => {
                        console.log(error);
                    });
                });
            });
        }, error => console.log(error));
    }

    sendComment(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault();
        const {publisher, recordId} = this.props.match.params;
        axios.post(`http://localhost:8080/users/${publisher}/records/${recordId}/comments`, {
            text: this.state.newCommentText
        }, {headers: {'Authorization': `Bearer ${this.props.auth.token}`}})
            .then(success => {
                this.setState((oldState: IState) => {
                    let topComments: Array<IComment> = oldState.comments.get(0) || [];
                    let updTopComments: Array<IComment> = [...topComments];
                    let newComment: IComment = {
                        commentId: success.data,
                        commentator: this.props.auth.username,
                        text: oldState.newCommentText,
                        timestamp: new Date().toUTCString(),
                        commenterAva: store.session.get("userAva")
                    };
                    updTopComments.unshift(newComment);
                    let updMap: Map<number, Array<IComment>> = new Map(oldState.comments);
                    updMap.set(0, updTopComments);
                    return {
                        ...oldState,
                        newCommentText: "",
                        comments: updMap
                    }
                });
            }, error => console.log(error));

    }

    handleChange(event: React.ChangeEvent<HTMLInputElement>) {
        const {name, value} = event.target;
        this.setState((oldState: IState) => {
            return {...oldState, [name]: value}
        });
    }

    componentDidMount() {
        console.log("full record did mount")

        const {publisher, recordId} = this.props.match.params;
        axios.get(`http://localhost:8080/users/${publisher}/records/${recordId}`, {
            headers: {'Authorization': `Bearer ${this.props.auth.token}`}
        }).then(success => {
            this.setState((oldState: IState) => {
                return {
                    ...oldState,
                    recordJson: success.data
                }
            });
        }, error => console.log(error));

        axios.get(`http://localhost:8080/users/${publisher}/records/${recordId}/image`, {
            responseType: 'arraybuffer',
            headers: {'Authorization': `Bearer ${this.props.auth.token}`}
        }).then(success => {
            this.setState((oldState: IState) => {
                return {
                    ...oldState,
                    image: Buffer.from(success.data, "binary").toString("base64")
                }
            });
        }, error => console.log(error));
        this.loadNextComments();
    }

    handleDelete() {
        if (this.state.recordJson === undefined) return;
        const {publisher, id} = this.state.recordJson;
        axios.delete(`http://localhost:8080/users/${publisher}/records/${id}`, {
            headers: {'Authorization': `Bearer ${this.props.auth.token}`}
        }).then(success => this.setState({deleteAccomplished: true}), error => alert(error));
    }

    handleCommentDeleteRequest(commId: number, blockNum: number) {
        console.log(`attempt to delete comId ${commId} blockNum ${blockNum}`);
        this.setState({commDel: {targetId: commId, targetBlock: blockNum}});
    }

    doCommentDelete() {
        if (this.state.commDel === undefined) {
            console.log("unexpected state: comment delete data is undefined");
            return;
        }
        if (this.state.recordJson === undefined) {
            console.log("unexpected state: record json is not yet loaded");
            return;
        }
        const {publisher, id} = this.state.recordJson;
        const {targetId, targetBlock} = this.state.commDel;
        axios.delete(`http://localhost:8080/users/${publisher}/records/${id}/comments/${targetId}`, {
            headers: {'Authorization': `Bearer ${this.props.auth.token}`}
        })
            .then(success =>
                this.setState((oldState: IState) => {
                    // @ts-ignore
                    const updCommsBlock = oldState.comments.get(targetBlock).filter(x => x.commentId !== targetId);
                    let updComms = new Map(oldState.comments);
                    updComms.set(targetBlock, updCommsBlock);
                    return {comments: updComms, commDel: undefined};
                }), error => console.log(error));
    }

    render() {
        if (!this.state.recordJson) return <div>empty div</div>;
        if (this.state.deleteAccomplished) return <Redirect to={`/profile/${this.state.recordJson.publisher}`}/>;

        const date: Date = new Date(this.state.recordJson.timestamp);
        const activeStyle = {"font-weight": "bold"};
        const ls = (this.state.recordJson.reaction !== null && this.state.recordJson.reaction) ? activeStyle : {};
        const dls = (this.state.recordJson.reaction !== null && !this.state.recordJson.reaction) ? activeStyle : {};

        let commentComponents: Array<Comment> = [];
        for (let i = 0; i <= this.state.nextCommentPage; i++) {
            if (this.state.comments.get(i) === undefined) {
                console.log("failed to load comments block");
                continue;
            }
            // @ts-ignore
            commentComponents.push(...this.state.comments.get(i).map((cd: IComment) =>
                <Comment key={cd.commentId} comment={cd} deleteCB={cd.commentator === this.props.auth.username
                    ? () => this.handleCommentDeleteRequest(cd.commentId, i)
                    : undefined}/>
            ));
        }

        return (
            <div>
                <Modal show={this.state.deleteRequested} onHide={() => this.setState({deleteRequested: false})}>
                    <ModalHeader closeButton>
                        <ModalTitle>Are you sure you want to delete this record?</ModalTitle>
                    </ModalHeader>
                    <ModalBody>
                        It will also delete all comments, evaluation and so affect your statistics.
                    </ModalBody>
                    <ModalFooter>
                        <Button variant={"danger"} onClick={this.handleDelete}>Yes, delete this record.</Button>
                        <Button variant={"info"} onClick={() => this.setState({deleteRequested: false})}>
                            No, I have changed my mind.
                        </Button>
                    </ModalFooter>
                </Modal>

                <Modal show={this.state.commDel} onHide={() => this.setState({commDel: undefined})}>
                    <ModalHeader closeButton>
                        <ModalTitle>Are you sure you want to delete this comment?</ModalTitle>
                    </ModalHeader>
                    <ModalBody>
                        The consequences of your actions here are irreversible.
                    </ModalBody>
                    <ModalFooter>
                        <Button variant={"danger"} onClick={this.doCommentDelete}>Yes, delete this comment.</Button>
                        <Button variant={"info"} onClick={() => this.setState({commDel: undefined})}>
                            No, I have changed my mind.
                        </Button>
                    </ModalFooter>
                </Modal>

                <UserStats auth={this.props.auth} targetUsername={this.state.recordJson.publisher}/>
                <div>
                    <img width={700} height={300} src={'data:image/jpeg;base64, ' + this.state.image}
                         alt={this.state.recordJson.caption + "-image"}/>
                    <h3>{this.state.recordJson.caption}</h3>
                    <h5>{date.getDate() + ' ' + monthNames[date.getMonth()] + ", " + date.getFullYear()}</h5>
                    <p>{this.state.recordJson.adText}</p>
                    <hr/>
                    <div>
                        <button style={ls}
                                onClick={this.handleEvaluation.bind(this, true)}>Like {this.state.recordJson.likes}
                        </button>
                        <button style={dls} onClick={this.handleEvaluation.bind(this, false)}>
                            Dislike {this.state.recordJson.dislikes}
                        </button>
                    </div>
                    {this.state.recordJson.publisher === this.props.auth.username &&
                    <div>
                        <Link
                            to={`/users/${this.props.match.params.publisher}/records/${this.state.recordJson.id}/edit`}>
                            <button>Edit record</button>
                        </Link>
                        <button onClick={() => this.setState({deleteRequested: true})}>Delete record</button>
                    </div>
                    }
                </div>

                <div>
                    <h3>You may also like these</h3>
                    <RecordTargetRecom auth={this.props.auth}
                                       publisher={this.state.recordJson.publisher}
                                       recordId={this.state.recordJson.id}/>
                </div>

                <h6>Comments: {this.state.recordJson.numOfComments}</h6>
                <br/>
                <form onSubmit={this.sendComment}>
                    <h3>Add your comment</h3>
                    <input type={"text"}
                           placeholder={"My opinion is..."}
                           name={"newCommentText"}
                           value={this.state.newCommentText}
                           onChange={this.handleChange}/>
                    <button>Send</button>
                </form>
                <div>
                    {commentComponents}
                    {this.state.hasMoreCommentPages &&
                    <button onClick={this.loadNextComments}>Load more comments</button>}
                </div>
            </div>
        );
    }
}

export type {IComment};
export default withRouter(FullRecordView);