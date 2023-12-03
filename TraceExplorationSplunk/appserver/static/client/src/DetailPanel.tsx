import React, {useState} from "react";
import {useOnSelectionChange} from "reactflow";
import {Tab, Tabs} from "react-bootstrap";

export function DetailPanel(props: any) {
    const [selectedNodes, setSelectedNodes] = useState<string[]>([]);

    useOnSelectionChange({
        onChange: ({nodes, edges}) => {
            setSelectedNodes(nodes.map((node) => node.id));
        },
    });
    return (
        <div style={{width: '100vw', height: '40vh'}}>
            <Tabs
                id={"tabs"}
                className="mb-3 p-3">
                {selectedNodes.map(id => props.data.find((node: any) => node.spanID === id)).map(node => (
                    <Tab eventKey={node?.spanID} title={node?.label} key={node?.spanID}>
                        <h3>{node?.shortDescription}</h3>
                        <p className="fs-5 col-md-8">
                            {node?.description}
                        </p>
                        <div className="row g-5">
                            <div className="col-md-6">
                                <h4>Input</h4>
                                <div className="overflow-auto" style={{"height": "120px"}}>
                                    <pre>{JSON.stringify(node?.inputs, null, 2)}</pre>
                                </div>
                            </div>
                            {node?.state === "OK" ?
                                <div className="col-md-6">
                                    <h4>Output</h4>
                                    <div className="overflow-auto" style={{"height": "120px"}}>
                                        <pre>{JSON.stringify(node?.output, null, 2)}</pre>
                                    </div>
                                </div> :
                                <div className="col-md-6">
                                    <h4>StackTrace</h4>
                                    <div className="overflow-auto" style={{"height": "120px"}}>
                                        <pre>{node?.stacktrace}</pre>
                                    </div>
                                </div>}
                        </div>
                    </Tab>
                ))}
            </Tabs>
        </div>
    )
}