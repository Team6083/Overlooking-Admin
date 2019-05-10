sendHook("/hook/memberProfiles/profileList", "GET", undefined, (response) => {
    response = JSON.parse(response);
    console.log(response);

    for (let i = 0; i < response.length; i++) {
        let u = response[i];

        let linked = "";
        for (let j = 0; j < u.linkedAcc.length; j++) {
            linked += u.linkedAcc[j].email;
            linked += " ";
        }

        usersTable.row.add([u.uid, u.configName,linked]).draw();
    }
});

const usersTable = $("#profilesTable").DataTable();

