import React from 'react';
import defaultAvatar from "../../assets/defaultAvatar.png"
import {IAuthProps} from "../../cms_backbone/CMSNavbarRouting";
import {IMiniRecord} from "../../digest/Digest";
import Thumbnail from "../../digest/Thumbnail";
import {Link} from "react-router-dom";

interface IProps {
    auth: IAuthProps,
    publisher: string,
    publisherAva?: string,
    publisherBanner?: string,
    followers: number,
    uploads: number,
    isFollowed: boolean,
    lastRecords?: Array<IMiniRecord>,

    followCallback(publisher: string): void
}

class PublisherCard extends React.Component<IProps, any> {
    render() {
        const followButtonStyle: Object = this.props.isFollowed ? {fontStyle: "italic"} : {fontWeight: "bold"}
        const ava = this.props.publisherAva ? "data:image/jpeg;base64, " + this.props.publisherAva : defaultAvatar;
        const recordCards = this.props.lastRecords === undefined ? [] : this.props.lastRecords
            .map((recData: IMiniRecord) => <Thumbnail auth={this.props.auth} data={recData}/>);

        return (
            <div>
                <img src={ava} alt={`${this.props.publisher}-ava`}/>
                <Link to={`/profile/${this.props.publisher}`}><h3>{this.props.publisher}</h3></Link>
                {this.props.publisherBanner && <img src={"data:image/jpeg;base64, " + this.props.publisherBanner}
                                                    alt={`${this.props.publisherBanner}-banner`}/>}
                {this.props.lastRecords && <div>
                    {recordCards}
                </div>}
                <div>
                    <h5>Uploads: {this.props.uploads}</h5>
                    <h5>Followers: {this.props.followers}</h5>
                </div>
                {this.props.auth.username !== this.props.publisher && <button style={followButtonStyle}
                                                                              onClick={this.props.followCallback.bind(this, this.props.publisher)}>
                    {this.props.isFollowed ? "Unfollow" : "Follow"}
                </button>}
            </div>
        );
    }
}

export default PublisherCard;