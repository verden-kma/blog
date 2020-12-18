import React from "react";

class RecommendedRecords extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            ...props.authData,
            records: []
        }
    }

    render() {
        return (
            <div></div>
        )
    }
}

export default RecommendedRecords;