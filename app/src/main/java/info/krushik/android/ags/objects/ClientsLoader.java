package info.krushik.android.ags.objects;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

import info.krushik.android.ags.db.DataBaseHelper;

//AsyncTaskLoader - работа в фоновом потоке, отличие от AsyncTask: возвращает результат не обращаясь повторно в БД
public class ClientsLoader extends AsyncTaskLoader<ArrayList<Client>> {

    private Context mContext;
    private ArrayList<Client> Clients;

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

        Clients = data;

        if (isStarted()) {//проверка застартован
            super.deliverResult(data);
        }
    }

    //Старт
    @Override
    protected void onStartLoading() {
        if (Clients != null) {//если студенты не пустые
            deliverResult(Clients);//он их возвращает
        }

        if (takeContentChanged() || Clients == null) {
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

        if (Clients != null) {
            Clients = null;//очищает массив студентов
        }
    }
}
