package com.ilnar.tester;

import android.util.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
    Вспомогательный класс для хранения вопроса. Содержит ответы, текст вопроса и номер правильного ответа.
 */
class Question {

    private String text;
    private List<String> answersList = new ArrayList<>();
    private int correctAnswer;

    public Question() {
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void addAnswer(String answer) {
        answersList.add(answer);
    }

    public List<String> getAnswersList() {
        return answersList;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public boolean checkAnswer(int i) {
        return i == correctAnswer;
    }
}

/*
    Вспомогательный класс для хранения списка вопросов и считывания с файла.
 */
public class QuestionsList {
    private List<Question> questions = new ArrayList<>();

    public QuestionsList() {

    }

    public QuestionsList(File f) throws IOException {
        /*
            Файл должен представлять из себя данные в формате json.
            [
                {
                    "text": "текст вопроса",
                    "answers": [
                        ответы, через запятую
                    ],
                    "correctAnswer" : номер правильного ответа
                }
            ]
         */
        try (JsonReader reader = new JsonReader(new FileReader(f))) {
            reader.beginArray();
            while (reader.hasNext()) {//пока в массиве объектов есть данные
                Question current = new Question();
                reader.beginObject();//считываем следующий вопрос
                while (reader.hasNext()) {
                    String token = reader.nextName();
                    switch (token) {
                        case "text":
                            current.setText(reader.nextString());
                            break;
                        case "answers":
                            reader.beginArray();
                            while (reader.hasNext()) {//считываем массив ответов
                                current.addAnswer(reader.nextString());
                            }
                            reader.endArray();
                            break;
                        case "correctAnswer":
                            current.setCorrectAnswer(reader.nextInt());
                            break;
                        default:
                            reader.skipValue();
                    }
                }
                reader.endObject();
                questions.add(current);
            }
            reader.endArray();
        }
    }

    public int size() {
        return questions.size();
    }

    public Question getQuestion(int pos) {
        return questions.get(pos);
    }

    public void add(Question q) {
        questions.add(q);
    }
}