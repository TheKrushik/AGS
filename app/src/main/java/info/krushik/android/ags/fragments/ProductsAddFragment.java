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
import info.krushik.android.ags.objects.Product;

public class ProductsAddFragment extends Fragment {

    private static final String EXTRA_PRODUCT = "info.krushik.android.ags.fragments.PRODUCT";

    private Product mProduct;
    private EditText mEditTextProductName;
    private EditText mEditTextPrice;
    private Button mButtonSaveProduct;

    public static ProductsAddFragment newInstance(Product product) {
        ProductsAddFragment fragment = new ProductsAddFragment();

        Bundle args = new Bundle();
        args.putParcelable(EXTRA_PRODUCT, product);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_products_add, container, false);

        Bundle args = getArguments();
        mProduct = args.getParcelable(EXTRA_PRODUCT);

        mEditTextProductName = (EditText) view.findViewById(R.id.editTextProductName);
        mEditTextPrice = (EditText) view.findViewById(R.id.editTextPrice);

        mEditTextProductName.setText(mProduct.ProductName);
        mEditTextPrice.setText(String.valueOf(mProduct.Price));

        mButtonSaveProduct = (Button) view.findViewById(R.id.buttonSaveProduct);
        mButtonSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProduct.ProductName = mEditTextProductName.getText().toString();
                mProduct.Price = Integer.parseInt(mEditTextPrice.getText().toString());

                if (mListener != null) {
                    mListener.onProductSaved(mProduct);
                }
            }
        });

        return view;
    }

    private ProductListener mListener;

    public void setOnProductListener(ProductListener listener) {
        mListener = listener;
    }

    public interface ProductListener {
        void onProductSaved(Product product);
    }

}
