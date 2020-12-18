import React from "react";
import axios from 'axios'

class PublisherPreview extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            ...props.data,
            lastRecImgs: []
        }
    }

    async componentDidMount() {
        const imgs64 = await Promise.all(
            this.state.lastRecords.map(recId => {
                return axios.get(`http://localhost:8080/users/${this.state.publisherName}/records/${recId}/image-min`, {
                    responseType: 'arraybuffer',
                    headers: {
                        'Authorization': `${this.state.authType} ${this.state.token}`,
                        'Accept': 'image/jpeg'
                    }
                }).then((response) => {
                    return Buffer.from(response.data, 'binary').toString('base64')
                }, (error) => console.log(error.response))
            })
        )

        this.setState({lastRecImgs: imgs64})
    }

    render() {
        return (
            <div>
                <h3>{this.state.publisherName}</h3>
                <h5>Followers {this.state.followers}</h5>
                <h5>Uploads {this.state.uploads}</h5>
                {this.state.isFollowed && <h5>is followed</h5>}
                <br/>
                {this.state.lastRecImgs.map((imgSrc, index) => {
                    return <img key={index} style={{width: 200, height: 120}}
                                src={"data:image/jpeg;base64," + imgSrc}
                                alt={index}/>
                })}
            </div>
        )
    }
}

export default PublisherPreview;