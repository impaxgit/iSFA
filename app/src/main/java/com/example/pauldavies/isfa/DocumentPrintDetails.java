package com.example.pauldavies.isfa;

import java.util.ArrayList;

public class DocumentPrintDetails
{
    /*This class defines the details to be printed on the document.
    * They are the header and line details.
    * It cuts across all the documents that have header and lines.
    * It therefore should not be changed unless the developer is very sure that the changes won't affect the rest of the documents;
    * especially the manner in which it is called from wherever in the application.*/

    String document_to;
    String document_number;
    String document_date;
    String document_type;

    public ArrayList<String[]> document_lines  =   new ArrayList<>();

    public DocumentPrintDetails(String document_to, String document_number, String document_date, String document_type)
    {
        this.document_to    =   document_to;
        this.document_number=   document_number;
        this.document_date  =   document_date;
        this.document_type  =   document_type;
    }

    public String getDocument_type()
    {
        return document_type;
    }

    public String getDocument_to()
    {
        return document_to;
    }

    public String getDocument_number()
    {
        return document_number;
    }

    public String getDocument_date()
    {
        return document_date;
    }

    public ArrayList<String[]> getDocument_lines()
    {
        return document_lines;
    }
}
