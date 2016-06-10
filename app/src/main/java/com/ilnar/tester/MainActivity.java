package com.ilnar.tester;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    /*
        файл теста, поле ФИО студента, поле группы студента,поле выбора файла
     */
    File path;
    EditText name;
    EditText group;
    TextView selectFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = (EditText)findViewById(R.id.name);
        group = (EditText)findViewById(R.id.group);
        selectFile = (TextView) findViewById(R.id.select_file);
        if (selectFile != null) {
            selectFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                        выводит диалогое окно для выбора файла
                    */
                    new FileChooser(MainActivity.this).setFileListener(new FileChooser.FileSelectedListener() {
                        @Override
                        public void fileSelected(File file) {
                            path = file;
                            selectFile.setText(file.getName());
                        }
                    }).showDialog();
                }
            });
        }
        Button start = (Button)findViewById(R.id.start_button);
        if (start != null) {
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*
                        Создает новое activity для теста, введенные данные передаются через intent
                     */
                    boolean error = false;
                    if (TextUtils.isEmpty(name.getText())) {
                        name.setHint(getString(R.string.enter_name));
                        error = true;
                    }
                    if (TextUtils.isEmpty(group.getText())) {
                        group.setHint(getString(R.string.enter_group));
                        error = true;
                    }
                    if (path == null) {
                        selectFile.setError(getString(R.string.select_file));
                        error = true;
                    }
                    if (!error) {
                        Intent intent = new Intent(MainActivity.this, TestActivity.class);
                        intent.putExtra("path", path.toString());
                        intent.putExtra("studentName", name.getText().toString());
                        intent.putExtra("studentGroup", group.getText().toString());
                        startActivity(intent);
                    }
                }
            });
        }
    }
}
