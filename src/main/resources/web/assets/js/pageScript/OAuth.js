let usersListReq = new XMLHttpRequest();

usersListReq.open("GET", "/hook/OAuth/usersList");

firebase.auth().onAuthStateChanged(function (user) {
    if (user) {
        user.getIdToken().then((idToken) => {
            usersListReq.setRequestHeader("auth-idtoken", idToken);
            usersListReq.send();
        }).catch((err) => {
            console.err(err);
        });
    }
});

const usersTable = $("#usersTable").DataTable();

usersListReq.onload = () => {
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

        usersTable.row.add([u.uid, u.name, u.email, classData, u.permission, status]).draw();
    }
};

