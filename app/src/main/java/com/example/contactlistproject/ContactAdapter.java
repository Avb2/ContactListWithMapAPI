package com.example.contactlistproject;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactlistproject.db.ContactDataSource;
import com.example.contactlistproject.models.Contact;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter {

    private ArrayList<Contact> contactData;
    private View.OnClickListener mOnClickListener;
    private boolean isDeleting;
    private Context parentContext;


    public ContactAdapter(ArrayList<Contact> arrayList, View.OnClickListener onClickListener, Context context) {
        contactData = arrayList;
        mOnClickListener = onClickListener;
        parentContext = context;
    }

    /// List Item
    public class ContactViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewContact;
        public TextView textPhone;

        public TextView email;
        public Button deleteButton;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContact = itemView.findViewById(R.id.textContactName);
            textPhone = itemView.findViewById(R.id.textPhoneNumber);
            deleteButton = itemView.findViewById(R.id.buttonDeleteContact);
            email = itemView.findViewById(R.id.emailShow);
            itemView.setTag(this);
            itemView.setOnClickListener(mOnClickListener);
        }

        public TextView getEmail(){
            return email;
        }

        public TextView getPhoneTextView() {
            return textPhone;
        }
        public Button getDeleteButton() {
            return deleteButton;
        }
        public TextView getContactTextView() {
            return this.textViewContact;
        }
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ContactViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ContactViewHolder cvh = (ContactViewHolder) holder;
        cvh.getContactTextView().setText(contactData.get(position).getContactName());
        cvh.getPhoneTextView().setText(contactData.get(position).getPhoneNumber());
        cvh.getEmail().setText(contactData.get(position).geteMail());

        if (isDeleting) {
            cvh.getDeleteButton().setVisibility(View.VISIBLE);
            cvh.getDeleteButton().setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    deleteItem(position);
                }
            });
        } else {
            cvh.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return contactData.size();
    }

    public void setDelete(Boolean b){
        isDeleting = b;
    }

    private void deleteItem(int position) {
        Contact contact = contactData.get(position);
        ContactDataSource ds = new ContactDataSource(parentContext);

        try {
            ds.open();
            boolean didDelete = ds.deleteContact(contact.getContactID());
            ds.close();
            if (didDelete) {
                contactData.remove(position);
                notifyDataSetChanged();
            } else {
                Toast.makeText(parentContext, "Delete Failed", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(parentContext, "Delete Failed!", Toast.LENGTH_LONG).show();
        }
    }



}