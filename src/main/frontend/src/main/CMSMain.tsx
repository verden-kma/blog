import React from "react";
import Header from "./Header";
import Footer from "./Footer";
import Digest from "../digest/Digest";
import store from "store"
import {BrowserRouter, Route, Switch, withRouter} from "react-router-dom";
import Publishers from "./Publishers";
import PostRecord from "./PostRecord";
import RecordPreview, {PreviewContextEnum} from "../RecordsPreview";
import {searchModes} from "./Search";

interface IAuthProps {
    username: string,
    authType: string,
    token: string
}

class CMSMain extends React.Component<any, any> {

    render() {
        const authData = {
            username: store.get("username"),
            authType: store.get("authType"),
            token: store.get("token")
        }
        return (
            <div>
                <BrowserRouter>
                    <Header {...authData}/>
                    <Switch>
                        <Route exact path={"/digest"}>
                            <Digest {...authData} />
                        </Route>
                        <Route exact path={"/publishers"}>
                            <Publishers/>
                        </Route>
                        <Route exact path={"/post-record"}>
                            <PostRecord {...authData}/>
                        </Route>
                        <Route exact path={"/profile"}>
                            <RecordPreview {...{...authData, previewContext: PreviewContextEnum.PUBLISHER_RECORDS}}/>
                        </Route>
                        <Route exact path={`/search/${searchModes[0]}`}>
                            <RecordPreview {...{...authData, previewContext: PreviewContextEnum.SEARCH}}/>
                        </Route>
                        {/*<Route exact path={`/search?mode=${searchModes[1]}&query=:query`}>*/}
                        {/*    <PublishersPreview {...authData}/>*/}
                        {/*</Route>*/}
                    </Switch>
                    <Footer/>
                </BrowserRouter>
            </div>
        )
    }

}

export type {IAuthProps};
export default withRouter(CMSMain);