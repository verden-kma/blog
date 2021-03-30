import React from "react";
import Header from "./Header";
import Footer from "./Footer";
import Digest from "../digest/Digest";
import store from "store"
import {BrowserRouter, Redirect, Route, Switch, withRouter} from "react-router-dom";
import PostRecord from "./PostRecord";
import RecordPreview from "../expose_record/RecordsPreview";
import RecordsPreview, {RecordPreviewContext} from "../expose_record/RecordsPreview";
import {searchModes} from "./Search";
import PublishersPreview, {PublisherPreviewContext} from "../expose_publisher/PublishersPreview";
import FullRecordView from "../expose_record/record_page/FullRecordView";
import PublisherMainPage from "../expose_publisher/full_publisher_page/PublisherMainPage";
import EditUserProfile from "../expose_publisher/user_profile_details/EditUserProfile";
import ChangePassword from "../expose_publisher/user_profile_details/ChangePassword";

interface IAuthProps {
    username: string,
    authType: string,
    token: string
}

class CMSNavbarRouting extends React.Component<any, any> {
    constructor(props: any) {
        super(props);
    }

    render() {
        if (!store.get("isAuthorized")) {
            return <Redirect to={"/login"}/>
        }
        const authData: IAuthProps = {
            username: store.get("username"),
            authType: store.get("authType"),
            token: store.get("token")
        }

        return (
            <div>
                <BrowserRouter>
                    <Header username={authData.username}/>
                    <Switch>
                        <Route exact path={"/digest"}>
                            <Digest {...authData} />
                        </Route>
                        <Route exact path={"/publishers"}>
                            <PublishersPreview {...{
                                auth: authData,
                                previewContext: PublisherPreviewContext.RECOMMENDATION
                            }} />
                        </Route>
                        <Route exact path={"/records"}>
                            <RecordsPreview {...{auth: authData, previewContext: RecordPreviewContext.RECOMMENDATION}}/>
                        </Route>
                        <Route exact path={"/post-record"}>
                            <PostRecord {...authData}/>
                        </Route>
                        <Route exact path={"/profile/:targetUsername"}>
                            <PublisherMainPage auth={authData}/>
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
                        <Route exact path={"/edit-user-details"}>
                            <EditUserProfile {...authData}/>
                        </Route>
                        <Route exact path={"/change-password"}>
                            <ChangePassword  {...authData}/>
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

export type {IAuthProps};
export {monthNames};
export default withRouter(CMSNavbarRouting);