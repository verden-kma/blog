import React from "react";
import Login from "./auth/Login";
import {Route, Switch, withRouter} from "react-router-dom";
import Registration from "./auth/Registration";
import CMSMain from "./main/CMSMain";

class App extends React.Component<any, any> {
    constructor(props: any) {
        super(props);
        this.props.history.push("/login")
    }

    render() {
        return (
            <div>
                <Switch>
                    <Route exact path={"/login"} component={Login}/>
                    <Route exact path={"/register"} component={Registration}/>
                    <Route exact path={"/"} component={CMSMain}/>


                    {/*<Route path={"/digest"}>*/}
                    {/*    something*/}
                    {/*    /!*<Digest*!/*/}
                    {/*    /!*    username = {store.get("username")}*!/*/}
                    {/*    /!*    authType = {store.get("authType")}*!/*/}
                    {/*    /!*    token= {store.get("token")} />*!/*/}
                    {/*</Route>*/}
                </Switch>
            </div>
        );
    }

}

export default withRouter(App);
