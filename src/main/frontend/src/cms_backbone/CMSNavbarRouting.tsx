import React from "react";
import Header from "./Header";
import Footer from "./Footer";
import Digest from "../digest/Digest";
import store from "store"
import {BrowserRouter, Redirect, Route, Switch, withRouter} from "react-router-dom";
import PostRecord from "./PostRecord";
import RecordPreview, {RecordPreviewContext} from "../expose_record/RecordsPreview";
import {searchModes} from "./Search";
import PublishersPreview, {PublisherPreviewContext} from "../expose_publisher/PublishersPreview";
import FullRecordView from "../expose_record/record_page/FullRecordView";

interface IAuthProps {
    username: string,
    authType: string,
    token: string
}

const JUST_NOW: string = "just now";

class CMSNavbarRouting extends React.Component<any, any> {
    constructor(props: any) {
        super(props);
    }

    render() {
        if (!store.get("isAuthorized")) {
            return <Redirect to={"/login"}/>
        }
        const authData = {
            username: store.get("username"),
            authType: store.get("authType"),
            token: store.get("token")
        }
        return (
            <div>
                <BrowserRouter>
                    <Header {...authData} loginCallback={() => this.forceUpdate()}/>
                    <Switch>
                        <Route exact path={"/digest"}>
                            <Digest {...authData} />
                        </Route>
                        <Route exact path={"/publishers"}>
                            recommended publishers are to be implemented
                        </Route>
                        <Route exact path={"/records"}>
                            recommended records are to be implemented
                        </Route>
                        <Route exact path={"/post-record"}>
                            <PostRecord {...authData}/>
                        </Route>
                        <Route exact path={"/profile"}>
                            <RecordPreview {...{
                                auth: authData,
                                previewContext: RecordPreviewContext.PUBLISHER_RECORDS
                            }}/>
                        </Route>
                        <Route exact path={`/search/${searchModes[0]}`}>
                            <RecordPreview {...{auth: authData, previewContext: RecordPreviewContext.SEARCH}}/>
                        </Route>
                        <Route exact path={`/search/${searchModes[1]}`}>
                            <PublishersPreview {...{auth: authData, previewContext: PublisherPreviewContext.SEARCH}}/>
                        </Route>
                        <Route exact path={"/users/:publisher/records/:recordId"}>
                            <FullRecordView {...{auth: authData}}/>
                        </Route>
                    </Switch>
                    <Footer/>
                </BrowserRouter>
            </div>
        )
    }

}


const monthNames = ["January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
];

export {JUST_NOW};
export type {IAuthProps};
export {monthNames};
export default withRouter(CMSNavbarRouting);