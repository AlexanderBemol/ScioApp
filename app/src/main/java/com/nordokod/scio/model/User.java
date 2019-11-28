package com.nordokod.scio.model;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nordokod.scio.constants.ProfilePhotoHost;
import com.nordokod.scio.entity.InvalidValueException;
import com.nordokod.scio.process.DownloadImageProcess;
import com.nordokod.scio.process.MediaProcess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class User {
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    public User() {
        mAuth = FirebaseAuth.getInstance();
    }

    /**
     * Revisa si el usuario está logeado
     * @return boolan con el resultado
     */
    public boolean isUserLogged() {
        return mAuth.getCurrentUser() != null;
    }
    /**
     * Registrar Usuario por Correo Electrónio
     * @param user Entidad del usuario
     */
    public Task<AuthResult> signUpWithMail(com.nordokod.scio.entity.User user) {
        return mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword());
    }

    /**
     * Acceder con correo y contraseña
     * @param user
     */
    public Task<AuthResult> signInWithMail(com.nordokod.scio.entity.User user){
        return mAuth.signInWithEmailAndPassword(user.getEmail(),user.getPassword());
    }

    /**
     * Acceder con cuenta de google
     * @param googleSignInAccount dado en el controlador
     */
    public Task<AuthResult> signInWithGoogle(GoogleSignInAccount googleSignInAccount) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        return mAuth.signInWithCredential(authCredential);
    }

    /**
     * Acceder con cuenta de google
     * @param accessToken token otorgado por fb
     */
    public Task<AuthResult> signInWithFacebook(AccessToken accessToken){
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        return mAuth.signInWithCredential(credential);
    }

    /**
     * Envíar correo de verificación al correo
     */
    public Task<Void> sendVerificationMail(){
        if(mAuth.getCurrentUser()!=null){
            return mAuth.getCurrentUser().sendEmailVerification();
        }
        else{
            return null;
        }
    }

    /**
     * Actualizar foto de perfil del usuario
     * @param photo URI de la foto a subir
     * @param context contexto actual de la aplicación
     * @return UploadTask o nulo si hay error
     * @throws FileNotFoundException error al guardar foto en local
     */
    public UploadTask uploadProfilePhoto(Uri photo, Context context) throws FileNotFoundException {
        Bitmap compressedPhoto=new MediaProcess().compressPhoto(photo);//primero se comprime
        /*
         * se crea un archivo en la memoria interna que alojará la foto comprimida
         * se hace referencia al archivo que se llamará userPhoto- concatenada al UID (id del user)
         */
        String pictureFile = "userPhoto-" + Objects.requireNonNull(mAuth.getCurrentUser()).getUid()+".jpg";
        File storageDir = context.getFilesDir();
        File file=new File(storageDir,pictureFile);
        if(file.exists()){
            file.delete();
        }
        FileOutputStream out = new FileOutputStream(file);
        compressedPhoto.compress(Bitmap.CompressFormat.PNG, 100, out);

        /*
         * Referencia al almacenamiento de firebase
         */
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        /*
         * se crea referencia a la carpeta que aloja las fotos de los usarios y se solicita subir archivo
         */
        StorageReference sRef = storageReference.child(com.nordokod.scio.entity.User.KEY_USERS).child(com.nordokod.scio.entity.User.KEY_PROFILE_PHOTO).child("userPhoto-"+mAuth.getCurrentUser().getUid());
        return sRef.putFile(Uri.fromFile(file));
    }

    /**
     * Crear nodo con la información del usuario
     * @param user entidad usuario
     * @return Task con el resultado
     */
    public Task<Void> createUserInformation(com.nordokod.scio.entity.User user){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put(com.nordokod.scio.entity.User.KEY_EMAIL,user.getEmail());
        data.put(com.nordokod.scio.entity.User.KEY_USERNAME,user.getUsername());
        data.put(com.nordokod.scio.entity.User.KEY_PHOTO_PATH,user.getPhotoPath());
        data.put(com.nordokod.scio.entity.User.KEY_BIRTHDAY,user.getBirthdayDate());
        data.put(com.nordokod.scio.entity.User.KEY_STUDY_LEVEL,user.getStudyLevel());
        data.put(com.nordokod.scio.entity.User.KEY_PROFILE_PROVIDER,user.getProvider());
        data.put(com.nordokod.scio.entity.User.KEY_STATE,user.getState());
        return  db.collection(com.nordokod.scio.entity.User.KEY_USERS).document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).set(data);
    }
    /**
     * Actualiza nodo con la información del usuario
     * @param user entidad usuario
     * @return Task con el resultado
     */
    public Task<Void> updateUserInformation(com.nordokod.scio.entity.User user){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put(com.nordokod.scio.entity.User.KEY_EMAIL,user.getEmail());
        data.put(com.nordokod.scio.entity.User.KEY_USERNAME,user.getUsername());
        data.put(com.nordokod.scio.entity.User.KEY_PHOTO_PATH,user.getPhotoPath());
        data.put(com.nordokod.scio.entity.User.KEY_BIRTHDAY,user.getBirthdayDate());
        data.put(com.nordokod.scio.entity.User.KEY_STUDY_LEVEL,user.getStudyLevel());
        data.put(com.nordokod.scio.entity.User.KEY_PROFILE_PROVIDER,user.getProvider());
        data.put(com.nordokod.scio.entity.User.KEY_STATE,user.getState());
        return  db.collection(com.nordokod.scio.entity.User.KEY_USERS).document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).update(data);
    }

    /**
     * Obtener toda la información del usuario
     * @param user user con UID
     * @return task con resultado
     */
    public Task<DocumentSnapshot> getUserInformation(com.nordokod.scio.entity.User user){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection(com.nordokod.scio.entity.User.KEY_USERS).document(Objects.requireNonNull(user.getUid())).get();
    }

    /**
     * obtener la entidad usuario con la info basica (no firestore) del usuario (ya no usar)
     * @return entidad usuario
     * @throws InvalidValueException (valores inválidos)
     */
    public com.nordokod.scio.entity.User getBasicUserInfo() throws InvalidValueException {
        com.nordokod.scio.entity.User user = new com.nordokod.scio.entity.User();
        user.setUsername(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName()));
        user.setEmail(mAuth.getCurrentUser().getEmail());
        user.setUid(mAuth.getCurrentUser().getUid());
        user.setPhotoPath(Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser().getPhotoUrl()).toString()));
        return user;
    }

    /**
     * Actualiza la información básica del usuario en el servicio privado
     * @param user usuario (nombre y foto)
     * @return task con resultado
     */
    public Task<Void> updateBasicUserInfo(com.nordokod.scio.entity.User user){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(user.getUsername())
                .setPhotoUri(Uri.parse(user.getPhotoPath()))
                .build();
        return Objects.requireNonNull(mAuth.getCurrentUser()).updateProfile(profileUpdates);
    }

    /**
     * Obtiene en que servicio se encuentra alojada la foto del usuario
     * @param user usuario
     * @return enum ProfilePhotoHost con respuesta
     */
    public ProfilePhotoHost getProfilePhotoHost (com.nordokod.scio.entity.User user){
        String photoPath = user.getPhotoPath();//obtener link de la foto
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();//obtenemos referencia a la bd
        if(photoPath.contains(storageReference.toString()))
            return ProfilePhotoHost.FIREBASE_STORAGE;
        else
            return ProfilePhotoHost.GOOGLE_OR_FACEBOOK_STORAGE;
    }

    /**
     * Obtener foto de usuario cuando está en firebase storage
     *
     * @return FileDownload task con el resultado
     * @throws IOException en error en uri, o almacenamiento.
     */
    public Task<byte[]> getFirebaseProfilePhoto(com.nordokod.scio.entity.User user){
        String photoPath = user.getPhotoPath();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();//obtenemos referencia a la bd
        StorageReference sRef = storage.getReferenceFromUrl(photoPath);//obtenemos la referencia al link
        return sRef.getBytes(1024);
    }

    /**
     * Obtener foto de perfil de usuario de Facebook o Google
     * @param customListener listener con acciones de exito o error
     * @param user usuario
     */
    public void getExternalProfilePhoto(DownloadImageProcess.CustomListener customListener, com.nordokod.scio.entity.User user){
        String photoPath = user.getPhotoPath();
        if (user.getProvider().equals("facebook.com")) {
            String facebookUserId = user.getUid();
            photoPath = "https://graph.facebook.com/" + facebookUserId + "/picture?type=medium";
        }
        DownloadImageProcess dip = new DownloadImageProcess();
        dip.setListener(customListener);
        dip.execute(photoPath);
    }

    /**
     * Obtener la foto de perfil de los archivos locales
     * @param context contexto actual
     * @param user usuario
     * @return la foto en formato bitmap o nulo si no se encontró
     */
    public Bitmap getLocalProfilePhoto(Context context, com.nordokod.scio.entity.User user){
        String pictureFile = "userPhoto-" + user.getUid()+".JPEG";
        File storageDir = context.getFilesDir();
        File file=new File(storageDir,pictureFile);
        if(file.exists()){
            return BitmapFactory.decodeFile(file.getPath());
        }else{
            return null;
        }
    }

    /**
     * Guardar foto de perfil de usuario en local
     * @param context contexto actual
     * @param photo Bitmap de la foto
     * @param user usuario
     * @throws IOException excepción de tipo IOException
     */
    public void saveProfilePhotoInLocal(Context context, Bitmap photo, com.nordokod.scio.entity.User user) throws IOException {
        String pictureFile = "userPhoto-" + user.getUid()+".JPEG";
        File storageDir = context.getFilesDir();
        File file=new File(storageDir,pictureFile);
        OutputStream os = new FileOutputStream(file);
        photo.compress(Bitmap.CompressFormat.JPEG,100,os);
        os.flush();
        os.close();
    }
    /**
     * Cerrar Sesión
     */
    public void logOut(){
        mAuth.signOut();
    }

    /**
     * convertir documentSnapshot en usuario
     * @param documentSnapshot DocumentSnapchot no nulo
     * @return entidad User con el usuario
     */

    public com.nordokod.scio.entity.User getUserFromDocument(DocumentSnapshot documentSnapshot){
        return new com.nordokod.scio.entity.User(
                        documentSnapshot.getId(),
                (String)documentSnapshot.get(com.nordokod.scio.entity.User.KEY_USERNAME),
                (String)documentSnapshot.get(com.nordokod.scio.entity.User.KEY_EMAIL),
                (String)documentSnapshot.get(com.nordokod.scio.entity.User.KEY_PHOTO_PATH),
                        documentSnapshot.getDate(com.nordokod.scio.entity.User.KEY_BIRTHDAY),
                        Integer.parseInt(String.valueOf(documentSnapshot.get(com.nordokod.scio.entity.User.KEY_STUDY_LEVEL))),
                        Integer.parseInt(String.valueOf(documentSnapshot.get(com.nordokod.scio.entity.User.KEY_STATE))),
                (String) documentSnapshot.get(com.nordokod.scio.entity.User.KEY_PROFILE_PROVIDER)
        );
    }

}
