import React from "react";
import axios from 'axios'
import RecordCard from "../main/RecordCard";

interface IProps {
    username: string,
    authType: string,
    token: string
}

interface IState {
    recordJsons: Array<IRecord>,
    // recordImgs: Array<string>,
    recordImgs: { [id: number]: string }
    numPages: number,
    currPage: number
}

interface IRecord {
    id: number,
    caption: string,
    adText?: string,
    timestamp: string,
    isEdited: boolean,
    reaction?: boolean,
    likes: number,
    dislikes: number,
    numOfComments: number
}

class UserPage extends React.Component<IProps, IState> {
    constructor(props: IProps) {
        super(props);
        this.state = {
            recordJsons: [],
            recordImgs: {},
            numPages: 0,
            currPage: 0
        }
    }

    componentDidMount() {
        axios.get(`http://localhost:8080/users/${this.props.username}/records?page=${this.state.currPage}`,
            {
                headers: {
                    'Authorization': `${this.props.authType} ${this.props.token}`
                }
            }).then(success => {
                console.log(success.data)
                this.setState((oldState: IState) => {
                    return {
                        ...oldState,
                        numPages: success.data.totalPagesNum,
                        recordJsons: success.data.pageItems
                    }
                });
//todo : handle situation: user loads record, deletes it, loads exact same record with different image
                // (should work as IDs are different)

                this.state.recordJsons.filter(({id}: IRecord) => this.state.recordImgs[id] === undefined)
                    .map(({id}: IRecord) => {
                        axios.get(`http://localhost:8080/users/${this.props.username}/records/${id}/image-min`,
                            {
                                responseType: 'arraybuffer',
                                headers: {
                                    'Authorization': `${this.props.authType} ${this.props.token}`
                                }
                            }).then(response => {
                            this.setState((oldState: IState) => {
                                return {
                                    ...oldState,
                                    recordImgs: {
                                        ...oldState.recordImgs,
                                        [id]: Buffer.from(response.data, 'binary').toString('base64')
                                    }
                                }
                            });
                            console.log("got response image, state\n")
                            console.log(this.state)
                        })
                    })
            },
            error => {
                console.log(error)
            })
    }

    render() {
        const records = this.state.recordJsons.map((r: IRecord) =>
            <RecordCard key={r.id} {...{...r, image: this.state.recordImgs[r.id]}}/>
        )
        return (<div>
            {records}
        </div>)
    }
}

export type {IRecord};
export default UserPage;