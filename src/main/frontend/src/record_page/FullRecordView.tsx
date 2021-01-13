import React from 'react';
import {IAuthProps} from "../main/CMSMain";
import {RouteComponentProps, withRouter} from "react-router";

interface IProps extends IAuthProps, RouteComponentProps<any> {

}

class FullRecordView extends React.Component<IProps, any> {
    constructor(props: IProps) {
        super(props);
    }

    render() {
        console.log("this.props.match.params")
        console.log(this.props.match.params)
        return (
            <div>
                todo
            </div>
        );
    }
}

export default withRouter(FullRecordView);