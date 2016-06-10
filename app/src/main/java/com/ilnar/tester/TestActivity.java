package com.ilnar.tester;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class TestActivity extends AppCompatActivity {

    QuestionsList questionsList;
    RadioGroup radioGroup;
    TextView text;
    TextView info;
    Button submit;
    int currentQuestion = -1;
    int correct = 0;
    String studentName;
    String studentGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        if (getIntent() != null) {
            //получаем данные, переданные с другого activity
            Intent intent = getIntent();
            File f = new File(intent.getStringExtra("path"));
            studentName = intent.getStringExtra("studentName");
            studentGroup = intent.getStringExtra("studentGroup");
            try {
                questionsList = new QuestionsList(f);
            } catch (Exception e) {
                //Если не смогли считать вопросы, выводим сообщение об ошибки, завершаем работу
                Toast.makeText(this, getString(R.string.file_read_error), Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }
        submit = (Button)findViewById(R.id.submit_button);
        radioGroup = (RadioGroup)findViewById(R.id.radio_group);
        text = (TextView)findViewById(R.id.question_text);
        info = (TextView)findViewById(R.id.info);
        showQuestion();
        if (submit != null && radioGroup != null) {
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int radioButtonID = radioGroup.getCheckedRadioButtonId();
                    View radioButton = radioGroup.findViewById(radioButtonID);
                    int index = radioGroup.indexOfChild(radioButton);
                    if (questionsList.getQuestion(currentQuestion).checkAnswer(index)) {
                        correct++;
                    }
                    showQuestion();
                }
            });
        }
    }

    /*
        Показывает следующий вопрос на экране
     */
    private void showQuestion() {
        if (currentQuestion + 1 < questionsList.size()) {
            Question question = questionsList.getQuestion(++currentQuestion);
            final RadioButton[] radioButton = new RadioButton[question.getAnswersList().size()];
            while (radioGroup.getChildCount() > 0) {
                radioGroup.removeView(radioGroup.getChildAt(0));
            }
            //очистили предыдующие ответы
            text.setText(question.getText());
            info.setText(String.format(getString(R.string.question_number), currentQuestion + 1, questionsList.size()));
            for (int i = 0; i < question.getAnswersList().size(); i++) {
                radioButton[i] = new RadioButton(this);
                radioGroup.addView(radioButton[i]);
                radioButton[i].setText(question.getAnswersList().get(i));
            }
            //довавили новые ответы
        } else {
            //если вопросов больше нет, показываем результат
            showResult();
        }
    }

    /*
        Показывает результат теста
     */
    private void showResult() {
        radioGroup.setVisibility(View.GONE);//скрывает лишние объекты
        submit.setVisibility(View.GONE);
        info.setText(String.format(getString(R.string.correct_answers), correct, questionsList.size()));
        text.setText(String.format("%s: %s\n %s: %s", getString(R.string.name), studentName, getString(R.string.group), studentGroup));
    }
}
