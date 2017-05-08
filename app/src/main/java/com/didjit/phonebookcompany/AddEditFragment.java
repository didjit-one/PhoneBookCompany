
package com.didjit.phonebookcompany;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.didjit.phonebookcompany.data.DataBaseDescription.Contact;

/**
 * Created by didjit on 025 25.04.17.
 */

public class AddEditFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {


    public interface AddEditFragmentListener {

        void onAddEditCompleted(Uri contactUri);
    }


    private static final int CONTACT_LOADER = 0;
    private AddEditFragmentListener listener;
    private Uri contactUri;
    private boolean addingNewContact = true;


    private TextInputLayout nameTextInputLayout;
    private TextInputLayout phoneTextInputLayout;
    private TextInputLayout positionTextInputLayout;
    private TextInputLayout departmentTextInputLayout;

    private FloatingActionButton saveContactFAB;

    private CoordinatorLayout coordinatorLayout; // used with SnackBars


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (AddEditFragmentListener) context;
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


        View view =
                inflater.inflate(R.layout.fragment_add_edit, container, false);
        nameTextInputLayout =
                (TextInputLayout) view.findViewById(R.id.nameTextInputLayout);
        nameTextInputLayout.getEditText().addTextChangedListener(
                nameChangedListener);
        phoneTextInputLayout =
                (TextInputLayout) view.findViewById(R.id.phoneTextInputLayout);
        positionTextInputLayout =
                (TextInputLayout) view.findViewById(R.id.positionTextInputLayout);
        departmentTextInputLayout =
                (TextInputLayout) view.findViewById(R.id.departmentTextInputLayout);



        saveContactFAB = (FloatingActionButton) view.findViewById(
                R.id.savefloatingActionButton);
        saveContactFAB.setOnClickListener(saveContactButtonClicked);
        updateSaveButtonFAB();

        // used to display SnackBars with brief messages
        coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(
                R.id.coordinatorLayout);

        Bundle arguments = getArguments(); // null if creating new contact

        if (arguments != null) {
            addingNewContact = false;
            contactUri = arguments.getParcelable(MainActivity.CONTACT_URI);
        }

        // if editing an existing contact, create Loader to get the contact
        if (contactUri != null)
            getLoaderManager().initLoader(CONTACT_LOADER, null, this);

        return view;
    }


    private final TextWatcher nameChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        // called when the text in nameTextInputLayout changes
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            updateSaveButtonFAB();
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    // shows saveButtonFAB only if the name is not empty
    private void updateSaveButtonFAB() {
        String input =
                nameTextInputLayout.getEditText().getText().toString();

        // if there is a name for the contact, show the FloatingActionButton
        if (input.trim().length() != 0)
            saveContactFAB.show();
        else
            saveContactFAB.hide();
    }

    // responds to event generated when user saves a contact
    private final View.OnClickListener saveContactButtonClicked =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // hide the virtual keyboard
                    ((InputMethodManager) getActivity().getSystemService(
                            Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getView().getWindowToken(), 0);
                    saveContact(); // save contact to the database
                }
            };

    // saves contact information to the database
    private void saveContact() {
        // create ContentValues object containing contact's key-value pairs
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contact.COLUMN_NAME,
                nameTextInputLayout.getEditText().getText().toString());
        contentValues.put(Contact.COLUMN_PHONE,
                phoneTextInputLayout.getEditText().getText().toString());
        contentValues.put(Contact.COLUMN_DEPARTMENT,
                departmentTextInputLayout.getEditText().getText().toString());
        contentValues.put(Contact.COLUMN_POSITION,
                positionTextInputLayout.getEditText().getText().toString());

        if (addingNewContact) {
            // use Activity's ContentResolver to invoke
            // insert on the AddressBookContentProvider
            Uri newContactUri = getActivity().getContentResolver().insert(
                    Contact.CONTENT_URI, contentValues);

            if (newContactUri != null) {
                Snackbar.make(coordinatorLayout,
                        R.string.contact_added, Snackbar.LENGTH_LONG).show();
                listener.onAddEditCompleted(newContactUri);
            } else {
                Snackbar.make(coordinatorLayout,
                        R.string.contact_not_added, Snackbar.LENGTH_LONG).show();
            }
        } else {
            // use Activity's ContentResolver to invoke
            // insert on the AddressBookContentProvider
            int updatedRows = getActivity().getContentResolver().update(
                    contactUri, contentValues, null, null);

            if (updatedRows > 0) {
                listener.onAddEditCompleted(contactUri);
                Snackbar.make(coordinatorLayout,
                        R.string.contact_updated, Snackbar.LENGTH_LONG).show();
            } else {
                Snackbar.make(coordinatorLayout,
                        R.string.contact_not_updated, Snackbar.LENGTH_LONG).show();
            }
        }
    }


    // called by LoaderManager to create a Loader
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id) {
            case CONTACT_LOADER:
                return new CursorLoader(getActivity(),
                        contactUri, // Uri of contact to display
                        null, // null projection returns all columns
                        null, // null selection returns all rows
                        null, // no selection arguments
                        null); // sort order
            default:
                return null;
        }
    }

    // called by LoaderManager when loading completes
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // if the contact exists in the database, display its data
        if (data != null && data.moveToFirst()) {
            // get the column index for each data item
            int nameIndex = data.getColumnIndex(Contact.COLUMN_NAME);
            int phoneIndex = data.getColumnIndex(Contact.COLUMN_PHONE);
            int positIndex = data.getColumnIndex(Contact.COLUMN_POSITION);
            int depIndex = data.getColumnIndex(Contact.COLUMN_DEPARTMENT);

            // fill EditTexts with the retrieved data
            nameTextInputLayout.getEditText().setText(
                    data.getString(nameIndex));
            phoneTextInputLayout.getEditText().setText(
                    data.getString(phoneIndex));
            departmentTextInputLayout.getEditText().setText(
                    data.getString(depIndex));
            positionTextInputLayout.getEditText().setText(
                    data.getString(positIndex));

            updateSaveButtonFAB();
        }
    }

    // called by LoaderManager when the Loader is being reset
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}