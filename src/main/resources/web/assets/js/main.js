// Initialize Firebase
var config = {
    apiKey: "AIzaSyDqGsMpbk3UFSDgJdBTp6hx1jGtZMFAvjg",
    authDomain: "overlooking-admin.firebaseapp.com",
    databaseURL: "https://overlooking-admin.firebaseio.com",
    projectId: "overlooking-admin",
    storageBucket: "overlooking-admin.appspot.com",
    messagingSenderId: "660461854849"
};
firebase.initializeApp(config);

firebase.auth().onAuthStateChanged(function (user) {
    if (user) {
        console.log("User login: ", user.email);
        console.log(user.providerData);
    } else {
        // No user is signed in.
        console.log("User logout");
    }
});

function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function (m, key, value) {
        vars[key] = value;
    });
    return vars;
}

function signOut() {
    firebase.auth().signOut().then(function () {
        // Sign-out successful.
    }).catch(function (error) {
        console.error(error);
    });
}

const authWarnFunc = ()=>{
    if(!firebase.auth().currentUser){
        showAuthWarn();
    }
};

const authWarnTimeout = 3000;