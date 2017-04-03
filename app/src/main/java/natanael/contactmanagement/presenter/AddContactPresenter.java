package natanael.contactmanagement.presenter;

import natanael.contactmanagement.model.ContactDetail;
import natanael.contactmanagement.model.IAddContactRepository;
import natanael.contactmanagement.model.IContactDetailRepository;
import natanael.contactmanagement.view.IAddContactView;
import natanael.contactmanagement.view.IContactDetailView;

public class AddContactPresenter implements IAddContactPresenter, IAddContactRepository.OnFinishedListener
{
    private IAddContactView mainView;
    private IAddContactRepository addContactRepository;

    public AddContactPresenter(IAddContactView mainView, IAddContactRepository findItemsInteractor)
    {
        this.mainView = mainView;
        this.addContactRepository = findItemsInteractor;
    }

    public IAddContactView getMainView()
    {
        return mainView;
    }

    @Override
    public void addContact(ContactDetail contactDetail)
    {
        if(addContactRepository!=null)
            addContactRepository.addContact(this,contactDetail);
    }

    @Override public void onDestroy()
    {
        mainView = null;
    }

    @Override
    public void onFinished(ContactDetail item)
    {
        if(mainView!=null)
            mainView.onFinished(item);
    }

    @Override
    public void onFailure(String message)
    {
        if(mainView!=null)
            mainView.onFailure(message);
    }
}