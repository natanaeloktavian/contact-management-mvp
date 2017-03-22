package natanael.contactmanagement.rest;

import java.util.ArrayList;
import natanael.contactmanagement.model.Contact;
import retrofit2.Call;
import retrofit2.http.GET;
import rx.Observable;

public interface ApiInterface
{
    //@GET("contacts.json")
    //Call<ArrayList<Contact>> getContacts();

    @GET("contacts.json")
    Observable<ArrayList<Contact>> getContacts();
}