package crashreporter.devs.com.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.devs.acr.AutoErrorReporter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((Button)findViewById(R.id.btn_crash)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = null;
                s.length();
            }
        });
        
        
    }
}
