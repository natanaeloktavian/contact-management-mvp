package natanael.contactmanagement.model;

import java.util.ArrayList;

public interface IContactListRepository
{
    interface OnFinishedListener
    {
        void onDataLoaded(ArrayList<Contact> items);
        void onFailure(String message);
    }

    void loadContacts(OnFinishedListener listener);
}