import React from 'react';
import {IAuthProps, JUST_NOW, monthNames} from "../../cms_backbone/CMSNavbarRouting";
import {RouteComponentProps, withRouter} from "react-router";
import axios from "axios";
import {IRecord} from "../RecordsPreview";
import Comment from "./Comment";

interface IProps extends IAuthProps, RouteComponentProps<any> {
}

interface IState {
    recordJson?: IRecord,
    image?: string,
    comments: Map<number, Array<IComment>>,
    currCommentPage: number,
    hasMorePages: boolean,
    newCommentText: string
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
            hasMorePages: true,
            newCommentText: ""
        }
        this.handleEvaluation = this.handleEvaluation.bind(this);
        this.loadMoreComments = this.loadMoreComments.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.sendComment = this.sendComment.bind(this);
    }

    handleEvaluation() {

    }

    loadMoreComments() {

    }

    sendComment() {
        const {publisher, recordId} = this.props.match.params;
        axios.post(`http://localhost:8080/users/${publisher}/records/${recordId}/comments`, {
            commenter: this.props.username,
            text: this.state.newCommentText
        }, {headers: {'Authorization': `${this.props.authType} ${this.props.token}`}})
            .then(success => {
                this.setState((oldState: IState) => {
                    let topComments: Array<IComment> | undefined = oldState.comments.get(0);
                    if (topComments === undefined) topComments = [];
                    let updTopComments: Array<IComment> = [...topComments];
                    let newComment = {
                        commentId: success.data,
                        commentator: this.props.username,
                        text: oldState.newCommentText,
                        timestamp: JUST_NOW
                    };
                    // if (has ava) {
                    //     set ava
                    // }
                    updTopComments.unshift(newComment) // add ava
                    let updMap: Map<number, Array<IComment>> = new Map(oldState.comments);
                    updMap.set(0, updTopComments);
                    return {
                        ...oldState,
                        comments: updMap
                    }
                })
            }, error => console.log(error));
    }

    handleChange(event: React.ChangeEvent<HTMLInputElement>) {
        const {name, value} = event.target;
        this.setState((oldState: IState) => {
            return {...oldState, [name]: value}
        });
    }

    componentDidMount() {
        const {publisher, recordId} = this.props.match.params;
        axios.get(`http://localhost:8080/users/${publisher}/records/${recordId}`, {
            headers: {'Authorization': `${this.props.authType} ${this.props.token}`}
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
            headers: {'Authorization': `${this.props.authType} ${this.props.token}`}
        }).then(success => {
            this.setState((oldState: IState) => {
                return {
                    ...oldState,
                    image: Buffer.from(success.data, "binary").toString("base64")
                }
            });
        }, error => console.log(error));

        axios.get(`http://localhost:8080/users/${publisher}/records/${recordId}/comments?block=0`, {
            headers: {'Authorization': `${this.props.authType} ${this.props.token}`}
        }).then(success => {
            this.setState((oldState: IState) => {
                const initialComments: Array<IComment> = success.data.pageItems.map((comment: IComment) => {
                    axios.get(`http://localhost:8080/users/${comment.commentator}/avatar`, {
                        responseType: 'arraybuffer',
                        headers: {'Authorization': `${this.props.authType} ${this.props.token}`}
                    }).then(success => {
                        // todo: avatar might be absent and it is not an error
                        return {
                            ...comment,
                            commentatorAva: Buffer.from(success.data, 'binary').toString('base64')
                        }
                    }, error => {
                        {
                            return <Comment key={comment.commentId} {...{
                                ...comment
                            }} />
                        }
                    });
                });

                let comments: Map<number, Array<IComment>> = new Map();
                comments.set(0, initialComments);
                return {
                    ...oldState,
                    hasMorePages: success.data.isLast,
                    comments: comments
                }
            })
        }, error => console.log(error));
    }

    render() {
        if (!this.state.recordJson) return <div>empty div</div>;
        const date: Date = new Date(this.state.recordJson.timestamp);
        const activeStyle = {"font-weight": "bold"};
        const ls = (this.state.recordJson.reaction !== null && this.state.recordJson.reaction) ? activeStyle : {};
        const dls = (this.state.recordJson.reaction !== null && !this.state.recordJson.reaction) ? activeStyle : {};

        let comments: Array<Comment> = []
        for (let i = 0; i < this.state.currCommentPage; i++) {
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
                <img width={700} height={300} src={'data:image/jpeg;base64, ' + this.state.image}
                     alt={this.state.recordJson.caption + "-image"}/>
                <h3>{this.state.recordJson.caption}</h3>
                <h5>{date.getDate() + ' ' + monthNames[date.getMonth()] + ", " + date.getFullYear()}</h5>
                <p>{this.state.recordJson.adText}</p>
                <hr/>
                <div>
                    {/*// maybe buggy bind*/}
                    <button style={ls}
                            onClick={this.handleEvaluation.bind(this, this.state.recordJson.id, true)}>Like {this.state.recordJson.likes}
                    </button>
                    <button style={dls}
                            onClick={this.handleEvaluation.bind(this, this.state.recordJson.id, false)}>Dislike {this.state.recordJson.dislikes}
                    </button>
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
                </div>
            </div>
        );
    }
}

export type {IComment};
export default withRouter(FullRecordView);