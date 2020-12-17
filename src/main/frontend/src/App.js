import React from "react";
import './App.css';
import {BrowserRouter, Route, Switch} from 'react-router-dom'
import Auth from "./Auth";
import Cms from "./Cms";
import store from "store";


class App extends React.Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div>
                <BrowserRouter>
                    <Switch>
                        <Route path="/login" component={Auth}/>
                        <Route path="/" component={Cms}/>
                    </Switch>
                </BrowserRouter>
            </div>
        )
    }
}

function isLoggedIn() {
    return !!store.get('loggedIn');
}

export {isLoggedIn};
export default App;
