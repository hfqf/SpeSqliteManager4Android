package com.points.spesqlitemanager.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.RoomDatabase;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.points.spesqlitemanager.R;
import com.points.spesqlitemanager.room.AppDatabase;
import com.points.spesqlitemanager.room.ServerModel;
import cn.autorepairehelper.spesqlite.spesqlite.SpeSqliteBaseInterface;
import cn.autorepairehelper.spesqlite.spesqlite.SpeSqliteOpenHelperService;
import cn.autorepairehelper.spesqlite.spesqlite.SpeSqliteRoomService;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;




    /**
     * 临时测试方法
     * @param model
     */
    private void insert(RoomDatabase roomm,ServerModel model) {
        new Thread(() -> {
            ((AppDatabase)roomm).serverDao().insert(model);
        }).start();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //room根据模型自动创建数据库


        //1.直接创建SQLiteOpenHelper
//        SpeSqliteOpenHelperService.getInstance(this);
        //2.创建SQLiteOpenHelper且需要监听db
//        SpeSqliteOpenHelperService.getInstance(this, new SpeSqliteBaseInterface() {
//            @Override
//            public <T> void onCreate(T db, RoomDatabase room) {
//
//            }
//
//            @Override
//            public <T> void onOpen(T db, RoomDatabase room) {
//
//            }
//
//            @Override
//            public <T> void onUpgrade(T db, int oldVersion, int newVersion, RoomDatabase room) {
//
//            }
//        });
        //3.直接创建room
        SpeSqliteRoomService.getInstance(this,AppDatabase.class);

        //4.创建room，且需要监听db
        SpeSqliteRoomService.getInstance(this,AppDatabase.class,new SpeSqliteBaseInterface() {
            @Override
            public <T> void onCreate(T db, RoomDatabase room) {
                //TODO  删除测试插入代码
                if(room != null){
                    ServerModel model = new ServerModel();
                    model.setId("1");
                    model.setHost("1");
                    model.setLang("1");
                    model.setName("1");
                    model.setVersion("1");
                    insert(room,model);
                }

            }
            @Override
            public <T> void onOpen(T db, RoomDatabase room) {

            }

            @Override
            public <T> void onUpgrade(T db, int oldVersion, int newVersion, RoomDatabase room) {

            }
        });


        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}