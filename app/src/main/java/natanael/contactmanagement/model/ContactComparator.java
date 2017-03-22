package natanael.contactmanagement.model;

import java.util.Comparator;

public class ContactComparator implements Comparator<Contact>
{
    public int compare(Contact left, Contact right)
    {
        if(left.getFirstName()!=null && left.getFirstName()!=null)
        {
            char a = left.getFirstName().charAt(0);
            char b = right.getFirstName().charAt(0);

            return left.getFirstName().compareToIgnoreCase(right.getFirstName());
        }
        return 0;
    }
}