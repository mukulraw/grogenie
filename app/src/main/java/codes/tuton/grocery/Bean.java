package codes.tuton.grocery;

import android.app.Application;
import android.content.Context;


public class Bean extends Application {
    private static Context context;

    public String baseurl = "https://mrtecks.com/";


    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        //ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));

    }
}
