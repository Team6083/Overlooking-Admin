package com.github.team6083.overlookingAdmin.module;

import com.google.cloud.firestore.DocumentReference;

import java.util.Map;

public interface StorableModule {
    DocumentReference getDocumentReference();
    Map getStorableMap();
}
