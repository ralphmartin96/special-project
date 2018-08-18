package com.ics;

import com.ics.utils.PdfUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class Main {

    private static String allText;
    private static String filepath;

    private static int startRowIndex;
    private static int endRowIndex;
    private static int startColIndex;
    private static int endColIndex;

    private static OriginalSP parse(OriginalSP sp){
        allText = getAllTextFromPDF(filepath);
        PdfUtil.print(allText);

        parserTitle(sp);
        parserAuthors(sp);
        parserAbstract(sp);

        return sp;
    }

    private static void parserTitle(OriginalSP sp){
        String title;

        startRowIndex = 1;
        endRowIndex = 1;
        startColIndex = 0;
        endColIndex = 100;

        title = PdfUtil.getAreaValue(startRowIndex, endRowIndex, startColIndex, endColIndex, allText);

        sp.setTitle(title);
    }

    private static void parserAuthors(OriginalSP sp){
        String [] authors;
        String authorsText;

        System.err.println(sp.getTitle().replaceAll(" ", ","));

        startRowIndex = PdfUtil.findRowIndex(sp.getTitle(), allText)+1;
        endRowIndex = PdfUtil.findRowIndex("Abstract", allText)-1;
        startColIndex = 0;
        endColIndex = 100;

        authorsText = PdfUtil.getAreaValue(startRowIndex, endRowIndex, startColIndex, endColIndex, allText)
                        .replaceAll("and", ",")
                        .replaceAll(", ", ",")
                        .trim();

        if(authorsText.contains(","))
            authors = authorsText.split(",");
        else
            authors = authorsText.split("\n");

        sp.setAuthors(authors);
    }

    private static void parserAbstract(OriginalSP sp){
        String abstractText;

        startRowIndex = PdfUtil.findRowIndex("Abstract", allText);
        endRowIndex = PdfUtil.findRowIndex("INTRODUCTION", allText)-1;
        startColIndex = 0;
        endColIndex = 100;

        abstractText = PdfUtil.getAreaValue(startRowIndex, endRowIndex, startColIndex, endColIndex, allText)
                        .replaceAll("(?i)ABSTRACT( )*[^A-Z0-9]", "").trim();

        sp.setAbstractText(abstractText);
    }

    private static String getAllTextFromPDF(String filepath){
        String text="";

        try{
            PDDocument document = PDDocument.load(new File(filepath));
            PDFTextStripper pdfTextStripper = new PDFTextStripper();

            text = pdfTextStripper.getText(document);

            document.close();

        }catch(IOException e){
            e.printStackTrace();
        }

        return text;
    }

    private static void test(OriginalSP sp){
        System.out.println("==========================================\n");
        System.out.println("Title:\n" + sp.getTitle()+"\n");
        System.out.println("Authors:\n");
        for(String author : sp.getAuthors())
            System.out.println(author);
        System.out.println("\n");
        System.out.println("Abstract: \n" + sp.getAbstractText());
        System.out.println("==========================================\n");
    }

    public static void main(String[] args) {

        filepath = "C:\\Users\\samsung\\Desktop\\Decena Doria Video Searching Using Sketches\\Journal Article\\cs190-ieee.pdf";

        OriginalSP sp = new OriginalSP();

        parse(sp);
        test(sp);
    }
}
