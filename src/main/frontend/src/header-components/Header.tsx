import * as React from "react";
import BlogLogo from "./BlogLogo"
import Publishers from "./Publishers"
import Search from "./Search"
import NewRecord from "./NewRecord"
import MyProfile from "./MyProfile"
import UserAccount from "./UserAccount"
import "./headerStyles.css"

class Header extends React.Component<any, any> {
    render() {
        return (
            <header className={"header-block"}>
                <BlogLogo/>
                <Publishers/>
                <Search/>
                <NewRecord/>
                <MyProfile/>
                <UserAccount/>
            </header>
        );
    }
}

export default Header;