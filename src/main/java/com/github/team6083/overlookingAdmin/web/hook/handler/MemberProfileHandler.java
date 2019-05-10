package com.github.team6083.overlookingAdmin.web.hook.handler;

import com.github.team6083.overlookingAdmin.firebase.db.MemberProfileCollection;
import com.github.team6083.overlookingAdmin.firebase.db.UsersCollection;
import com.github.team6083.overlookingAdmin.util.UserPermission;
import com.github.team6083.overlookingAdmin.web.hook.HookHandler;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Query;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class MemberProfileHandler extends HookHandler {

    public MemberProfileHandler() throws NoSuchMethodException {
        super();
    }

    @Override
    protected void setHookMethodMap() throws NoSuchMethodException {
        hookMethodMap.put("/profileList", this.getClass().getMethod("profileList", String.class), UserPermission.LEADER, NanoHTTPD.Method.GET);
    }

    private NanoHTTPD.Response profileList(String body) throws ExecutionException, InterruptedException {
        List<com.github.team6083.overlookingAdmin.module.MemberProfile> list = MemberProfileCollection.getAll();

        JSONArray out = new JSONArray();

        for (com.github.team6083.overlookingAdmin.module.MemberProfile memberProfile: list){
            JSONObject object = memberProfile.encodeJSON();
            object.put("uid", memberProfile.documentReference.getId());
            object.put("configName", memberProfile.getFields().configName);

            Query query = UsersCollection.getCollection().whereEqualTo("memberProfileRef", memberProfile.documentReference.getId());
            Iterator it = query.get().get().iterator();

            JSONArray linkedArr = new JSONArray();
            while (it.hasNext()){
                DocumentSnapshot documentSnapshot = (DocumentSnapshot) it.next();
                JSONObject json = new JSONObject(documentSnapshot.getData());

                JSONObject userOut = new JSONObject();
                userOut.put("name", json.getString("name"));
                userOut.put("email", json.getString("email"));
                linkedArr.put(userOut);
            }

            object.put("linkedAcc", linkedArr);

            out.put(object);
        }

        return newFixedLengthResponse(out.toString());
    }
}
