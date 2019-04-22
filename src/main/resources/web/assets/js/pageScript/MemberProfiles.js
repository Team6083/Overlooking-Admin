let profilesListReq = new XMLHttpRequest();

profilesListReq.open("GET", "/hook/MemberProfiles/profileList");

firebase.auth().onAuthStateChanged(function (user) {
    if (user) {
        user.getIdToken().then((idToken) => {
            profilesListReq.setRequestHeader("auth-idtoken", idToken);
            profilesListReq.send();
        }).catch((err) => {
            console.err(err);
        });
    }
});

const usersTable = $("#profilesTable").DataTable();

profilesListReq.onload = () => {
    const response = JSON.parse(profilesListReq.response);
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
};

