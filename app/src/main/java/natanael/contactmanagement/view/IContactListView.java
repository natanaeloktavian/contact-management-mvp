package natanael.contactmanagement.view;

import java.util.ArrayList;

import natanael.contactmanagement.model.Contact;

public interface IContactListView
{
    void onDataLoaded(ArrayList<Contact> contacts);

    void onItemClicked(Contact contact);

    void onFailure(String message);
}