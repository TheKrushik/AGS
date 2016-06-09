package info.krushik.android.ags.objects;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;

import info.krushik.android.ags.db.DataBaseHelper;

//AsyncTaskLoader - работа в фоновом потоке, отличие от AsyncTask: возвращает результат не обращаясь повторно в БД
public class ProductsLoader extends AsyncTaskLoader<ArrayList<Product>> {

    private Context mContext;
    private ArrayList<Product> Products;

    //Конструктор
    public ProductsLoader(Context context) {
        super(context);

        this.mContext = context;
    }

    //Работа в фоновом потоке
    @Override
    public ArrayList<Product> loadInBackground() {
        DataBaseHelper helper = new DataBaseHelper(mContext);//создаем helper

        return helper.getProducts();//вызываем метод все студенты
    }

    //Возврат результата
    @Override
    public void deliverResult(ArrayList<Product> data) {
        if (isReset()) {//проверка зарезечен
            return;
        }

        Products = data;

        if (isStarted()) {//проверка застартован
            super.deliverResult(data);
        }
    }

    //Старт
    @Override
    protected void onStartLoading() {
        if (Products != null) {//если студенты не пустые
            deliverResult(Products);//он их возвращает
        }

        if (takeContentChanged() || Products == null) {
            forceLoad();
        }
    }

    //Стоп
    @Override
    protected void onStopLoading() {
        cancelLoad();//останавливает работу
    }

    //Перезагрузка
    @Override
    protected void onReset() {
        onStopLoading();

        if (Products != null) {
            Products = null;//очищает массив студентов
        }
    }
}
