import React from "react";
import {IAuthProps} from "../../cms_backbone/CMSNavbarRouting";
import RecordPreview, {RecordPreviewContext} from "../../expose_record/RecordsPreview";
import axios from "axios";
import UserStats from "../UserStats";

interface IPublisherProps {
    auth: IAuthProps,
    targetUsername: string
}

interface IPublisherState {
    userAva?: string,
    topBanner?: string,
}

class PublisherMainPage extends React.Component<IPublisherProps, IPublisherState> {
    constructor(props: IPublisherProps) {
        super(props);
        let emptyImgs = new Map();
        emptyImgs.set(0, []);
        this.state = {}
    }

    componentDidMount() {
        axios.get(`http://localhost:8080/users/${this.props.targetUsername}/avatar`, {
            responseType: 'arraybuffer',
            headers: {'Authorization': `${this.props.auth.authType} ${this.props.auth.token}`}
        }).then(success => {
            if (success.data) {
                this.setState({userAva: Buffer.from(success.data, 'binary').toString('base64')})
            }
        }, error => console.log(error));
        axios.get(`http://localhost:8080/users/${this.props.targetUsername}/top-banner`, {
            responseType: 'arraybuffer',
            headers: {'Authorization': `${this.props.auth.authType} ${this.props.auth.token}`}
        }).then(success => {
            if (success.data) {
                this.setState({topBanner: Buffer.from(success.data, 'binary').toString('base64')})
            }
        }, error => console.log(error));
    }


    render() {

        return (<div>
            {this.state.topBanner && <img src={'data:image/jpeg;base64, ' + this.state.topBanner} alt={'top banner'}/>}
            <UserStats auth={this.props.auth} targetUsername={this.props.targetUsername}/>
            <RecordPreview auth={this.props.auth} previewContext={RecordPreviewContext.PUBLISHER_RECORDS}
                           targetUsername={this.props.targetUsername}/>
        </div>)
    }
}

export default PublisherMainPage;