import React from 'react'
import axios from 'axios'

class Thumbnail extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            ...props.data,
            image: null
        }
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
            </div>
        )
    }
}

export default Thumbnail;