package info.krushik.android.ags.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import info.krushik.android.ags.R;
import info.krushik.android.ags.fragments.ProductsAddFragment;
import info.krushik.android.ags.fragments.ProductsListFragment;
import info.krushik.android.ags.fragments.UpDownLoadFragment;
import info.krushik.android.ags.loaders.ClientsLoader;
import info.krushik.android.ags.objects.LoginDataBaseAdapter;
import info.krushik.android.ags.db.DataBaseHelper;
import info.krushik.android.ags.fragments.ClientsAddFragment;
import info.krushik.android.ags.fragments.ClientsListFragment;
import info.krushik.android.ags.fragments.HelloTextFragment;
import info.krushik.android.ags.objects.Client;
import info.krushik.android.ags.objects.Product;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Client>> {


    ImageButton mIBtnSale, mIBtnClients, mIBtnProducts, mIBtnUpDownLoad;


    private ProgressDialog mDialog;
    private SaveTask mSaveTask;
    //    private SaveTaskProduct mSaveTaskProduct;
    private DataBaseHelper mHelper;
    private ArrayList<Product> mProducts;


    FloatingActionButton mFab;
    FloatingActionButton mFabProducts;
    FloatingActionButton mFabClients;
    FloatingActionButton mFabSale;
    CoordinatorLayout mRootLayout;

    //Save the FAB's active status
    //false -> mFab = close
    //true -> mFab = open
    private boolean FAB_Status = false;

    //Animations
    Animation show_fab_products;
    Animation hide_fab_products;
    Animation show_fab_clients;
    Animation hide_fab_clients;
    Animation show_fab_sale;
    Animation hide_fab_sale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mIBtnSale = (ImageButton) findViewById(R.id.iBtnSale);
        mIBtnClients = (ImageButton) findViewById(R.id.iBtnClients);
        mIBtnProducts = (ImageButton) findViewById(R.id.iBtnProducts);
        mIBtnUpDownLoad = (ImageButton) findViewById(R.id.iBtnUpDownLoad);

        Intent intentLogin = getIntent();
        String userName = intentLogin.getStringExtra(LoginDataBaseAdapter.COLUMN_USERNAME);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        HelloTextFragment fragmentHello = HelloTextFragment.newInstance(userName);
        transaction.replace(R.id.fragmentView, fragmentHello);
        transaction.commit();


        mRootLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        //Floating Action Buttons
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFabProducts = (FloatingActionButton) findViewById(R.id.fabProducts);
        mFabClients = (FloatingActionButton) findViewById(R.id.fabClients);
        mFabSale = (FloatingActionButton) findViewById(R.id.fabSale);

        //Animations
        show_fab_products = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_products_show);
        hide_fab_products = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_products_hide);
        show_fab_clients = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_clients_show);
        hide_fab_clients = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_clients_hide);
        show_fab_sale = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_sale_show);
        hide_fab_sale = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_sale_hide);


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (FAB_Status == false) {
                    //Display FAB menu
                    expandFAB();
                    FAB_Status = true;
                } else {
                    //Close FAB menu
                    hideFAB();
                    FAB_Status = false;
                }
            }
        });


        //Initialize an empty list of 50 elements
//        List list = new ArrayList();
//        for (int i = 0; i < 50; i++) {
//            list.add(new Object());
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mSaveTask != null) {
            mSaveTask.cancel(true);
        }
//        if (mSaveTaskProduct != null) {
//            mSaveTaskProduct.cancel(true);
//        }
    }

    public void OnFabClick(View v) {
        hideFAB();
        FAB_Status = false;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        switch (v.getId()) {
            case R.id.fabProducts:
//                onProductSelected(new Product());
                edit(new Product());
//                Toast.makeText(getApplication(), "Floating Action Button Products", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fabClients:
                onClientSelected(new Client());
                break;
            case R.id.fabSale:
                Toast.makeText(getApplication(), "Floating Action Button Sale", Toast.LENGTH_SHORT).show();
                break;
        }
        transaction.commit();

    }

    private void expandFAB() {

        //Floating Action Button Products
        FrameLayout.LayoutParams layoutParamsProducts = (FrameLayout.LayoutParams) mFabProducts.getLayoutParams();
        layoutParamsProducts.rightMargin += (int) (mFabProducts.getWidth() * 1.7);
        layoutParamsProducts.bottomMargin += (int) (mFabProducts.getHeight() * 0.25);
        mFabProducts.setLayoutParams(layoutParamsProducts);
        mFabProducts.startAnimation(show_fab_products);
        mFabProducts.setClickable(true);

        //Floating Action Button Clients
        FrameLayout.LayoutParams layoutParamsClients = (FrameLayout.LayoutParams) mFabClients.getLayoutParams();
        layoutParamsClients.rightMargin += (int) (mFabClients.getWidth() * 1.5);
        layoutParamsClients.bottomMargin += (int) (mFabClients.getHeight() * 1.5);
        mFabClients.setLayoutParams(layoutParamsClients);
        mFabClients.startAnimation(show_fab_clients);
        mFabClients.setClickable(true);

        //Floating Action Button Sale
        FrameLayout.LayoutParams layoutParamsSale = (FrameLayout.LayoutParams) mFabSale.getLayoutParams();
        layoutParamsSale.rightMargin += (int) (mFabSale.getWidth() * 0.25);
        layoutParamsSale.bottomMargin += (int) (mFabSale.getHeight() * 1.7);
        mFabSale.setLayoutParams(layoutParamsSale);
        mFabSale.startAnimation(show_fab_sale);
        mFabSale.setClickable(true);
    }

    private void hideFAB() {

        //Floating Action Button Products
        FrameLayout.LayoutParams layoutParamsProducts = (FrameLayout.LayoutParams) mFabProducts.getLayoutParams();
        layoutParamsProducts.rightMargin -= (int) (mFabProducts.getWidth() * 1.7);
        layoutParamsProducts.bottomMargin -= (int) (mFabProducts.getHeight() * 0.25);
        mFabProducts.setLayoutParams(layoutParamsProducts);
        mFabProducts.startAnimation(hide_fab_products);
        mFabProducts.setClickable(false);

        //Floating Action Button Clients
        FrameLayout.LayoutParams layoutParamsClients = (FrameLayout.LayoutParams) mFabClients.getLayoutParams();
        layoutParamsClients.rightMargin -= (int) (mFabClients.getWidth() * 1.5);
        layoutParamsClients.bottomMargin -= (int) (mFabClients.getHeight() * 1.5);
        mFabClients.setLayoutParams(layoutParamsClients);
        mFabClients.startAnimation(hide_fab_clients);
        mFabClients.setClickable(false);

        //Floating Action Button Sale
        FrameLayout.LayoutParams layoutParamsSale = (FrameLayout.LayoutParams) mFabSale.getLayoutParams();
        layoutParamsSale.rightMargin -= (int) (mFabSale.getWidth() * 0.25);
        layoutParamsSale.bottomMargin -= (int) (mFabSale.getHeight() * 1.7);
        mFabSale.setLayoutParams(layoutParamsSale);
        mFabSale.startAnimation(hide_fab_sale);
        mFabSale.setClickable(false);
    }

    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.iBtnSale:
                break;
            case R.id.iBtnClients:
                init(false);
//                Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iBtnProducts:
                mHelper = new DataBaseHelper(this);
                init();
//                Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iBtnUpDownLoad:
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                UpDownLoadFragment fragmentUpDownLoad = new UpDownLoadFragment();
                transaction.replace(R.id.fragmentView, fragmentUpDownLoad);
                transaction.commit();
                break;
            case R.id.buttonUpLoadClients:

                break;
            case R.id.buttonUpLoadPoducts:

                Serializer serializer = new Persister();
                File file = new File(getFilesDir() + "/products.xml");
                try {

                    serializer.write(mProducts, file);
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "NoSaved", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, mProducts.toString(), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void init() {//вычитка студентов и установка фрагментов
        mProducts = mHelper.getProducts();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ProductsListFragment fragment = ProductsListFragment.newInstance(mProducts);
        fragment.setProductsItemListener(new ProductsListFragment.ProductsItemListener() {
            @Override
            public void onItemClick(long id) {
                Product pruduct = mHelper.getProduct(id);
                edit(pruduct);
            }
        });
        transaction.replace(R.id.fragmentView, fragment);
        transaction.commit();
    }

    private void edit(Product pruduct) {//принимает студента на редактирование
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        ProductsAddFragment fragment2 = ProductsAddFragment.newInstance(pruduct);
        fragment2.setOnProductListener(new ProductsAddFragment.ProductListener() {
            @Override
            public void onProductSaved(Product pruduct) {
                if (pruduct.id == 0) {
                    mHelper.insertProduct(pruduct);
                } else {
                    mHelper.updateProduct(pruduct);
                }
                init();
            }
        });
        transaction.replace(R.id.fragmentView, fragment2);

        transaction.commit();
    }

    private void init(boolean restart) {//вычитка студентов и установка фрагментов
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();

        if (restart) {
            getSupportLoaderManager().restartLoader(0, null, this);
        } else {
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }

    @Override
    public Loader<ArrayList<Client>> onCreateLoader(int id, Bundle args) {
        return new ClientsLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Client>> loader, final ArrayList<Client> data) {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                onLoaded(data);
            }
        };
        handler.sendEmptyMessage(0);
    }

    private void onLoaded(ArrayList<Client> clients) {
        ClientsListFragment fragment1 = ClientsListFragment.newInstance(clients);
        fragment1.setClientListener(new ClientsListFragment.ClientListener() {
            @Override
            public void clientSelected(Client client) {
                onClientSelected(client);
            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentView, fragment1);
        transaction.commit();

        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    private void onClientSelected(Client client) {
        ClientsAddFragment fragment2 = ClientsAddFragment.newInstance(client);
        fragment2.setClientListener(new ClientsAddFragment.ClientListener() {
            @Override
            public void clientSaved(Client client) {
                mSaveTask = new SaveTask();
                mSaveTask.execute(client);
            }
        });

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentView, fragment2);
        transaction.commit();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Client>> loader) {

    }

//    @Override
//    public Loader<ArrayList<Product>> onCreateLoader(int id, Bundle args) {
//        return new ProductsLoader(this);
//    }
//
//    @Override
//    public void onLoadFinished(Loader<ArrayList<Product>> loader, final ArrayList<Product> data) {
//        Handler handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                onLoaded(data);
//            }
//        };
//        handler.sendEmptyMessage(0);
//    }
//
//    private void onLoaded(ArrayList<Product> products) {
//        ProductsListFragment fragment1 = ProductsListFragment.newInstance(products);
//        fragment1.setProductListener(new ProductsListFragment.ProductListener() {
//            @Override
//            public void productSelected(Product product) {
//                onProductSelected(product);
//            }
//        });
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragmentView, fragment1);
//        transaction.commit();
//
//        if (mDialog != null) {
//            mDialog.dismiss();
//        }
//    }

//    private void onProductSelected(Product product) {
//        ProductsAddFragment fragment2 = ProductsAddFragment.newInstance(product);
//        fragment2.setProductListener(new ProductsAddFragment.ProductListener() {
//            @Override
//            public void productSaved(Product product) {
//                mSaveTaskProduct = new SaveTaskProduct();
//                mSaveTaskProduct.execute(product);
//            }
//        });
//
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragmentView, fragment2);
//        transaction.commit();
//    }

//    @Override
//    public void onLoaderReset(Loader<ArrayList<Product>> loader) {
//
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu1:
                Intent intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class SaveTask extends AsyncTask<Client, Void, Boolean> {
        private ProgressDialog mDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Loading...");
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Boolean doInBackground(Client... params) {
            Client client = params[0];
            DataBaseHelper helper = new DataBaseHelper(MainActivity.this);

            return helper.saveClient(client);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            if (mDialog != null) {
                mDialog.dismiss();
            }

            init(true);
        }
    }

//    class SaveTaskProduct extends AsyncTask<Product, Void, Boolean> {
//        private ProgressDialog mDialog;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
//            mDialog = new ProgressDialog(MainActivity.this);
//            mDialog.setMessage("Loading...");
//            mDialog.setCancelable(false);
//            mDialog.show();
//        }
//
//        @Override
//        protected Boolean doInBackground(Product... params) {
//            Product product = params[0];
//            DataBaseHelper helper = new DataBaseHelper(MainActivity.this);
//
//            return helper.saveProduct(product);
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
//
//            if (mDialog != null) {
//                mDialog.dismiss();
//            }
//
//            init(true);
//        }
//    }

}
