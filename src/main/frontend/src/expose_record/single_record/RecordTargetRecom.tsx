import React from 'react';
import {IAuthProps} from "../../cms_backbone/CMSNavbarRouting";
import axios, {AxiosResponse} from "axios";
import {Image} from "react-bootstrap";
import {Link} from "react-router-dom";

interface IProps {
    auth: IAuthProps,
    publisher: string,
    recordId: number
}

interface IRecordId {
    publisher: string,
    recordOwnId: number
}

interface IState {
    recordIcons: Map<IRecordId, string>
}

class RecordTargetRecom extends React.Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);
        this.state = {
            recordIcons: new Map()
        }
    }

    componentDidMount() {
        axios.get(`http://localhost:8080/recommendations/evaluations/${this.props.publisher}/${this.props.recordId}`,
            {headers: {'Authorization': `Bearer ${this.props.auth.token}`}})
            .then((success: AxiosResponse<Array<IRecordId>>) => success.data.forEach(recId => {
                axios.get(`http://localhost:8080/users/${recId.publisher}/records/${recId.recordOwnId}/image-icon`,
                    {responseType: "arraybuffer", headers: {'Authorization': `Bearer ${this.props.auth.token}`}})
                    .then(imageResp => {
                        const image = Buffer.from(imageResp.data, 'binary').toString('base64');
                        this.setState(oldState => {
                            let updRecIcons = new Map(oldState.recordIcons);
                            updRecIcons.set(recId, image);
                            return {recordIcons: updRecIcons};
                        })
                    }, error => console.log(error));
            }), error => console.log(error));
    }

    render() {
        const recommThumbnails = Array.from(this.state.recordIcons).map(([recId, img]) =>
            <Link to={`/users/${recId.publisher}/records/${recId.recordOwnId}`}
                  onClick={() => window.location.reload()} key={recId.publisher + recId.recordOwnId}>
                <Image src={"data:image/jpeg;base64, " + img} alt={""}/>
            </Link>
        )

        return (
            <div>
                {recommThumbnails}
            </div>
        );
    }
}

export default RecordTargetRecom;