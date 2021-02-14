import React from "react";
import Login from "./auth/Login";
import {Route, Switch, withRouter} from "react-router-dom";
import Registration from "./auth/Registration";
import CMSMain from "./cms_backbone/CMSNavbarRouting";
import ProtectedPage from "./auth/ProtectedPage";

class App extends React.Component<any, any> {
    constructor(props: any) {
        super(props);
    }

    render() {
        return (
            <div>
                <Switch>
                    <Route exact path={"/login"} component={Login}/>
                    <Route exact path={"/register"} component={Registration}/>
                    <ProtectedPage path={"/"}>
                        <CMSMain/>
                    </ProtectedPage>
                </Switch>
            </div>
        );
    }

}

export default withRouter(App);
