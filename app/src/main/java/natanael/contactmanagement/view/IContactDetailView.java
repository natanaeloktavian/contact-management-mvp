package natanael.contactmanagement.view;

import java.util.ArrayList;

import natanael.contactmanagement.model.Contact;
import natanael.contactmanagement.model.ContactDetail;

public interface IContactDetailView
{
    void setItem(ContactDetail contactDetail);
    void onFavoriteChanged(ContactDetail contactDetail);
}