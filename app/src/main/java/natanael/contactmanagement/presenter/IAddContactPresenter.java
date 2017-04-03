package natanael.contactmanagement.presenter;

import natanael.contactmanagement.model.ContactDetail;

public interface IAddContactPresenter
{
    void addContact(ContactDetail coontactDetail);

    void onDestroy();
}