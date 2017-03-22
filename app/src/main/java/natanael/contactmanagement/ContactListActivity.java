package natanael.contactmanagement;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import natanael.contactmanagement.adapter.ContactAdapter;
import natanael.contactmanagement.model.Contact;
import natanael.contactmanagement.model.ContactListRepository;
import natanael.contactmanagement.model.RecyclerItemClickListener;
import natanael.contactmanagement.presenter.ContactListPresenter;
import natanael.contactmanagement.presenter.IContactListPresenter;
import natanael.contactmanagement.view.IContactListView;

public class ContactListActivity extends AppCompatActivity implements IContactListView
{
    private TextView emptyLabel;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private CoordinatorLayout coordinatorLayout;
    private IContactListPresenter presenter;

    private ContactAdapter contactAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        this.setReference();
        presenter.loadContacts();
    }

    private void setReference()
    {
        coordinatorLayout = (CoordinatorLayout) this.findViewById(R.id.coordinatorLayout);
        recyclerView = (RecyclerView) this.findViewById(R.id.recyclerView);
        fab = (FloatingActionButton)this.findViewById(R.id.fab);
        emptyLabel = (TextView)this.findViewById(R.id.emptyLabel);

        presenter = new ContactListPresenter(this, new ContactListRepository());
    }

    @Override
    protected void onDestroy()
    {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onDataLoaded(ArrayList<Contact> contacts)
    {
        if(contacts==null || contacts.size()==0)
        {
            emptyLabel.setVisibility(View.VISIBLE);
        }
        else
        {
            emptyLabel.setVisibility(View.GONE);
            contactAdapter = new ContactAdapter(getApplicationContext(), contacts);
            if (recyclerView != null)
                this.initializeRecyclerView(recyclerView, contactAdapter);
        }
    }

    @Override
    public void onItemClicked(Contact contact)
    {

    }

    @Override
    public void onFailure(String message)
    {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void initializeRecyclerView(final RecyclerView recyclerView, final RecyclerView.Adapter adapter)
    {
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this)
        {
            @Override
            public boolean canScrollVertically()
            {
                return  true;
            }
        };

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this.getApplicationContext(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener()
                {
                    @Override public void onItemClick(View view, int position)
                    {
                        presenter.onItemClicked(position);
                    }

                    @Override public void onLongItemClick(View view, int position)
                    {

                    }
                })
        );

        recyclerView.setAdapter(adapter);
    }
}
