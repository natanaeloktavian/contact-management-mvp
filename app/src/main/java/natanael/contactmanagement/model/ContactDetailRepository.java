package natanael.contactmanagement.model;

import natanael.contactmanagement.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ContactDetailRepository implements IContactDetailRepository
{
    private Retrofit mRetrofit;

    public ContactDetailRepository (Retrofit retrofit)
    {
        this.mRetrofit = retrofit;
    }

    @Override
    public void loadContactDetails(final OnFinishedListener listener,final String url)
    {
        ApiInterface apiService = mRetrofit.create(ApiInterface.class);

        Call<ContactDetail> call = apiService.getContactDetails(url);
        call.enqueue(new Callback<ContactDetail>()
        {
            @Override
            public void onResponse(Call<ContactDetail>call, Response<ContactDetail> response)
            {
                ContactDetail contacts = response.body();
                listener.onDataLoaded(contacts);
            }

            @Override
            public void onFailure(Call<ContactDetail>call, Throwable t)
            {
            }
        });
    }

    @Override
    public void updateFavorite(final OnFinishedListener listener, String url, ContactDetail contactDetail)
    {
        ApiInterface apiService = mRetrofit.create(ApiInterface.class);

        Call<ContactDetail> call = apiService.updateFavorite(url,contactDetail);
        call.enqueue(new Callback<ContactDetail>()
        {
            @Override
            public void onResponse(Call<ContactDetail>call, Response<ContactDetail> response)
            {
                ContactDetail contacts = response.body();
                listener.OnFavoriteChanged(contacts);
            }

            @Override
            public void onFailure(Call<ContactDetail>call, Throwable t)
            {

            }
        });
    }
}