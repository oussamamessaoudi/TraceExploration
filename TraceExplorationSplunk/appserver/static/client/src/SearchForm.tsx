import React, {useState} from "react";
import {performSearch} from "./SearchUtils";

export function SearchForm(props: any) {
    const [application, setApplication] = useState<string>("")
    const [traceId, setTraceId] = useState<string>("")
    return (
        <div className="container-fluid m-2">
            <form className="form">
                <div className="row">
                    <div className="col">
                        <label htmlFor="application" className="sr-only">Application</label>
                        <input type="text"
                               className="form-control"
                               id="application"
                               placeholder="Application"
                               onChange={(event) => setApplication(event.target.value)}
                               value={application}/>
                    </div>
                    <div className="col">
                        <label htmlFor="traceID" className="sr-only">Trace ID</label>
                        <input type="text"
                               className="form-control"
                               id="traceID"
                               placeholder="Trace ID"
                               onChange={(event) => setTraceId(event.target.value)}
                               value={traceId}/>
                    </div>
                    <div className="col">
                        <button type="submit" disabled={props.state === "LOADING"} className="btn btn-primary"
                                onClick={async (event) => {
                                    event.preventDefault();
                                    props.setState("LOADING")
                                    await performSearch("656b71c0eec96801a5d88396fd41e22a")
                                        .then(value => props.updateData(value))
                                    props.setState("SUCCESS")
                                }
                                }
                        >Submit
                        </button>
                    </div>
                </div>
            </form>
        </div>)
}