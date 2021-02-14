import React from "react";
import {monthNames} from "../../cms_backbone/CMSNavbarRouting";
import {IComment} from "./FullRecordView";
import defaultAva from "../../assets/defaultAvatar.png";

function Comment(props: IComment) {
    const date = new Date(props.timestamp);
    const ava = props.commenterAva ? 'data:image/jpeg;base64, ' + props.commenterAva : defaultAva;
    return (<div>
        <img src={ava} alt={`${props.commentator}-ava`}/>
        <span>{props.commentator}</span>
        <span>{date.getDate() + ' ' + monthNames[date.getMonth()] + ", " + date.getFullYear()}</span>
        <p>props.text</p>
    </div>)
}

export default Comment;