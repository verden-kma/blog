import React from "react";
import axios from "axios";
import store from 'store';
import {Redirect} from 'react-router-dom';
import {isLoggedIn} from './App.js'

class Auth extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            isRegister: "true",
            username: "",
            password: "",
            repeatPassword: "",
            status: null,
            description: null
        }
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    handleChange(event) {
        const {name, value} = event.target
        this.setState({[name]: value})
    }

    handleSubmit(event) {
        event.preventDefault()
        if (this.state.isRegister === "true") {
            if (this.state.password !== this.state.repeatPassword) {
                alert("error: passwords do not match!");
                return;
            }
            axios.post("http://localhost:8080/users", {
                username: this.state.username,
                password: this.state.password,
                status: this.state.status,
                description: this.state.description
            }).then((response) => {
                alert("registration is successful")
            }, (error) => {
                alert("failed to register " + (error.response && error.response.status))
            })
        } else axios.post("http://localhost:8080/users/login", {
            username: this.state.username,
            password: this.state.password
        }).then((response) => {
            console.log(response.data)

            const {history} = this.props;
            store.set('loggedIn', true)
            history.push('/');
        }, (error) => {
            alert("wrong credentials " + (error.response && error.response.status))
        })
    }

    render() {
        if (isLoggedIn()) {
            return <Redirect to="/users"/>;
        }
        return (
            <form onSubmit={this.handleSubmit}>
                Register
                <input type="radio"
                       name="isRegister"
                       checked={this.state.isRegister === "true"}
                       value={true}
                       onChange={this.handleChange}/>
                <br/>
                Log In
                <input type="radio"
                       name="isRegister"
                       checked={this.state.isRegister === "false"}
                       value={false}
                       onChange={this.handleChange}/>
                <br/>
                <input type="text"
                       name="username"
                       placeholder="Username"
                       value={this.state.username}
                       onChange={this.handleChange}/>
                <br/>
                <input type="password"
                       name="password"
                       placeholder="Password"
                       value={this.state.password}
                       onChange={this.handleChange}/>
                <br/>

                {
                    this.state.isRegister === "true" &&
                    <div>
                        <input
                            type="password"
                            name="repeatPassword"
                            placeholder="Repeat password"
                            value={this.state.repeatPassword}
                            onChange={this.handleChange}/>
                        <br/>
                        <input
                            type="text"
                            name="status"
                            placeholder="your status"
                            value={this.state.status}/>
                        <br/>
                        <input
                            type="text"
                            name="description"
                            placeholder="write about yourself"
                            value={this.state.description}/>
                    </div>
                }

                <br/>

                <button>Ok</button>
            </form>
        );
    }
}

export default Auth;