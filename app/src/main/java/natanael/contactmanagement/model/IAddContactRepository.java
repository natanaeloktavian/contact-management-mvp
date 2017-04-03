package natanael.contactmanagement.model;

import java.util.ArrayList;

public interface IAddContactRepository
{
    interface OnFinishedListener
    {
        void onFinished(ContactDetail item);
        void onFailure(String message);
    }

    void addContact(OnFinishedListener listener,ContactDetail contactDetail);
}