import React from "react";
import Header from "./Header";
import Footer from "./Footer";
import Digest from "../digest/Digest";
import store from "store2"
import {BrowserRouter, Redirect, Route, RouteComponentProps, Switch, withRouter} from "react-router-dom";
import PostRecord from "./PostRecord";
import RecordsPreviewPage, {RecordPreviewContext} from "../expose_record/multiple_records/RecordsPreviewPage";
import PublishersPreview, {PublisherPreviewContext} from "../expose_publisher/multiple_publishers/PublishersPreview";
import FullRecordView from "../expose_record/single_record/FullRecordView";
import PublisherMainPage from "../expose_publisher/full_publisher_page/PublisherMainPage";
import EditUserProfile from "../expose_publisher/user_profile_details/EditUserProfile";
import ChangePassword from "../expose_publisher/user_profile_details/ChangePassword";
import EditRecord from "../expose_record/single_record/EditRecord";
import "./local-styles.css"
import 'bootstrap/dist/css/bootstrap.css';
import MainPage from "./MainPage";
import axios from "axios";

interface IAuthProps {
    username: string,
    token: string,
    permissions?: Array<any> // todo: user enum
}

interface IState {
    query: string
}

class CMSNavbarRouting extends React.Component<RouteComponentProps<any>, IState> {
    constructor(props: RouteComponentProps<any>) {
        super(props);
        this.state = {
            query: ""
        }
        setInterval(() => {
            console.log("request refresh")
            axios.get("http://localhost:8080/refresh-token", {
                headers: {'Authorization': `Bearer ${store.session.get('token')}`}
            }).then(success => store.session.set('token', success.data),
                error => {
                    console.log(error);
                    store.session.clearAll();
                })
        }, store.session.get('expiration') / 2);
    }


    render() {
        if (!store.session.get("isAuthorized")) {
            return <Redirect to={"/login"}/>
        }
        const authData: IAuthProps = {
            username: store.session.get("username"),
            token: store.session.get("token")
        }

        return (
            <BrowserRouter>
                <div id={"content"}>
                    <Header username={authData.username}/>
                    <main>
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
                                <RecordsPreviewPage key={"records"} {...{
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
                            <Route exact path={"/search/record"}>
                                <RecordsPreviewPage key={"search-records" + this.state.query}
                                                    {...{
                                                        auth: authData,
                                                        previewContext: RecordPreviewContext.SEARCH
                                                    }}
                                />
                            </Route>
                            <Route exact path={"/search/publisher"}>
                                <PublishersPreview key={"search-publisher" + this.state.query} {...{
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