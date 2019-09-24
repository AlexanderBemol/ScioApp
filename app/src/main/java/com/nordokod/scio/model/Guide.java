package com.nordokod.scio.model;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Guide {
    private FirebaseAuth mAuth;
    public Guide(){
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * crear una guía online
     * @param guide la entidad guía a guardar
     * @return Task con resultado
     */
    public Task<DocumentReference> createGuide(com.nordokod.scio.entity.Guide guide){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put(com.nordokod.scio.entity.Guide.KEY_CATEGORY,guide.getCategory());
        data.put(com.nordokod.scio.entity.Guide.KEY_TOPIC,guide.getTopic());
        data.put(com.nordokod.scio.entity.Guide.KEY_ONLINE,false);
        data.put(com.nordokod.scio.entity.Guide.KEY_ACTIVATED,true);
        data.put(com.nordokod.scio.entity.Guide.KEY_DATETIME,guide.getDatetime());
        return db.collection(com.nordokod.scio.entity.Guide.KEY_GUIDES).document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).collection(com.nordokod.scio.entity.Guide.KEY_PERSONAL_GUIDES).add(data);
    }

    /**
     * Editar la guía
     * @param guide entidad guía con respectiva id
     * @return Task con respuesta
     */
    public Task<Void> updateGuide(com.nordokod.scio.entity.Guide guide){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put(com.nordokod.scio.entity.Guide.KEY_CATEGORY,guide.getCategory());
        data.put(com.nordokod.scio.entity.Guide.KEY_TOPIC,guide.getTopic());
        data.put(com.nordokod.scio.entity.Guide.KEY_DATETIME,guide.getDatetime());
        data.put(com.nordokod.scio.entity.Guide.KEY_ONLINE,guide.isOnline());
        data.put(com.nordokod.scio.entity.Guide.KEY_ACTIVATED,guide.isActivated());
        return db.collection(com.nordokod.scio.entity.Guide.KEY_GUIDES).document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).collection(com.nordokod.scio.entity.Guide.KEY_PERSONAL_GUIDES).document(guide.getId()).update(data);
    }

    /**
     * Eliminar la guía
     * @param guide entidad guía con respectiva id
     * @return Task con resultao
     */
    public Task<Void> deleteGuide(com.nordokod.scio.entity.Guide guide){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection(com.nordokod.scio.entity.Guide.KEY_GUIDES).document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).collection(com.nordokod.scio.entity.Guide.KEY_PERSONAL_GUIDES).document(guide.getId()).delete();
    }

    /**
     * Obtener todas las guías del usuario
     * @return Task con resultado
     */
    public Task<QuerySnapshot> getAllGuides(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection(com.nordokod.scio.entity.Guide.KEY_GUIDES).document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).collection(com.nordokod.scio.entity.Guide.KEY_PERSONAL_GUIDES).get();
    }

    public Task<ShortDynamicLink> generateGuideLink(com.nordokod.scio.entity.Guide guide){
        String uri="https://sendosg.com/guides?user="+ Objects.requireNonNull(mAuth.getCurrentUser()).getUid()+"&guide="+guide.getId();
        String domain="https://sendosg.com";
        return FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(uri))
                .setDomainUriPrefix(domain)
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.nordokod.scio").build()
                ).buildShortDynamicLink();
    }
}
