package natanael.contactmanagement.view;

import natanael.contactmanagement.model.ContactDetail;

public interface IAddContactView
{
    void onFinished(ContactDetail contactDetail);

    void onFailure(String message);
}