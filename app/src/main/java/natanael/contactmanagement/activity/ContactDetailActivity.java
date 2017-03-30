package natanael.contactmanagement.activity;

import natanael.contactmanagement.R;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import natanael.contactmanagement.app.MyApplication;
import natanael.contactmanagement.model.ContactDetail;
import natanael.contactmanagement.model.ContactDetailRepository;
import natanael.contactmanagement.presenter.ContactDetailPresenter;
import natanael.contactmanagement.presenter.IContactDetailPresenter;
import natanael.contactmanagement.view.IContactDetailView;
import retrofit2.Retrofit;

public class ContactDetailActivity extends AppCompatActivity implements IContactDetailView
{
    @Inject
    Retrofit mRetrofit;

    private IContactDetailPresenter presenter;
    private Toolbar toolbar;
    private ImageView favIcon,phoneIcon,emailIcon,imageView;
    private TextView nameLabel,phoneLabel,emailLabel;
    private ContactDetail contactDetail;
    private String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_detail);
        initialize();
    }

    private void initialize()
    {
        toolbar = (Toolbar) this.findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        setReference();
        setEventHandler();

        id = getIntent().getStringExtra("id");
        ((MyApplication) getApplication()).getRetrofitComponent().inject(this);
        presenter = new ContactDetailPresenter(this, new ContactDetailRepository(mRetrofit));
        presenter.loadContactDetail("contacts/"+id+".json");
    }

    private void setReference()
    {
        nameLabel = (TextView)this.findViewById(R.id.nameLabel);
        phoneLabel = (TextView)this.findViewById(R.id.phoneLabel);
        emailLabel = (TextView)this.findViewById(R.id.emailLabel);
        imageView = (ImageView)this.findViewById(R.id.imageView);
        favIcon = (ImageView)this.findViewById(R.id.favIcon);
        emailIcon = (ImageView)this.findViewById(R.id.emailIcon);
        phoneIcon = (ImageView)this.findViewById(R.id.phoneIcon);
    }

    private void setEventHandler()
    {
        if(favIcon!=null)
        {
            favIcon.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    contactDetail.setFavorite(!contactDetail.getFavorite());

                    if (contactDetail.getFavorite())
                    {
                        favIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_48dp));
                    }
                    else
                    {
                        favIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_48dp));
                    }

                    presenter.updateFavorite("contacts/"+id+".json",contactDetail);
                }
            });
        }
        if(emailIcon!=null)
        {
            emailIcon.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[] { contactDetail.getEmail() });

                    final PackageManager pm = getPackageManager();
                    final List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
                    ResolveInfo best = null;
                    for (final ResolveInfo info : matches)
                        if (info.activityInfo.packageName.endsWith(".gm") ||
                                info.activityInfo.name.toLowerCase().contains("gmail")) best = info;
                    if (best != null)
                        intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
                    startActivity(intent);
                }
            });
        }
        if(phoneIcon!=null)
        {
            phoneIcon.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    makePhoneCall();
                }
            });
        }
    }

    public void makePhoneCall()
    {
        String uri = "tel:" + contactDetail.getPhoneNumber();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(ContactDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE},1);

            return;
        }
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case 1:
            {
                if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    makePhoneCall();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void setItem(ContactDetail contactDetail)
    {
        this.contactDetail = contactDetail;
        nameLabel.setText(contactDetail.getFirstName()+" "+contactDetail.getLastName());
        phoneLabel.setText(contactDetail.getPhoneNumber());
        emailLabel.setText(contactDetail.getEmail());
        Picasso.with(getApplicationContext())
                .load(contactDetail.getProfilePic())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .fit()
                .into(imageView);
        try
        {
            if (contactDetail.getFavorite())
            {
                favIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_black_48dp));
            }
            else
            {
                favIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_favorite_border_black_48dp));
            }
        }
        catch (Exception ex)
        {

        }
    }

    @Override
    public void onFavoriteChanged(ContactDetail contactDetail)
    {

    }

    @Override
    protected void onDestroy()
    {
        presenter.onDestroy();
        super.onDestroy();
    }
}