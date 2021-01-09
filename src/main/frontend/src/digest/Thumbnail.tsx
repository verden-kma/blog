import React from 'react'
import axios from 'axios'
import {IAuthProps} from "../main/CMSMain";
import {IMiniRecord} from "./Digest";

interface IProps {
    auth: IAuthProps,
    data: IMiniRecord
}

interface IState {
    image?: string
}

class Thumbnail extends React.Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);
        this.state = {}
        this.likeRecord = this.likeRecord.bind(this)
        this.followPublisher = this.followPublisher.bind(this)
    }

    likeRecord() {
        console.log(`http://localhost:8080/users/${this.props.data.publisher}/records/${this.props.data.recordOwnId}/likers`)
        console.log('Authorization ' + `${this.props.auth.authType} ${this.props.auth.token}`)


        axios.put(`http://localhost:8080/users/${this.props.data.publisher}/records/${this.props.data.recordOwnId}/likers`,
            {}, {
                headers: {
                    'Authorization': `${this.props.auth.authType} ${this.props.auth.token}`
                }
            }).then((success) => alert("liked record"),
            (error) => console.log("failed to like"))
    }

    followPublisher() {
        console.log(`http://localhost:8080/users/${this.props.data.publisher}/followers`)

        axios.put(`http://localhost:8080/users/${this.props.data.publisher}/followers`, {}, {
            headers: {
                'Authorization': `${this.props.auth.authType} ${this.props.auth.token}`
            }
        }).then((success) => alert("followed publisher"),
            (error) => console.log("failed to follow"))
    }

    componentDidMount() {
        axios.get(`http://localhost:8080/users/${this.props.data.publisher}/records/${this.props.data.recordOwnId}/image-min`,
            {
                responseType: 'arraybuffer',
                headers: {
                    'Authorization': `${this.props.auth.authType} ${this.props.auth.token}`,
                    'Accept': 'image/jpeg'
                }
            }).then((response) => {
            const imgUrl = Buffer.from(response.data, 'binary').toString('base64')
            this.setState({image: imgUrl})
        }, (error) => console.log(error.response))
    }

    render() {
        return (
            <div>
                <img style={{width: 200, height: 120}}
                     src={'data:image/jpeg;base64, ' + this.state.image}
                     alt={this.props.data.caption}/>
                <br/>
                <h3>{this.props.data.publisher}</h3>
                <h4>{this.props.data.caption}</h4>
                <br/>
                <button onClick={this.likeRecord}>like</button>
                <button onClick={this.followPublisher}>follow publisher</button>
            </div>
        )
    }
}

export default Thumbnail;