package info.krushik.android.ags.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;

import info.krushik.android.ags.db.DataBaseHelper;
import info.krushik.android.ags.objects.Client;

//AsyncTaskLoader - работа в фоновом потоке, отличие от AsyncTask: возвращает результат не обращаяfalseсь повторно в БД
public class ClientsLoader extends AsyncTaskLoader<ArrayList<Client>> {

    private Context mContext;
    private ArrayList<Client> mClients;

    //Конструктор
    public ClientsLoader(Context context) {
        super(context);

        this.mContext = context;
    }

    //Работа в фоновом потоке
    @Override
    public ArrayList<Client> loadInBackground() {
        DataBaseHelper helper = new DataBaseHelper(mContext);//создаем helper

        return helper.getClients();//вызываем метод все студенты
    }

    //Возврат результата
    @Override
    public void deliverResult(ArrayList<Client> data) {
        if (isReset()) {//проверка зарезечен
            return;
        }

        mClients = data;

        if (isStarted()) {//проверка застартован
            super.deliverResult(data);
        }
    }

    //Старт
    @Override
    protected void onStartLoading() {
        if (mClients != null) {//если студенты не пустые
            deliverResult(mClients);//он их возвращает
        }

        if (takeContentChanged() || mClients == null) {
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

        if (mClients != null) {
            mClients = null;//очищает массив студентов
        }
    }
}
