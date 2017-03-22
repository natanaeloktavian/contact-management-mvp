package natanael.contactmanagement.presenter;

import java.util.ArrayList;
import java.util.Collections;

import natanael.contactmanagement.model.Contact;
import natanael.contactmanagement.model.ContactComparator;
import natanael.contactmanagement.model.IContactListRepository;
import natanael.contactmanagement.view.IContactListView;

public class ContactListPresenter implements IContactListPresenter, IContactListRepository.OnFinishedListener
{
    private IContactListView contactListView;
    private IContactListRepository contactListRepository;
    private ArrayList<Contact> items;

    public ContactListPresenter(IContactListView mainView,IContactListRepository contactListRepository)
    {
        this.contactListView = mainView;
        this.contactListRepository = contactListRepository;
    }

    @Override
    public void loadContacts()
    {
        if(contactListRepository!=null)
            contactListRepository.loadContacts(this);
    }

    @Override
    public void onItemClicked(int position)
    {
        if (contactListView != null && items!=null)
            contactListView.onItemClicked(items.get(position));
    }

    @Override public void onDestroy()
    {
        contactListView = null;
    }

    @Override
    public void onDataLoaded(ArrayList<Contact> items)
    {
        Collections.sort(items, new ContactComparator());
        this.items = items;
        if(contactListView!=null)
            contactListView.onDataLoaded(items);
    }

    @Override
    public void onFailure(String message)
    {
        if(contactListView!=null)
            contactListView.onFailure(message);
    }
}