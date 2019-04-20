package com.github.team6083.overlookingAdmin.web.hook.worker;

import com.github.team6083.overlookingAdmin.firebase.db.MemberProfileCollection;
import com.github.team6083.overlookingAdmin.firebase.db.UsersCollection;
import com.github.team6083.overlookingAdmin.module.MemberProfile;
import com.github.team6083.overlookingAdmin.util.UserPermission;
import com.github.team6083.overlookingAdmin.web.hook.HookHandler;
import com.github.team6083.overlookingAdmin.web.hook.HookWorker;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Query;
import com.google.firebase.auth.FirebaseAuthException;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class MemberProfilesWorker implements HookWorker {
    @Override
    public NanoHTTPD.Response serve(String uri, Map<String, String> header, String body, NanoHTTPD.Method method) {
        String idToken = HookHandler.getIdToken(header);
        NanoHTTPD.Response r = null;

        if (uri.equals("/MemberProfiles/profileList")) {
            try {
                if (HookHandler.checkPermission(idToken, UserPermission.LEADER)) {
                    List<MemberProfile> list = MemberProfileCollection.getAll();

                    JSONArray out = new JSONArray();

                    for (MemberProfile memberProfile: list){
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

                    r = newFixedLengthResponse(out.toString());
                } else {
                    r = newFixedLengthResponse(NanoHTTPD.Response.Status.UNAUTHORIZED, NanoHTTPD.MIME_PLAINTEXT, "no permission");
                }
            } catch (InterruptedException | ExecutionException | ParseException e) {
                e.printStackTrace();
            } catch (FirebaseAuthException e) {
                e.printStackTrace();
            }
        }

        return r;
    }
}
