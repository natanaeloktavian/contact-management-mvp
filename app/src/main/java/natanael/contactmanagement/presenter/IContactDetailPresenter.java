package natanael.contactmanagement.presenter;

import natanael.contactmanagement.model.ContactDetail;

public interface IContactDetailPresenter
{
    void loadContactDetail(String url);
    void updateFavorite(String url, ContactDetail contactDetail);

    void onDestroy();
}