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

$("#newUserBtn").on('click', ()=>{
    $('#editUserModal').modal('show');
});

$("#editUserSave").on('click', ()=>{
   let data = {
       id:$ ("#editId").val(),
       name: $("#editName").val(),
       email: $("#editEmail").val(),
       password: $("#editPassword").val(),
       userPermission: $("#editPermission").val(),
   };

   sendHook("/hook/users/addUser", "POST", JSON.stringify(data), (response)=>{
       let r = JSON.parse(response);
       if(!r.ok){
           console.error(r.msg);
       } else {
           console.log("user successfully saved")
       }
   });
});

