package info.krushik.android.ags.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import info.krushik.android.ags.R;
import info.krushik.android.ags.activity.MainActivity;
import info.krushik.android.ags.objects.Client;

public class ClientsAddFragment extends Fragment {
    private static final String EXTRA_CLIENT = "info.krushik.android.ags.fragments.CLIENT";

    private Client mClient;
    private EditText mEditTextIdCard;
    private EditText mEditTextFirstName;
    private EditText mEditTextLastName;
    private EditText mEditTextPhone;
    private EditText mEditTextEmail;
    private Button mButtonSaveClient;

    public static ClientsAddFragment newInstance(Client client) {
        ClientsAddFragment fragment = new ClientsAddFragment();

        Bundle args = new Bundle();
        args.putParcelable(EXTRA_CLIENT, client);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_clients_add, container, false);

        Bundle args = getArguments();
        mClient = args.getParcelable(EXTRA_CLIENT);

        mEditTextIdCard = (EditText) view.findViewById(R.id.editTextIdCard);
        mEditTextFirstName = (EditText) view.findViewById(R.id.editTextFirstName);
        mEditTextLastName = (EditText) view.findViewById(R.id.editTextLastName);
        mEditTextPhone = (EditText) view.findViewById(R.id.editTextPhone);
        mEditTextEmail = (EditText) view.findViewById(R.id.editTextEmail);


        if(mClient.idCard != 0) {
            mEditTextIdCard.setText(String.valueOf(mClient.idCard));
        }
        mEditTextFirstName.setText(mClient.FirstName);
        mEditTextLastName.setText(mClient.LastName);
        if (mClient.Phone != 0) {
            mEditTextPhone.setText(String.valueOf(mClient.Phone));
        }
        mEditTextEmail.setText(mClient.Email);

        mButtonSaveClient = (Button) view.findViewById(R.id.buttonSaveClient);
        mButtonSaveClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClient.idCard = Integer.parseInt(mEditTextIdCard.getText().toString());
                mClient.FirstName = mEditTextFirstName.getText().toString();
                mClient.LastName = mEditTextLastName.getText().toString();
                mClient.Phone = Integer.parseInt(mEditTextPhone.getText().toString());
                mClient.Email = mEditTextEmail.getText().toString();



                if (mListener != null) {
                    mListener.clientSaved(mClient);
                }
            }
        });


        return view;
    }

    private ClientListener mListener;

    public void setClientListener(ClientListener listener) {
        mListener = listener;
    }

    public interface ClientListener {
        void clientSaved(Client client);
    }
}
