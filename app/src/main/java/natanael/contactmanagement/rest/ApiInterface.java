package natanael.contactmanagement.rest;

import java.util.ArrayList;
import natanael.contactmanagement.model.Contact;
import natanael.contactmanagement.model.ContactDetail;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Url;
import rx.Observable;

public interface ApiInterface
{
    //@GET("contacts.json")
    //Call<ArrayList<Contact>> getContacts();

    @GET("contacts.json")
    Observable<ArrayList<Contact>> getContacts();

    @GET
    Call<ContactDetail> getContactDetails(@Url String url);

    @PUT
    Call <ContactDetail> updateFavorite(@Url String url,@Body ContactDetail contactDetail);
}