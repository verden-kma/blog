import React from "react";
import {Redirect, Route} from "react-router-dom";
import store from "store";

const ProtectedPage = ({children, ...rest}: any) => {
    return <Route {...rest}
                  render={({location}) => store.get("isAuthorized")
                      ? children
                      : <Redirect to={{pathname: "/login", state: {from: location}}}/>
                  }
    />
}

export default ProtectedPage;