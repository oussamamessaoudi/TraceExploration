declare const window: any;

export let SearchManager: any;
export let ChartView: any;


SearchManager = window.require("splunkjs/mvc/searchmanager");

export const searchId = "listings-search";
/**
 * Gets all of the listings which are minBeds < listing > maxBeds
 */
export const performSearch = async (
    traceId: string): Promise<any> => {
    return new Promise(async (resolve, reject) => {

        await window.splunkjs.mvc.Components
            .revokeInstance(searchId);

        if (!SearchManager) {
            // The SearchManager constructor is loaded asynchronously.
            // So check if it is already loaded.
            alert("The search manager constructor is still loading!");
            resolve({
                fields: [],
                rows: []
            });
        }

        const searchManager = new SearchManager({
            id: searchId,
            earliest_time: "-5h@y",
            latest_time: "now",
            preview: false,
            cache: false,
            search: `source="http:Testing Exploration Trace" | spath "properties.traceId" | search "properties.traceId"=${traceId}`
        });
        searchManager.on("error", function () {
            console.log("ERROR")
            throw "Error!"
        });
        searchManager.on("failed", function () {
            console.log("FAILED")
            throw "Failure!"
        });
        searchManager.data("results")
            .on("data", function (state: any, job: any) {

                const fields: string[] = job.fields;
                const timeIndex = fields.findIndex(value => value === "_time");
                const dataIndex = fields.findIndex(value => value === "_raw");

                let dataBrut = job.rows.map((row: string[]) => ({
                    time: row[timeIndex],
                    data: JSON.parse(row[dataIndex])
                }));
                window.test = dataBrut;
                resolve(dataBrut);
            });

    });
};
