package com.nordokod.scio.process;

import android.content.Context;

import com.facebook.FacebookException;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageException;
import com.nordokod.scio.constants.ErrorMessage;
import com.nordokod.scio.entity.InputDataException;
import com.nordokod.scio.entity.InvalidValueException;
import com.nordokod.scio.entity.OperationCanceledException;
import com.nordokod.scio.entity.PermissionException;
import com.nordokod.scio.entity.PhoneNetworkException;


public class ExceptionManager {
    public ErrorMessage categorizeException(Exception exception){
        //errores de conexión
        if(exception instanceof PhoneNetworkException){
            //dispositivo sin conexión
            return ErrorMessage.NETWORK_PHONE;
        }
        else if(exception instanceof FirebaseNetworkException){
            //error en conexión con firebase
            return ErrorMessage.NETWORK_FIREABSE;
        }
        //errores de autenticación
        else if(exception instanceof FirebaseAuthInvalidCredentialsException){
            //credenciales invalidas al iniciar sesión
            return ErrorMessage.AUTH_INVALID_CREDENTIALS;
        }
        else if(exception instanceof FirebaseAuthEmailException){
            //error al enviar email
            return ErrorMessage.AUTH_SEND_EMAIL;
        }
        else if(exception instanceof FirebaseAuthInvalidUserException){
            //usuario inactivo o eliminado, no puede iniciar sesión
            return ErrorMessage.AUTH_INVALID_USER;
        }
        else if(exception instanceof FirebaseAuthUserCollisionException){
            //el usuario ya se encuentra registrado
            return ErrorMessage.AUTH_USER_COLLISION;
        }
        else if(exception instanceof FirebaseAuthException){
            //error desconocido en FirebaseAuth
            return ErrorMessage.AUTH_UNKNOW;
        }
        //errores de firestore
        else if(exception instanceof FirebaseFirestoreException){
            FirebaseFirestoreException firebaseFirestoreException=(FirebaseFirestoreException)exception;
            switch (firebaseFirestoreException.getCode()){
                case ABORTED:break;
                case ALREADY_EXISTS:break;
                case CANCELLED:break;
                case DATA_LOSS:break;
                case DEADLINE_EXCEEDED:break;
                case INTERNAL:break;
                case INVALID_ARGUMENT:break;
                case NOT_FOUND:break;
                case OUT_OF_RANGE:break;
                case PERMISSION_DENIED:break;
                case RESOURCE_EXHAUSTED:break;
                case UNAUTHENTICATED:break;
                case UNAVAILABLE:break;
                case UNIMPLEMENTED:break;
                case UNKNOWN:break;
            }
            return ErrorMessage.FIRESTORE_EXCEPTION;
        }
        //errores de firebase storage
        else if(exception instanceof StorageException){
            StorageException storageException=(StorageException)exception;
            switch (storageException.getErrorCode()){
                case StorageException.ERROR_BUCKET_NOT_FOUND: break;
                case StorageException.ERROR_CANCELED: break;
                case StorageException.ERROR_INVALID_CHECKSUM: break;
                case StorageException.ERROR_NOT_AUTHENTICATED: break;
                case StorageException.ERROR_NOT_AUTHORIZED: break;
                case StorageException.ERROR_OBJECT_NOT_FOUND: break;
                case StorageException.ERROR_PROJECT_NOT_FOUND: break;
                case StorageException.ERROR_QUOTA_EXCEEDED: break;
                case StorageException.ERROR_RETRY_LIMIT_EXCEEDED: break;
                case StorageException.ERROR_UNKNOWN: break;
            }
            return ErrorMessage.STORAGE_EXCEPTION;
        }
        //error desconocido de Firebase
        else if(exception instanceof FirebaseException){
            return ErrorMessage.FIREBASE_EXCEPTION;
        }
        //error autenticación de Facebook
        else if(exception instanceof FacebookException){
            return ErrorMessage.FACEBOOK_EXCEPTION;
        }
        //error de autenticación de Google
        else if(exception instanceof GoogleAuthException){
            return ErrorMessage.GOOGLE_EXCEPTION;
        }
        //otros errores
        else if(exception instanceof PermissionException){
            //el usuario no tiene permisos para la operación
            return ErrorMessage.USER_PERMISSION;
        }
        else if(exception instanceof OperationCanceledException){
            //operación cancelada por el usuario
            return ErrorMessage.CANCELED_OPERATION;
        }
        else if(exception instanceof InvalidValueException){
            //algun dato no fue correctamente seteado
            return ErrorMessage.INVALID_INTERN_VALUE;
        }
        else if(exception instanceof InputDataException){
            //dato introducido inválido
            switch (((InputDataException) exception).getCode()){
                case DATETIME_AFTER:return ErrorMessage.DATETIME_AFTER;
                case DATETIME_BEFORE:return ErrorMessage.DATETIME_BEFORE;
                case INVALID_MAIL:return ErrorMessage.INVALID_MAIL;
                case INVALID_PASSWORD:return ErrorMessage.INVALID_PASSWORD;
                case INVALID_USERNAME:return ErrorMessage.INVALID_USERNAME;
                case PASSWORDS_DONT_MATCH:return ErrorMessage.PASSWORDS_DONT_MATCH;
            }
        }
        else{
            //cualquier otro error
            return ErrorMessage.UNKNOW_EXCEPTION;
        }
        return ErrorMessage.UNKNOW_EXCEPTION;
    }
    public void showErrorMessage(Context context,ErrorMessage errorMessage){
        //error message tendra los int de los titulos, descripción del error e icono
        if(errorMessage!=ErrorMessage.CANCELED_OPERATION){//MOSTRAR SÓLO SI NO ES OPERACIÓN CANCELADA POR USUARIO
            
        }
    }
}
