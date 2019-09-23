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
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
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
        String pictureFile = "userPhoto-" +mAuth.getCurrentUser().getUid()+".jpg";
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
     * actualiza el link de la foto de perfil del usuario con la actual guardada en storage.
     * @return Task, o nulo si hay error
     */
    public Task<Void> updateProfilePhotoLink(){
        /*
         * se obtiene link de la foto
         */
        StorageReference sRef = storageReference.child(com.nordokod.scio.entity.User.KEY_USERS).child(com.nordokod.scio.entity.User.KEY_PROFILE_PHOTO).child("userPhoto-"+ Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        /*
         * se actualiza link de foto del usario
         */
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(sRef.toString()))
                .build();
        /*
         * listener para saber cuando se actalizó el link correctamente
         */
        return mAuth.getCurrentUser().updateProfile(profileUpdates);
    }

    /**
     * actualizar nombre de usuario
     * @param username nuevo nombre de usuario
     * @return Task del resultado
     */
    public Task<Void> updateUsername(String username){
        UserProfileChangeRequest profileUpdates= new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();
        return Objects.requireNonNull(mAuth.getCurrentUser()).updateProfile(profileUpdates);
    }

    /**
     * establecer la información no basica del usuario
     * @param user entidad usuario (se requiere cumpleaños y nivel de estudios)
     * @return Task con el resultado0
     */
    public Task<Void> setUserInformation(com.nordokod.scio.entity.User user){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put(com.nordokod.scio.entity.User.KEY_BIRTHDAY,user.getBirthdayDate());
        data.put(com.nordokod.scio.entity.User.KEY_STUDY_LEVEL,user.getStudyLevel());
        return  db.collection(com.nordokod.scio.entity.User.KEY_USERS).document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).set(data);
    }

    /**
     * obtener la entidad usuario con la info basica (no firestore) del usuario
     * @return entidad usuario
     * @throws InvalidValueException (valores inválidos)
     */
    public com.nordokod.scio.entity.User getBasicUserInfo() throws InvalidValueException {
        com.nordokod.scio.entity.User user = new com.nordokod.scio.entity.User();
        user.setUsername(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName());
        user.setEmail(mAuth.getCurrentUser().getEmail());
        user.setUid(mAuth.getCurrentUser().getUid());
        user.setPhotoPath(Objects.requireNonNull(mAuth.getCurrentUser().getPhotoUrl()).getPath());
        return user;
    }

    /**
     * Obtener toda la información del usuario
     * @return entidad usaurio
     * @throws InvalidValueException dato inválido
     */
    public com.nordokod.scio.entity.User getAllUserInfo() throws InvalidValueException {
        com.nordokod.scio.entity.User user = new com.nordokod.scio.entity.User();
        user.setUsername(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName());
        user.setEmail(mAuth.getCurrentUser().getEmail());
        user.setUid(mAuth.getCurrentUser().getUid());
        user.setPhotoPath(Objects.requireNonNull(mAuth.getCurrentUser().getPhotoUrl()).getPath());
        return user;
    }

    /**
     * Obtiene en que servicio se encuentra alojada la foto del usuario
     * @return enum ProfilePhotoHost con respuesta
     */
    public ProfilePhotoHost getProfilePhotoHost (){
        String photoPath = Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getPhotoUrl()).toString();//obtener link de la foto
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
    public Task<byte[]> getFirebaseProfilePhoto() throws IOException {
        String photoPath = Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getPhotoUrl()).toString();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();//obtenemos referencia a la bd
        StorageReference sRef = storage.getReferenceFromUrl(photoPath);//obtenemos la referencia al link
        return sRef.getBytes(1024);
    }

    /**
     * Obtener foto de perfil de usuario de Facebook o Google
     * @param customListener listener con acciones de exito o error
     */
    public void getExternalProfilePhoto(DownloadImageProcess.CustomListener customListener){
        String photoPath = Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getPhotoUrl()).toString();
        for (UserInfo profile : Objects.requireNonNull(mAuth.getCurrentUser()).getProviderData()) {
            // check if the provider id matches "facebook.com"
            if (profile.getProviderId().equals("facebook.com")) {
                String facebookUserId = profile.getUid();
                photoPath = "https://graph.facebook.com/" + facebookUserId + "/picture?type=medium";
            }
        }
        DownloadImageProcess dip = new DownloadImageProcess();
        dip.setListener(customListener);
        dip.execute(photoPath);
    }

    /**
     * Obtener la foto de perfil de los archivos locales
     * @param context contexto actual
     * @return la foto en formato bitmap o nulo si no se encontró
     */
    public Bitmap getLocalProfilePhoto(Context context){
        String pictureFile = "userPhoto-" +mAuth.getCurrentUser().getUid()+".jpg";
        File storageDir = context.getFilesDir();
        File file=new File(storageDir,pictureFile);
        if(file.exists()){
            return BitmapFactory.decodeFile(file.getPath());
        }else{
            return null;
        }
    }

    public void saveProfilePhotoInLocal(Context context,Bitmap photo) throws IOException {
        String pictureFile = "userPhoto-" + Objects.requireNonNull(mAuth.getCurrentUser()).getUid()+".JPEG";
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


}
