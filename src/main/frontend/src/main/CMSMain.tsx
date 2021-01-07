import React from "react";
import Header from "./Header";
import Footer from "./Footer";
import Digest from "../digest/Digest";
import store from "store"
import {BrowserRouter, Route, Switch, withRouter} from "react-router-dom";
import Publishers from "./Publishers";
import PostRecord from "./PostRecord";
import UserPage from "../user_page/UserPage";

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
                    <Header/>
                    <Switch>
                        <Route path={"/digest"}>
                            <Digest {...authData} />
                        </Route>
                        <Route path={"/publishers"}>
                            <Publishers/>
                        </Route>
                        <Route path={"/post-record"}>
                            <PostRecord {...authData}/>
                        </Route>
                        <Route path={"/profile"}>
                            <UserPage {...authData}/>
                        </Route>
                    </Switch>
                    <Footer/>
                </BrowserRouter>
            </div>
        )

    }

}

export default withRouter(CMSMain);