import React, {useCallback, useState} from 'react';
import ReactFlow, {
    addEdge,
    Background,
    Connection,
    Controls,
    Edge,
    MiniMap,
    ReactFlowProvider,
    useEdgesState,
    useNodesState,
    useOnSelectionChange,
} from 'reactflow';

import 'reactflow/dist/style.css';
import {Tab, Tabs} from "react-bootstrap";
import * as Icon from 'react-bootstrap-icons';
import ELK from 'elkjs/lib/elk.bundled.js';


const elk = new ELK();
const layoutNodes = (nodes: any, edges: any) => {
    const layoutOptions = {
        'elk.algorithm': 'org.eclipse.elk.layered',
        'org.eclipse.elk.layered.considerModelOrder.portModelOrder': true,
        'elk.direction': 'DOWN',
        'elk.layered.spacing.nodeNodeBetweenLayers': 100,
        'elk.spacing.nodeNode': 80,
    };
    const graph: any = {
        id: 'root',
        layoutOptions: layoutOptions,
        children: nodes,
        edges: edges,
    };

    elk.layout(graph).then(({children}) => {
        children!.forEach((node, index) => {
            nodes[index].position = {x: node.x, y: node.y};
        });
    });
};

type Noeud = {
    spanID: string;
    label: string;
    type: string;
    shortDescription: string;
    description: string;
    inputs?: object[];
    output?: object;
    stacktrace?: string;
    state: string
    nextSpanID?: string
};
const DATA: Noeud[] = [
    {
        spanID: "1",
        label: "RA0001",
        type: "RA",
        shortDescription: "Short Description",
        description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
        inputs: [
            {
                contexteUtilisateur: {
                    typeUtilisateur: "MembreClient"
                }
            }, {
                preferenceCommunication: {
                    typeUtilisateur: "Mail"
                }
            }
        ],
        output: {
            preferenceCommunication: {
                typeCanal: "Mail"
            }
        },
        state: "OK",
        nextSpanID: "2"
    },
    {
        spanID: "2",
        label: "RA0002",
        type: "RA",
        shortDescription: "Short Description",
        description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
        output: {
            preferenceCommunication: {
                typeCanal: "Mail"
            }
        },
        state: "NOK",
        stacktrace: "Exception in thread \"main\" java.lang.RuntimeException: Something has gone wrong, aborting!\n" +
            "    at com.myproject.module.MyProject.badMethod(MyProject.java:22)\n" +
            "    at com.myproject.module.MyProject.oneMoreMethod(MyProject.java:18)\n" +
            "    at com.myproject.module.MyProject.anotherMethod(MyProject.java:14)\n" +
            "    at com.myproject.module.MyProject.someMethod(MyProject.java:10)\n" +
            "    at com.myproject.module.MyProject.main(MyProject.java:6)",
        nextSpanID: "3"
    },
    {
        spanID: "3",
        label: "RA0003",
        type: "RA",
        shortDescription: "Short Description",
        description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
        output: {
            preferenceCommunication: {
                typeCanal: "Mail"
            }
        },
        state: "OK",
        nextSpanID: "4"
    },
    {
        spanID: "4",
        label: "Centrale Sic",
        type: "PORT",
        shortDescription: "Short Description",
        description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
        output: {
            preferenceCommunication: {
                typeCanal: "Mail"
            }
        },
        state: "OK",
        nextSpanID: "5"
    },
    {
        spanID: "5",
        label: "RA0005",
        type: "RA",
        shortDescription: "Short Description",
        description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
        output: {
            preferenceCommunication: {
                typeCanal: "Mail"
            }
        },
        state: "OK",
        nextSpanID: "6"
    },
    {
        spanID: "6",
        label: "Sociodemo",
        type: "PORT",
        shortDescription: "Short Description",
        description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
        output: {
            preferenceCommunication: {
                typeCanal: "Mail"
            }
        },
        state: "NOK",
        nextSpanID: "7"
    },
    {
        spanID: "7",
        label: "RA0007",
        type: "RA",
        shortDescription: "Short Description",
        description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
        output: {
            preferenceCommunication: {
                typeCanal: "Mail"
            }
        },
        state: "OK",
    },
]

let initialNodes = DATA.map((value, index) => ({
    id: value.spanID,
    data: {
        label:
            <span>{value.type === "RA" ?
                <Icon.BoxSeam size={20} className={"me-1"}/> :
                <Icon.BoxArrowRight size={20} className={"me-1"}/>}
                {value.label}
            </span>
    },
    style: {backgroundColor: value.state === "OK" ? "darkseagreen" : "lightcoral"},
    position: {x: (index + 1) * 200, y: (index + 1) * 75}
}));

let initialEdges = DATA.filter(value => value.nextSpanID != null).map(({spanID, nextSpanID}) =>
    ({id: 'e' + spanID + '-' + nextSpanID, source: spanID, target: nextSpanID!, selectable: false})
)

//layoutNodes(initialNodes, initialEdges);

export function Flow() {
    const [nodes, setNodes, onNodesChange] = useNodesState(initialNodes);
    const [edges, setEdges, onEdgesChange] = useEdgesState(initialEdges);
    const onConnect = useCallback(
        (params: Edge<any> | Connection) => setEdges((eds) => addEdge(params, eds)),
        [setEdges],
    );
    return (
        <div style={{width: '100vw', height: '50vh'}}>
            <ReactFlow nodes={nodes}
                       edges={edges}
                       onNodesChange={onNodesChange}
                       onEdgesChange={onEdgesChange}
                       onConnect={onConnect}
                       fitView>
                <Controls showInteractive={false}/>
                <MiniMap/>
                <Background gap={12} size={1}/>
            </ReactFlow>
        </div>
    );
}

export function DetailPanel() {
    const [selectedNodes, setSelectedNodes] = useState<string[]>([]);

    useOnSelectionChange({
        onChange: ({nodes, edges}) => {
            setSelectedNodes(nodes.map((node) => node.id));
        },
    });
    return (
        <div style={{width: '100vw', height: '40vh'}}>
            <Tabs
                id="uncontrolled-tab-example p-3"
                className="mb-3">
                {selectedNodes.map(id => DATA.find(node => node.spanID === id)).map(node => (
                    <Tab eventKey={node?.spanID} title={node?.label} className={"p-3"} key={node?.spanID}>
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

export function Navigation() {
    return (
        <nav className="navbar navbar-dark bg-dark">
            <div className="container-fluid">
                <a className="navbar-brand">Trace Explorer</a>
                <form className="d-flex" role="search">
                    <input className="form-control me-2" type="search" placeholder="Trace ID" aria-label="Search"/>
                    <button className="btn btn-outline-success" type="submit">Search</button>
                </form>
            </div>
        </nav>
    );
}

export default function App() {
    return (
        <ReactFlowProvider>
            <Navigation/>
            <Flow/>
            <DetailPanel/>
        </ReactFlowProvider>)
}
