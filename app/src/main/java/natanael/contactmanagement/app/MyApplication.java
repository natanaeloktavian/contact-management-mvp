package natanael.contactmanagement.app;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import natanael.contactmanagement.dagger.DaggerRetrofitComponent;
import natanael.contactmanagement.dagger.MyApplicationModule;
import natanael.contactmanagement.dagger.RetrofitComponent;
import natanael.contactmanagement.dagger.RetrofitModule;

public class MyApplication extends Application
{
    private RetrofitComponent mRetrofitComponent;

    @Override
    public void onCreate()
    {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        mRetrofitComponent = DaggerRetrofitComponent.builder()
                .myApplicationModule(new MyApplicationModule(this))
                .retrofitModule(new RetrofitModule("http://gojek-contacts-app.herokuapp.com/"))
                .build();

    }

    public RetrofitComponent getRetrofitComponent()
    {
        return mRetrofitComponent;
    }
}