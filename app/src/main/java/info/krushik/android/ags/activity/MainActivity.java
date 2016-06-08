package info.krushik.android.ags.activity;

import android.app.ProgressDialog;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import info.krushik.android.ags.R;
import info.krushik.android.ags.adapters.ClientsLoader;
import info.krushik.android.ags.adapters.LoginDataBaseAdapter;
import info.krushik.android.ags.db.DataBaseHelper;
import info.krushik.android.ags.fragments.AddClientsFragment;
import info.krushik.android.ags.fragments.ListClientsFragment;
import info.krushik.android.ags.fragments.TextHelloFragment;
import info.krushik.android.ags.objects.Client;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Client>> {

    ImageButton mIBtnBuy, mIBtnClients, mIBtnProducts, mIBtnUpDownLoad;

    private ProgressDialog mDialog;
    private SaveTask mSaveTask;

    FloatingActionButton mFab;
    FloatingActionButton mFabProducts;
    FloatingActionButton mFabClients;
    FloatingActionButton mFabBuy;
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
    Animation show_fab_buy;
    Animation hide_fab_buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIBtnBuy = (ImageButton) findViewById(R.id.iBtnBuy);
        mIBtnClients = (ImageButton) findViewById(R.id.iBtnClients);
        mIBtnProducts = (ImageButton) findViewById(R.id.iBtnProducts);
        mIBtnUpDownLoad = (ImageButton) findViewById(R.id.iBtnUpDownLoad);

        Intent intentLogin = getIntent();
        String userName = intentLogin.getStringExtra(LoginDataBaseAdapter.COLUMN_USERNAME);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        TextHelloFragment fragmentHello = TextHelloFragment.newInstance(userName);
        transaction.replace(R.id.fragmentView, fragmentHello);
        transaction.commit();


        mRootLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        //Floating Action Buttons
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFabProducts = (FloatingActionButton) findViewById(R.id.fabProducts);
        mFabClients = (FloatingActionButton) findViewById(R.id.fabClients);
        mFabBuy = (FloatingActionButton) findViewById(R.id.fabBuy);

        //Animations
        show_fab_products = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_products_show);
        hide_fab_products = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_products_hide);
        show_fab_clients = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_clients_show);
        hide_fab_clients = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_clients_hide);
        show_fab_buy = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_buy_show);
        hide_fab_buy = AnimationUtils.loadAnimation(getApplication(), R.anim.fab_buy_hide);


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
    }

    public void OnFabClick(View v) {
        hideFAB();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.fabProducts:
                Toast.makeText(getApplication(), "Floating Action Button Products", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fabClients:
                onClientSelected(new Client());
                break;
            case R.id.fabBuy:
                Toast.makeText(getApplication(), "Floating Action Button Buy", Toast.LENGTH_SHORT).show();
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

        //Floating Action Button Buy
        FrameLayout.LayoutParams layoutParamsBuy = (FrameLayout.LayoutParams) mFabBuy.getLayoutParams();
        layoutParamsBuy.rightMargin += (int) (mFabBuy.getWidth() * 0.25);
        layoutParamsBuy.bottomMargin += (int) (mFabBuy.getHeight() * 1.7);
        mFabBuy.setLayoutParams(layoutParamsBuy);
        mFabBuy.startAnimation(show_fab_buy);
        mFabBuy.setClickable(true);
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

        //Floating Action Button Buy
        FrameLayout.LayoutParams layoutParamsBuy = (FrameLayout.LayoutParams) mFabBuy.getLayoutParams();
        layoutParamsBuy.rightMargin -= (int) (mFabBuy.getWidth() * 0.25);
        layoutParamsBuy.bottomMargin -= (int) (mFabBuy.getHeight() * 1.7);
        mFabBuy.setLayoutParams(layoutParamsBuy);
        mFabBuy.startAnimation(hide_fab_buy);
        mFabBuy.setClickable(false);
    }

    public void OnClick(View v){
        switch (v.getId()){
            case R.id.iBtnBuy:
                break;
            case R.id.iBtnClients:
                init(false);
                Toast.makeText(MainActivity.this, "test", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iBtnProducts:
                break;
            case R.id.iBtnUpDownLoad:
                break;
        }
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
        ListClientsFragment fragment1 = ListClientsFragment.newInstance(clients);
        fragment1.setClientListener(new ListClientsFragment.ClientListener() {
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
        AddClientsFragment fragment2 = AddClientsFragment.newInstance(client);
        fragment2.setClientListener(new AddClientsFragment.ClientListener() {
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

}
