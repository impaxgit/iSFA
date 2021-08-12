package com.example.pauldavies.isfa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


public class DB extends SQLiteOpenHelper {
    Context context;
    CommonClass commonClass;

    public DB(Context context) {
        super(context, Commons.DB_NAME, null, Commons.DB_VERSION);
        this.context = context;
        commonClass = new CommonClass();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Commons.product_master);
        db.execSQL(Commons.product_staging);
        db.execSQL(Commons.customer_master);
        db.execSQL(Commons.journey_plan);
        db.execSQL(Commons.tender_types);
        db.execSQL(Commons.daily_check);
        db.execSQL(Commons.sales_order_header);
        db.execSQL(Commons.sales_trans_lines);
        db.execSQL(Commons.sales_trans_header);
        db.execSQL(Commons.transaction_tender_types);
        db.execSQL(Commons.bad_stock);
        db.execSQL(Commons.staging_bad_stock);
        db.execSQL(Commons.stock_request);
        db.execSQL(Commons.feedback);
        db.execSQL(Commons.customer_check_in_out);
        db.execSQL(Commons.sales_order_lines);
        db.execSQL(Commons.routes);
        db.execSQL(Commons.customer_activities);
        db.execSQL(Commons.permissions);
        db.execSQL(Commons.uom);
        db.execSQL(Commons.so_staging);
        db.execSQL(Commons.outlet_activities);
        db.execSQL(Commons.customer_remiders);
        db.execSQL(Commons.outlet_activities_cust);
        db.execSQL(Commons.notificatiions);
        db.execSQL(Commons.feedback_type);
        db.execSQL(Commons.feedback_market);
        db.execSQL(Commons.assets);
        db.execSQL(Commons.bins);
        db.execSQL(Commons.expense_type);
        db.execSQL(Commons.expenses);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /*Sync data before dropping, to preserve the data.*/
        db.execSQL(Commons.feedback_market_drop);
        db.execSQL(Commons.feedback_type_drop);
        db.execSQL(Commons.product_master_drop);
        db.execSQL(Commons.product_staging_drop);
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
        db.execSQL(Commons.staging_bad_stock_drop);
        db.execSQL(Commons.stock_request_drop);
        db.execSQL(Commons.feedback_drop);
        db.execSQL(Commons.customer_check_in_out_drop);
        db.execSQL(Commons.routes_drop);
        db.execSQL(Commons.customer_activities_drop);
        db.execSQL(Commons.permisions_drop);
        db.execSQL(Commons.uom_drop);
        db.execSQL(Commons.so_staging_drop);
        db.execSQL(Commons.outlet_activities_drop);
        db.execSQL(Commons.customer_remiders_drop);
        db.execSQL(Commons.outlet_activities_cust_drop);
        db.execSQL(Commons.notification_drop);
        db.execSQL(Commons.assets_drop);
        db.execSQL(Commons.bins_drop);
        db.execSQL(Commons.expense_type_drop);
        db.execSQL(Commons.expenses_drop);


        this.onCreate(db);

        /*Prompt user to re-import data from external*/
    }

    public long deleteFeedbackTypes() {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(Commons.FEEDBACK_TYPE, null, null);
    }

    public long deleteExpense_Types() {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(Commons.EXPENSES_TYPE, null, null);
    }

    public long saveFeedbacktypes(int id, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.FEEDBACK_ID, id);
        contentValues.put(Commons.FEEDBACK_MESSAGE, type);

        long flag = db.insert(Commons.FEEDBACK_TYPE, null, contentValues);

        db.close();

        return flag;
    }

    public long saveExpenseTypes(String code , String type)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.EXPENSE_CODE, code);
        contentValues.put(Commons.EXPENSE_NAME, type);

        long flag = db.insert(Commons.EXPENSES_TYPE, null, contentValues);

        db.close();

        return flag;
    }

    public long saveExpense(String expensetype,String date,float amount,String notes,float totals,String salesperson,String status)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.EXPENSE_TYPE, expensetype);
        contentValues.put(Commons.EXPENSE_DATE, date);
        contentValues.put(Commons.EXPENSE_value, amount);
        contentValues.put(Commons.EXPENSE_NOTES,notes);
        contentValues.put(Commons.EXPENSE_TOTALS,totals);
        contentValues.put(Commons.EXPENSE_SALESPERSON,salesperson);
        contentValues.put(Commons.EXPENSE_STATUS,status);


        long flag = db.insert(Commons.EXPENSES, null, contentValues);

        db.close();

        return flag;
    }

    public Cursor getExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + Commons.EXPENSES +" order by " + Commons.EXPENSE_DATE + " desc", null);

        return cursor;
    }

    public Cursor getFeedbackTypes() {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("select * from " + Commons.FEEDBACK_TYPE, null);
    }

    public Cursor getExpenseTypes()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + Commons.EXPENSES_TYPE, null);
    }

    public Cursor getMarketFeedback() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + Commons.FEEDBACK_MARKET, null);

        return cursor;
    }

    public String getFeedbackType(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + Commons.FEEDBACK_MESSAGE + " from " + Commons.FEEDBACK_TYPE + " where " + Commons.FEEDBACK_ID + " = ? ", new String[]{String.valueOf(id)});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            return cursor.getString(cursor.getColumnIndex(Commons.FEEDBACK_MESSAGE));
        } else {
            return "";
        }
    }

    public long saveMarketFeedback(String type, String feedback, String customer) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.FEEDBACK_MARKET_TYPE, type);
        contentValues.put(Commons.FEEDBACK_MARKET_NOTE, feedback);
        contentValues.put(Commons.FEEDBACK_MARKET_CUSTOMER_CODE, customer);

        long flag = db.insert(Commons.FEEDBACK_MARKET, null, contentValues);

        db.close();

        return flag;
    }

    public boolean checkNotifications() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select count(*) as total from " + Commons.NOTIFICATIONS + " where " + Commons.NOTIFICATIONS_STATUS + " = ? ", new String[]{"0"});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            if (cursor.getInt(cursor.getColumnIndex("total")) > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public long createNotifications(String notification_id, String subject, String content, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.NOTIFICATIONS_ID, notification_id);
        contentValues.put(Commons.NOTIFICATIONS_SUBJECT, subject);
        contentValues.put(Commons.NOTIFICATIONS_CONTENT, content);
        contentValues.put(Commons.NOTIFICATIONS_STATUS, status);

        return db.insert(Commons.NOTIFICATIONS, null, contentValues);
    }

    public boolean deleteNotifications(String notification_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Commons.NOTIFICATIONS, Commons.NOTIFICATIONS_ID + "=?", new String[]{notification_id});
        return true;
    }

    public long ReadNotifications(String id, String subject, String content) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.NOTIFICATIONS_SUBJECT, subject);
        contentValues.put(Commons.NOTIFICATIONS_CONTENT, content);
        contentValues.put(Commons.NOTIFICATIONS_STATUS, 1);

        return db.update(Commons.NOTIFICATIONS, contentValues, Commons.NOTIFICATIONS_ID + " = ? ", new String[]{String.valueOf(id)});

    }

    public Cursor getNotifications() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + Commons.NOTIFICATIONS + " where " + Commons.NOTIFICATIONS_STATUS + " = ? order by " + Commons.NOTIFICATIONS_DATE + " desc", new String[]{"0"});
    }

    public long switchOffReminder(String customer, int code, boolean isChecked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long flag;

        if (isChecked) {
            contentValues.put(Commons.CUSTOMER_REMIDER_FLAG, 0);
        } else {
            contentValues.put(Commons.CUSTOMER_REMIDER_FLAG, 1);
        }

        flag = db.update(Commons.CUSTOMER_REMINDERS, contentValues, Commons.CUSTOMER_REMINDER_CUSTOMER + " = ? and " + Commons.CUSTOMER_REMIDER_ID + " = ? ", new String[]{customer, String.valueOf(code)});


        db.close();

        return flag;
    }

    public boolean isNewActivity(int code, String customer) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select count(*) as total from " + Commons.OUTLET_ACTIVITIES_CUSTOMER + " where " + Commons.OUTLET_ACTIVITY_CUSTOMER + " = ? and " + Commons.OUTLET_ACTIVITY_ID_CUSTOMER + " = ? ", new String[]{customer, String.valueOf(code)});

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            if (cursor.getInt(cursor.getColumnIndex("total")) > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public long saveCustomerActivityDone(String customer, int activity_id, String activity_name, boolean isChecked) {
        SQLiteDatabase db = this.getWritableDatabase();
        long flag;

        /*First determine if it is an insert for the day or just an update*/
        if (this.isNewActivity(activity_id, customer)) {
            //The activity exists, update.
            ContentValues contentValues = new ContentValues();
            if (isChecked) {
                contentValues.put(Commons.OUTLET_ACTIVITY_IS_ROUTE_CUSTOMER, 1);
            } else {
                contentValues.put(Commons.OUTLET_ACTIVITY_IS_ROUTE_CUSTOMER, 0);
            }

            flag = db.update(Commons.OUTLET_ACTIVITIES_CUSTOMER, contentValues, Commons.OUTLET_ACTIVITY_ID_CUSTOMER + " = ? and " + Commons.OUTLET_ACTIVITY_CUSTOMER + " = ? ", new String[]{String.valueOf(activity_id), customer});
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(Commons.OUTLET_ACTIVITY_ID_CUSTOMER, activity_id);
            contentValues.put(Commons.OUTLET_ACTIVITY_NAME_CUSTOMER, activity_name);
            contentValues.put(Commons.OUTLET_ACTIVITY_DATE, commonClass.getCurrentDate()); //This represents the date when this activity was done
            contentValues.put(Commons.OUTLET_ACTIVITY_CUSTOMER, customer); //The customer to which this activity was done.

            if (isChecked) {
                contentValues.put(Commons.OUTLET_ACTIVITY_IS_ROUTE_CUSTOMER, 1);
            } else {
                contentValues.put(Commons.OUTLET_ACTIVITY_IS_ROUTE_CUSTOMER, 0);
            }

            flag = db.insert(Commons.OUTLET_ACTIVITIES_CUSTOMER, null, contentValues);
        }

        db.close();

        return flag;
    }

    public void deleteReminders() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Commons.CUSTOMER_REMINDERS, null, null);
    }

    public long saveCustomerReminders(String customer, String note) {
        long flag;

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.CUSTOMER_REMINDER_CUSTOMER, customer);
        contentValues.put(Commons.CUSTOMER_REMINDER_DATE, commonClass.getCurrentDate());
        contentValues.put(Commons.CUSTOMER_REMINDER_NOTE, note);
        contentValues.put(Commons.CUSTOMER_REMIDER_FLAG, 0);

        flag = db.insert(Commons.CUSTOMER_REMINDERS, null, contentValues);

        db.close();

        return flag;
    }

    public Cursor getCustomerReminders(String customer) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("select * from " + Commons.CUSTOMER_REMINDERS + " where " + Commons.CUSTOMER_REMIDER_FLAG + " = ? and " + Commons.CUSTOMER_REMINDER_CUSTOMER + " = ? ", new String[]{"0", customer});
    }

    public void createOutletActivities(int id, String name, String startdate, String enddate, String createdby, int isregion, int isterritory, int route, int isoutlet) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.OUTLET_ACTIVITY_ID, id);
        contentValues.put(Commons.OUTLET_ACTIVITY_NAME, name);
        contentValues.put(Commons.OUTLET_ACTIVITY_START_DATE, startdate);
        contentValues.put(Commons.OUTLET_ACTIVITY_END_DATE, enddate);
        contentValues.put(Commons.OUTLET_ACTIVITY_CREATED_BY, createdby);
        contentValues.put(Commons.OUTLET_ACTIVITY_IS_REGION, isregion);
        contentValues.put(Commons.OUTLET_ACTIVITY_IS_TERRITORY, isterritory);
        contentValues.put(Commons.OUTLET_ACTIVITY_IS_ROUTE, route);
        contentValues.put(Commons.OUTLET_ACTIVITY_IS_OUTLET, isoutlet);

        db.insert(Commons.OUTLET_ACTIVITIES, null, contentValues);

        db.close();
    }

    public long deleteOutletActivities() {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(Commons.OUTLET_ACTIVITIES, null, null);
    }

    public Cursor getOutletActivities() {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("select * from " + Commons.OUTLET_ACTIVITIES + " where " + Commons.OUTLET_ACTIVITY_IS_OUTLET + " = ? or " + Commons.OUTLET_ACTIVITY_IS_ROUTE + " = ? or " + Commons.OUTLET_ACTIVITY_IS_TERRITORY + " = ? or " + Commons.OUTLET_ACTIVITY_IS_REGION + " = ?", new String[]{"1", "1", "1", "1"});
    }

    public Cursor getOutletActivities(String customer) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("select * from " + Commons.OUTLET_ACTIVITIES_CUSTOMER + " where " + Commons.OUTLET_ACTIVITY_IS_ROUTE_CUSTOMER + " = ? and " + Commons.OUTLET_ACTIVITY_CUSTOMER + " = ? ", new String[]{"1", customer});
    }

    public Cursor searchCustomer(String criterion) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + Commons.CUSTOMER_MASTER + " where " + Commons.CUSTOMER_CODE + " like ? or " + Commons.CUSTOMER_OUTLET_TYPE + " like ? or " + Commons.CUSTOMER_NAME + " like ? or " + Commons.CUSTOMER_LOCATION + " like ? ", new String[]{"%" + criterion + "%", "%" + criterion + "%", "%" + criterion + "%", "%" + criterion + "%"});

        return cursor;
    }

    public Cursor searchCustomer() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + Commons.CUSTOMER_MASTER + " where " + Commons.CUSTOMER_STATUS + " = ? ", new String[]{"0"});

        return cursor;
    }

    //Insert statements
    public long createSalesOrder_Temp(String customer_code, String sku_code, String sku_name, float unit_price, float line_amount, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.SO_CUSTOMER_CODE, customer_code);
        contentValues.put(Commons.SO_SKU_CODE, sku_code);
        contentValues.put(Commons.SO_SKU_NAME, sku_name);
        contentValues.put(Commons.SO_UNIT_PRICE, unit_price);
        contentValues.put(Commons.SO_LINE_AMOUNT, line_amount);
        contentValues.put(Commons.SO_DATE, date);
        contentValues.put(Commons.SO_QTY, 0);

        return db.insert(Commons.STAGING_SALES_ORDER_GEN, null, contentValues);

    }

    public long createBadStock_Temp(String customercode,String item_code,String productname, int expired, int damaged, int totals)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.STAGING_BAD_STOCK_CUSTOMER_CODE,customercode);
        contentValues.put(Commons.STAGING_BAD_STOCK_ITEM_CODE,item_code);
        contentValues.put(Commons.STAGING_BAD_STOCK_ITEM_NAME, productname);
        contentValues.put(Commons.STAGING_BAD_STOCK_EXPIRED, expired);
        contentValues.put(Commons.STAGING_BAD_STOCK_DAMAGED, damaged);
        contentValues.put(Commons.STAGING_BAD_STOCK_QTY, totals);

        return db.insert(Commons.STAGING_BAD_STOCK, null, contentValues);
    }

    public long createProducts_Temp(String product_code, String product_name, String product_category, String product_barcode, String product_search_name, String product_packaging, float product_cost, float product_price, int sync, String location,int damaged,int expired,int totals,String customer_code) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.STAGING_PRODUCT_CODE, product_code);
        contentValues.put(Commons.STAGING_PRODUCT_NAME, product_name);
        contentValues.put(Commons.STAGING_PRODUCT_CATEGORY, product_category);
        contentValues.put(Commons.STAGING_PRODUCT_BARCODE, product_barcode);
        contentValues.put(Commons.STAGING_PRODUCT_SEARCH_NAME, product_search_name);
        contentValues.put(Commons.STAGING_PRODUCT_PACKAGING, product_packaging);
        contentValues.put(Commons.STAGING_PRODUCT_COST, product_cost);
        contentValues.put(Commons.STAGING_PRODUCT_PRICE, product_price);
        contentValues.put(Commons.STAGING_PRODUCT_SYNC, sync);
        contentValues.put(Commons.STAGING_PRODUCT_LOCATION, location);
        contentValues.put(Commons.STAGING_PRODUCT_DAMAGED,damaged);
        contentValues.put(Commons.STAGING_PRODUCT_EXPIRED,expired);
        contentValues.put(Commons.STAGING_PRODUCT_TOTALS,totals);
        contentValues.put(Commons.STAGING_CUSTOMER_CODE,customer_code);

        return db.insert(Commons.STAGING_PRODUCTS, null, contentValues);
    }


    public long createUom(String uom_unit, String uom_description) {
        SQLiteDatabase db = this.getWritableDatabase();

        //Delete the unit before inserting it again
        db.delete(Commons.UOM, Commons.UOM_UNIT + " = ?", new String[]{uom_unit});
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.UOM_UNIT, uom_unit);
        contentValues.put(Commons.UOM_DESCRIPTION, uom_description);

        return db.insert(Commons.UOM, null, contentValues);
    }

    public boolean createCustomer(String customer_code, String customer_name, String customer_description, String customer_outlet_type, String customer_location, String customer_geo_coordinates, String customer_date_created, String customer_type, int customer_is_taxable, String customer_email, String customer_phone, String customer_contact_person, String customer_contact_email, String customer_contact_phone, float customer_credit_limit, float customer_credit_balance, int customer_status, int sync, String owner_name, String owner_number, String owner_email, String owner_address) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
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
        contentValues.put(Commons.CUSTOMER_OWNER_NAME, owner_name);
        contentValues.put(Commons.CUSTOMER_OWNER_EMAIL, owner_email);
        contentValues.put(Commons.CUSTOMER_OWNER_ADDRESS, owner_address);
        contentValues.put(Commons.CUSTOMER_OWNER_MOBILE, owner_number);

        db.insert(Commons.CUSTOMER_MASTER, null, contentValues);

        return true;
    }

    public boolean createRoutes(int route_id, String route_code, String route_name, String route_description, String territory, String region, int is_active) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.ROUTE_ID, route_id);
        contentValues.put(Commons.ROUTE_CODE, route_code);
        contentValues.put(Commons.ROUTE_NAME, route_name);
        contentValues.put(Commons.ROUTE_DESCRIPTION, route_description);
        contentValues.put(Commons.ROUTE_TERRITORY, territory);
        contentValues.put(Commons.ROUTE_REGION, region);
        contentValues.put(Commons.ROUTE_IS_ACTIVE, is_active);

        db.insert(Commons.ROUTES, null, contentValues);

        return true;
    }

    public boolean createLocation(int bin_id, String bin_code, String bin_name, String bin_description, String location, int is_active) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.BIN_ID, bin_id);
        contentValues.put(Commons.BIN_CODE, bin_code);
        contentValues.put(Commons.BIN_NAME, bin_name);
        contentValues.put(Commons.BIN_DESCRIPTION, bin_description);
        contentValues.put(Commons.BIN_LOCATION, location);
        contentValues.put(Commons.BIN_IS_ACTIVE, is_active);

        db.insert(Commons.BINS, null, contentValues);

        return true;
    }

    public boolean createDailyRouteCheck(String mode, String regnumber, float odometerstart, float odometerend, String comment, String dailydate, String starttime, String endtime, int isclosed, String end_comment, int brakes, int tyres, int spare_tyre, int water, int dl, int insurance, int lights, int oil, int tyre_pressure) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
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
        contentValues.put(Commons.DAILY_VEHICLE_BRAKES, brakes);
        contentValues.put(Commons.DAILY_VEHICLE_TYRES, tyres);
        contentValues.put(Commons.DAILY_VEHICLE_SPARE_TYRE, spare_tyre);
        contentValues.put(Commons.DAILY_VEHICLE_WATER, water);
        contentValues.put(Commons.DAILY_VEHICLE_DRIVING_LICENCE, dl);
        contentValues.put(Commons.DAILY_VEHICLE_INSURANCE, insurance);
        contentValues.put(Commons.DAILY_VEHICLE_LIGHTS, lights);
        contentValues.put(Commons.DAILY_VEHICLE_OIL, oil);
        contentValues.put(Commons.DAILY_VEHICLE_TYRE_PRESSURE, tyre_pressure);

        db.insert(Commons.DAILY_CHECK, null, contentValues);

        return true;
    }

    public boolean createJourneyPlans(int planid, String customer_code, String plan_date, int plan_sequence, int synced) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.PLAN_ID, planid);
        contentValues.put(Commons.PLAN_CUSTOMER_CODE, customer_code);
        contentValues.put(Commons.PLAN_DATE, plan_date);
        contentValues.put(Commons.PLAN_SEQUENCE, plan_sequence);
        contentValues.put(Commons.PLAN_SYNC, synced);

        db.insert(Commons.JOURNEY_PLAN, null, contentValues);

        return true;
    }

    public long createCustomerCheckIn(String customer_code, String check_date_and_time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.CUSTOMER_CHECK_IN_CUSTOMER_ID, customer_code);
        contentValues.put(Commons.CUSTOMER_CHECK_IN_DATE_AND_TIME, check_date_and_time);
        contentValues.put(Commons.CUSTOMER_CHECK_OUT_FLAG, 0);
        contentValues.put(Commons.CUSTOMER_CHECK_IN_FLAG, 1);
        contentValues.put(Commons.CUSTOMER_CHECK_ACTIVITIES, 0);
        contentValues.put(Commons.CUSTOMER_CHECK_OUT_DATE_AND_TIME, "");

        return db.insert(Commons.CUSTOMER_CHECK_IN_OUT, null, contentValues);
    }

    public long createCustomerActivities(String customer_code, int call_objective, int stock_take, int gen_order, int inv_print, int pay_collect, int machandise, int deliver_order, int asset_track, int complete_call, String visit_date, int is_closed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Commons.CUSTOMER_ACTIVITY_CUSTOMER_CODE, customer_code);
        contentValues.put(Commons.CUSTOMER_ACTIVITY_CALL_OBJECTIVE, call_objective);
        contentValues.put(Commons.CUSTOMER_ACTIVITY_STOCK_TAKE, stock_take);
        contentValues.put(Commons.CUSTOMER_ACTIVITY_GENERATE_ORDER, gen_order);
        contentValues.put(Commons.CUSTOMER_ACTIVITY_INVOICE_AND_PRINT, inv_print);
        contentValues.put(Commons.CUSTOMER_ACTIVITY_PAYMENT_COLLECTION, pay_collect);
        contentValues.put(Commons.CUSTOMER_ACTIVITY_MACHANDISING, machandise);
        contentValues.put(Commons.CUSTOMER_ACTIVITY_DELIVERY, deliver_order);
        contentValues.put(Commons.CUSTOMER_ACTIVITY_ASSET_TRACKING, asset_track);
        contentValues.put(Commons.CUSTOMER_ACTIVITY_COMPLETE_CALL, complete_call);
        contentValues.put(Commons.CUSTOMER_ACTIVITY_VISIT_DATE, visit_date);
        contentValues.put(Commons.CUSTOMER_ACTIVITY_CHECK_LIST_CLOSED, is_closed);

        return db.insert(Commons.CUSTOMER_ACTIVITIES, null, contentValues);
    }

    public long createPermisions(String username, String user_type, int call_objective, int stock_take, int generate_order, int invoice_and_print, int payment_collection, int merchandising, int delivery, int tracking, int complete_call, int is_active) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.PERMISIONS_USERNAME, username);
        contentValues.put(Commons.PERMISIONS_CALL_OBJECTIVE, call_objective);
        contentValues.put(Commons.PERMISIONS_STOCK_TAKE, stock_take);
        contentValues.put(Commons.PERMISIONS_GENERATE_ORDER, generate_order);
        contentValues.put(Commons.PERMISIONS_INVOICE_AND_PRINT, invoice_and_print);
        contentValues.put(Commons.PERMISIONS_PAYMENT_COLLECTION, payment_collection);
        contentValues.put(Commons.PERMISIONS_MACHANDISING, merchandising);
        contentValues.put(Commons.PERMISIONS_DELIVERY, delivery);
        contentValues.put(Commons.PERMISIONS_ASSET_TRACKING, tracking);
        contentValues.put(Commons.PERMISIONS_COMPLETE_CALL, complete_call);
        contentValues.put(Commons.PERMISION_PERSON_TYPE, user_type);
        contentValues.put(Commons.PERMISION_IS_ACTIVE, is_active);

        return db.insert(Commons.PERMISIONS, null, contentValues);
    }

    public long createProducts(String product_code, String product_name, String product_category, String product_barcode, String product_search_name, String product_packaging, float product_cost, float product_price, int sync, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.PRODUCT_CODE, product_code);
        contentValues.put(Commons.PRODUCT_NAME, product_name);
        contentValues.put(Commons.PRODUCT_CATEGORY, product_category);
        contentValues.put(Commons.PRODUCT_BARCODE, product_barcode);
        contentValues.put(Commons.PRODUCT_SEARCH_NAME, product_search_name);
        contentValues.put(Commons.PRODUCT_PACKAGING, product_packaging);
        contentValues.put(Commons.PRODUCT_COST, product_cost);
        contentValues.put(Commons.PRODUCT_PRICE, product_price);
        contentValues.put(Commons.PRODUCT_SYNC, sync);
        contentValues.put(Commons.PRODUCT_LOCATION, location);

        return db.insert(Commons.PRODUCT_MASTER, null, contentValues);
    }

    public String getProductName(String product_code) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + Commons.PRODUCT_NAME + " from " + Commons.PRODUCT_MASTER + " where " + Commons.PRODUCT_CODE + " = ? ", new String[]{product_code});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(Commons.PRODUCT_NAME));
        } else {
            return "";
        }
    }

    public float getProductUnitPrice(String product_code) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + Commons.PRODUCT_PRICE + " from " + Commons.PRODUCT_MASTER + " where " + Commons.PRODUCT_CODE + " = ?", new String[]{product_code});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getFloat(cursor.getColumnIndex(Commons.PRODUCT_PRICE));
        } else {
            return 0;
        }
    }

    public float getProductUnitCost(String product_code) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + Commons.PRODUCT_PRICE + " from " + Commons.PRODUCT_MASTER + " where " + Commons.PRODUCT_CODE + " = ?", new String[]{product_code});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getFloat(cursor.getColumnIndex(Commons.PRODUCT_PRICE));
        } else {
            return 0;
        }
    }


    public long createSalesOrderHeader(String sales_order_number, String sales_order_customer_code, String sales_order_date, float amount, String order_created_by, String comment, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.SALES_ORDER_HEADER_NO, sales_order_number);
        contentValues.put(Commons.SALES_ORDER_HEADER_CUSTOMER_CODE, sales_order_customer_code);
        contentValues.put(Commons.SALES_ORDER_HEADER_DATE_AND_TIME, sales_order_date);
        contentValues.put(Commons.SALES_ORDER_HEADER_AMOUNT, amount);
        contentValues.put(Commons.SALES_ORDER_HEADER_CREATED_BY, order_created_by);
        contentValues.put(Commons.SALES_ORDER_HEADER_COMMENT, comment);
        contentValues.put(Commons.SALES_ORDER_HEADER_STATUS, status);

        return db.insert(Commons.SALES_ORDER_HEADER, null, contentValues);
    }


    public long createSalesOrderLines(String line_number, String header_number, String line_code, String line_name, String description, int qty, float unit_price, float amount, float cost, String date_created) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.SALES_ORDER_LINE_NO, line_number);
        contentValues.put(Commons.SALES_ORDER_LINE_HEADER, header_number);
        contentValues.put(Commons.SALES_ORDER_LINE_ITEM_CODE, line_code);
        contentValues.put(Commons.SALES_ORDER_LINE_ITEM_NAME, line_name);
        contentValues.put(Commons.SALES_ORDER_LINE_ITEM_DESCRIPTION, description);
        contentValues.put(Commons.SALES_ORDER_LINE_ITEM_QTY, qty);
        contentValues.put(Commons.SALES_ORDER_LINE_UNIT_PRICE, unit_price);
        contentValues.put(Commons.SALES_ORDER_LINE_TOTAL_AMOUNT, amount);
        contentValues.put(Commons.SALES_ORDER_LINE_ITEM_COST, cost);
        contentValues.put(Commons.SALES_ORDER_LINE_DATE_AND_TIME, date_created);

        return db.insert(Commons.SALES_ORDER_LINES, null, contentValues);
    }

    //Fetch statements

    public static String salesTax(String customer_code) {
        return "0";
    }

    public String salesSubTotal(String customer_code) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor invoice_cursor = db.rawQuery("select sum(" + Commons.SO_LINE_AMOUNT + ") as total from " + Commons.STAGING_SALES_ORDER_GEN + " where " + Commons.SO_CUSTOMER_CODE + " = ? ", new String[]{customer_code});
        if (invoice_cursor.getCount() > 0) {
            invoice_cursor.moveToFirst();
            return String.valueOf(invoice_cursor.getFloat(invoice_cursor.getColumnIndex("total")));
        } else {
            return "0";
        }
    }


    public long GetSalesOrders(String trans_header_id, String customer_code, String date, Float trans_amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.SALES_TRANS_HEADER_ID, trans_header_id);
        contentValues.put(Commons.SALES_TRANS_HEADER_CUSTOMER_CODE, customer_code);
        contentValues.put(Commons.SALES_TRANS_HEADER_DATE_TIME, date);
        contentValues.put(Commons.SALES_TRANS_HEADER_TOTAL_AMOUNT, trans_amount);


        return db.insert(Commons.SALES_TRANSACTION_HEADER, null, contentValues);
    }

    public boolean deleteSalesOrders() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Commons.SALES_TRANSACTION_HEADER, null, null);

        return true;
    }

    public boolean deleteSalesOrderLines(String header_number) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Commons.SALES_TRANSACTION_LINES, Commons.SALES_TRANS_LINE_HEADER_CODE + " = ? ", new String[]{header_number});

        return true;
    }

    public long GetSalesOrderLines(String header_code, String product_code, String product_description, int qty_ordered, int qty_delivered, float unit_price, float total_amount, String comments) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.SALES_TRANS_LINE_HEADER_CODE, header_code);
        contentValues.put(Commons.SALES_TRANS_ITEM_LINES_CODE, product_code);
        contentValues.put(Commons.SALES_TRANS_LINE_ITEM_DESCRIPTION, product_description);
        contentValues.put(Commons.SALES_TRANS_LINE_QTY_ORDERED, qty_ordered);
        contentValues.put(Commons.SALES_TRANS_LINE_QTY_DELIVERED, qty_delivered);
        contentValues.put(Commons.SALES_TRANS_LINE_UNIT_PRICE, unit_price);
        contentValues.put(Commons.SALES_TRANS_LINE_TOTAL_PRICE, total_amount);
        contentValues.put(Commons.SALES_TRANS_LINE_COMMENTS, comments);

        return db.insert(Commons.SALES_TRANSACTION_LINES, null, contentValues);
    }

    public long createAssets(String asset_no, String asset_name, String onSite, String condition, String reason, String lastServiceDate, String date, String nextServiceDate, String comments) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.ASSETS_NO, asset_no);
        contentValues.put(Commons.ASSETS_NAME, asset_name);
        contentValues.put(Commons.ASSETS_ON_SITE, onSite);
        contentValues.put(Commons.ASSETS_CONDITION, condition);
        contentValues.put(Commons.ASSETS_REASON, reason);
        contentValues.put(Commons.ASSETS_LAST_SERVICE_DATE, lastServiceDate);
        contentValues.put(Commons.ASSETS_DATE, date);
        contentValues.put(Commons.ASSETS_NEXT_SERVICE_DATE, nextServiceDate);
        contentValues.put(Commons.ASSETS_COMMENTS, comments);

        return db.insert(Commons.ASSETS, null, contentValues);
    }

    public static String companyVATNumber() {
        return "VAT No.: " + "3959475948";
    }

    public static String companyEmail() {
        return "Email: " + "info@impaxafrica.com";
    }

    public static String companyContact() {
        return "Phone: " + "094384938387";
    }

    public static String companyPostal() {
        return "P.O Box 24347-87878, Nairobi";
    }

    public static String companyName() {
        return "Impax Business Solutions Ltd";
    }

    public float getSalesInvoiceSum(String header_code) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select sum(" + Commons.SALES_ORDER_LINE_TOTAL_AMOUNT + ") as total from " + Commons.SALES_ORDER_LINES + " where " + Commons.SALES_ORDER_LINE_HEADER + " =? ", new String[]{header_code});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            return cursor.getFloat(cursor.getColumnIndex("total"));
        } else {
            return 0;
        }
    }

    public float getSaesOrderSum(String customer_code)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select sum(" + Commons.SO_LINE_AMOUNT + ") as total from " + Commons.STAGING_SALES_ORDER_GEN + " where " + Commons.SO_CUSTOMER_CODE + " = ?", new String[]{customer_code});
        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            return cursor.getFloat(cursor.getColumnIndex("total"));
        } else
            {
            return 0;
        }
    }

    public int getStockTakeTotal(String item_code)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select sum(" + Commons.STAGING_PRODUCT_TOTALS + ") as totals from " + Commons.STAGING_PRODUCTS + " where " + Commons.STAGING_CUSTOMER_CODE + "=?", new String[]{item_code});

        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex("totals"));
        }
        else
            {
            return 0;
        }
    }

    public int getExpensesAmount(String salesPerson,String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select sum(" + Commons.EXPENSE_value + ") as totals from " + Commons.EXPENSES + " where " + Commons.EXPENSE_SALESPERSON + "=? and " + Commons.EXPENSE_DATE+ " =?", new String[]{salesPerson,date});

        if (cursor.getCount() > 0)
        {
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex("totals"));
        }
        else
        {
            return 0;
        }
    }

    public Cursor getDeliveredSalesOrdersLines(String header_code) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("select * from " + Commons.SALES_TRANSACTION_LINES + " where " + Commons.SALES_TRANS_LINE_HEADER_CODE + " = ? and " + Commons.SALES_TRANS_LINE_QTY_DELIVERED + " > ?", new String[]{header_code, "0"});
    }

    public String getSalesOrderDate(String header_code) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + Commons.SALES_TRANS_HEADER_DATE_TIME + " from " + Commons.SALES_TRANSACTION_HEADER + " where " + Commons.SALES_TRANS_HEADER_ID + " = ?", new String[]{header_code});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex(Commons.SALES_TRANS_HEADER_DATE_TIME));
        } else {
            return "";
        }
    }

    public String getSalesOrderTotal(String header_code) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + Commons.SALES_TRANS_HEADER_TOTAL_PAID + " from " + Commons.SALES_TRANSACTION_HEADER + " where " + Commons.SALES_TRANS_HEADER_ID + " = ? ", new String[]{header_code});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            return String.valueOf(cursor.getFloat(cursor.getColumnIndex(Commons.SALES_TRANS_HEADER_TOTAL_PAID)));
        } else {
            return "0";
        }
    }

    public Cursor getSalesTrans(String customer) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + Commons.SALES_TRANSACTION_HEADER + " where " + Commons.SALES_TRANS_HEADER_CUSTOMER_CODE + " = ? ", new String[]{customer});
    }

    public Cursor getSalesLinesTrans(String header_code) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + Commons.SALES_TRANSACTION_LINES + " where " + Commons.SALES_TRANS_LINE_HEADER_CODE + " = ? ", new String[]{header_code});
    }

    public float getSalesLinesSum(String header_code) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select sum(" + Commons.SALES_TRANS_LINE_TOTAL_PRICE + ") as total from " + Commons.SALES_TRANSACTION_LINES + " where " + Commons.SALES_TRANS_LINE_HEADER_CODE + " = ? and " + Commons.SALES_TRANS_LINE_QTY_DELIVERED + " > ? ", new String[]{header_code, "0"});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            return cursor.getFloat(cursor.getColumnIndex("total"));
        } else {
            return 0;
        }
    }

    public int getSalesLinesTransSum(String header_code) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select count(*) as lines from " + Commons.SALES_TRANSACTION_LINES + " where " + Commons.SALES_TRANS_LINE_HEADER_CODE + " = ? ", new String[]{header_code});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex("lines"));
        } else {
            return 0;
        }
    }

    public String getCustomerCurrentSalesOrderTotal(String customer_code)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor total_cursor = db.rawQuery("select sum(" + Commons.SO_LINE_AMOUNT + ") as total from " + Commons.STAGING_SALES_ORDER_GEN + " where " + Commons.SO_CUSTOMER_CODE + " = ? ", new String[]{customer_code});
        if (total_cursor.getCount() > 0) {
            total_cursor.moveToFirst();
            return String.valueOf(total_cursor.getFloat(total_cursor.getColumnIndex("total")));
        } else {
            return "0";
        }
    }

    public Cursor getProductFromTemp(String customer_code) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + Commons.STAGING_SALES_ORDER_GEN + " where " + Commons.SO_CUSTOMER_CODE + " = ? and " + Commons.SO_QTY + " != 0", new String[]{customer_code});
    }

    public Cursor getSalesOrderForPreview(String customer_code) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + Commons.STAGING_SALES_ORDER_GEN + " where " + Commons.SO_CUSTOMER_CODE + " = ? and " + Commons.SO_QTY + " >0", new String[]{customer_code});
    }

    public Cursor getCustomerActivities(String customer_code) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + Commons.CUSTOMER_ACTIVITIES + " where " + Commons.CUSTOMER_ACTIVITY_CUSTOMER_CODE + " = ? and " + Commons.CUSTOMER_ACTIVITY_CHECK_LIST_CLOSED + " = ?", new String[]{customer_code, "0"});
    }

    public Cursor getUom() {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("select * from " + Commons.UOM + " order by " + Commons.UOM_UNIT + " asc", null);
    }

    public Cursor getProducts() {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("select * from " + Commons.PRODUCT_MASTER + " order by " + Commons.PRODUCT_NAME + " asc", null);
    }

    public Cursor getProductsForSalesOrderTakingIntoTemp(String bin)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        if(bin.trim().length()>0)
        {
            return db.rawQuery("select * from " + Commons.PRODUCT_MASTER+" where "+Commons.PRODUCT_LOCATION+" = ? " + " order by " + Commons.PRODUCT_NAME + " asc", new String[]{bin});
        }
        else
        {
            return db.rawQuery("select * from " + Commons.PRODUCT_MASTER+ " order by " + Commons.PRODUCT_NAME + " asc", null);
        }


    }

    public Cursor getProductsForSalesOrderTakingIntoTemp()
    {
        SQLiteDatabase db = this.getReadableDatabase();


        return db.rawQuery("select * from " + Commons.PRODUCT_MASTER+" order by " + Commons.PRODUCT_NAME + " asc", null);
    }

    public Cursor getStagingProducts(String customer_code)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + Commons.STAGING_PRODUCTS + " where "+Commons.STAGING_PRODUCT_TOTALS + " >0 and  "+Commons.STAGING_CUSTOMER_CODE +" =?" +" order by " + Commons.STAGING_PRODUCT_NAME + " asc", new String[]{customer_code});
    }


    public Cursor getProductsOntoGripList(String customer_code) {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("select * from " + Commons.STAGING_SALES_ORDER_GEN + " where " + Commons.SO_CUSTOMER_CODE + " = ? order by " + Commons.SO_SKU_NAME + " asc", new String[]{customer_code});
    }

    public Cursor getProductsOntoList(String customer_code)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("select * from " + Commons.STAGING_PRODUCTS + " where " + Commons.STAGING_CUSTOMER_CODE + " = ? order by " + Commons.STAGING_PRODUCT_NAME + " asc", new  String[]{customer_code});
    }

    public Cursor getOrderLines(String header_code) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + Commons.SALES_ORDER_LINES + " where " + Commons.SALES_ORDER_LINE_HEADER + " = ? ", new String[]{header_code});
    }

    public Cursor getOrderLines(String header_code, String sku_code) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + Commons.SALES_ORDER_LINES + " where " + Commons.SALES_ORDER_LINE_HEADER + " = ? and " + Commons.SALES_ORDER_LINE_ITEM_CODE + " = ?", new String[]{header_code, sku_code});
    }

    public Cursor getOrdersHeaders(String customer_code, String sales_header_number) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + Commons.SALES_ORDER_HEADER + " where " + Commons.SALES_ORDER_HEADER_CUSTOMER_CODE + " = ? and " + Commons.SALES_ORDER_HEADER_NO + " = ?", new String[]{customer_code, sales_header_number});
    }

    public Cursor getOrdersSalesOrderHeaders(String customer_code) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + Commons.SALES_ORDER_HEADER + " where " + Commons.SALES_ORDER_HEADER_CUSTOMER_CODE + " = ? and " + Commons.SALES_ORDER_HEADER_NO + " = ?", new String[]{customer_code});
    }

    public Cursor getSavedOrdersSalesOrderHeaders(String customer_code) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + Commons.SALES_ORDER_HEADER + " where " + Commons.SALES_ORDER_HEADER_CUSTOMER_CODE + " = ? and " + Commons.SALES_ORDER_HEADER_STATUS + " = ? ", new String[]{customer_code, "0"});
    }

    public Cursor getPermissions() {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.rawQuery("select * from " + Commons.PERMISIONS, null);
    }

    public Cursor getCustomerCheckInOut(String customer_code) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + Commons.CUSTOMER_CHECK_IN_OUT + " where " + Commons.CUSTOMER_CHECK_IN_FLAG + " = ? and " + Commons.CUSTOMER_CHECK_OUT_FLAG + " = ? and " + Commons.CUSTOMER_CHECK_IN_CUSTOMER_ID + " = ? ", new String[]{"1", "0", customer_code});
    }

    public Cursor getAllCustomerCheckInOut(String customer_code) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + Commons.CUSTOMER_CHECK_IN_OUT + " where " + Commons.CUSTOMER_CHECK_IN_CUSTOMER_ID + " = ?", new String[]{customer_code});
    }

    public Cursor getJourneyPlans() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + Commons.JOURNEY_PLAN + " join " + Commons.CUSTOMER_MASTER + " on " + Commons.JOURNEY_PLAN + "." + Commons.PLAN_CUSTOMER_CODE + "=" + Commons.CUSTOMER_MASTER + "." + Commons.CUSTOMER_CODE + " where " + Commons.JOURNEY_PLAN + "." + Commons.PLAN_DATE + " = ?", new String[]{commonClass.getCurrentDate()});
    }

    public Cursor getCustomersRouteBased(String route)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;

        if (route.trim().length() > 0) //Meaning route has been provided; filter
        {
            cursor = db.rawQuery("select * from " + Commons.CUSTOMER_MASTER + " where " + Commons.CUSTOMER_STATUS + " = ? and " + Commons.CUSTOMER_LOCATION + " = ? ", new String[]{"1", route});
        } else {
            cursor = db.rawQuery("select * from " + Commons.CUSTOMER_MASTER + " where " + Commons.CUSTOMER_STATUS + " = ? ", new String[]{"1"});
        }

        return cursor;
    }

    public String getCustomers(String customer_code) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor customer_cursor = db.rawQuery("select * from " + Commons.CUSTOMER_MASTER + " where " + Commons.CUSTOMER_CODE + " = ?", new String[]{customer_code});
        if (customer_cursor.getCount() > 0) {
            customer_cursor.moveToFirst();
            return customer_cursor.getString(customer_cursor.getColumnIndex(Commons.CUSTOMER_NAME));
        } else {
            return "";
        }
    }

    public float getSalesOrderPainAmount(String customer, String header_code) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + Commons.SALES_TRANS_HEADER_TOTAL_PAID + " from " + Commons.SALES_TRANSACTION_HEADER + " where " + Commons.SALES_TRANS_HEADER_CUSTOMER_CODE + " = ? and " + Commons.SALES_TRANS_HEADER_ID + " = ? ", new String[]{customer, header_code});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getFloat(cursor.getColumnIndex(Commons.SALES_TRANS_HEADER_TOTAL_PAID));
        } else {
            return 0;
        }
    }

    public Cursor getRoutes()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + Commons.ROUTES + " order by " + Commons.ROUTE_CODE + " asc", null);
    }

    public Cursor getBins()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + Commons.BINS + " order by " + Commons.BIN_CODE + " asc", null);
    }

    public Cursor getDailyChecks() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + Commons.DAILY_CHECK + " where " + Commons.DAILY_IS_TRIP_CLOSED + "=0", null);
    }

    public float getSalesOrderHeaderAmount(String header_code) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + Commons.SALES_ORDER_HEADER_AMOUNT + " from " + Commons.SALES_ORDER_HEADER + " where " + Commons.SALES_ORDER_HEADER_NO + " = ? ", new String[]{header_code});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getFloat(cursor.getColumnIndex(Commons.SALES_ORDER_HEADER_AMOUNT));
        } else {
            return 0;
        }
    }

    public Cursor checkItemExist(String customer_code, String product_code)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("select " + Commons.SO_SKU_CODE + ", " + Commons.SO_QTY + " from " + Commons.STAGING_SALES_ORDER_GEN + " where " + Commons.SO_SKU_CODE + " = ? and " + Commons.SO_CUSTOMER_CODE + " = ? ", new String[]{product_code, customer_code});
    }


    //Delete statements
    public boolean deleteCustomer(String customer_code) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Commons.CUSTOMER_MASTER, Commons.CUSTOMER_CODE + " = ?", new String[]{customer_code});

        return true;
    }


    public long deleteStagingProduct(String customer_code)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(Commons.STAGING_PRODUCTS, Commons.STAGING_CUSTOMER_CODE + " = ?", new String[]{customer_code});
    }


    public long deleteSalesOrderTemp(String customer_code, String product_code) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Commons.STAGING_SALES_ORDER_GEN, Commons.SO_CUSTOMER_CODE + " = ? and " + Commons.SO_SKU_CODE + " = ? ", new String[]{customer_code, product_code});
    }


    public long deleteSalesOrderTemp(String customer_code) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Commons.STAGING_SALES_ORDER_GEN, Commons.SO_CUSTOMER_CODE + " = ? and " + Commons.SO_DATE + " != ? ", new String[]{customer_code, commonClass.getCurrentDate()});
    }

    public long deleteSalesTempForCustomer(String customer_code) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Commons.STAGING_SALES_ORDER_GEN, Commons.SO_CUSTOMER_CODE + " = ? ", new String[]{customer_code});
    }

    public long deleteSalesOrderTempAllForCustomer(String customer_code) {
        //It is just resetting the quantity and line amount figures in readiness for a new order
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.SO_QTY, 0);
        contentValues.put(Commons.SO_LINE_AMOUNT, 0);

        return db.update(Commons.STAGING_SALES_ORDER_GEN, contentValues, Commons.SO_CUSTOMER_CODE + " = ?", new String[]{customer_code});
    }

    public boolean deletePermissions() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Commons.PERMISIONS, null, null);

        return true;
    }

    public boolean deleteRoutes(String route_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Commons.ROUTES, Commons.ROUTE_ID + " = ?", new String[]{route_id});

        return true;
    }

    public boolean deleteLocation(String bin_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Commons.BINS, Commons.BIN_ID + " = ?", new String[]{bin_id});

        return true;
    }

    public void deleteSalesOrder(String header_code) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (db.delete(Commons.SALES_ORDER_HEADER, Commons.SALES_ORDER_HEADER_NO + " = ? ", new String[]{header_code}) != -1) //If a header was deleted, then delete the lines corresponding to the header.
        {
            db.delete(Commons.SALES_ORDER_LINES, Commons.SALES_ORDER_LINE_HEADER + " = ? ", new String[]{header_code});
        }
    }

    public long deleteProduct(String product_code) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(Commons.PRODUCT_MASTER, Commons.PRODUCT_CODE + " = ?", new String[]{product_code});
    }

    public boolean deleteAssets(String asset_no) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Commons.ASSETS, Commons.ASSETS_NO + "=?", new String[]{asset_no});
        return true;
    }

    public long deleteBadStock(String product_code)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(Commons.BAD_STOCK, Commons.BAD_STOCK_ITEM_CODE + "=?", new String[]{product_code});
    }

    public long deleteJourneyPlan(int plan_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(Commons.JOURNEY_PLAN, Commons.PLAN_ID + " = ?", new String[]{String.valueOf(plan_id)});
    }

    //Update statements
    public long updateSalesOrderHeaderPaidAmount(String header_code, float amount_paid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.SALES_TRANS_HEADER_TOTAL_PAID, amount_paid);

        return db.update(Commons.SALES_TRANSACTION_HEADER, contentValues, Commons.SALES_TRANS_HEADER_ID + " = ? ", new String[]{header_code});
    }

    public long commitSalesOrder(String customer_code, String product_code, float qty, float line_amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.SO_QTY, qty);
        contentValues.put(Commons.SO_LINE_AMOUNT, line_amount);

        return db.update(Commons.STAGING_SALES_ORDER_GEN, contentValues, Commons.SO_CUSTOMER_CODE + " = ? and " + Commons.SO_SKU_CODE + " = ? ", new String[]{customer_code, product_code});
    }


    public long checkOutOfCustomer() {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.CUSTOMER_CHECK_IN_FLAG, 2);
        contentValues.put(Commons.CUSTOMER_CHECK_OUT_FLAG, 1);

        return db.update(Commons.CUSTOMER_CHECK_IN_OUT, contentValues, Commons.CUSTOMER_CHECK_IN_FLAG + " = ? and " + Commons.CUSTOMER_CHECK_OUT_FLAG + " = ? ", new String[]{"1", "0"});
    }

    public long updateSalesOrderHeaderToPrinted(String sales_order_code) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.SALES_ORDER_HEADER_STATUS, 1);

        return db.update(Commons.SALES_ORDER_HEADER, contentValues, Commons.SALES_ORDER_HEADER_NO + " = ? ", new String[]{sales_order_code});
    }

    public long updateSalesOrderHeaderAmount(String header_code, float amount) //Used after each and every line insert
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //Get the current amount, sum up and update
        amount = amount + this.getSalesOrderHeaderAmount(header_code);

        contentValues.put(Commons.SALES_ORDER_HEADER_AMOUNT, amount);

        return db.update(Commons.SALES_ORDER_HEADER, contentValues, Commons.SALES_ORDER_HEADER_NO + " = ? ", new String[]{header_code});
    }

    public long updateSalesOrderLine(String header_code, String product_code, int qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.SALES_TRANS_LINE_QTY_DELIVERED, qty);

        return db.update(Commons.SALES_TRANSACTION_LINES, contentValues, Commons.SALES_TRANS_LINE_HEADER_CODE + " = ? and " + Commons.SALES_TRANS_ITEM_LINES_CODE + " = ? ", new String[]{header_code, product_code});
    }

    public long updateSalesOrderLine(String header_code, String product_code, float line_amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.SALES_TRANS_LINE_TOTAL_PRICE, line_amount);

        return db.update(Commons.SALES_TRANSACTION_LINES, contentValues, Commons.SALES_TRANS_LINE_HEADER_CODE + " = ? and " + Commons.SALES_TRANS_ITEM_LINES_CODE + " = ? ", new String[]{header_code, product_code});
    }

    public boolean closeDailyCheck(String endtime, float odo_end, String end_comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.DAILY_TIME_END, endtime);
        contentValues.put(Commons.DAILY_ODOMETER_READING_END, odo_end);
        contentValues.put(Commons.DAILY_COMENT_END, end_comment);
        contentValues.put(Commons.DAILY_IS_TRIP_CLOSED, 1);
        db.update(Commons.DAILY_CHECK, contentValues, Commons.DAILY_IS_TRIP_CLOSED + " = ? ", new String[]{"0"});

        return true;
    }

    public long updateBadStock(String item_code,String customer_code,int expired, int damaged, int totals)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.STAGING_PRODUCT_EXPIRED, expired);
        contentValues.put(Commons.STAGING_PRODUCT_DAMAGED, damaged);
        contentValues.put(Commons.STAGING_PRODUCT_TOTALS, totals);

       return db.update(Commons.STAGING_PRODUCTS, contentValues, Commons.STAGING_PRODUCT_CODE + " =?  and " + Commons.STAGING_CUSTOMER_CODE +" =? ", new String[]{item_code,customer_code});
    }

    public float startCustomerActivityEngagement(String customer_code)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.CUSTOMER_CHECK_ACTIVITIES, 1);

        return db.update(Commons.CUSTOMER_CHECK_IN_OUT, contentValues, Commons.CUSTOMER_CHECK_OUT_FLAG + "=? and " + Commons.CUSTOMER_CHECK_IN_FLAG + " = ? and " + Commons.CUSTOMER_CHECK_IN_CUSTOMER_ID + " = ? ", new String[]{"0", "1", customer_code});
    }

    public long updateSalesTemp(String sku_code, int qty, float line_amount) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.SO_QTY, qty);
        contentValues.put(Commons.SO_LINE_AMOUNT, line_amount);

        return db.update(Commons.STAGING_SALES_ORDER_GEN, contentValues, Commons.SO_SKU_CODE + " =? ", new String[]{sku_code});
    }

    public float closeCustomerActivitiesCheckin() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Commons.CUSTOMER_CHECK_ACTIVITIES, 2);

        return db.update(Commons.CUSTOMER_CHECK_IN_OUT, contentValues, Commons.CUSTOMER_CHECK_OUT_FLAG + " = ? and " + Commons.CUSTOMER_CHECK_IN_FLAG + " =? ", new String[]{"0", "1"});
    }

    public float updateCustomerCheckActivities(String customer_code, String activity_name, int action) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        //Determine the field to affect depending on the call to from the source. Then affect the effective field respectively.
        switch (activity_name) {
            case Commons.CUSTOMER_ACTIVITY_ASSET_TRACKING:
                contentValues.put(Commons.CUSTOMER_ACTIVITY_ASSET_TRACKING, action);
                break;
            case Commons.CUSTOMER_ACTIVITY_CALL_OBJECTIVE:
                contentValues.put(Commons.CUSTOMER_ACTIVITY_CALL_OBJECTIVE, action);
                break;
            case Commons.CUSTOMER_ACTIVITY_COMPLETE_CALL:
                contentValues.put(Commons.CUSTOMER_ACTIVITY_COMPLETE_CALL, action);
                break;
            case Commons.CUSTOMER_ACTIVITY_STOCK_TAKE:
                contentValues.put(Commons.CUSTOMER_ACTIVITY_STOCK_TAKE, action);
                break;
            case Commons.CUSTOMER_ACTIVITY_GENERATE_ORDER:
                contentValues.put(Commons.CUSTOMER_ACTIVITY_GENERATE_ORDER, action);
                break;
            case Commons.CUSTOMER_ACTIVITY_INVOICE_AND_PRINT:
                contentValues.put(Commons.CUSTOMER_ACTIVITY_INVOICE_AND_PRINT, action);
                break;
            case Commons.CUSTOMER_ACTIVITY_PAYMENT_COLLECTION:
                contentValues.put(Commons.CUSTOMER_ACTIVITY_PAYMENT_COLLECTION, action);
                break;
            case Commons.CUSTOMER_ACTIVITY_MACHANDISING:
                contentValues.put(Commons.CUSTOMER_ACTIVITY_MACHANDISING, action);
                break;
            case Commons.CUSTOMER_ACTIVITY_DELIVERY:
                contentValues.put(Commons.CUSTOMER_ACTIVITY_DELIVERY, action);
                break;
            default:
                commonClass.createToaster(context, "Could not identify activity.", Commons.TOASTER_LONG, R.drawable.sad);
                break;
        }

        Log.d("Testing", customer_code);
        return db.update(Commons.CUSTOMER_ACTIVITIES, contentValues, Commons.CUSTOMER_ACTIVITY_CUSTOMER_CODE + " = ? and " + Commons.CUSTOMER_ACTIVITY_CHECK_LIST_CLOSED + " = ? ", new String[]{customer_code, "0"});
    }

    public long createBadStock(String item_code,String productname, int expired, int damaged, int totals)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(Commons.BAD_STOCK_ITEM_CODE,item_code);
        contentValues.put(Commons.BAD_STOCK_ITEM_NAME, productname);
        contentValues.put(Commons.BAD_STOCK_EXPIRED, expired);
        contentValues.put(Commons.BAD_STOCK_DAMAGED, damaged);
        contentValues.put(Commons.BAD_STOCK_QTY, totals);

       return db.insert(Commons.BAD_STOCK, null, contentValues);
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


    public  Cursor getAssets()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return  db.rawQuery("select * from " + Commons.ASSETS , null);
    }


    public  Cursor getStock()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("select * from " + Commons.BAD_STOCK,null);
    }

}
