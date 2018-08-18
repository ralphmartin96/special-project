package com.ics;

public class OriginalSP {
    private String title;
    private String [] authors;
    private String abstractText;

    protected String getTitle(){
        return this.title;
    }

    protected String [] getAuthors(){
        return this.authors;
    }

    protected String getAbstractText(){
        return this.abstractText;
    }

    protected void setTitle(String title){
        this.title = title;
    }

    protected void setAuthors(String [] authors){
        this.authors = authors;
    }

    protected void setAbstractText(String abstractText){
        this.abstractText = abstractText;
    }
}
