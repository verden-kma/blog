import React from "react";
import {IAuthProps} from "../../cms_backbone/CMSNavbarRouting";
import RecordPreview, {RecordPreviewContext} from "../../expose_record/multiple_records/RecordsPreviewPage";
import axios from "axios";
import UserStats from "../UserStats";
import {withRouter} from "react-router";
import {RouteComponentProps} from "react-router-dom";
import {Container, Row} from "react-bootstrap";

interface IPublisherProps extends RouteComponentProps<any> {
    auth: IAuthProps
}

interface IPublisherState {
    targetUsername: string,
    topBanner?: string,
}

class PublisherMainPage extends React.Component<IPublisherProps, IPublisherState> {
    constructor(props: IPublisherProps) {
        super(props);
        let emptyImgs = new Map();
        emptyImgs.set(0, []);
        this.state = {
            targetUsername: this.props.match.params.targetUsername
        }
    }

    componentDidMount() {
        axios.get(`http://localhost:8080/users/${this.state.targetUsername}/top-banner`, {
            responseType: 'arraybuffer',
            headers: {'Authorization': `Bearer ${this.props.auth.token}`}
        }).then(success => {
            if (success.data) {
                this.setState({topBanner: Buffer.from(success.data, 'binary').toString('base64')})
            }
        }, error => console.log(error));
    }


    render() {
        const screenWidth = window.innerWidth;
        const records = (<div className={"col-sm-8 col-md-7 col-lg-9 mx-3"}>
            <RecordPreview auth={this.props.auth} previewContext={RecordPreviewContext.PUBLISHER_RECORDS}
                           targetUsername={this.state.targetUsername}/>
        </div>);

        const stats = (<div className={"col-sm-8 col-md-4 col-lg-2 d-flex justify-content-center"}>
            <UserStats auth={this.props.auth} targetUsername={this.state.targetUsername}/>
        </div>);

        return (<div>
            <Container fluid>
                <Row>
                    {this.state.topBanner &&
                    <img width={"100%"} src={'data:image/jpeg;base64, ' + this.state.topBanner} alt={'top banner'}/>}
                </Row>
                {/*<Row>*/}
                {
                    screenWidth > 768
                        ? <Row>{records} {stats}</Row>
                        : <Row className={"justify-content-center"}>{stats} {records}</Row>
                }
                {/*</Row>*/}

            </Container>
        </div>)
    }
}

export default withRouter(PublisherMainPage);