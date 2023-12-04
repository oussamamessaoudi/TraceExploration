import React, {useCallback, useEffect, useState} from 'react';
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
} from 'reactflow';

import 'reactflow/dist/style.css';
import * as Icon from 'react-bootstrap-icons';
import {SearchForm} from "./SearchForm";
import {Noeud} from "./Noeud";


const DATA: Noeud[] = [
    {
        spanID: "1",
        label: "RA0001",
        type: "RA",
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

export function Flow(props: any) {
    const [nodes, setNodes, onNodesChange] = useNodesState(initialNodes);
    const [edges, setEdges, onEdgesChange] = useEdgesState(initialEdges);
    const onConnect = useCallback(
        (params: Edge<any> | Connection) => setEdges((eds) => addEdge(params, eds)),
        [setEdges],
    );
    if (props.state !== "SUCCESS") {
        return <div>{props.state} ... </div>
    }
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

export default function App() {
    const [brutData, updateDataBrut] = useState<any>();
    const [data, setData] = useState<any>();
    const [state, setState] = useState<"INITIAL" | "LOADING" | "SUCCESS">("INITIAL");

    useEffect(() => {
        /*var groupBy = groupBy(brutData, ({ row }) => row.properties.spanId)
        const data = Object.keys(groupBy).map(e => ({
            spanID: e,
            label: groupBy[e].map(v => v.data.logger)[0],
            type: "RA",
            inputs: groupBy[e].flatMap(v => v.data.message.inputs).filter(e => e != undefined),
            outputs: groupBy[e].map(v => v.data.message.output).filter(e => e != undefined),
            stacktrace: groupBy[e].map(v => v.data.message.stacktrace).filter(e => e != undefined)
        }))*/
        console.log(data);

    }, [brutData]);
    return (
        <ReactFlowProvider>
            <SearchForm state={state} updateData={updateDataBrut} setState={setState}/>
            <Flow state={state}/>
            {/*<DetailPanel/>*/}
        </ReactFlowProvider>)
}


