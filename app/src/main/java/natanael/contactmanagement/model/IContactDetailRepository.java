package natanael.contactmanagement.model;

public interface IContactDetailRepository
{
    interface OnFinishedListener
    {
        void onDataLoaded(ContactDetail item);
        void OnFavoriteChanged(ContactDetail item);
    }

    void loadContactDetails(OnFinishedListener listener,String url);

    void updateFavorite(OnFinishedListener listener,String url,ContactDetail contactDetail);
}