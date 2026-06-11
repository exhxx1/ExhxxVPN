package com.exhxx78.vpn;

import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import libxray.Libxray; // استدعاء مكتبة المحرك الأساسية

public class ExhxxVpnService extends VpnService {
    private Thread vpnThread;
    private ParcelFileDescriptor vpnInterface;
    private boolean isRunning = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "🚀 جاري تشغيل محرك Exhxx Xray...", Toast.LENGTH_SHORT).show();
        isRunning = true;
        
        vpnThread = new Thread(() -> {
            try {
                // 1. استخراج ملف الـ JSON المخفي بداخل التطبيق وحفظه بملفات النظام المؤقتة
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

                // 2. بناء واجهة الشبكة (TUN) وتوجيه كل ترافيك الجهاز لها
                Builder builder = new Builder();
                builder.setSession("ExhxxVPN");
                builder.addAddress("10.0.0.2", 24);
                builder.addDnsServer("8.8.8.8");
                builder.addRoute("0.0.0.0", 0); // سحب الإنترنت بالكامل
                vpnInterface = builder.establish();

                // 3. إطلاق الصاروخ! تشغيل محرك Xray وتمرير ملف الكونفج له برمجياً
                Log.d("ExhxxVPN", "جاري ربط الكونفج بالمحرك...");
                
                // أمر تشغيل النواة (Xray Core) من المكتبة المستدعاة
                Libxray.runXray(configFile.getAbsolutePath());
                
                while (isRunning) {
                    // إبقاء الخدمة حية ومستقرة بداخل النفق
                    Thread.sleep(1000);
                }

            } catch (Exception e) {
                Log.e("ExhxxVPN", "خطأ في تشغيل المحرك: " + e.getMessage());
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
            // إيقاف محرك Xray برمجياً عند قطع الاتصال
            Libxray.stopXray();
            
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
        Toast.makeText(this, "🔴 تم قطع الاتصال وإغلاق المحرك.", Toast.LENGTH_SHORT).show();
    }
}
