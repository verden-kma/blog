import React from 'react'
import axios from 'axios'

class Thumbnail extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            ...props.data,
            image: null
        }
        this.likeRecord = this.likeRecord.bind(this)
        this.followPublisher = this.followPublisher.bind(this)
    }

    likeRecord() {
        console.log(`http://localhost:8080/users/${this.state.publisher}/records/${this.state.recordOwnId}/likers`)
        console.log('Authorization ' + `${this.state.authType} ${this.state.token}`)


        axios.put(`http://localhost:8080/users/${this.state.publisher}/records/${this.state.recordOwnId}/likers`,
            {}, {
                headers: {
                    'Authorization': `${this.state.authType} ${this.state.token}`
                }
            }).then((success) => alert("liked record"),
            (error) => console.log("failed to like"))
    }

    followPublisher() {
        console.log(`http://localhost:8080/users/${this.state.publisher}/followers`)

        axios.put(`http://localhost:8080/users/${this.state.publisher}/followers`, {}, {
            headers: {
                'Authorization': `${this.state.authType} ${this.state.token}`
            }
        }).then((success) => alert("followed publisher"),
            (error) => console.log("failed to follow"))
    }

    componentDidMount() {
        axios.get(`http://localhost:8080/users/${this.state.publisher}/records/${this.state.recordOwnId}/image-min`,
            {
                responseType: 'arraybuffer',
                headers: {
                    'Authorization': `${this.state.authType} ${this.state.token}`,
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
                     src={"data:image/jpeg;base64, " + this.state.image}
                     alt={this.state.caption}/>
                <br/>
                <h3>{this.state.publisher}</h3>
                <h4>{this.state.caption}</h4>
                <br/>
                <button onClick={this.likeRecord}>like</button>
                <button onClick={this.followPublisher}>follow publisher</button>
            </div>
        )
    }
}

export default Thumbnail;