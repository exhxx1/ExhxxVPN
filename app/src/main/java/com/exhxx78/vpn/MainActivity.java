package com.exhxx78.vpn;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private boolean isConnected = false;
    private Button btnConnect;
    private TextView statusText;

    private GradientDrawable getRoundedBg(String colorStr, float radius) {
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.RECTANGLE);
        shape.setCornerRadius(radius);
        shape.setColor(Color.parseColor(colorStr));
        return shape;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setBackgroundColor(Color.parseColor("#0F172A")); // لون ليلي فخم

        TextView title = new TextView(this);
        title.setText("Exhxx VPN 🛡️\nالمطور: محمد عدنان");
        title.setTextColor(Color.parseColor("#38BDF8"));
        title.setTextSize(24);
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 0, 0, 100);

        statusText = new TextView(this);
        statusText.setText("غير متصل");
        statusText.setTextColor(Color.parseColor("#94A3B8"));
        statusText.setTextSize(18);
        statusText.setGravity(Gravity.CENTER);
        statusText.setPadding(0, 0, 0, 40);

        btnConnect = new Button(this);
        btnConnect.setText("اتـصـال");
        btnConnect.setBackground(getRoundedBg("#16A34A", 100f)); // زر دائري أخضر
        btnConnect.setTextColor(Color.WHITE);
        btnConnect.setTextSize(22);
        btnConnect.setPadding(60, 40, 60, 40);

        layout.addView(title);
        layout.addView(statusText);
        layout.addView(btnConnect);
        
        setContentView(layout);

        btnConnect.setOnClickListener(v -> toggleVpn());
    }

    private void toggleVpn() {
        if (!isConnected) {
            // محاكاة الاتصال (المرحلة القادمة سيتم ربطها بالمحرك)
            isConnected = true;
            statusText.setText("متصل بالسيرفر: 51.254.130.47 🟢");
            statusText.setTextColor(Color.parseColor("#22C55E"));
            btnConnect.setText("قـطـع الاتـصـال");
            btnConnect.setBackground(getRoundedBg("#DC2626", 100f)); // يتحول أحمر
            Toast.makeText(this, "جاري تهيئة Xray Core...", Toast.LENGTH_SHORT).show();
        } else {
            isConnected = false;
            statusText.setText("غير متصل 🔴");
            statusText.setTextColor(Color.parseColor("#94A3B8"));
            btnConnect.setText("اتـصـال");
            btnConnect.setBackground(getRoundedBg("#16A34A", 100f)); // يرجع أخضر
        }
    }
}
