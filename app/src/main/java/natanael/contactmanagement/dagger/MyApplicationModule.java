package natanael.contactmanagement.dagger;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class MyApplicationModule
{
    Application mApplication;

    public MyApplicationModule(Application mApplication)
    {
        this.mApplication = mApplication;
    }

    @Provides
    @Singleton
    Application provideApplication()
    {
        return mApplication;
    }
}