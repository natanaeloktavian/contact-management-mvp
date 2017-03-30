package natanael.contactmanagement.presenter;

import natanael.contactmanagement.model.ContactDetail;
import natanael.contactmanagement.model.IContactDetailRepository;
import natanael.contactmanagement.view.IContactDetailView;

public class ContactDetailPresenter implements IContactDetailPresenter, IContactDetailRepository.OnFinishedListener
{
    private IContactDetailView mainView;
    private IContactDetailRepository contactDetailRepository;

    public ContactDetailPresenter(IContactDetailView mainView, IContactDetailRepository findItemsInteractor)
    {
        this.mainView = mainView;
        this.contactDetailRepository = findItemsInteractor;
    }

    public IContactDetailView getMainView()
    {
        return mainView;
    }

    @Override
    public void loadContactDetail(String url)
    {
        if(contactDetailRepository!=null)
            contactDetailRepository.loadContactDetails(this, url);
    }

    @Override
    public void updateFavorite(String url, ContactDetail contactDetail)
    {
        if(contactDetailRepository!=null)
            contactDetailRepository.updateFavorite(this, url, contactDetail);
    }

    @Override public void onDestroy()
    {
        mainView = null;
    }

    @Override
    public void onDataLoaded(ContactDetail item)
    {
        if(mainView!=null)
            mainView.setItem(item);
    }

    @Override
    public void OnFavoriteChanged(ContactDetail item)
    {
        if(mainView!=null)
            mainView.onFavoriteChanged(item);
    }
}