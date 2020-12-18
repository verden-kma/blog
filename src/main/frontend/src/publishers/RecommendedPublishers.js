import React from "react";
import axios from 'axios'
import PublisherPreview from "./PublisherPreview";

class RecommendedPublishers extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            ...props.authData,
            publishers: []
        }
        this.fetchRecomData = this.fetchRecomData.bind(this);
    }

    async fetchRecomData() {
        const recUsers = await axios.get(`http://localhost:8080/recommendations/subscriptions/${this.state.username}?block=0`, {
            headers: {
                'Authorization': `${this.state.authType} ${this.state.token}`
            }
        }).then((resp) => {
            console.log(resp.data)
            return resp.data
        }, (error) => {
            console.log(error.response)
        })

        console.log("recUsers")
        console.log(recUsers)

        const recomUsersData =
            await Promise.all(
                recUsers.map(username => {
                    return axios.get(`http://localhost:8080/users/${username}/short`, {
                        headers: {
                            'Authorization': `${this.state.authType} ${this.state.token}`
                        }
                    }).then((resp) => resp.data,
                        (error) => console.log(error.response))
                })
            )


        console.log("recomUsersData")
        console.log(recomUsersData)

        this.setState({publishers: recomUsersData})
    }

    componentDidMount() {
        this.fetchRecomData()
    }

    render() {
        return (
            <div>
                <h2>Recommended publishers:</h2>
                {
                    this.state.publishers.map(publisher => {
                        console.log("publisher : ")
                        console.log(publisher)
                        return <PublisherPreview key={publisher.publisherName}
                                                 data={{
                                                     ...publisher,
                                                     authType: this.state.authType,
                                                     token: this.state.token
                                                 }}/>
                    })
                }
            </div>
        )
    }
}

export default RecommendedPublishers;