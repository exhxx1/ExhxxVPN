package com.exhxx78.vpn;

import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ExhxxVpnService extends VpnService {
    private Thread vpnThread;
    private ParcelFileDescriptor vpnInterface;
    private boolean isRunning = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "النفق جاهز! (بانتظار دمج ملف محرك Xray.aar لاحقاً)", Toast.LENGTH_LONG).show();
        isRunning = true;
        
        vpnThread = new Thread(() -> {
            try {
                // استخراج الكونفج (جاهز للعمل)
                File configFile = new File(getFilesDir(), "config.json");
                if (!configFile.exists()) {
                    InputStream is = getAssets().open("config.json");
                    FileOutputStream fos = new FileOutputStream(configFile);
                    byte[] buffer = new byte[1024];
                    int byteCount;
                    while ((byteCount = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, byteCount);
                    }
                    fos.flush(); fos.close(); is.close();
                }

                // بناء النفق وسحب الترافيك
                Builder builder = new Builder();
                builder.setSession("ExhxxVPN");
                builder.addAddress("10.0.0.2", 24);
                builder.addDnsServer("8.8.8.8");
                builder.addRoute("0.0.0.0", 0);
                vpnInterface = builder.establish();
                
                Log.d("ExhxxVPN", "النفق متصل. يحتاج ملف AAR لفك تشفير VLESS.");
                
                while (isRunning) {
                    Thread.sleep(1000);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        vpnThread.start();
        
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        try {
            if (vpnInterface != null) {
                vpnInterface.close();
                vpnInterface = null;
            }
            if (vpnThread != null) {
                vpnThread.interrupt();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "🔴 تم قطع الاتصال.", Toast.LENGTH_SHORT).show();
    }
}
