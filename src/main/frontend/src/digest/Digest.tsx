import React from "react";
import axios from 'axios'
import Thumbnail from "./Thumbnail";

interface IProps {
    username: string,
    authType: string,
    token: string
}

interface IState {
    authType: string,
    token: string
    username: string,
    records: Array<IRecord>
}

interface IRecord {
    publisher: string,
    recordOwnId: number,
    caption: string
}

class Digest extends React.Component<IProps, IState> {
    constructor(props: IProps | Readonly<IProps>) {
        super(props);
        this.state = {
            username: props.username, // was props.userData.username
            authType: props.authType,
            token: props.token,
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