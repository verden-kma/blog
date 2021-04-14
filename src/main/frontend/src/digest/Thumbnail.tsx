import React from 'react'
import axios from 'axios'
import {IAuthProps} from "../cms_backbone/CMSNavbarRouting";
import {IMiniRecord} from "./Digest";
import {Link} from "react-router-dom";

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
    }

    componentDidMount() {
        axios.get(`http://localhost:8080/users/${this.props.data.publisher}/records/${this.props.data.recordOwnId}/image-icon`,
            {
                responseType: 'arraybuffer',
                headers: {
                    'Authorization': `Bearer ${this.props.auth.token}`,
                    'Accept': 'image/jpeg'
                }
            }).then((response) => {
            this.setState({image: Buffer.from(response.data, 'binary').toString('base64')})
        }, (error) => console.log(error.response))
    }

    render() {
        return (
            <div className={"col-sm-6 col-md-3 col-lg-2 p-0"}>
                <Link to={`/users/${this.props.data.publisher}/records/${this.props.data.recordOwnId}`}>
                    {this.state.image &&
                    <img className={"thumbnail-image"}
                         src={"data:image/jpeg;base64, " + this.state.image}
                         alt={`${this.props.data.caption}`}/>}
                </Link>
            </div>
        )
    }
}

export default Thumbnail;