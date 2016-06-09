package info.krushik.android.ags.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import info.krushik.android.ags.R;
import info.krushik.android.ags.objects.Client;

public class ClientsListFragment extends Fragment {
    private static final String EXTRA_CLIENTS = "info.krushik.android.ags.fragments.CLIENTS";

    private ListView mListView;
    private ArrayList<Client> mClients;

    public static ClientsListFragment newInstance(ArrayList<Client> clients) {
        ClientsListFragment fragment = new ClientsListFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_CLIENTS, clients);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clients_list, container, false);
        Bundle args = getArguments();
        mClients = args.getParcelableArrayList(EXTRA_CLIENTS);

        ArrayAdapter<Client> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                mClients);

        mListView = (ListView) view.findViewById(R.id.listViewClient);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Client client = mClients.get(position);//позиция в списке, то куда кликнули

                if (mListener != null) {
                    mListener.clientSelected(client);
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
        void clientSelected(Client client);
    }
}