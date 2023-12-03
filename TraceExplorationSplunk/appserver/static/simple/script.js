STORE = updateStore("initial")

function updateStore(newState, newData) {
    STORE = {
        state: newState,
        data: mapBrutData(newData)
    };
    console.log("store", STORE)
    $("#group").html("")
    STORE.data && STORE.data.forEach(e => {
        $("#group").append(buildElement(e))
    })
}

function mapBrutData(brutData) {
    if (brutData === undefined) {
        return undefined;
    }
    const groupBy = Object.groupBy(brutData, ({data}) => data.properties.spanId)
    return Object.keys(groupBy).map(e => ({
        spanID: e,
        severity: Math.max(...groupBy[e].map(v => SEVERITY[v.data.severity])),
        label: groupBy[e].map(v => v.data.logger)[0],
        inputs: groupBy[e].flatMap(v => v.data.message.inputs).filter(e => e != undefined),
        outputs: groupBy[e].map(v => v.data.message.output).filter(e => e != undefined),
        exception: groupBy[e].map(v => v.data.message.exception).filter(e => e != undefined),
        start: Math.min(...groupBy[e].map(v => new Date(v.time).getTime())),
        end: Math.max(...groupBy[e].map(v => new Date(v.time).getTime())),
    })).sort((e1, e2) => e1.start - e2.start)
}

$('#search').bind('click', function (event) {
    event.preventDefault();

    updateStore("loading")

    performSearch("Testing Exploration Trace", $("#traceID").val())
        .then(data => updateStore("success", data))
        .catch((error) => {
            console.log(error)
            updateStore("error")
        })
});

function buildElement(props) {

    return `<li class="list-group-item ${props.severity === 2 ? 'list-group-item-success' : 'list-group-item-danger'}">
                            <h2>${props.label} </h2>
                            <div class='row g-5'>
                                <div class='col-md-6'>
                                    <h3>Input</h3>
                                    <div class='overflow-auto'>
                                        <pre class="p-3 mb-2 text-white" style="background-color: ${props.severity === 2 ? 'darkcyan' : 'red'};">${JSON.stringify(props.inputs, undefined, 2)}</pre>
                                    </div>
                                </div>
                                <div class='col-md-6'>
                                    <h3>${props.severity === 2 ? 'Output' : 'Exception'}</h3>
                                    <div className='overflow-auto' >
                                        <pre class="p-3 mb-2 text-white" style="background-color: ${props.severity === 2 ? 'darkcyan' : 'red'};">${JSON.stringify(props.severity === 2 ? props.outputs : props.exception, undefined, 2)}</pre>
                                    </div>
                                </div>
                            </div>
            </li>`

}

SearchManager = window.require("splunkjs/mvc/searchmanager");
const searchId = "traceExploration-search";

async function performSearch(application, traceId) {
    return new Promise(async (resolve, reject) => {

        await window.splunkjs.mvc.Components
            .revokeInstance(searchId);

        if (!SearchManager) {
            alert("The search manager constructor is still loading!");
            resolve([]);
        }

        const searchManager = new SearchManager({
            id: searchId,
            earliest_time: "-5h@y",
            latest_time: "now",
            preview: false,
            cache: false,
            search: `source="http:${application}" | spath "properties.traceId" | search "properties.traceId"=${traceId}`
        });
        searchManager.on("error", function (error) {
            reject(error)
        });
        searchManager.on("failed", function (error) {
            reject(error)
        });
        searchManager.data("results")
            .on("data", function (state, job) {

                const fields = job.fields;
                const timeIndex = fields.findIndex(value => value === "_time");
                const dataIndex = fields.findIndex(value => value === "_raw");

                let dataBrut = job.rows.map((row) => ({
                    time: row[timeIndex],
                    data: JSON.parse(row[dataIndex])
                }));
                window.test = dataBrut;
                resolve(dataBrut);
            });

    });
}

const SEVERITY = {
    TRACE: 0,
    DEBUG: 1,
    INFO: 2,
    WARN: 3,
    ERROR: 4,
}