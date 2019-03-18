package com.nordokod.scio.process;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

import com.nordokod.scio.Entidad.ConfigurationApp;

import java.util.Objects;

public class SystemsEventsProcess extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("testeo","holo");
        Log.d("testeo",intent.getAction());
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        assert pm != null;
        boolean screenState = pm.isInteractive();


        SystemWriteProcess swp=new SystemWriteProcess(context);
        ConfigurationApp appconf;
        Intent servicioApps = new Intent(context,LockAppProcess.class);
        Log.d("testeo",intent.getAction());
        switch (Objects.requireNonNull(intent.getAction())){
            case Intent.ACTION_BOOT_COMPLETED://activar al encender
                context.startService(servicioApps);
                break;
            case Intent.ACTION_PACKAGE_ADDED://actualizar lista apps
                appconf=swp.getAllApps();
                swp.saveUserConfig(appconf);
                break;
            case Intent.ACTION_PACKAGE_REMOVED://actualizar lista apps
                appconf=swp.getAllApps();
                swp.saveUserConfig(appconf);
                break;
            case Intent.ACTION_SCREEN_OFF://detener al bloquear
                Log.d("testeo","screen off");
                context.stopService(servicioApps);
                break;
            case Intent.ACTION_SCREEN_ON://activar al desbloquear y buen nivel de carga
                Log.d("testeo","screen on ");

                    context.startService(servicioApps);

                break;
            case Intent.ACTION_BATTERY_LOW://desactivar con bajo nivel de carga
                context.stopService(servicioApps);
                break;
            case Intent.ACTION_BATTERY_OKAY://activar si la carga está bien y no esta apagada la pantalla
                if(screenState){
                    context.startService(servicioApps);
                }
                break;

        }
    }
}
