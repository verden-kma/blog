import React from "react";
import {IRecord} from "../user_page/UserPage";

interface ICard extends IRecord {
    image: string
}

const monthNames = ["January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
];

function RecordCard(props: ICard) {
    console.log("RecordCard props")
    console.log(props)
    const date: Date = new Date(props.timestamp);
    return (<div>
        {props.isEdited && <div>edited</div>}
        <h3>{props.caption}</h3>
        <h5>{date.getDate() + ' ' + monthNames[date.getMonth()] + ", " + date.getFullYear()}</h5>
        <img src={'data:image/jpeg;base64, ' + props.image} alt={props.caption}/>
        <div>
            <button>Like {props.likes}</button>
            <button>Dislike {props.dislikes}</button>
        </div>
        <h6>Comments: {props.numOfComments}</h6>
    </div>)
}

export default RecordCard;