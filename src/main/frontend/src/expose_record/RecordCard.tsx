import React from "react";
import {IRecord} from "./RecordsPreviewPage";
import {monthNames} from "../cms_backbone/CMSNavbarRouting";
import {Link} from "react-router-dom";

interface ICardProps extends IRecord {
    image?: string,

    handleEvaluation(id: number, forLike: boolean): void
}

class RecordCard extends React.Component<ICardProps, any> {

    render() {
        const activeStyle = {"font-weight": "bold"};
        const ls = (this.props.reaction !== null && this.props.reaction) ? activeStyle : {};
        const dls = (this.props.reaction !== null && !this.props.reaction) ? activeStyle : {};

        const date: Date = new Date(this.props.timestamp);
        return (<div>
            {this.props.isEdited && <div>edited</div>}
            <h3>{this.props.caption}</h3>
            <h5>{date.getDate() + ' ' + monthNames[date.getMonth()] + ", " + date.getFullYear()}</h5>
            <Link to={`/users/${this.props.publisher}/records/${this.props.id}`}>
                <img width={700} height={300} src={'data:image/jpeg;base64, ' + this.props.image}
                     alt={this.props.caption}/>
            </Link>
            <div>
                <button style={ls}
                        onClick={() => this.props.handleEvaluation(this.props.id, true)}>Like {this.props.likes}
                </button>
                <button style={dls}
                        onClick={() => this.props.handleEvaluation(this.props.id, false)}>Dislike {this.props.dislikes}
                </button>
            </div>
            <h6>Comments: {this.props.numOfComments}</h6>
        </div>)
    }

}

export default RecordCard;