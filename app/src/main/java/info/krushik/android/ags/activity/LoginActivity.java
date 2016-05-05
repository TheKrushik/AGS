package info.krushik.android.ags.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import info.krushik.android.ags.R;
import info.krushik.android.ags.objects.LoginInfo;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String LOGIN_INFO = "info.krushik.android.ags.login_info";

    private EditText mEtLogin;
    private EditText mEtPassword;
    private Button mBtnLogin;
    private Button mBtnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEtLogin = (EditText) findViewById(R.id.etLogin);
        mEtPassword = (EditText) findViewById(R.id.etPassword);
        mBtnLogin = (Button) findViewById(R.id.btnLogin);
        mBtnRegister = (Button) findViewById(R.id.btnRegister);

        mBtnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.btnLogin:
                //вход в систему и переход к внутренним Activity.
                LoginInfo loginInfo = new LoginInfo();
                loginInfo.getLogin();
                loginInfo.getPassword();

                Intent intent = new Intent(this, BuyActivity.class);
                intent.putExtra(LOGIN_INFO, loginInfo);
                startActivity(intent);
                break;
            case R.id.btnRegister:
                //здесь реализация регистра нового пользователя базы
                break;
        }
    }
}
