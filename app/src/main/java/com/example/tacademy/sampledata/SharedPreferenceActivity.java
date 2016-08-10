package com.example.tacademy.sampledata;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SharedPreferenceActivity extends AppCompatActivity {


//이름을 몰라도 다른 곳에서 쓸수 있도록 해주기위해
    //mprefs = PreferenceManager.getDefaultSharedPreferences(this) 선언해주면된다.
    //패키지명으로 Preference로 만들어 짐. 일반적으로 많이 사용

    EditText emailView, passwordView;
    SharedPreferences mPrefs; //SharedPreferences 는 값을 읽어오기 위해 선언
    SharedPreferences.Editor mEditor; //  SharedPreferences 값을 저장하기 위해 선언
    private static final String PREF_NAME = "my_setttings";  //저장할 파일 키 이름 선언

    private static final String KEY_EMAIL ="email"; // 어떤 값으로 저장할지 선언 key 선언
    private static final String KEY_PASSWORD = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_preference);
        emailView = (EditText)findViewById(R.id.edit_email);
        passwordView = (EditText)findViewById(R.id.edit_password);

        Button btn  = (Button)findViewById(R.id.btn_save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailView.getText().toString();
                String password = passwordView.getText().toString();
//                mEditor.putString(KEY_EMAIL, email); // 에디터를 이용하여 값을 저장하고 여끼까지 메모리에 저장한다.
//                mEditor.putString(KEY_PASSWORD, password); // 에디터를 이용하여 값을 저장하고 여끼까지 메모리에 저장한다.
//                mEditor.commit(); // commit 해야 File에 저장한다.

                PropertyManager.getInstance().setEmail(email); // PropertyManager 를 선언해주어 세팅없이 불러와주는 줌 ..
                PropertyManager.getInstance().setPassword(password);
            }
        });
        mPrefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPrefs.edit();


//        String email = mPrefs.getString(KEY_EMAIL, "");   //  String email = mPrefs.getString(키값, " ") ==> SharedPreferences 저장된 값을 읽어오고싶다.
//        String password = mPrefs.getString(KEY_PASSWORD, "");
        String email = PropertyManager.getInstance().getEmail();
        String password = PropertyManager.getInstance().getPassword();
        emailView.setText(email);
        passwordView.setText(password);
    }
}
