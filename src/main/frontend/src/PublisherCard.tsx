import React from 'react';
import defaultAvatar from "./assets/defaultAvatar.png"
import {Link} from "react-router-dom";

interface IProps {
    publisher: string,
    publisherAva?: string,
    publisherBanner?: string,
    followers: number,
    uploads: number,
    isFollowed: boolean,
    lastRecords?: Array<{ id: number, img: string }>,

    followCallback(publisher: string): void
}

class PublisherCard extends React.Component<IProps, any> {
    render() {
        const followButtonStyle: Object = this.props.isFollowed ? {fontStyle: "italic"} : {fontWeight: "bold"}
        const ava = this.props.publisherAva ? "data:image/jpeg;base64, " + this.props.publisherAva : defaultAvatar;
        return (
            <div>
                <img src={ava} alt={`${this.props.publisher}-ava`}/>
                <h3>{this.props.publisher}</h3>
                {this.props.publisherBanner && <img src={"data:image/jpeg;base64, " + this.props.publisherBanner}
                                                    alt={`${this.props.publisherBanner}-banner`}/>}
                {this.props.lastRecords && <div>
                    {this.props.lastRecords[0] &&
                    <Link to={`/users/${this.props.publisher}/records/${this.props.lastRecords[0].id}`}>
                        <img src={"data:image/jpeg;base64, " + this.props.lastRecords[0].img} alt={"failed to load"}/>
                    </Link>}
                    {this.props.lastRecords[1] &&
                    <Link to={`/users/${this.props.publisher}/records/${this.props.lastRecords[1].id}`}>
                        <img src={"data:image/jpeg;base64, " + this.props.lastRecords[1].img} alt={"failed to load"}/>
                    </Link>
                    }
                    {this.props.lastRecords[2] &&
                    <Link to={`/users/${this.props.publisher}/records/${this.props.lastRecords[2].id}`}>
                        <img src={"data:image/jpeg;base64, " + this.props.lastRecords[2].img} alt={"failed to load"}/>
                    </Link>
                    }
                </div>}
                <div>
                    <h5>Uploads: {this.props.uploads}</h5>
                    <h5>Followers: {this.props.followers}</h5>
                </div>
                <button style={followButtonStyle}
                        onClick={this.props.followCallback.bind(this, this.props.publisher)}>
                    {this.props.isFollowed ? "Unfollow" : "Follow"}
                </button>
            </div>
        );
    }
}

export default PublisherCard;