package natanael.contactmanagement.dagger;

import javax.inject.Singleton;

import dagger.Component;
import natanael.contactmanagement.ContactListActivity;

@Singleton
@Component(modules={MyApplicationModule.class,RetrofitModule.class})
public interface RetrofitComponent
{
    void inject(ContactListActivity activity);
}