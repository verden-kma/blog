import React from "react";
import BlogLogo from "./BlogLogo";
import Publishers from "./Publishers";
import Search from "./Search";
import NewRecord from "./NewRecord";
import MyProfile from "./MyProfile";
import {Link, Route, Switch} from "react-router-dom";
import RecommendedPublishers from "../publishers/RecommendedPublishers";
import Digest from "../digest/Digest";

class Header extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: props.userData.username,
            authType: props.userData.authType,
            token: props.userData.token
        }
    }

    render() {
        return (
            <div>
                <header className={"header-block"}>
                    {/*<BrowserRouter>*/}
                    <Link to={'/digest'}><BlogLogo/></Link>
                    <Link to={'/publishers'}><Publishers/></Link>
                    <Search/>
                    <Link to={'post-record'}><NewRecord/></Link>
                    <Link to={'profile'}><MyProfile/></Link>
                    {/*<UserAccount/> todo: settings*/}


                    {/*</BrowserRouter>*/}
                </header>
                <Switch>
                    <Route path={'/digest'}>
                        <div>
                            <br/>
                            <Digest userData={this.props.userData}/>
                        </div>
                    </Route>
                    <Route path={'/publishers'}>
                        <RecommendedPublishers authData={this.state}/>
                    </Route>
                    <Route path={'post-record'}>

                    </Route>
                    <Route path={'profile'}>

                    </Route>
                </Switch>
            </div>
        );
    }
}

export default Header;