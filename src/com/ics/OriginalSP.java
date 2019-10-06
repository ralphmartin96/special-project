package com.ics;

import java.util.ArrayList;

class OriginalSP {
    private String title;
    private ArrayList<String> authors;
    private String adviser;
    private String abstractText;
    private ArrayList<String> indexTerms;

    OriginalSP(String title, ArrayList<String> authors, String abstractText, ArrayList<String> indexTerms){
        this.title = title;
        this.authors = authors;
        this.abstractText = abstractText;
        this.indexTerms = indexTerms;
    }

    String getTitle(){
        return this.title;
    }

    ArrayList<String> getAuthors(){
        return this.authors;
    }

    String getAdviser(){
        return authors.get(authors.size()-1);
    }

    String getAbstractText(){
        return this.abstractText;
    }

    ArrayList<String> getIndexTerms(){
        return this.indexTerms;
    }

    private void setTitle(String title){
        this.title = title;
    }

    private void setAuthors(ArrayList<String> authors){
        this.authors = authors;
    }

    private void setAbstractText(String abstractText){
        this.abstractText = abstractText;
    }

    private void setIndexTerms(ArrayList<String> indexTerms){
        this.indexTerms = indexTerms;
    }
}
