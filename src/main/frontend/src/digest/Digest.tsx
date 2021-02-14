import React from "react";
import axios from 'axios'
import Thumbnail from "./Thumbnail";
import {IAuthProps} from "../cms_backbone/CMSNavbarRouting";

interface IState {
    records: Array<IMiniRecord>
}

interface IMiniRecord {
    publisher: string,
    recordOwnId: number,
    caption: string
}

class Digest extends React.Component<IAuthProps, IState> {
    constructor(props: IAuthProps) {
        super(props);
        this.state = {
            records: []
        }
    }

    componentDidMount() {
        axios.get('http://localhost:8080/digest?page=0', {
            headers: {
                'Authorization': `${this.props.authType} ${this.props.token}`
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
                key={record.publisher + "-" + record.recordOwnId}
                auth={this.props}
                data={record}/>
        )
        return (
            <div>
                {thumbnails}
            </div>
        )
    }
}

export type {IMiniRecord};
export default Digest;