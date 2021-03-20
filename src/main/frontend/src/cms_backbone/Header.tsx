import React from "react";
import {Link, withRouter} from "react-router-dom";
import Search from "./Search";
import {IAuthProps} from "./CMSNavbarRouting";
import {handleLogOut} from "../auth/Login";
import {RouteComponentProps} from "react-router";

interface IProps extends IAuthProps, RouteComponentProps<any> {
    loginCallback(): void
}

interface IState { // todo: query user data
    userAva: string,
    userPageBanner: string,
    status: string,
    description: string
}

class Header extends React.Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);
    }

    render() {
        return (<div>
            <ul>
                <li><Link to={"/digest"}>{Sprout()}</Link></li>
                <br/>
                <li><Link to={"/publishers"}>{Publishers()}</Link></li>
                <br/>
                <li><Link to={"/records"}>{Records()}</Link></li>
                <br/>
                <li><Search {...this.props} searchCallback={(searchData) => {
                    console.log(searchData)
                }}/></li>
                <br/>
                <li><Link to={"/post-record"}>{NewRecord()}</Link></li>
                <br/>
                <li><Link to={`/profile/${this.props.username}`}>{Profile()}</Link></li>
                <br/>
                <li>
                    <button onClick={() => {
                        handleLogOut();
                        this.props.loginCallback()
                    }}>log out
                    </button>
                </li>
            </ul>
        </div>)
    }
}

function Sprout() {
    return (<div>
        Sprout
    </div>)
}

function Publishers() {
    return (<div>
        Publishers
    </div>)
}

function Records() {
    return (<div>
        Records
    </div>)
}

function NewRecord() {
    return (<div>
        New record
    </div>)
}

function Profile() {
    return (<div>
        Profile
    </div>)
}

export default withRouter(Header);