sendHook("/hook/fieldConfig/profileList", "GET", undefined, (response) => {
    response = JSON.parse(response);
    console.log(response);

    for (let i = 0; i < response.length; i++) {
        let u = response[i];


        configsTable.row.add([u.uid, u.name, ""]).draw();
    }
});

const configsTable = $("#configsTable").DataTable();

const temp = (num) => {
    let tableBody = $("#editFieldTable tbody");

    const data = JSON.parse(configsListReq.response)[num];

    const keys = Object.keys(data.config);

    for (let i = 0; i < keys.length; i++) {
        let key = keys[i];
        let d = data.config[key];

        let tr = document.createElement("tr");

        let td1 = document.createElement("td");
        td1.append(document.createTextNode(key));
        tr.append(td1);

        let td2 = document.createElement("td");
        td2.append(document.createTextNode(d));
        tr.append(td2);

        tableBody.append(tr);
    }
};

