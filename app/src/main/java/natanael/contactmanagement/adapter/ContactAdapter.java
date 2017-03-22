package natanael.contactmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;

import natanael.contactmanagement.model.Contact;
import natanael.contactmanagement.R;
import natanael.contactmanagement.widget.RoundedImageView;

import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder>
{
    private ArrayList<Contact> items;
    public ArrayList<Contact> selectedItems;
    public ViewHolder viewHolder;
    public Context context;
    private HashMap<String,Integer> sectionDictionary;

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public View view;
        public TextView sectionLabel,nameLabel;
        public RoundedImageView imageView;

        public ViewHolder(View v)
        {
            super(v);

            view = v;
            sectionLabel = (TextView)v.findViewById(R.id.sectionLabel);
            nameLabel = (TextView)v.findViewById(R.id.nameLabel);
            imageView = (RoundedImageView)v.findViewById(R.id.imageView);
        }
    }

    public ContactAdapter(Context context,ArrayList<Contact> items)
    {
        this.selectedItems = new ArrayList<>();
        this.items = items;
        this.context = context;
        this.sectionDictionary = new HashMap<>();

        for(int i=0;i<this.items.size();i++)
        {
            Contact data = this.items.get(i);
            String letter = data.getFirstName().substring(0,1).toUpperCase();
            if(!sectionDictionary.containsKey(letter))
                sectionDictionary.put(letter,i);
        }
    }

    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_layout, parent, false);

        this.viewHolder = new ViewHolder(v);
        return this.viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Contact data = items.get(position);

        String letter = data.getFirstName().substring(0,1).toUpperCase();
        Boolean sectionVisible = false;
        if(sectionDictionary.containsValue(position))
            sectionVisible = true;

        if (holder.nameLabel != null)
            holder.nameLabel.setText(data.getFirstName()+" "+data.getLastName());
        if(holder.sectionLabel!=null)
        {
            holder.sectionLabel.setText(letter);
            if(sectionVisible)
                holder.sectionLabel.setVisibility(View.VISIBLE);
            else
                holder.sectionLabel.setVisibility(View.INVISIBLE);
        }
        if(holder.imageView!=null)
        {
            Picasso.with(context)
                    .load(data.getProfilePic())
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .fit()
                    .into(holder.imageView);
        }
    }

    @Override
    public int getItemCount()
    {
        if(items!=null)
            return items.size();
        else
            return 0;
    }
}