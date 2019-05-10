sendHook("/hook/apps/appsList", "GET", undefined, (response) => {
    response = JSON.parse(response);
    console.log(response);

    for(let i=0;i<response.length;i++){
        let u = response[i];

        let classData = "";
        if(u.classDataString !== undefined){
            let classDataJSON = JSON.parse(u.classDataString);
            classData = classDataJSON.className + " #" + classDataJSON.number;
        }

        let status = "";
        if(!u.verified){
            status += "not verified ";
        }

        if(u.disabled){
            status += "disabled";
        }

        appsTable.row.add([u.docId, u.name, u.app_token]).draw();
    }
});

const appsTable = $("#usersTable").DataTable();

