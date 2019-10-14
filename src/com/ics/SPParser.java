package com.ics;

import com.ics.utils.PdfUtil;

//import net.sourceforge.tess4j.ITesseract;
//import net.sourceforge.tess4j.Tesseract;
//import net.sourceforge.tess4j.Tesseract1;
//import net.sourceforge.tess4j.TesseractException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import org.opencv.core.Core;
//import org.opencv.core.CvType;
//import org.opencv.core.Mat;
//import org.opencv.core.Scalar;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.util.*;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;

public class SPParser {

    private static String allText=null;
    private static String filepath="";
    private static String filetype="";

    private static int startRowIndex;
    private static int endRowIndex;
    private static int startColIndex;
    private static int endColIndex;

    private static DatabaseHelper db;

    static {System.loadLibrary(Core.NATIVE_LIBRARY_NAME);}

    private static OriginalSP parse(){

        String title;
        String abstractText;
        ArrayList<String> indexTerms;
        ArrayList<String> authors;

        OriginalSP sp = null;

        getTextFromFile();

        if(allText != null) {

            title = parserTitle();
            authors = parserAuthors();
            abstractText = parserAbstract();
            indexTerms = parserIndexTerms();

            sp = new OriginalSP(title, authors, abstractText, indexTerms);

            db.insertIntoProjects(sp);
        }

        return sp;
    }

    private static String parserTitle(){
        String title;

        startRowIndex = 1;
        endRowIndex = PdfUtil.findRowIndex("Abstract", allText)-2;
        startColIndex = 0;
        endColIndex = 100;

        title = PdfUtil.getAreaValue(startRowIndex, endRowIndex, startColIndex, endColIndex, allText)
                            .replaceAll("\n", " ")
                            .trim();

        return title;
    }

    private static ArrayList<String> parserAuthors(){
        ArrayList<String> authors = new ArrayList<>();
        String [] authorsList;
        String authorsText;

        startRowIndex = PdfUtil.findRowIndex("Abstract", allText)-1;
        endRowIndex = startRowIndex;
        startColIndex = 0;
        endColIndex = 100;

        authorsText = PdfUtil.getAreaValue(startRowIndex, endRowIndex, startColIndex, endColIndex, allText)
                        .replaceAll("and", ",")
                        .replaceAll(", ", ",")
                        .trim();

        if(authorsText.contains(","))
            authorsList = authorsText.split(",");
        else
            authorsList = authorsText.split("\n");

        Collections.addAll(authors, authorsList);

        return authors;
    }

    private static String parserAbstract(){
        String abstractText;

        startRowIndex = PdfUtil.findRowIndex("Abstract", allText);
        endRowIndex = PdfUtil.findRowIndex("Index Terms", allText)-1;
        startColIndex = 0;
        endColIndex = 100;

        if(endRowIndex < startRowIndex)
            endRowIndex = PdfUtil.findRowIndex("INTRODUCTION", allText)-1;

        abstractText = PdfUtil.getAreaValue(startRowIndex, endRowIndex, startColIndex, endColIndex, allText)
                        .replaceAll(".*(?i)ABSTRACT( )*[^A-Z0-9]", "").trim();

        return abstractText;
    }

    private static ArrayList<String> parserIndexTerms(){
        String indexTermsText;
        ArrayList<String> indexTerms = new ArrayList<>();

        startRowIndex = PdfUtil.findRowIndex("Index Terms", allText);
        endRowIndex = PdfUtil.findRowIndex("INTRODUCTION", allText)-1;
        startColIndex = 0;
        endColIndex = 100;

        if(startRowIndex>0) {
            indexTermsText = PdfUtil.getAreaValue(startRowIndex, endRowIndex, startColIndex, endColIndex, allText)
                    .replaceAll("(?i)Index Terms.*?(?=[A-Za-z0-9])", "")
                    .replaceAll("\n", " ")
                    .replaceAll(", ", ",")
                    .replaceAll("- ", "")
                    .trim();

            Collections.addAll(indexTerms, indexTermsText.split(","));
        }

        return indexTerms;
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
//        System.out.println("==========================================\n");
//        System.out.println("Title:\n" + sp.getTitle()+"\n");
//        System.out.println("Authors:\n");
//        for(String author : sp.getAuthors())
//            System.out.println(author);
//        System.out.println("\n");
//        System.out.println("Adviser: \n" + sp.getAdviser()+"\n");
//        System.out.println("Abstract: \n" + sp.getAbstractText()+"\n");
//        System.out.println("Index Terms: \n");
//        for(String indexTerm : sp.getIndexTerms())
//            System.out.println(indexTerm);
//        System.out.println("==========================================\n");

        db.getAllDataFromProjects();

//        db.deleteTableProjects();

    }

    private static void getTextFromFile(){
        System.out.println("TYPE: "+filetype);

        switch (filetype) {
            case "PDF":
                allText = getAllTextFromPDF(filepath);
//                PdfUtil.print(allText);
                break;
            case "TEX":
                System.out.println("IEEE file found.");
                break;
            case "JPG":
            case "JPEG":
            case "PNG":
                System.out.println("Image file found.");
                break;
            default:
                System.out.println("Invalid file!");
                break;
        }
    }

    private static String chooseFile(){
        String path="";

        File initialDirectory = new File("C:\\Users\\samsung\\IdeaProjects\\special-project\\input");

        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setCurrentDirectory(initialDirectory);
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int returnValue = jfc.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            if (jfc.getSelectedFile().isFile()) {
                System.out.println("You selected the directory: " + jfc.getSelectedFile());
                path = jfc.getSelectedFile().getPath();
            }
        }

        return path;
    }

//UNCOMMENT HIDDEN FUNCTIONS
//    private static void selectMultipleFiles(){
//        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
//        jfc.setDialogTitle("Multiple file and directory selection:");
//        jfc.setMultiSelectionEnabled(true);
//        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
//
//        int returnValue = jfc.showOpenDialog(null);
//        if (returnValue == JFileChooser.APPROVE_OPTION) {
//            File[] files = jfc.getSelectedFiles();
//            System.out.println("Directories found\n");
//            Arrays.asList(files).forEach(x -> {
//                if (x.isDirectory()) {
//                    System.out.println(x.getName());
//                }
//            });
//            System.out.println("\n- - - - - - - - - - -\n");
//            System.out.println("Files Found\n");
//            Arrays.asList(files).forEach(x -> {
//                if (x.isFile()) {
//                    System.out.println(x.getName());
//                }
//            });
//        }
//    }
//
//    private static String findMatch(String pattern, String input) {
//        Pattern p = Pattern.compile(pattern);
//        Matcher m = p.matcher(input);
//        if (m.find()) {
//            return m.group();
//        }
//        return "";
//    }
//
//    private static void testOpenCV(){
//        System.out.println("Welcome to OpenCV " + Core.VERSION);
//        Mat m = new Mat(5, 10, CvType.CV_8UC1, new Scalar(0));
//        System.out.println("OpenCV Mat: " + m);
//        Mat mr1 = m.row(1);
//        mr1.setTo(new Scalar(1));
//        Mat mc5 = m.col(5);
//        mc5.setTo(new Scalar(5));
//        System.out.println("OpenCV Mat data:\n" + m.dump());
//    }
//
//    private static void testTesseract(){
//        File imageFile = new File(filepath);
//        ITesseract instance = new Tesseract();  // JNA Interface Mapping
////         ITesseract instance = new Tesseract1(); // JNA Direct Mapping
//
//        try {
//            String result = instance.doOCR(imageFile);
//            System.out.println(result);
//        } catch (TesseractException e) {
//            System.err.println(e.getMessage());
//        }
//    }

    public static void main(String[] args) {

        db = new DatabaseHelper();

        int path_length;

        while(filepath.equals("")) {
            filepath = chooseFile();
            if(filepath.equals("")) System.err.println("Err: input file path");
        }

        path_length = filepath.length();

        filetype = filepath.substring(path_length-5, path_length)
                    .replaceAll(".*?\\.", "")
                    .toUpperCase()
                    .trim();

        OriginalSP sp = parse();

        test(sp);

        db.deleteTableProjects();

//
//        testOpenCV();
//        testTesseract();
    }
}
