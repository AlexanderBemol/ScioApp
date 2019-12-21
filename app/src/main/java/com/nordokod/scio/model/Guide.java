package com.nordokod.scio.model;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firestore.v1.DocumentTransform;

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
        data.put(com.nordokod.scio.entity.Guide.KEY_DATETIME,guide.getTestDatetime());
        data.put(com.nordokod.scio.entity.Guide.KEY_CREATION_DATE, FieldValue.serverTimestamp());
        data.put(com.nordokod.scio.entity.Guide.KEY_UPDATE_DATE, FieldValue.serverTimestamp());
        data.put(com.nordokod.scio.entity.Guide.KEY_CREATION_USER, mAuth.getUid());
        data.put(com.nordokod.scio.entity.Guide.KEY_UPDATE_USER, mAuth.getUid());
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
        data.put(com.nordokod.scio.entity.Guide.KEY_DATETIME,guide.getTestDatetime());
        data.put(com.nordokod.scio.entity.Guide.KEY_ONLINE,guide.isOnline());
        data.put(com.nordokod.scio.entity.Guide.KEY_ACTIVATED,guide.isActivated());
        data.put(com.nordokod.scio.entity.Guide.KEY_UPDATE_DATE, FieldValue.serverTimestamp());
        data.put(com.nordokod.scio.entity.Guide.KEY_UPDATE_USER, mAuth.getUid());
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

    /**
     * Generar link para compartir guía
     * @param guide guía a compartir
     * @return task con link corto
     */
    public Task<ShortDynamicLink> generateGuideLink(com.nordokod.scio.entity.Guide guide){
        String uri="https://sendosg.com/?user="+ Objects.requireNonNull(mAuth.getCurrentUser()).getUid()+"&guide="+guide.getId();
        String domain="https://sendosg.com/guides";
        return FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse(uri))
                .setDomainUriPrefix(domain)
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.nordokod.scio").build()
                ).setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder().setTitle("¡Aquí te va una guía de "+mAuth.getCurrentUser().getDisplayName()+"!")
                            .setDescription("Descarga esta guía sobre "+guide.getTopic())
                            .setImageUrl(Uri.parse("https://res.cloudinary.com/teepublic/image/private/s--tWLvXpQb--/t_Preview/b_rgb:d1d1d1,c_limit,f_jpg,h_630,q_90,w_630/v1524281456/production/designs/2612742_0.jpg"))
                            .build()
                )
                .buildShortDynamicLink();
    }

    public Task<DocumentSnapshot> getPublicGuide(PendingDynamicLinkData pendingDynamicLinkData){
        Uri deepLink = null;
        if (pendingDynamicLinkData != null) {
            deepLink = pendingDynamicLinkData.getLink();
        }
        if(deepLink!=null){
            String uid = deepLink.getQueryParameter("user");
            String guideID = deepLink.getQueryParameter("guide");
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            return db.collection(com.nordokod.scio.entity.Guide.KEY_GUIDES).document(Objects.requireNonNull(uid)).collection(com.nordokod.scio.entity.Guide.KEY_PERSONAL_GUIDES).document(Objects.requireNonNull(guideID)).get();
        }
        return null;
    }
    public com.nordokod.scio.entity.Guide getGuideFromDocument(DocumentSnapshot document){
        return new com.nordokod.scio.entity.Guide(
                Integer.parseInt(Objects.requireNonNull(Objects.requireNonNull(document.getData()).get(com.nordokod.scio.entity.Guide.KEY_CATEGORY)).toString()),
                document.getId(),
                (String)    document.getData().get(com.nordokod.scio.entity.Guide.KEY_TOPIC),
                Objects.requireNonNull(document.getReference().getParent().getParent()).getId(),
                (boolean)   document.getData().get(com.nordokod.scio.entity.Guide.KEY_ONLINE),
                (boolean)   document.getData().get(com.nordokod.scio.entity.Guide.KEY_ACTIVATED),
                Objects.requireNonNull(document.getTimestamp(com.nordokod.scio.entity.Guide.KEY_DATETIME)).toDate(),
                Objects.requireNonNull(document.getTimestamp(com.nordokod.scio.entity.Guide.KEY_CREATION_DATE)).toDate(),
                Objects.requireNonNull(document.getTimestamp(com.nordokod.scio.entity.Guide.KEY_CREATION_DATE)).toDate(),
                (String)    document.getData().get(com.nordokod.scio.entity.Guide.KEY_CREATION_USER),
                (String)    document.getData().get(com.nordokod.scio.entity.Guide.KEY_UPDATE_USER)
        );
    }
    public Task<DocumentReference> importGuide (DocumentSnapshot documentSnapshot){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = documentSnapshot.getData();
        Objects.requireNonNull(data).put(com.nordokod.scio.entity.Guide.KEY_UPDATE_DATE,FieldValue.serverTimestamp());
        Objects.requireNonNull(data).put(com.nordokod.scio.entity.Guide.KEY_UPDATE_USER,mAuth.getUid());
        return db.collection(com.nordokod.scio.entity.Guide.KEY_GUIDES).document(Objects.requireNonNull(mAuth.getUid())).collection(com.nordokod.scio.entity.Guide.KEY_PERSONAL_GUIDES).add(Objects.requireNonNull(data));
    }
}
