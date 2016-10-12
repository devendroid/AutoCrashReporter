package crashreporter.devs.com.myapplication;

import android.app.Application;
import com.devs.acr.AutoErrorReporter;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AutoErrorReporter.get(this)
                .setEmailAddresses("devs.ideal@gmail.com")
                .setEmailSubject("Auto Crash Report")
                .start();
    }
}