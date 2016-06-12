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
import info.krushik.android.ags.objects.Product;

public class ProductsListFragment extends Fragment {
    private static final String EXTRA_PRODUCTS = "info.krushik.android.ags.fragments.PRODUCTS";

    private ListView mListView;
    private ArrayList<Product> mProducts;


    public static ProductsListFragment newInstance(ArrayList<Product> products) {
        ProductsListFragment fragment = new ProductsListFragment();

        Bundle args = new Bundle();
        args.putParcelableArrayList(EXTRA_PRODUCTS, products);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_products_list, container, false);
        Bundle args = getArguments();
        mProducts = args.getParcelableArrayList(EXTRA_PRODUCTS);

        ArrayAdapter<Product> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                mProducts);

        mListView = (ListView) view.findViewById(R.id.listViewProduct);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product product = mProducts.get(position);//позиция в списке, то куда кликнули

                if (mListener != null) {
                    mListener.onItemClick(product.id);
                }
            }
        });

        return view;
    }

    private ProductsItemListener mListener;

    public void setProductsItemListener(ProductsItemListener listener) {
        mListener = listener;
    }

    public interface ProductsItemListener {
        void onItemClick(long product);
    }

}
