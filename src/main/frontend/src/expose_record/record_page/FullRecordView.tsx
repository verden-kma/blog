import React from 'react';
import {IAuthProps, monthNames} from "../../cms_backbone/CMSNavbarRouting";
import {Redirect, RouteComponentProps, withRouter} from "react-router";
import axios from "axios";
import store from "store"
import {IRecord} from "../RecordsPreviewPage";
import Comment from "./Comment";
import genericHandleEvaluation from "../../utils/GenericHandleEvaluation";
import UserStats from "../../expose_publisher/UserStats";
import {Link} from "react-router-dom";
import {Button, Modal, ModalBody, ModalFooter, ModalTitle} from "react-bootstrap";
import ModalHeader from "react-bootstrap/ModalHeader";


interface IProps extends RouteComponentProps<any> {
    auth: IAuthProps
}

interface IState {
    recordJson?: IRecord,
    image?: string,
    comments: Map<number, Array<IComment>>,
    currCommentPage: number,
    hasMorePages: boolean,
    newCommentText: string,
    deleteRequested: boolean,
    deleteAccomplished: boolean
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
            currCommentPage: 0,
            hasMorePages: false,
            newCommentText: "",
            deleteRequested: false,
            deleteAccomplished: false
        }
        this.handleEvaluation = this.handleEvaluation.bind(this);
        this.loadMoreComments = this.loadMoreComments.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.sendComment = this.sendComment.bind(this);
        this.handleDelete = this.handleDelete.bind(this);
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

    loadMoreComments() {
        // todo: add this feature
        alert("not implemented")
    }

    sendComment(event: React.FormEvent<HTMLFormElement>) {
        event.preventDefault();
        const {publisher, recordId} = this.props.match.params;
        axios.post(`http://localhost:8080/users/${publisher}/records/${recordId}/comments`, {
            commenter: this.props.auth.username,
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
                        commenterAva: store.get("userAva")
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

        axios.get(`http://localhost:8080/users/${publisher}/records/${recordId}/comments?block=0`, {
            headers: {'Authorization': `Bearer ${this.props.auth.token}`}
        }).then(success => {
            this.setState((oldState: IState) => {
                let comments: Map<number, Array<IComment>> = new Map();
                comments.set(0, success.data.pageItems || []);
                return {
                    ...oldState,
                    hasMorePages: success.data.isLast,
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
                                comments.get(0)[index].commenterAva =
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

    handleDelete() {
        if (this.state.recordJson === undefined) return;
        const {publisher, id} = this.state.recordJson;
        axios.delete(`http://localhost:8080/users/${publisher}/records/${id}`, {
            headers: {'Authorization': `Bearer ${this.props.auth.token}`}
        }).then(success => this.setState({deleteAccomplished: true}), error => alert(error));
    }

    render() {
        if (!this.state.recordJson) return <div>empty div</div>;
        if (this.state.deleteAccomplished) return <Redirect to={`/profile/${this.state.recordJson.publisher}`}/>;

        const date: Date = new Date(this.state.recordJson.timestamp);
        const activeStyle = {"font-weight": "bold"};
        const ls = (this.state.recordJson.reaction !== null && this.state.recordJson.reaction) ? activeStyle : {};
        const dls = (this.state.recordJson.reaction !== null && !this.state.recordJson.reaction) ? activeStyle : {};

        let comments: Array<Comment> = [];
        for (let i = 0; i <= this.state.currCommentPage; i++) {
            if (this.state.comments.get(i) === undefined) {
                console.log("failed to load comments block");
                continue;
            }
            // @ts-ignore
            comments.push(...this.state.comments.get(i).map((cd: IComment) =>
                <Comment commentId={cd.commentId}
                         commentator={cd.commentator}
                         text={cd.text}
                         timestamp={cd.timestamp}
                         commenterAva={cd.commenterAva}/>
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
                    {comments}
                    <button>Load more comments</button>
                </div>
            </div>
        );
    }
}

export type {IComment};
export default withRouter(FullRecordView);