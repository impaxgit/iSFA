package com.example.pauldavies.isfa;

import android.content.Context;
import android.print.PrintManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class SettingsCustom extends AppCompatActivity
{
    Context context;
    CommonClass commonClass;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_custom);

        this.setTitle("Settings");

        context =   this;
        commonClass =   new CommonClass();

        findViewById(R.id.settings_printer).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PrintManager printManager   =   (PrintManager)context.getSystemService(PRINT_SERVICE);
                String print_job_name       =   context.getString(R.string.app_name);
                DocumentPrintDetails documentPrintDetails   =   new DocumentPrintDetails("Paul Davies", "123456789",  commonClass.getCurrentDate(), "Invoice");
                documentPrintDetails.document_lines.add(new String[]{"Item Name", "11", "55", "0", "605"});
                documentPrintDetails.document_lines.add(new String[]{"Item Name", "1", "750", "20", "730"});
                documentPrintDetails.document_lines.add(new String[]{"Item Name", "2", "125", "0", "250"});
                documentPrintDetails.document_lines.add(new String[]{"Item Name", "8", "74", "5", "592"});
                documentPrintDetails.document_lines.add(new String[]{"Item Name", "5", "10", "5", "50"});
                ArrayList<DocumentPrintDetails> documentPrintInfo    =   new ArrayList<>();
                documentPrintInfo.add(documentPrintDetails);

                printManager.print(print_job_name, new DocumentPrinter(context, "PDD TESTING", documentPrintInfo, "2227", "0"), null);
            }
        });
    }
}
