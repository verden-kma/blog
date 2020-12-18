import React from "react";
import axios from 'axios'
import Thumbnail from "../digest/Thumbnail";

class RecommendedRecords extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            ...props.authData,
            records: []
        }
    }

    componentDidMount() {
        axios.get(`http://localhost:8080/recommendations/evaluations/${this.state.username}?block=0`, {
            headers: {
                'Authorization': `${this.state.authType} ${this.state.token}`
            }
        }).then((response) => {
            this.setState({records: response.data})
        }, (error) => {
            console.log(error)
        })

        //     .then((success) => success.data.map((recordIdData) => {
        //     const recordData = axios.get(`http://localhost:8080/users/${recordIdData.publisher}/records/${recordIdData.recordOwnId}`, {
        //         headers : {
        //             'Authorization' : `${this.state.authType} ${this.state.token}`
        //         }
        //     })
        //     // return {
        //     //     ...record,
        //     //     image : null
        //     // }
        // }), (error) => console.log(error.response))
        //
        // this.setState({records : recomRecs})
    }

    render() {
        // console.log("this.state.records")
        // console.log(this.state.records)

        const thumbnails = this.state.records.map(record =>
            <Thumbnail
                key={record.publisher + record.recordOwnId}
                data={
                    {
                        ...record,
                        authType: this.state.authType,
                        token: this.state.token
                    }
                }/>
        )

        return (
            <div>
                {thumbnails}
            </div>
        )
    }
}

export default RecommendedRecords;