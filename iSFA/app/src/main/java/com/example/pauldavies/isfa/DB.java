package com.example.pauldavies.isfa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper
{
    Context context;

    public DB(Context context)
    {
        super(context, Commons.DB_NAME, null, Commons.DB_VERSION);
        this.context    =   context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(Commons.product_master);
        db.execSQL(Commons.customer_master);
        db.execSQL(Commons.journey_plan);
        db.execSQL(Commons.tender_types);
        db.execSQL(Commons.daily_check);
        db.execSQL(Commons.sales_order_header);
        db.execSQL(Commons.sales_trans_lines);
        db.execSQL(Commons.sales_trans_header);
        db.execSQL(Commons.transaction_tender_types);
        db.execSQL(Commons.bad_stock);
        db.execSQL(Commons.stock_request);
        db.execSQL(Commons.feedback);
        db.execSQL(Commons.customer_check_in_out);
        db.execSQL(Commons.sales_order_lines);
        db.execSQL(Commons.routes);
        db.execSQL(Commons.notificatiions);
        db.execSQL(Commons.assets);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

        /*Sync data before dropping, to preserve the data.*/
        db.execSQL(Commons.product_master_drop);
        db.execSQL(Commons.customer_master_drop);
        db.execSQL(Commons.journey_plan_drop);
        db.execSQL(Commons.tender_types_drop);
        db.execSQL(Commons.daily_check_drop);
        db.execSQL(Commons.sales_order_header_drop);
        db.execSQL(Commons.sales_order_lines_drop);
        db.execSQL(Commons.sales_trans_header_drop);
        db.execSQL(Commons.sales_trans_lines_drop);
        db.execSQL(Commons.transaction_tender_types_drop);
        db.execSQL(Commons.bad_stock_drop);
        db.execSQL(Commons.stock_request_drop);
        db.execSQL(Commons.feedback_drop);
        db.execSQL(Commons.customer_check_in_out_drop);
        db.execSQL(Commons.routes_drop);
        db.execSQL(Commons.notification_drop);
        db.execSQL(Commons.assets_drop);

        this.onCreate(db);

        /*Prompt user to re-import data from external*/
    }

    //Insert statements
    public long createCustomer(String customer_code, String customer_name, String customer_description, String customer_outlet_type, String customer_location, String customer_geo_coordinates, String customer_date_created, String customer_type, int  customer_is_taxable, String customer_email, String customer_phone, String customer_contact_person, String customer_contact_email, String customer_contact_phone, float customer_credit_limit, float customer_credit_balance, int customer_status, int sync)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues =   new ContentValues();
        contentValues.put(Commons.CUSTOMER_CODE, customer_code);
        contentValues.put(Commons.CUSTOMER_NAME, customer_name);
        contentValues.put(Commons.CUSTOMER_DESCRIPTION, customer_description);
        contentValues.put(Commons.CUSTOMER_OUTLET_TYPE, customer_outlet_type);
        contentValues.put(Commons.CUSTOMER_LOCATION, customer_location);
        contentValues.put(Commons.CUSTOMER_GEO_COORDINATES, customer_geo_coordinates);
        contentValues.put(Commons.CUSTOMER_DATE_CREATED, customer_date_created);
        contentValues.put(Commons.CUSTOMER_TYPE, customer_type);
        contentValues.put(Commons.CUSTOMER_TAXABLE, customer_is_taxable);
        contentValues.put(Commons.CUSTOMER_EMAIL, customer_email);
        contentValues.put(Commons.CUSTOMER_PHONE, customer_phone);
        contentValues.put(Commons.CUSTOMER_CONTACT_PERSON, customer_contact_person);
        contentValues.put(Commons.CUSTOMER_CONTACT_EMAIL, customer_contact_email);
        contentValues.put(Commons.CUSTOMER_CONTACT_PHONE, customer_contact_phone);
        contentValues.put(Commons.CUSTOMER_CREDIT_LIMIT, customer_credit_limit);
        contentValues.put(Commons.CUSTOMER_CREDIT_BALANCE, customer_credit_balance);
        contentValues.put(Commons.CUSTOMER_STATUS, customer_status);
        contentValues.put(Commons.CUSTOMER_SYNC, sync);

        return db.insert(Commons.CUSTOMER_MASTER, null, contentValues);
    }

    public long createOrdersHeaders(int order_id,String order_no, String customer_code, String date , Float amount,String created_by)

    {
       SQLiteDatabase db=this.getWritableDatabase();
       ContentValues contentValues=new ContentValues();
       contentValues.put(Commons.SALES_ORDER_HEADER_ID,order_id);
       contentValues.put(Commons.SALES_ORDER_HEADER_NO,order_no);
       contentValues.put(Commons.SALES_ORDER_HEADER_CUSTOMER_CODE,customer_code);
       contentValues.put(Commons.SALES_ORDER_HEADER_DATE_AND_TIME,date);
       contentValues.put(Commons.SALES_ORDER_HEADER_AMOUNT,amount);
       contentValues.put(Commons.SALES_ORDER_HEADER_CREATED_BY,created_by);

        return db.insert(Commons.SALES_ORDER_HEADER,null,contentValues);
    }

    public long GetSalesOrders(int id,String trans_header_id, String customer_code, String date , Float trans_amount,String created_by,String status)

    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Commons.HEADER_ID,id);
        contentValues.put(Commons.SALES_TRANS_HEADER_NO,trans_header_id);
        contentValues.put(Commons.SALES_TRANS_HEADER_CUSTOMER_CODE,customer_code);
        contentValues.put(Commons.SALES_TRANS_HEADER_DATE_TIME,date);
        contentValues.put(Commons.SALES_TRANS_HEADER_TOTAL_AMOUNT,trans_amount);
        contentValues.put(Commons.SALES_TRANS_HEADER_CREATED_BY,created_by);

        return  db.insert(Commons.SALES_TRANSACTION_HEADER,null,contentValues);
    }

    public long GetSalesOrderLines(int id,String order_header,String orderLine_no,String product_code, String product_name,String product_description, Integer qty_ordered,Integer qty_delivered,Float unit_price, Float total_amount,String date,String status,String comments)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Commons.LINE_ID,id);
        contentValues.put(Commons.SALES_TRANS_LINES_HEADER_ID,order_header);
        contentValues.put(Commons.SALES_TRANS_LINE_NO,orderLine_no);
        contentValues.put(Commons.SALES_TRANS_ITEM_LINES_CODE,product_code);
        contentValues.put(Commons.SALES_TRANS_ITEM_LINES_NAME,product_name);
        contentValues.put(Commons.SALES_TRANS_LINE_ITEM_DESCRIPTION,product_description);
        contentValues.put(Commons.SALES_TRANS_LINE_QTY_ORDERED,qty_ordered);
        contentValues.put(Commons.SALES_TRANS_LINE_QTY_DELIVERED,qty_delivered);
        contentValues.put(Commons.SALES_TRANS_LINE_UNIT_PRICE,unit_price);
        contentValues.put(Commons.SALES_TRANS_LINE_TOTAL_PRICE,total_amount);
        contentValues.put(Commons.SALES_TRANS_LINE_DATE_AND_TIME,date);
        contentValues.put(Commons.SALES_TRANS_LINE_STATUS,status);
        contentValues.put(Commons.SALES_TRANS_LINE_COMMENTS,comments);

        return db.insert(Commons.SALES_TRANSACTION_LINES, null,contentValues) ;
    }

    public  long GetProducts(String product_code,String product_name,String category,String barcode, String packaging,float cost,float price,String sales_person_id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Commons.PRODUCT_CODE,product_code);
        contentValues.put(Commons.PRODUCT_NAME,product_name);
        contentValues.put(Commons.PRODUCT_CATEGORY,category);
        contentValues.put(Commons.PRODUCT_BARCODE,barcode);
        contentValues.put(Commons.PRODUCT_PACKAGING,packaging);
        contentValues.put(Commons.PRODUCT_COST,cost);
        contentValues.put(Commons.PRODUCT_PRICE,price);
        contentValues.put(Commons.PRODUCT_SALESPERSON_ID,sales_person_id);

        return  db.insert(Commons.PRODUCT_MASTER,null,contentValues);
    }

    public long createOrderLines(int id,String order_header,String orderLine_no,String product_code, String product_name,String product_description, Float unit_price, Integer qty, Float total_amount,String date)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Commons.SALES_ORDER_LINE_ID,id);
        contentValues.put(Commons.SALES_ORDER_HEADER,order_header);
        contentValues.put(Commons.SALES_ORDER_LINE_NO, orderLine_no);
        contentValues.put(Commons.SALES_ORDER_LINE_ITEM_CODE,product_code);
        contentValues.put(Commons.SALES_ORDER_LINE_ITEM_NAME,product_name);
        contentValues.put(Commons.SALES_ORDER_LINE_ITEM_DESCRIPTION,product_description);
        contentValues.put(Commons.SALES_ORDER_LINE_ITEM_QTY,qty);
        contentValues.put(Commons.SALES_ORDER_LINE_UNIT_PRICE,unit_price);
        contentValues.put(Commons.SALES_ORDER_LINE_TOTAL_AMOUNT,total_amount);
        contentValues.put(Commons.SALES_ORDER_LINE_DATE_AND_TIME,date);

     return  db.insert(Commons.SALES_ORDER_LINES,null,contentValues);
    }

    public long createRoutes(int route_id, String route_code, String route_name, String route_description, String territory, String region, int is_active)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues =   new ContentValues();
        contentValues.put(Commons.ROUTE_ID, route_id);
        contentValues.put(Commons.ROUTE_CODE, route_code);
        contentValues.put(Commons.ROUTE_NAME, route_name);
        contentValues.put(Commons.ROUTE_DESCRIPTION, route_description);
        contentValues.put(Commons.ROUTE_TERRITORY, territory);
        contentValues.put(Commons.ROUTE_REGION, region);
        contentValues.put(Commons.ROUTE_IS_ACTIVE, is_active);

        return db.insert(Commons.ROUTES, null, contentValues);
    }

    public boolean createDailyRouteCheck(String mode, String regnumber, float odometerstart, float odometerend, String comment, String dailydate, String starttime, String endtime, int isclosed, String end_comment)
    {

        SQLiteDatabase db   =   this.getWritableDatabase();
        ContentValues contentValues =   new ContentValues();
        contentValues.put(Commons.DAILY_MODE_OF_TRANSPORT, mode);
        contentValues.put(Commons.DAILY_REGISTRATION_NUMBER, regnumber);
        contentValues.put(Commons.DAILY_ODOMETER_READING_START, odometerstart);
        contentValues.put(Commons.DAILY_ODOMETER_READING_END, odometerend);
        contentValues.put(Commons.DAILY_COMMENT, comment);
        contentValues.put(Commons.DAILY_DATE, dailydate);
        contentValues.put(Commons.DAILY_TIME_START, starttime);
        contentValues.put(Commons.DAILY_TIME_END, endtime);
        contentValues.put(Commons.DAILY_IS_TRIP_CLOSED, isclosed);
        contentValues.put(Commons.DAILY_COMENT_END, end_comment);

        db.insert(Commons.DAILY_CHECK, null, contentValues);

        return true;
    }

    public long createBadStock(String productcode, String expired, String damaged, String total, String comments)
{
    SQLiteDatabase db= this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put(Commons.BAD_STOCK_ITEM_CODE,productcode);
    contentValues.put(Commons.BAD_STOCK_EXPIRED,expired);
    contentValues.put(Commons.BAD_STOCK_DAMAGED,damaged);
    contentValues.put(Commons.BAD_STOCK_QTY,total);
    contentValues.put(Commons.BAD_STOCK_REASON,comments);

    return db.insert(Commons.BAD_STOCK,null,contentValues);
}

    public long createNotifications(String notification_id, String subject, String content, String status)
    {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.NOTIFICATIONS_ID,notification_id);
        contentValues.put(Commons.NOTIFICATIONS_SUBJECT,subject);
        contentValues.put(Commons.NOTIFICATIONS_CONTENT,content);
        contentValues.put(Commons.NOTIFICATIONS_STATUS,status);

        return db.insert(Commons.NOTIFICATIONS,null,contentValues);
    }

    public long createAssets(String asset_no, String asset_name, String onSite, String condition, String reason, String lastServiceDate, String date, String nextServiceDate, String comments)
    {
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.ASSETS_NO,asset_no);
        contentValues.put(Commons.ASSETS_NAME,asset_name);
        contentValues.put(Commons.ASSETS_ON_SITE,onSite);
        contentValues.put(Commons.ASSETS_CONDITION,condition);
        contentValues.put(Commons.ASSETS_REASON,reason);
        contentValues.put(Commons.ASSETS_LAST_SERVICE_DATE,lastServiceDate);
        contentValues.put(Commons.ASSETS_DATE,date);
        contentValues.put(Commons.ASSETS_NEXT_SERVICE_DATE,nextServiceDate);
        contentValues.put(Commons.ASSETS_COMMENTS,comments);

        return db.insert(Commons.ASSETS,null,contentValues);
    }


    //Fetch statements
    public Cursor getCustomers()
    {
        SQLiteDatabase db   =   this.getReadableDatabase();
        return db.rawQuery("select * from "+Commons.CUSTOMER_MASTER, null);
    }

    public Cursor getRoutes()
    {
        SQLiteDatabase db   =   this.getReadableDatabase();
        return db.rawQuery("select * from "+Commons.ROUTES+" order by "+Commons.ROUTE_CODE+" asc", null);
    }

    public Cursor getDailyChecks()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from "+Commons.DAILY_CHECK +" where "+Commons.DAILY_IS_TRIP_CLOSED+"=0", null);
    }

    public  Cursor getOrdersHeaders()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return  db.rawQuery("select * from " + Commons.SALES_ORDER_HEADER ,null);
    }

    public  Cursor getOrderLines()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return  db.rawQuery("select * from " + Commons.SALES_ORDER_LINES ,null);
    }

    public Cursor getSalesTrans()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return  db.rawQuery("select * from " + Commons.SALES_TRANSACTION_HEADER ,null);
    }

    public Cursor getSalesLinesTrans()
    {
        SQLiteDatabase db = this.getReadableDatabase();
       return      db.rawQuery("select * from " + Commons.SALES_TRANSACTION_LINES,null) ;
    }

    public double getTotalOfAmount()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(Commons.SALES_TRANS_LINE_TOTAL_PRICE) FROM " + Commons.SALES_TRANSACTION_LINES + ";", null);
        c.moveToFirst();
        double amount = c.getDouble(0);
        c.close();
        return amount;
    }

    public  Cursor getBadStock()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + Commons.BAD_STOCK ,null);
    }

    public  Cursor getNotifications()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + Commons.NOTIFICATIONS +" where "+Commons.NOTIFICATIONS_STATUS+" = ? order by " +Commons.NOTIFICATIONS_DATE+" desc", new String[]{"0"});
    }

    public  Cursor getAssets()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return  db.rawQuery("select * from " + Commons.ASSETS , null);
    }


    //Delete statements
    public boolean  deleteCustomer(String customer_code)
    {
        SQLiteDatabase db =  this.getWritableDatabase();

        db.delete(Commons.CUSTOMER_MASTER, Commons.CUSTOMER_CODE+" = ?", new String[]{customer_code});

        return true;
    }


    public boolean deleteRoutes(String route_id)
    {
        SQLiteDatabase db   =   this.getWritableDatabase();
        db.delete(Commons.ROUTES, Commons.ROUTE_ID+" = ?", new String[]{route_id});

        return true;
    }

    public boolean deleteOrderLines(String sales_orderline_no)
    {
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete(Commons.SALES_ORDER_LINES, Commons.SALES_ORDER_LINE_NO+" = ?", new String[]{sales_orderline_no});
        return  true;
    }

    public boolean deleteOrders(String order_no)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(Commons.SALES_ORDER_HEADER,Commons.SALES_ORDER_HEADER_NO + " = ?", new String []{order_no});

        return true;
    }

    public  boolean deleteSalesOrders(String trans_header_id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(Commons.SALES_TRANSACTION_HEADER,Commons.SALES_TRANS_HEADER_NO + " = ?", new String []{trans_header_id});

        return true;
    }

    public  boolean deleteSalesOrderLines(String order_trans_line)
    {
        SQLiteDatabase db=this.getWritableDatabase();
       db.delete(Commons.SALES_TRANSACTION_LINES, Commons.SALES_TRANS_LINE_NO + "= ?" , new String[]{order_trans_line});

        return true;
    }

    public  boolean deleteProducts(String product_code,String salesperson_id )
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(Commons.PRODUCT_MASTER, Commons.PRODUCT_CODE + "= ? AND " + Commons.PRODUCT_SALESPERSON_ID + "=?"  , new String[]{product_code,salesperson_id});
      return true;
    }

    public  boolean deleteBadStock(String product_code,String salesperson_id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(Commons.BAD_STOCK, Commons.BAD_STOCK_ITEM_CODE + "= ? AND " + Commons.BAD_STOCK_ITEM_CODE + "=?", new String[]{product_code,salesperson_id});
        return true;
    }


    public boolean deleteNotifications(String notification_id)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(Commons.NOTIFICATIONS,Commons.NOTIFICATIONS_ID +"=?" ,new String[]{notification_id});
        return  true;
    }

    public  boolean deleteAssets(String asset_no)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Commons.ASSETS,Commons.ASSETS_NO + "=?",new String[]{asset_no});
        return  true;
    }

    //Update statements
    public boolean closeDailyCheck(String endtime, float odo_end, String end_comment)
    {
        SQLiteDatabase db   =   this.getWritableDatabase();
        ContentValues contentValues =   new ContentValues();
        contentValues.put(Commons.DAILY_TIME_END, endtime);
        contentValues.put(Commons.DAILY_ODOMETER_READING_END, odo_end);
        contentValues.put(Commons.DAILY_COMENT_END, end_comment);
        contentValues.put(Commons.DAILY_IS_TRIP_CLOSED, 1);
        db.update(Commons.DAILY_CHECK, contentValues, Commons.DAILY_IS_TRIP_CLOSED+" = ? ", new String[]{"0"});

        return true;
    }

    public long ReadNotifications (String id, String subject, String content)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put(Commons.NOTIFICATIONS_SUBJECT,subject);
        contentValues.put(Commons.NOTIFICATIONS_CONTENT,content);
        contentValues.put(Commons.NOTIFICATIONS_STATUS,1);

        return db.update(Commons.NOTIFICATIONS,contentValues,Commons.NOTIFICATIONS_ID +" = ? ",new String[]{String.valueOf(id)});

    }


    public  long StockEdit(String productCode,String name,String expired, String damaged, String totals,String comments)
    {
       SQLiteDatabase db = this.getWritableDatabase();
       ContentValues contentValues = new ContentValues();
       contentValues.put(Commons.BAD_STOCK_EXPIRED,expired);
       contentValues.put(Commons.BAD_STOCK_DAMAGED,damaged);
       contentValues.put(Commons.BAD_STOCK_QTY,totals);
       contentValues.put(Commons.BAD_STOCK_REASON,comments);

       return db.update(Commons.BAD_STOCK,contentValues,Commons.BAD_STOCK_ITEM_CODE + "=? AND " + Commons.BAD_STOCK_ITEM_NAME + "=?", new String[]{productCode,name});
    }

    public  long trackAsset(String asset_no, String asset_name, String onSite, String condition, String reason, String lastServiceDate, String date, String nextServiceDate, String comments)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.ASSETS_ON_SITE,onSite);
        contentValues.put(Commons.ASSETS_CONDITION,condition);
        contentValues.put(Commons.ASSETS_REASON,reason);
        contentValues.put(Commons.ASSETS_LAST_SERVICE_DATE,lastServiceDate);
        contentValues.put(Commons.ASSETS_DATE,date);
        contentValues.put(Commons.ASSETS_NEXT_SERVICE_DATE,nextServiceDate);
        contentValues.put(Commons.ASSETS_COMMENTS,comments);

        return  db.update(Commons.ASSETS,contentValues,Commons.ASSETS_NO + "= ? AND " + Commons.ASSETS_NAME + "=?", new String[]{asset_no,asset_name});
    }
    public boolean deliverSalesOrders(String trans_header_id)
    {
        SQLiteDatabase db   =   this.getWritableDatabase();
        ContentValues contentValues =   new ContentValues();
        contentValues.put(Commons.SALES_TRANS_HEADER_STATUS,1);
        db.update(Commons.SALES_TRANSACTION_HEADER, contentValues, Commons.SALES_TRANS_LINE_STATUS+" = ? ", new String[]{trans_header_id});

        return true;
    }

    public int deliverSalesOrderLines(String order_trans_line)
    {
        SQLiteDatabase db   =   this.getWritableDatabase();
        ContentValues contentValues =   new ContentValues();
        contentValues.put(Commons.SALES_TRANS_LINE_STATUS,1);

      return   db.update(Commons.SALES_TRANSACTION_LINES, contentValues, Commons.SALES_TRANS_LINE_STATUS+" = ? ", new String[]{order_trans_line});


    }
}
