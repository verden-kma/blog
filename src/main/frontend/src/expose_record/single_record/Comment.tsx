import React from "react";
import {monthNames} from "../../cms_backbone/CMSNavbarRouting";
import {IComment} from "./FullRecordView";
import defaultAva from "../../assets/defaultAvatar.png";

interface IProps {
    comment: IComment;
    deleteCB?: () => void;
}

function Comment(props: IProps) {
    const date = new Date(props.comment.timestamp);
    const ava = props.comment.commenterAva ? 'data:image/jpeg;base64, ' + props.comment.commenterAva : defaultAva;
    return (<div>
        <img src={ava} alt={`${props.comment.commentator}-ava`}/>
        <span>{props.comment.commentator}</span>
        <span>Published: {date.getDate() + ' ' + monthNames[date.getMonth()] + ", " + date.getFullYear()}</span>
        {props.deleteCB && <button onClick={props.deleteCB}>delete</button>}
        <p>{props.comment.text}</p>
    </div>)
}

export default Comment;