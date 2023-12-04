export type Noeud = {
    spanID: string;
    label: string;
    type: string;
    inputs?: object[];
    output?: object;
    stacktrace?: string;
    state: string
    nextSpanID?: string
};