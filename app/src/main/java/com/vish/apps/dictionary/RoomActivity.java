package com.vish.apps.dictionary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.vish.apps.dictionary.util.AppDatabase;
import com.vish.apps.dictionary.util.Creole;

public class RoomActivity extends AppCompatActivity {

    private EditText edtWord;
    private EditText edtDefinition;
    private EditText edtDefinitionEn;
    private Button btnAdd;
    public AppDatabase appDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        edtWord = findViewById(R.id.act_room_edt_word);
        edtDefinition = findViewById(R.id.act_room_edt_definition);
        edtDefinitionEn = findViewById(R.id.act_room_edt_definition_en);
        btnAdd = findViewById(R.id.act_room_btn_add);

        appDatabase = Room.databaseBuilder(this, AppDatabase.class, "word_creole").allowMainThreadQueries().build();
    }

    public void btnClick(View view) {
        Creole creole = new Creole();
        creole.setWord(edtWord.getText().toString());
        creole.setDefinition(edtDefinition.getText().toString());
        creole.setWordEn(edtDefinitionEn.getText().toString());

        appDatabase.appDatabaseObject().addWord(creole);
    }

}