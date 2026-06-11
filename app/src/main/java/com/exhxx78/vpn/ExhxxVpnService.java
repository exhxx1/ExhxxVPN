package com.exhxx78.vpn;

import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

public class ExhxxVpnService extends VpnService {
    private Thread vpnThread;
    private ParcelFileDescriptor vpnInterface;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "جاري تحويل مسار الإنترنت...", Toast.LENGTH_SHORT).show();
        
        vpnThread = new Thread(() -> {
            try {
                // تكوين واجهة الشبكة الوهمية (TUN)
                Builder builder = new Builder();
                builder.setSession("ExhxxVPN");
                builder.addAddress("10.0.0.2", 24); // IP داخلي وهمي
                builder.addDnsServer("8.8.8.8"); // Google DNS
                builder.addRoute("0.0.0.0", 0); // سحب كل انترنت الجهاز

                vpnInterface = builder.establish();
                
                // هنا سيتم ربط محرك Xray-core لاحقاً لقراءة الكونفج
                Log.d("ExhxxVPN", "تم إنشاء واجهة TUN بنجاح، بانتظار ربط محرك Xray...");

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
        Toast.makeText(this, "تم إغلاق الاتصال.", Toast.LENGTH_SHORT).show();
    }
}
