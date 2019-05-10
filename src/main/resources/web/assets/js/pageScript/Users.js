sendHook("/hook/users/usersList", "GET", undefined, (response) => {
    response = JSON.parse(response);
    console.log(response);

    for(let i=0;i<response.length;i++){
        let u = response[i];

        let classData = "";
        if(u.classData !== undefined){
            let classDataJSON = u.classData;
            classData = classDataJSON.className + " #" + classDataJSON.number;
        }

        let status = "";
        if(!u.verified){
            status += "not verified ";
        }

        if(u.disabled){
            status += "disabled";
        }

        usersTable.row.add([u.uid, u.name, u.email, classData, u.permission, status]).draw();
    }
});

const usersTable = $("#usersTable").DataTable();


