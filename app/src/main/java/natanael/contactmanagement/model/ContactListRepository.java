package natanael.contactmanagement.model;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import natanael.contactmanagement.rest.ApiClient;
import natanael.contactmanagement.rest.ApiInterface;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContactListRepository implements IContactListRepository
{
    @Override
    public void loadContacts(final OnFinishedListener listener)
    {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        /*Call<ArrayList<Contact>> call = apiService.getContacts();
        call.enqueue(new Callback<ArrayList<Contact>>()
        {
            @Override
            public void onResponse(Call<ArrayList<Contact>>call, Response<ArrayList<Contact>> response)
            {
                ArrayList<Contact> contacts = response.body();
                listener.onDataLoaded(contacts);
            }

            @Override
            public void onFailure(Call<ArrayList<Contact>>call, Throwable t)
            {
                listener.onFailure(t.getMessage());
            }
        });*/

        //Using Rx Android
        Observable<ArrayList<Contact>> getContactRequest = apiService.getContacts();
        getContactRequest.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<Contact>>()
                {
                    @Override
                    public final void onCompleted()
                    {
                    }

                    @Override
                    public final void onError(Throwable e)
                    {
                        listener.onFailure(e.getMessage());
                    }

                    @Override
                    public final void onNext(ArrayList<Contact> response)
                    {
                        listener.onDataLoaded(response);
                    }
                });
    }
}