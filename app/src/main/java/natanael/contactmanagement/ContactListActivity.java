package natanael.contactmanagement;

import android.content.Intent;
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
import java.util.Iterator;

import javax.inject.Inject;

import io.realm.RealmResults;
import natanael.contactmanagement.activity.AddContactActivity;
import natanael.contactmanagement.activity.ContactDetailActivity;
import natanael.contactmanagement.adapter.ContactAdapter;
import natanael.contactmanagement.app.MyApplication;
import natanael.contactmanagement.model.Contact;
import natanael.contactmanagement.model.ContactListRepository;
import natanael.contactmanagement.model.RecyclerItemClickListener;
import natanael.contactmanagement.presenter.ContactListPresenter;
import natanael.contactmanagement.presenter.IContactListPresenter;
import natanael.contactmanagement.realm.RealmController;
import natanael.contactmanagement.view.IContactListView;
import retrofit2.Retrofit;

public class ContactListActivity extends AppCompatActivity implements IContactListView
{
    @Inject Retrofit mRetrofit;

    private TextView emptyLabel;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private CoordinatorLayout coordinatorLayout;
    private IContactListPresenter presenter;

    private ContactAdapter contactAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    static final int ADD_CONTACT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        this.setReference();
        this.setAction();

        presenter.loadContacts();
    }

    private void setReference()
    {
        coordinatorLayout = (CoordinatorLayout) this.findViewById(R.id.coordinatorLayout);
        recyclerView = (RecyclerView) this.findViewById(R.id.recyclerView);
        fab = (FloatingActionButton)this.findViewById(R.id.fab);
        emptyLabel = (TextView)this.findViewById(R.id.emptyLabel);

        ((MyApplication) getApplication()).getRetrofitComponent().inject(this);
        presenter = new ContactListPresenter(this, new ContactListRepository(mRetrofit));
    }

    private void setAction()
    {
        if(fab!=null)
        {
            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(getApplicationContext(),AddContactActivity.class);
                    startActivityForResult(intent,ADD_CONTACT_REQUEST);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==ADD_CONTACT_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {
                presenter.loadContacts();
            }
        }
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

            RealmController.with(this).insertContacts(contacts);
        }
    }

    @Override
    public void onItemClicked(Contact contact)
    {
        Intent intent = new Intent(getApplicationContext(),ContactDetailActivity.class);
        intent.putExtra("id", contact.getId());
        startActivity(intent);
    }

    @Override
    public void onFailure(String message)
    {
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Load from internet failed. Loading from local database...", Snackbar.LENGTH_LONG);
        snackbar.show();

        this.loadFromLocalDatabase();
    }

    public void loadFromLocalDatabase()
    {
        ArrayList<Contact> contacts = new ArrayList<>();
        RealmResults<Contact> results = RealmController.with(this).getContacts();
        Iterator<Contact> iterator = results.iterator();
        while (iterator.hasNext())
        {
            Contact contact = iterator.next();
            contacts.add(contact);
        }
        this.onDataLoaded(contacts);
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
