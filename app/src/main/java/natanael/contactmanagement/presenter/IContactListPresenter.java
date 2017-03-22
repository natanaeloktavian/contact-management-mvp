package natanael.contactmanagement.presenter;

public interface IContactListPresenter
{
    void loadContacts();

    void onItemClicked(int position);

    void onDestroy();
}