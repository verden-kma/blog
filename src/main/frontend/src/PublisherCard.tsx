import React from 'react';
import defaultAvatar from "./assets/defaultAvatar.png"

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

// todo: links
class PublisherCard extends React.Component<IProps, any> {
    render() {
        const followButtonStyle: Object = this.props.isFollowed ? {"font-weight": "bold"} : {}
        const ava = this.props.publisherAva ? "data:image/jpeg;base64, " + this.props.publisherAva : defaultAvatar;
        return (
            <div>
                <img src={ava} alt={`${this.props.publisher}-ava`}/>
                <h3>{this.props.publisher}</h3>
                {this.props.publisherBanner && <img src={"data:image/jpeg;base64, " + this.props.publisherBanner}
                                                    alt={`${this.props.publisherBanner}-banner`}/>}
                {this.props.lastRecords && <div>
                    {this.props.lastRecords[0] &&
                    <img src={"data:image/jpeg;base64, " + this.props.lastRecords[0].img} alt={"failed to load"}/>}
                    {this.props.lastRecords[1] &&
                    <img src={"data:image/jpeg;base64, " + this.props.lastRecords[1].img} alt={"failed to load"}/>}
                    {this.props.lastRecords[2] &&
                    <img src={"data:image/jpeg;base64, " + this.props.lastRecords[2].img} alt={"failed to load"}/>}
                </div>}
                <div>
                    <h5>Uploads: {this.props.uploads}</h5>
                    <h5>Followers: {this.props.followers}</h5>
                </div>
                <button style={followButtonStyle}
                        onClick={this.props.followCallback.bind(this, this.props.publisher)}>
                    Follow
                </button>
            </div>
        );
    }
}

export default PublisherCard;