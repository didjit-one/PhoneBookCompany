package com.didjit.phonebookcompany;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.didjit.phonebookcompany.data.DataBaseDescription.Contact;

/**
 * Created by didjit on 025 25.04.17.
 */

public class DetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {


    public interface DetailFragmentListener {
        void onContactDeleted();

        void onEditContact(Uri contactUri);
    }

    private static final int CONTACT_LOADER = 0;

    private DetailFragmentListener listener;
    private Uri contactUri;

    private TextView nameTextView;
    private TextView phoneTextView;
    private TextView positionTextView;
    private TextView departmentTextView;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (DetailFragmentListener) context;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);


        Bundle arguments = getArguments();

        if (arguments != null)
            contactUri = arguments.getParcelable(MainActivity.CONTACT_URI);


        View view =
                inflater.inflate(R.layout.fragment_details, container, false);


        nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        phoneTextView = (TextView) view.findViewById(R.id.phoneTextView);
        departmentTextView = (TextView) view.findViewById(R.id.departmentTextView);
        positionTextView = (TextView) view.findViewById(R.id.positionTextView);


        // load the contact
        getLoaderManager().initLoader(CONTACT_LOADER, null, this);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_details_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                listener.onEditContact(contactUri);
                return true;
            case R.id.action_delete:

        // create an AlertDialog and return it

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());

            builder.setTitle(R.string.confirm_title);
            builder.setMessage(R.string.confirm_message);


            builder.setPositiveButton(R.string.button_delete,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(
                                DialogInterface dialog, int button) {getActivity().getContentResolver().delete(contactUri, null, null);
                            listener.onContactDeleted();
                        }
                    }
            );
            builder.setNegativeButton(R.string.button_cancel, null);

                 builder.create();
                builder.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // called by LoaderManager to create a Loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        CursorLoader cursorLoader;

        switch (id) {
            case CONTACT_LOADER:
                cursorLoader = new CursorLoader(getActivity(),
                        contactUri, // Uri of contact to display
                        null, // null projection returns all columns
                        null, // null selection returns all rows
                        null, // no selection arguments
                        null); // sort order
                break;
            default:
                cursorLoader = null;
                break;
        }

        return cursorLoader;
    }

    // called by LoaderManager when loading completes
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.moveToFirst()) {

            int nameIndex = data.getColumnIndex(Contact.COLUMN_NAME);
            int phoneIndex = data.getColumnIndex(Contact.COLUMN_PHONE);
            int departmentIndex = data.getColumnIndex(Contact.COLUMN_DEPARTMENT);
            int positionIndex = data.getColumnIndex(Contact.COLUMN_POSITION);

            nameTextView.setText(data.getString(nameIndex));
            phoneTextView.setText(data.getString(phoneIndex));
            departmentTextView.setText(data.getString(departmentIndex));
            positionTextView.setText(data.getString(positionIndex));
        }
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
