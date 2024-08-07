package com.example.quizappstates;

public class Question {
    public Question(String country, String capital) {
        this.country = country;
        this.capital = capital;
    }

    private String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public Question() {
    }

    private String capital;

}
