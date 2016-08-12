package com.example.tacademy.sampledata;

import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
//로더를 이용한 자동 검색 기능
public class LoaderActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    EditText inputView;
    ListView listView;
    SimpleCursorAdapter mAdapter;

    String[] projection = {ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME};
    String selection = "((" + ContactsContract.Contacts.DISPLAY_NAME + " NOT NULL) AND (" +
            ContactsContract.Contacts.DISPLAY_NAME + " != ''))";
    String sort = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);

        inputView = (EditText)findViewById(R.id.edit_input);
        listView= (ListView)findViewById(R.id.list_contact);

        String[] from = {ContactsContract.Contacts.DISPLAY_NAME};
        int[] to = {android.R.id.text1};

        mAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, null, from, to, 0);
        listView.setAdapter(mAdapter);

        inputView.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                String keyword = s.toString();
                Bundle b = new Bundle();
                b.putString("keyword", keyword);
                getSupportLoaderManager().restartLoader(0, b, LoaderActivity.this);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //로더를 써본다. 로더 매니저를 쓸려면 로더 콜백이 필요 == 로더 콜백 객체를 별로 만들어도되고 implement 로 해서 해도 됨
        getSupportLoaderManager().initLoader(0,null, this); //로더매니져.데이타좀갖다줘(리퀘스트 코드id,아규먼트 요청할때 값, 로더콜백)
        //로더매니져가 onCreateLoader 요청함
    }

    @Override//로더를 생성 ==> 커서로더 생성

    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        if (args != null) {
            String keyword = args.getString("keyword");
            if (!TextUtils.isEmpty(keyword)) {
                uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, Uri.encode(keyword));
            }
        }
        return new CursorLoader(this, uri, projection, selection, null, sort);
        // 만들어주고 그 얻은 데이타를 onLoaderFinished호출
        //이전에 쓰인 로더 는 알아서 닫아줌

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);  //swapCursor == > 기존에 있는 커서를 자기 껄로 교체만해 주는 놈
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null); //이전에 쓰인 로더 는 알아서 닫아줌
    }
}
