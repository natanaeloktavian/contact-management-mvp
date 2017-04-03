package natanael.contactmanagement.realm;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import natanael.contactmanagement.model.Contact;

public class RealmController
{
    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application)
    {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment)
    {
        if (instance == null)
            instance = new RealmController(fragment.getActivity().getApplication());

        return instance;
    }

    public static RealmController with(Activity activity)
    {

        if (instance == null)
            instance = new RealmController(activity.getApplication());

        return instance;
    }

    public static RealmController with(Application application)
    {

        if (instance == null)
            instance = new RealmController(application);

        return instance;
    }

    public static RealmController getInstance()
    {
        return instance;
    }

    public Realm getRealm()
    {
        return realm;
    }

    //find all objects in the Book.class
    public RealmResults<Contact> getContacts()
    {
        return realm.where(Contact.class).findAll();
    }

    //query a single item with the given id
    public Contact getContact(String id)
    {
        return realm.where(Contact.class).equalTo("id", id).findFirst();
    }

    public void insertContacts(ArrayList<Contact> contacts)
    {
        realm.beginTransaction();
        for(Contact contact : contacts)
        {
            realm.insertOrUpdate(contact);
        }
        realm.commitTransaction();
    }

    //query example
    public RealmResults<Contact> queryedContact()
    {
        return realm.where(Contact.class)
                .contains("first_name", "natanael")
                .or()
                .contains("last_name", "natanael")
                .findAll();
    }
}