package de.unidue.inf.is.domain;

public class Kommentar {

    private String kommentator;
    private String text;

    public Kommentar() {

    }

    public String getKommentator() {
        return kommentator;
    }

    public void setKommentator(String kommentator) {
        this.kommentator = kommentator;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
