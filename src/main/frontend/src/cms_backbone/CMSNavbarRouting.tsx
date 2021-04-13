import React from "react";
import Header from "./Header";
import Footer from "./Footer";
import Digest from "../digest/Digest";
import store from "store"
import {BrowserRouter, Redirect, Route, Switch, withRouter} from "react-router-dom";
import PostRecord from "./PostRecord";
import RecordsPreviewPage, {RecordPreviewContext} from "../expose_record/multiple_records/RecordsPreviewPage";
import {searchModes} from "./Search";
import PublishersPreview, {PublisherPreviewContext} from "../expose_publisher/multiple_publishers/PublishersPreview";
import FullRecordView from "../expose_record/single_record/FullRecordView";
import PublisherMainPage from "../expose_publisher/full_publisher_page/PublisherMainPage";
import EditUserProfile from "../expose_publisher/user_profile_details/EditUserProfile";
import ChangePassword from "../expose_publisher/user_profile_details/ChangePassword";
import EditRecord from "../expose_record/single_record/EditRecord";
import "./local-styles.css"
import 'bootstrap/dist/css/bootstrap.css';
import MainPage from "./MainPage";

interface IAuthProps {
    username: string,
    token: string,
    // todo: send role from login filter & expiration for token to set a refresher callback
    permissions?: Array<string>
}

class CMSNavbarRouting extends React.Component<any, any> {

    render() {
        if (!store.get("isAuthorized")) {
            return <Redirect to={"/login"}/>
        }
        const authData: IAuthProps = {
            username: store.get("username"),
            token: store.get("token")
        }

        return (
            <BrowserRouter>
                <div id={"content"}>
                    {/*<div id={"content-wrap"}>*/}
                    <Header username={authData.username}/>
                    <main id={"content-wrap"}>
                        <Switch>
                            <Route exact path={"/"}>
                                <MainPage/>
                            </Route>
                            <Route exact path={"/digest"}>
                                <Digest {...authData} />
                            </Route>
                            <Route exact path={"/publishers"}>
                                <PublishersPreview key={"publisher"} {...{
                                    auth: authData,
                                    previewContext: PublisherPreviewContext.RECOMMENDATION
                                }} />
                            </Route>
                            <Route exact path={"/records"}>
                                <RecordsPreviewPage key={"records"}{...{
                                    auth: authData,
                                    previewContext: RecordPreviewContext.RECOMMENDATION
                                }}/>
                            </Route>
                            <Route exact path={"/post-record"}>
                                <PostRecord {...authData}/>
                            </Route>
                            <Route exact path={"/profile/:targetUsername"}>
                                <PublisherMainPage auth={authData}/>
                            </Route>
                            <Route exact path={`/search/${searchModes[0]}`}>
                                <RecordsPreviewPage key={"search=records"} {...{
                                    auth: authData,
                                    previewContext: RecordPreviewContext.SEARCH
                                }}/>
                            </Route>
                            <Route exact path={`/search/${searchModes[1]}`}>
                                <PublishersPreview key={"search-publisher"} {...{
                                    auth: authData,
                                    previewContext: PublisherPreviewContext.SEARCH
                                }}/>
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
                            <Route exact path={"/users/:publisher/records/:recordId/edit"}>
                                <EditRecord {...{auth: authData}}/>
                            </Route>
                        </Switch>
                    </main>
                    {/*</div>*/}
                    <Footer/>
                </div>
            </BrowserRouter>
        )
    }
}


const monthNames = ["January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
];

export type {IAuthProps};
export {monthNames};
export default withRouter(CMSNavbarRouting);