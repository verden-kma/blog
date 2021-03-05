import React from 'react';
import store from "store"
import axios, {AxiosResponse} from "axios";
import {IAuthProps} from "../cms_backbone/CMSNavbarRouting";
import defaultAva from "../assets/defaultAvatar.png";
import handleFollow, {IPublisherFollow} from "../utils/HandleFollow";

interface IProps {
    auth: IAuthProps,
    targetUsername: string
}

interface IState {
    userData?: IUserData,
    userAva: string,
    subsAvas: Map<string, string>
}

interface IUserData {
    username: string,
    status: string,
    description: string,
    uploads: number,
    followers: number,
    likes: number,
    dislikes: number,
    comments: number,
    isFollowed: boolean
}

class UserStats extends React.Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);
        this.state = {
            userAva: store.get("userAva"),
            subsAvas: new Map<string, string>()
        }
        this.handleFollowAction = this.handleFollowAction.bind(this);
    }

    handleFollowAction() {
        if (this.state.userData === undefined) {
            console.log("user data is undefined");
            return;
        }
        const pubFollowData: IPublisherFollow = {
            publisherName: this.state.userData.username,
            followers: this.state.userData.followers,
            isFollowed: this.state.userData.isFollowed
        }

        const updateStateCB = (updPublFlwData: IPublisherFollow) => {
            this.setState((oldState: IState) => {
                if (oldState.userData == undefined) return oldState;
                let updData: IUserData = {...oldState.userData};
                updData.isFollowed = updPublFlwData.isFollowed;
                updData.followers = updPublFlwData.followers;
                return {
                    ...oldState,
                    userData: updData
                }
            })
        }

        handleFollow(pubFollowData, this.props.auth, updateStateCB);
    }

    componentDidMount() {
        axios.get(`http://localhost:8080/users/${this.props.targetUsername}`, {
            headers: {'Authorization': `${this.props.auth.authType} ${this.props.auth.token}`}
        }).then((success: AxiosResponse<IUserData>) => {
            this.setState({userData: success.data})
        }, error => console.log(error));
    }

    render() {
        if (!this.state.userData) return (<div></div>);
        const userAva = this.state.userAva ? 'data:image/jpeg;base64, ' + this.state.userAva : defaultAva;
        const followBtnStyle: Object =
            this.state.userData.isFollowed
                ? {"fontWeight": "bold"} :
                {"fontStyle": "italic"};
        return (
            <div>
                <h3>{this.state.userData.username}</h3>
                <img src={userAva} alt={`${this.props.targetUsername}'s avatar`}/>
                <h5>{this.state.userData.status}</h5>
                <p>{this.state.userData.description}</p>
                <h5>Uploads: {this.state.userData.uploads}</h5>
                <h5>Likes: {this.state.userData.likes}</h5>
                <h5>Dislikes: {this.state.userData.dislikes}</h5>
                <h5>Comments: {this.state.userData.comments}</h5>
                <h5>Followers: {this.state.userData.followers}</h5>
                <br/>
                {this.props.targetUsername !== this.props.auth.username &&
                <button style={followBtnStyle} onClick={this.handleFollowAction}>Follow
                </button>}
            </div>
        );
    }
}

export default UserStats;