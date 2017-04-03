package natanael.contactmanagement.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.List;

import natanael.contactmanagement.helper.Utility;
import natanael.contactmanagement.model.AddContactRepository;
import natanael.contactmanagement.model.ContactDetail;
import natanael.contactmanagement.model.ContactDetailRepository;
import natanael.contactmanagement.presenter.AddContactPresenter;
import natanael.contactmanagement.presenter.ContactDetailPresenter;
import natanael.contactmanagement.presenter.IAddContactPresenter;
import natanael.contactmanagement.presenter.IContactDetailPresenter;
import natanael.contactmanagement.view.IAddContactView;
import natanael.contactmanagement.R;

import rx.Observable;
import rx.functions.Action1;

public class AddContactActivity extends AppCompatActivity implements IAddContactView
{
    private IAddContactPresenter presenter;
    private Toolbar toolbar;
    private ImageView imageView;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private Button saveButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_contact);

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

        presenter = new AddContactPresenter(this, new AddContactRepository());
    }

    public void setReference()
    {
        imageView = (ImageView) this.findViewById(R.id.imageView);
        firstNameEditText = (EditText) this.findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) this.findViewById(R.id.lastNameEditText);
        phoneEditText = (EditText) this.findViewById(R.id.phoneEditText);
        emailEditText = (EditText) this.findViewById(R.id.emailEditText);
        saveButton = (Button) this.findViewById(R.id.saveButton);
    }

    private void setEventHandler()
    {
        if(imageView!=null)
        {
            imageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    selectImage();
                }
            });
        }
        if(saveButton!=null)
        {
            saveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    saveContact();
                }
            });
        }
    }

    private void saveContact()
    {
        if(firstNameEditText.getText().toString() == null || firstNameEditText.getText().toString().length()<3)
            Toast.makeText(getApplicationContext(),"First Name Not Valid",Toast.LENGTH_SHORT).show();
        else if(phoneEditText.getText().toString() == null || phoneEditText.getText().toString().length()<8)
            Toast.makeText(getApplicationContext(),"Phone Number Not Valid",Toast.LENGTH_SHORT).show();
        else
        {
            ContactDetail contactDetail = new ContactDetail();
            contactDetail.setFirstName(firstNameEditText.getText().toString());
            contactDetail.setLastName(lastNameEditText.getText().toString());
            contactDetail.setEmail(emailEditText.getText().toString());
            contactDetail.setPhoneNumber(phoneEditText.getText().toString());
            contactDetail.setFavorite(true);

            presenter.addContact(contactDetail);
        }
    }

    @Override
    public void onFinished(ContactDetail contactDetail)
    {
        setResult(Activity.RESULT_OK);

        finish();
    }

    @Override
    public void onFailure(String message)
    {
        showConfirmationSelection();
    }

    @Override
    protected void onDestroy()
    {
        presenter.onDestroy();
        super.onDestroy();
    }

    AlertDialog confirmationDialog;
    public void showConfirmationSelection()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.dialog_retry, null);
        builder.setView(convertView);
        //builder.setTitle("Degree");

        TextView title = (TextView)convertView.findViewById(R.id.titleLabel);
        Button noButton = (Button)convertView.findViewById(R.id.noButton);
        Button yesButton = (Button)convertView.findViewById(R.id.yesButton);

        if (noButton != null)
        {
            noButton.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    confirmationDialog.dismiss();
                    saveContact();
                }
            });
        }

        if (yesButton != null)
        {
            yesButton.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    confirmationDialog.dismiss();
                    finish();
                }
            });
        }

        confirmationDialog = builder.create();
        confirmationDialog.show();
    }



    Bitmap newImage = null;
    String userChoosenTask;
    public static Integer REQUEST_CAMERA = 1;
    public static Integer SELECT_FILE = 2;
    public boolean newProfilePicture = false;
    private void selectImage()
    {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(AddContactActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int item)
            {
                boolean result= Utility.checkPermission(AddContactActivity.this);
                if (items[item].equals("Take Photo"))
                {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                }
                else if (items[item].equals("Choose from Library"))
                {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                }
                else if (items[item].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK)
        {
            File file = new File(Environment.getExternalStorageDirectory()+File.separator + "img.jpg");
            if(requestCode == REQUEST_CAMERA)
            {
                Uri uri = Uri.fromFile(file);
                CropImage.activity(uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
            else if(requestCode == SELECT_FILE)
            {
                Uri uri = data.getData();
                CropImage.activity(uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
            else if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
            {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Bitmap bm=null ;
                File finalFile2 = null;
                if (resultCode == RESULT_OK)
                {
                    Uri resultUri = result.getUri();
                    //File finalFile = new File(getRealPathFromURI(resultUri));
                    try
                    {
                        bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), resultUri);
                        newImage = bm;
                        newProfilePicture = true;
                        if(bm!=null)
                        {
                            newProfilePicture = true;
                            Bitmap thePic = bm;
                            newImage = thePic;
                            imageView.setImageBitmap(thePic);
                            //if(finalFile!=null)
                            //    finalFile.delete();
                        }
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
                {
                    Exception error = result.getError();
                }

                if(file!=null)
                    file.delete();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                }
                else
                {
                    //code for deny
                }
                break;
        }
    }

    private void cameraIntent()
    {
        File file = new File(Environment.getExternalStorageDirectory()+File.separator + "img.jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
}