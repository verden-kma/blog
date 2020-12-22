import React from "react";
import axios from 'axios'
import Thumbnail from "./Thumbnail";

class Digest extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: props.userData.username,
            authType: props.userData.authType,
            token: props.userData.token,
            records: []
        }
    }

    componentDidMount() {
        console.log("digest mounted")
        axios.get('http://localhost:8080/digest?page=0', {
            headers: {
                'Authorization': `${this.state.authType} ${this.state.token}`
            }
        }).then((response) => {
            this.setState({records: response.data.pageItems})
        }, (error) => {
            console.log(error)
        })
    }

    render() {
        const thumbnails = this.state.records.map(record =>
            <Thumbnail
                key={record.publisher + record.recordOwnId}
                data={
                    {
                        ...record,
                        authType: this.state.authType,
                        token: this.state.token
                    }
                }/>
        )

        return (
            <div>
                {thumbnails}
            </div>
        )
    }
}

export default Digest;