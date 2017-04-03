package natanael.contactmanagement.model;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import natanael.contactmanagement.rest.ApiClient;
import natanael.contactmanagement.rest.ApiInterface;

public class AddContactRepository implements IAddContactRepository
{
    @Inject Retrofit retrofit;

    @Override
    public void addContact(final OnFinishedListener listener,final ContactDetail contactDetail)
    {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ContactDetail> call = apiService.pushContacts(contactDetail);
        call.enqueue(new Callback<ContactDetail>()
        {
            @Override
            public void onResponse(Call<ContactDetail>call, Response<ContactDetail> response)
            {
                ContactDetail contacts = response.body();
                listener.onFinished(contacts);
            }

            @Override
            public void onFailure(Call<ContactDetail>call, Throwable t)
            {
                listener.onFailure(t.getMessage());
            }
        });
    }
}