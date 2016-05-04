package info.krushik.android.ags;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mEtLogin;
    EditText mEtPassword;
    Button mBtnOk;
    Button mBtnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEtLogin = (EditText) findViewById(R.id.etLogin);
        mEtPassword = (EditText) findViewById(R.id.etPassword);

        mBtnOk = (Button) findViewById(R.id.btnOk);
        mBtnRegister = (Button) findViewById(R.id.btnRegister);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.btnOk:
                //вход в систему и переход к внутренним Activity

                break;
            case R.id.btnRegister:
                //здесь реализация регистра нового пользователя базы
                break;
        }
    }
}
