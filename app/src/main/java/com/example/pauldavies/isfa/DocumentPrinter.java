package com.example.pauldavies.isfa;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.text.TextPaint;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DocumentPrinter extends PrintDocumentAdapter
{
    Context context;
    PdfDocument pdfDocument;
    private int pageHeight;
    private int pageWidth;
    private int numberOfPages   =   1;
    private String file_name;
    private int leftBaseLine;
    CommonClass commonClass;
    SharedPrefs sharedPrefs;
    private String total;
    private String tax;
    DB db;

    private ArrayList<DocumentPrintDetails> documentPrintDetails;

    public DocumentPrinter(Context context, String file_name, ArrayList<DocumentPrintDetails> documentPrintDetails, String total, String tax)
    {
        super();

        this.context                =   context;
        this.file_name              =   file_name;
        this.documentPrintDetails   =   documentPrintDetails;
        this.total                  =   total;
        this.tax                    =   tax;
        db                          =   new DB(context);
        leftBaseLine                =   72;
        commonClass                 =   new CommonClass();
        sharedPrefs                 =   new SharedPrefs(context);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras)
    {
        pdfDocument =   new PrintedPdfDocument(context, newAttributes);
        pageHeight  =   newAttributes.getMediaSize().getHeightMils()/1000*72;
        pageWidth   =   newAttributes.getMediaSize().getWidthMils()/1000*72;

        if(cancellationSignal.isCanceled())
        {
            callback.onLayoutCancelled();
            return;
        }

        if(numberOfPages>0)
        {
            PrintDocumentInfo.Builder   printDocumentInfo
                    =   new PrintDocumentInfo.Builder(file_name)
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(numberOfPages);
            PrintDocumentInfo info = printDocumentInfo.build();

            callback.onLayoutFinished(info, true);

        }
        else
        {
            callback.onLayoutFailed("Could not get content for printing.");
        }
    }

    @Override
    public void onWrite(final PageRange[] pages, final ParcelFileDescriptor destination, final CancellationSignal cancellationSignal, final WriteResultCallback callback)
    {
        for(int i=0; i<numberOfPages; i++)
        {
            if(this.isPageInRange(pages, i))
            {
                PdfDocument.PageInfo pageInfo   =   new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, i).create();
                PdfDocument.Page    page        =   pdfDocument.startPage(pageInfo);
                if(cancellationSignal.isCanceled())
                {
                    callback.onWriteCancelled();
                    pdfDocument.close();
                    pdfDocument =   null;
                    return;
                }

                this.drawpage(page, i);
                pdfDocument.finishPage(page);

                try
                {
                    pdfDocument.writeTo(new FileOutputStream(destination.getFileDescriptor()));
                }
                catch (IOException e)
                {
                    callback.onWriteFailed(e.getMessage());
                    return;
                }
                finally {
                    pdfDocument.close();
                    pdfDocument =   null;
                }

                callback.onWriteFinished(pages);
            }
        }
    }

    private void drawpage(PdfDocument.Page page, int pageNumber)
    {
        DocumentPrintDetails documentDetail = documentPrintDetails.get(0);
        ArrayList<String[]> documentLines   =   documentDetail.document_lines;

        Canvas canvas   =   page.getCanvas();
        pageNumber++;

        TextPaint paint =   new TextPaint();
        paint.setColor(Color.DKGRAY);

        /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~DEFINITION OF THE HEADER GOES HERE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
        //The company title
        paint.setTextSize(30);
        canvas.drawText(DB.companyName(), this.getTitleStart(paint.measureText(DB.companyName()), paint.getTextSize()), leftBaseLine, paint);

        //The company postal address
        paint.setTextSize(20);
        canvas.drawText(DB.companyPostal(), this.getTitleStart(paint.measureText(DB.companyPostal()), paint.getTextSize()), leftBaseLine+18, paint);

        //The company contact
        canvas.drawText(DB.companyContact(), this.getTitleStart(paint.measureText(DB.companyContact()), paint.getTextSize()), leftBaseLine+36, paint);
        canvas.drawText(DB.companyEmail(), this.getTitleStart(paint.measureText(DB.companyEmail()), paint.getTextSize()), leftBaseLine+54, paint);

        int x_margin_left   =   20;
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(25);
        paint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
        canvas.drawText(documentDetail.getDocument_type(), x_margin_left, leftBaseLine+70, paint);

        paint.setTextSize(14);
        canvas.drawText(documentDetail.getDocument_type()+" To: "+documentDetail.getDocument_to(), x_margin_left, leftBaseLine+90, paint);

        int right_start =   pageWidth-(commonClass.getCurrentDate()+","+commonClass.getCurrentTime()).trim().length();

        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(DB.companyVATNumber(), right_start, leftBaseLine+70, paint);
        paint.setColor(Color.BLACK);
        canvas.drawText(documentDetail.getDocument_type()+" No.: "+documentDetail.getDocument_number(), right_start, leftBaseLine+90, paint);
        canvas.drawText(documentDetail.getDocument_type()+" on: "+documentDetail.getDocument_date(), right_start, leftBaseLine+110, paint);

        paint.setColor(Color.DKGRAY);
        canvas.drawText("Printed on: "+commonClass.getCurrentDate()+","+commonClass.getCurrentTime(), right_start, leftBaseLine+130, paint);

        paint.setColor(Color.BLACK);

        canvas.drawLine(0, 260, pageWidth, leftBaseLine+190, paint);
        canvas.drawLine(0, 261, pageWidth, leftBaseLine+191, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Description", x_margin_left, leftBaseLine+220, paint);
        canvas.drawText("Qty", x_margin_left+200, leftBaseLine+220, paint);
        canvas.drawText("Price", x_margin_left+280, leftBaseLine+220, paint);
        canvas.drawText("Disc.", x_margin_left+360, leftBaseLine+220, paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Amount", pageWidth-("Amount").trim().length(), leftBaseLine+220, paint);

        canvas.drawLine(0, 295, pageWidth,leftBaseLine+221, paint);

        //Get the lines onto the grid like
        int counter =   221;

        for(int i=0; i<documentLines.size(); i++)
        {
            String[] lines  =   documentLines.get(i);

            counter+=20;

            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(lines[0].trim(), x_margin_left, leftBaseLine+counter, paint);
            canvas.drawText(lines[1].trim(), x_margin_left+200, leftBaseLine+counter, paint);
            canvas.drawText(commonClass.getCurrencyFormat(Float.valueOf(lines[2].trim())), x_margin_left+280, leftBaseLine+counter, paint);
            canvas.drawText(commonClass.getCurrencyFormat(Float.valueOf(lines[3].trim())), x_margin_left+360, leftBaseLine+counter, paint);
            paint.setTextAlign(Paint.Align.RIGHT);
            canvas.drawText(commonClass.getCurrencyFormat(Float.valueOf(lines[4].trim())), pageWidth-lines[4].trim().length(), leftBaseLine+counter, paint);
        }

        counter += 5;
        canvas.drawLine(0, 295+(20*(documentLines.size()))+20, pageWidth,leftBaseLine+counter+20, paint);

        //Do the totals
        int current_y   =   leftBaseLine+counter+50;

        paint.setColor(Color.DKGRAY);
        paint.setTextSize(15);
        canvas.drawText("Sub Total:", 205, current_y, paint);
        canvas.drawText(commonClass.getCurrencyFormat(Float.valueOf(total))+" ;", 280+commonClass.getCurrencyFormat(Float.valueOf(total)).length(), current_y, paint);

        canvas.drawText("Sales Tax:", 380, current_y, paint);
        canvas.drawText(commonClass.getCurrencyFormat(Float.valueOf(tax))+" ;", 420+commonClass.getCurrencyFormat(Float.valueOf(tax)).length(), current_y, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(18);
        paint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC));
        canvas.drawText("Total:", 480, current_y, paint);
        canvas.drawText(commonClass.getCurrencyFormat(Float.valueOf(total)-Float.valueOf(tax)), 540+commonClass.getCurrencyFormat(Float.valueOf(total)-Float.valueOf(tax)).length(), current_y, paint);

        canvas.drawLine(0, current_y+10, pageWidth, leftBaseLine+counter+60, paint);
        canvas.drawLine(0, current_y+12, pageWidth, leftBaseLine+counter+62, paint);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(0, current_y+15, pageWidth, leftBaseLine+counter+65, paint);


        PdfDocument.PageInfo pageInfo   =   page.getInfo();

    }

    public int getTitleStart(float text_width, float text_size)
    {
        /*This function determines where the title starts on the page.
        * In this case, the title or text for that case should start at the center of the page.*/
        return (int)((pageWidth-text_width)/2f) - (int)(text_size/2f);
    }

    private boolean isPageInRange(PageRange[] pageRanges, int page)
    {
        for(int i=0; i<pageRanges.length; i++)
        {
            if(page>=pageRanges[i].getStart() && (page<=pageRanges[i].getEnd()))
            {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onFinish()
    {
        super.onFinish();
        Activity activity   =  (Activity) context;

        switch (activity.getLocalClassName())
        {
            case "GenerateCustomerOrder":
                //Sale the record to the sales header and transaction tables then delete the entries from the temp table.
                Cursor check_cursor =   db.getOrdersSalesOrderHeaders(sharedPrefs.getItem("print_sales_header"));
                if(check_cursor.getCount()>0)
                {
                    Toast.makeText(context, "Sales order with the same ID already saved.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Cursor sales_order_cursor   =   db.getSalesOrderForPreview(sharedPrefs.getItem("customer"));
                    if(sales_order_cursor.getCount()>0)
                    {
                        if(db.createSalesOrderHeader(sharedPrefs.getItem("print_sales_header"), sharedPrefs.getItem("customer"), sharedPrefs.getItem("print_sales_date"), db.getSaesOrderSum(sharedPrefs.getItem("customer")), sharedPrefs.getItem("username"), sharedPrefs.getItem("print_note"), "1") != -1)
                        {
                            while(sales_order_cursor.moveToNext())
                            {
                                db.createSalesOrderLines(commonClass.getLineId(), sharedPrefs.getItem("print_sales_header")
                                        , sales_order_cursor.getString(sales_order_cursor.getColumnIndex(Commons.SO_SKU_CODE))
                                        , sales_order_cursor.getString(sales_order_cursor.getColumnIndex(Commons.SO_SKU_NAME))
                                        , sales_order_cursor.getString(sales_order_cursor.getColumnIndex(Commons.SO_SKU_NAME))
                                        , sales_order_cursor.getInt(sales_order_cursor.getColumnIndex(Commons.SO_QTY))
                                        , sales_order_cursor.getFloat(sales_order_cursor.getColumnIndex(Commons.SO_UNIT_PRICE))
                                        , sales_order_cursor.getFloat(sales_order_cursor.getColumnIndex(Commons.SO_LINE_AMOUNT))
                                        , db.getProductUnitCost(sales_order_cursor.getString(sales_order_cursor.getColumnIndex(Commons.SO_SKU_CODE)))
                                        , commonClass.getCurrentDate());
                            }

                            db.deleteSalesTempForCustomer(sharedPrefs.getItem("customer"));
                            activity.recreate();
                        }
                        else
                        {
                            Toast.makeText(context, "Unable to save the order. Contact system admin.", Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(context, "No lines were found.", Toast.LENGTH_LONG).show();
                    }
                }
            break;
            case "SavedSalesOrderInvoiceAndPrint":
                if(db.updateSalesOrderHeaderToPrinted(sharedPrefs.getItem("print_sales_header")) != -1)
                {
                    activity.recreate();
                }
            break;
            default:

            break;
        }
    }
}
