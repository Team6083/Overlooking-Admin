let appsListReq = new XMLHttpRequest();

appsListReq.open("GET", "/hook/Apps/appsList");

firebase.auth().onAuthStateChanged(function (user) {
    if (user) {
        user.getIdToken().then((idToken) => {
            appsListReq.setRequestHeader("auth-idtoken", idToken);
            appsListReq.send();
        }).catch((err) => {
            console.err(err);
        });
    }
});

const appsTable = $("#usersTable").DataTable();

appsListReq.onload = () => {
    const response = JSON.parse(usersListReq.response);
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
};

