package com.example.fc;

import java.io.Serializable;

public class Class_Card implements Serializable {
    private String id, idParent, question, answer;

    public Class_Card() {
    }

    public Class_Card(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }


    public String getIdParent() {
        return idParent;
    }

    public void setIdParent(String idParent) {
        this.idParent = idParent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
