package info.krushik.android.ags.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import info.krushik.android.ags.R;
import info.krushik.android.ags.objects.Client;

public class AddClientsFragment extends Fragment {
    private static final String EXTRA_CLIENT = "info.krushik.android.ags.fragments.CLIENT";

    private Client mClient;
    private EditText mEditTextIdCard;
    private EditText mEditTextFirstName;
    private EditText mEditTextLastName;
    private EditText mEditTextPhone;
    private EditText mEditTextEmail;
    private Button mButtonSave;

    public static AddClientsFragment newInstance(Client client) {
        AddClientsFragment fragment = new AddClientsFragment();

        Bundle args = new Bundle();
        args.putParcelable(EXTRA_CLIENT, client);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_clients, container, false);

        Bundle args = getArguments();
        mClient = args.getParcelable(EXTRA_CLIENT);

        mEditTextIdCard = (EditText) view.findViewById(R.id.editTextIdCard);
        mEditTextFirstName = (EditText) view.findViewById(R.id.editTextFirstName);
        mEditTextLastName = (EditText) view.findViewById(R.id.editTextLastName);
        mEditTextPhone = (EditText) view.findViewById(R.id.editTextPhone);
        mEditTextEmail = (EditText) view.findViewById(R.id.editTextEmail);

        mEditTextIdCard.setText(String.valueOf(mClient.idCard));
        mEditTextFirstName.setText(mClient.FirstName);
        mEditTextLastName.setText(mClient.LastName);
        mEditTextPhone.setText(String.valueOf(mClient.Phone));
        mEditTextEmail.setText(mClient.Email);

        mButtonSave = (Button) view.findViewById(R.id.buttonSave);
        mButtonSave.setOnClickListener(new View.OnClickListener() {
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
