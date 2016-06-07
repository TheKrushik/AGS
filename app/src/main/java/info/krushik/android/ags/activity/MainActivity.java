package info.krushik.android.ags.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
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
import info.krushik.android.ags.adapters.LoginDataBaseAdapter;
import info.krushik.android.ags.db.DataBaseHelper;
import info.krushik.android.ags.fragments.AddClientsFragment;
import info.krushik.android.ags.fragments.ListClientsFragment;
import info.krushik.android.ags.fragments.TextHelloFragment;
import info.krushik.android.ags.objects.Client;

public class MainActivity extends AppCompatActivity {
//    TextView mTvHello;
    ImageButton mIBtnBuy, mIBtnClients, mIBtnProducts, mIBtnUpDownLoad;

    DataBaseHelper mHelper;
    ArrayList<Client> mClients;

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

//        mTvHello = (TextView) findViewById(R.id.tvHello);
        mIBtnBuy = (ImageButton) findViewById(R.id.iBtnBuy);
        mIBtnClients = (ImageButton) findViewById(R.id.iBtnClients);
        mIBtnProducts = (ImageButton) findViewById(R.id.iBtnProducts);
        mIBtnUpDownLoad = (ImageButton) findViewById(R.id.iBtnUpDownLoad);

        Intent intentLogin = getIntent();
        String userName = intentLogin.getStringExtra(LoginDataBaseAdapter.COLUMN_USERNAME);

//        mTvHello.setText(userName + ", выберите действие...");

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

    private void init() {//вычитка студентов и установка фрагментов
        mClients = mHelper.getClients();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ListClientsFragment fragment = ListClientsFragment.newInstance(mClients);
        fragment.setClientsItemListener(new ListClientsFragment.ClientsItemListener() {
            @Override
            public void onItemClick(long id) {
                Client client = mHelper.getClient(id);
                edit(client);
            }
        });
        transaction.replace(R.id.fragmentView, fragment);
        transaction.commit();
    }

    private void edit(Client client) {//принимает студента на редактирование
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        AddClientsFragment fragmentClient = AddClientsFragment.newInstance(client);
        fragmentClient.setOnClientListener(new AddClientsFragment.ClientListener() {
            @Override
            public void onClientSaved(Client client) {
                if (client.id == 0) {
                    mHelper.insertClient(client);
                } else {
                    mHelper.updateClient(client);
                }
                init();
            }
        });
        transaction.replace(R.id.fragmentView, fragmentClient);

        transaction.commit();
    }

    public void OnFabClick(View v) {
        hideFAB();
        switch (v.getId()) {
            case R.id.fabProducts:
                Toast.makeText(getApplication(), "Floating Action Button Products", Toast.LENGTH_SHORT).show();
                break;
            case R.id.fabClients:
                edit(new Client());
                break;
            case R.id.fabBuy:
                Toast.makeText(getApplication(), "Floating Action Button Buy", Toast.LENGTH_SHORT).show();
                break;
        }

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
                init();
                break;
            case R.id.iBtnProducts:
                break;
            case R.id.iBtnUpDownLoad:
                break;
        }
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

}
