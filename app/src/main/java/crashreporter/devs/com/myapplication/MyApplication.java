package crashreporter.devs.com.myapplication;

import android.app.Application;
import com.devs.acr.AutoErrorReporter;

/**
 *  Created by devendra on 2/5/16.
 */


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AutoErrorReporter.getInstance().inIt(this);
    }
}