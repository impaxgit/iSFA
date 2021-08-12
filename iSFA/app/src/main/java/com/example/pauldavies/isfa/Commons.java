package com.example.pauldavies.isfa;

import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.net.PortUnreachableException;

public class Commons
{
    /*
     * This class defines all the constant objects used throughout the app.
     * Changing it requires a consideration of context throughout the app.*/

    public static final String  URL_ROUTES               =   "getroutes.php";
    public static final String  URL_JOURNEY_PLAN         =   "journeyplan.php";
    public static final String  URL_GET_CUSTOMERS        =   "getcustomers.php";
    public static final String  URL_GET_ORDERSHEADERS    =   "getOrderHeaders.php";
    public static final String  URL_GET_ORDERSLINES      =   "getOrderLines.php";
    public static final String  URL_GET_TRANSORDERS      =   "getTransactionOrders.php";
    public static final String  URL_GET_TRANSORDERSLINES =   "getTransactionLines.php";
    public static final String  URL_GET_PRODUCTS         =   "products.php";
    public static final String  URL_GET_NOTIFICATIONS    =   "notifications.php";
    public static final String  URL_GET_ASSETS           =   "asset.php";
    public static final String  NO_RESULT                =   "no result";
    public static final String  URL_LOGIN                =   "login.php";
    public static final String  SESSION_EXCEED           =   "iSfa_more_than_required";
    public static final String  NOT_EXACT                =   "iSfa_no_precision";
    public static final String  RESULT_SUCCESS           =   "iSfa_success";
    public static final String  RESULT                   =   "iSfa_result";
    public static final String  ERROR_FETCHING_DATA      =   "iSfa_error_while_getting_data";
    public static final String  NO_RECORD_MATCH          =   "iSfa_no_record_matched";
    public static final String  PASSWORD_NO_VAL          =   "iSfa_password_not_provided";
    public static final String  USERNAME_NO_VAL          =   "iSfa_username_not_provided";
    public static final String  CON_REQ_UNACCEPTABLE     =   "request not acceptable";
    public static final String  CON_REQ_NOT_FOUND        =   "request not found";
    public static final String  CON_INTERNAL_ERROR       =   "internal error";
    public static final String  CON_FORBIDDEN            =   "forbidden";
    public static final String  CON_CLIENT_TIMEOUT       =   "client timeout";
    public static final String  CON_BAD_GATEWAY          =   "bad gateway";
    public static final String  CON_BAD_REQUEST          =   "bad request";
    public static final boolean YES                      =   true;
    public static final boolean NO                       =   false;
    public static final String  ERROR                    =   "An error occurred";
    public static final int     CON_TIMEOUT              =   10000;
    public static final String  CON_METHOD               =   "POST";
    public static final String  URL_CON                  =   "connection.php";
    public static final String  URL_ROOT                 =   "http://10.0.0.110:81/sfa/";
    public static final String  CON_FAILED               =   "iSfa_failed_to_connect";
    public static final String  CON_SUCCESS              =   "iSfa_successfully_connect";
    public static final String  NO_NETWORK               =   "no";
    public static final int     IMPAX_SPLASH_DURATION    =   5000;
    public static final int     CLIENT_SPLASH_DURATION   =   2500;
    public static final int     TOASTER_LONG             =   Toast.LENGTH_LONG;
    public static final int     TOASTER_SHORT            =   Toast.LENGTH_SHORT;
    public static final int     REQ_CAPTURE_IMAGE        =   100;
    public static final String  REQUIRED_FIELD           =   "Required field";


    //Database name and version
    public static final String  DB_NAME                  =  "isfa";
    public static final int     DB_VERSION               =  46;

    //DB Table names and fields

    //Product Master
    public static final String PRODUCT_MASTER         =   "product_master"; //Holding all customer information
        public static final String PRODUCT_CODE       =   "product_code";
        public static final String PRODUCT_NAME       =  "product_name";
        public static final String PRODUCT_CATEGORY   =   "product_category";
        public static final String PRODUCT_BARCODE    ="barcode";
        public static final String PRODUCT_SEARCH_NAME="search_name";
        public static final String PRODUCT_PACKAGING  ="packaging";
        public static final String PRODUCT_COST       ="cost";
        public static final String PRODUCT_PRICE      ="price";
        public static final String PRODUCT_SYNC       ="synced";
        public static final String PRODUCT_SALESPERSON_ID ="sales_person_id";
//JourneyPlan
    public static final String JOURNEY_PLAN           =   "journey_plan"; //Holding the journey plans info
        public static final String PLAN_ID            ="plan_id";
        public static final String PLAN_CUSTOMER_CODE ="customer_code";
        public static final String PLAN_DATE          ="plan_date";
        public static final String PLAN_SEQUENCE      ="sequence";
        public static final String PLAN_SYNC          ="synced";

//Customers
    public static final String CUSTOMER_MASTER        =    "customer_master"; //Holding all the product information
        public static final String CUSTOMER_CODE      ="customer_code";
        public static final String CUSTOMER_NAME      ="customer_name"; //Name of the outlet
        public static final String CUSTOMER_DESCRIPTION="customer_description";
        public static final String CUSTOMER_OUTLET_TYPE="customer_outlet_type";
        public static final String CUSTOMER_LOCATION="customer_location"; //Route
        public static final String CUSTOMER_GEO_COORDINATES="customer_geo_coordinates";
        public static final String CUSTOMER_DATE_CREATED="customer_date_created";
        public static final String CUSTOMER_TYPE="customer_type"; //If cash or credit
        public static final String CUSTOMER_TAXABLE="customer_is_taxable";
        public static final String CUSTOMER_EMAIL="customer_email";
        public static final String CUSTOMER_PHONE="customer_phone";
        public static final String CUSTOMER_CONTACT_PERSON="customer_contact_person";
        public static final String CUSTOMER_CONTACT_EMAIL="customer_contact_email";
        public static final String CUSTOMER_CONTACT_PHONE="customer_contact_phone";
        public static final String CUSTOMER_CREDIT_LIMIT="customer_credit_limit";
        public static final String CUSTOMER_CREDIT_BALANCE="customer_credit_balance";
        public static final String CUSTOMER_STATUS="customer_status"; //whether or not the customer is active
        public static final String CUSTOMER_SYNC="sync";


    public static final String TENDER_TYPES    =    "tender_types"; //Holds the types of payment tenders
        public static final String TENDER_CODE="tender_code";
        public static final String TENDER_NAME="tender_name";
        public static final String TENDER_TYPE="tender_type";

//Check in
    public static final String DAILY_CHECK     =    "daily_check_in"; //For information regarding daily check ins; include type of movement, odometer readings, data and time off and back
        public static final String DAILY_MODE_OF_TRANSPORT="daily_mode_of_transport";
        public static final String DAILY_REGISTRATION_NUMBER="daily_registration_number";
        public static final String DAILY_ODOMETER_READING_START =   "daily_odometer_reading_start";
        public static final String DAILY_ODOMETER_READING_END="daily_odometer_reading_end";
        public static final String DAILY_COMMENT="daily_comment";
        public static final String DAILY_DATE="daily_date";
        public static final String DAILY_TIME_START=  "daily_time_start";
        public static final String DAILY_TIME_END="daily_time_end";
        public static final String DAILY_IS_TRIP_CLOSED="trip_closed";
        public static final String DAILY_COMENT_END="end_comment";


    public static final String SALES_TRANSACTION_HEADER="sales_transaction_header"; //Headers regarding the sales done
        public static final String  HEADER_ID="id";
        public static final String  SALES_TRANS_HEADER_NO="sales_trans_header_no";
        public static final String  SALES_TRANS_HEADER_CUSTOMER_CODE="sales_trans_header_customer_code";
        public static final String  SALES_TRANS_HEADER_DATE_TIME="sales_trans_header_date_time";
        public static final String  SALES_TRANS_HEADER_TOTAL_AMOUNT="sales_trans_header_total_amount";
        public static final String  SALES_TRANS_HEADER_CREATED_BY= "sales_trans_header_created_by";
        public static final String  SALES_TRANS_HEADER_STATUS= "sales_trans_header_status";


    public static final String SALES_TRANSACTION_LINES="sales_transaction_line";//Sales transaction lines; each line must correspond with the header
        public static final String LINE_ID="id";
        public static final String SALES_TRANS_LINES_HEADER_ID="sales_trans_header_id";
        public static final String SALES_TRANS_LINE_NO="sales_trans_header_line_no";
        public static final String SALES_TRANS_ITEM_LINES_CODE="sales_trans_line_item_code";
        public static final String SALES_TRANS_ITEM_LINES_NAME="sales_trans_line_item_name";
        public static final String SALES_TRANS_LINE_ITEM_DESCRIPTION="sales_trans_line_item_description";
        public static final String SALES_TRANS_LINE_QTY_ORDERED="sales_trans_line_qty_ordered";
        public static final String SALES_TRANS_LINE_QTY_DELIVERED="sales_trans_line_qty_received";
        public static final String SALES_TRANS_LINE_UNIT_PRICE="sales_trans_line_unit_price";
        public static final String SALES_TRANS_LINE_TOTAL_PRICE="sales_trans_line_total_price";
        public static final String SALES_TRANS_LINE_DATE_AND_TIME="sales_trans_line_date_and_time";
        public static final String SALES_TRANS_LINE_STATUS="sales_trans_line_status";
        public static final String SALES_TRANS_LINE_COMMENTS="sales_trans_line_comments";


    public static final String TRANSACTION_TENDER_TYPES="transaction_tender_types"; //The tendering type for each transaction done
        public static final String TRANS_TENDER_TRANS_ID="trans_tender_trans_id";
        public static final String TRANS_TENDER_TENDER_CODE="trans_tender_tender_code";
        public static final String TRANS_TENDER_AMOUNT="trans_tender_amount";


    public static final String BAD_STOCK    =   "bad_stock";
        public static final String BAD_STOCK_ITEM_CODE="bad_stock_item_code";
        public static final String BAD_STOCK_ITEM_NAME="bad_stock_item_name";
        public static final String BAD_STOCK_REASON="bad_stock_reason";
        public static final String BAD_STOCK_DATE="bad_stock_date";
        public static final String BAD_STOCK_CUSTOMER_CODE="bad_stock_customer_code";
        public static final String BAD_STOCK_QTY="bad_stock_qty";
        public static final String BAD_STOCK_DAMAGED="bad_stock_damaged";
        public static final String BAD_STOCK_EXPIRED="bad_stock_expired";
        public static final String BAD_STOCK_STATUS="bad_stock_status";


    public static final String STOCK_REQUEST="stock_request";
        public static final String STOCK_REQUEST_ITEM_CODE=   "stock_request_item_code";
        public static final String STOCK_REQUEST_QTY="stock_request_qty";


    public static final String FEEDBACK="feedback";
        public static final String FEEDBACK_SOURCE="feedback_source";
        public static final String FEEDBACK_DATE="feedback_date";
        public static final String FEEDBACK_NOTE="feedback_note";


    public static final String CUSTOMER_CHECK_IN_OUT="customer_check_in_out";
        public static final String CUSTOMER_CHECK_IN_ID="customer_check_in_id";
        public static final String CUSTOMER_CHECK_IN_CUSTOMER_ID="customer_check_in_customer_id";
        public static final String CUSTOMER_CHECK_IN_DATE_AND_TIME="customer_check_in_date_and_time";
        public static final String CUSTOMER_CHECK_IN_ACTIVITY="customer_check_in_activity";


    public static final String SALES_ORDER_HEADER="sales_order_header";
        public static final String SALES_ORDER_HEADER_ID="sales_order_header_id";
        public static final String SALES_ORDER_HEADER_NO="sales_order_header_no";
        public static final String SALES_ORDER_HEADER_CUSTOMER_CODE="sales_order_header_customer_code";
        public static final String SALES_ORDER_HEADER_AMOUNT="sales_order_header_amount";
        public static final String SALES_ORDER_HEADER_DATE_AND_TIME="sales_order_header_date_and_time";
        public static final String SALES_ORDER_HEADER_CREATED_BY="sales_order_header_created_by";


    public static final String SALES_ORDER_LINES="sales_order_lines";
        public static final String SALES_ORDER_LINE_ID="sales_orderlines_id";
        public static final String SALES_ORDER_LINE_NO ="sales_orderline_no";
        public static final String SALES_ORDER_LINE_HEADER="sales_order_header";
        public static final String SALES_ORDER_LINE_ITEM_CODE="sales_order_line_item_code";
        public static final String SALES_ORDER_LINE_ITEM_NAME="sales_order_line_item_name";
        public static final String SALES_ORDER_LINE_ITEM_DESCRIPTION="sales_order_line_item_description";
        public static final String SALES_ORDER_LINE_ITEM_QTY="sales_order_line_item_qty";
        public static final String SALES_ORDER_LINE_UNIT_PRICE="sales_order_line_unit_price";
        public static final String SALES_ORDER_LINE_TOTAL_AMOUNT="sales_order_line_total_amount";
        public static final String SALES_ORDER_LINE_ITEM_COST="sales_order_line_item_cost";
        public static final String SALES_ORDER_LINE_DATE_AND_TIME="sales_order_line_date_and_time";


    public static final String ROUTES="routes";
        public static final String ROUTE_ID="route_id";
        public static final String ROUTE_CODE="route_code";
        public static final String ROUTE_NAME="route_name";
        public static final String ROUTE_DESCRIPTION="route_description";
        public static final String ROUTE_TERRITORY="territory";
        public static final String ROUTE_REGION="region";
        public static final String ROUTE_IS_ACTIVE="active";



    public  static final String NOTIFICATIONS = "notificatiions";
        public  static  final  String NOTIFICATIONS_ID      ="notificatiions_id";
        public  static  final  String NOTIFICATIONS_SUBJECT ="subject";
        public  static  final  String NOTIFICATIONS_CONTENT ="content";
        public  static  final  String NOTIFICATIONS_STATUS  ="status";
        public  static  final  String NOTIFICATIONS_DATE    ="date";



    public  static  final String ASSETS      ="asset_tracking";
        public  static  final String ASSETS_NO                  = "assets_no";
        public  static  final String ASSETS_NAME                = "assets_name";
        public  static  final String ASSETS_ON_SITE             = "assets_onSite";
        public  static  final String ASSETS_CONDITION           = "assets_condition";
        public  static  final String ASSETS_REASON              = "assets_reason";
        public  static  final String ASSETS_LAST_SERVICE_DATE   = "assets_lastServiceDate";
        public  static  final String ASSETS_DATE                = "assets_date";
        public  static  final String ASSETS_NEXT_SERVICE_DATE   = "assets_nextServiceDate";
        public  static  final String ASSETS_COMMENTS            = "assets_comments";


    /*SQLite table statements*/
    public  static final String product_master           =   "create table "+Commons.PRODUCT_MASTER+"("+Commons.PRODUCT_BARCODE+" text, "+Commons.PRODUCT_CODE+" text, "+Commons.PRODUCT_NAME+" text, "+Commons.PRODUCT_SEARCH_NAME+" text, "+Commons.PRODUCT_CATEGORY+" text, "+Commons.PRODUCT_COST+" real, "+Commons.PRODUCT_PRICE+" real, "+Commons.PRODUCT_PACKAGING+" text, "+Commons.PRODUCT_SYNC+" integer, "+Commons.PRODUCT_SALESPERSON_ID+" text)";
    public  static final String customer_master          =   "create table "+Commons.CUSTOMER_MASTER+"("+Commons.CUSTOMER_CODE+" text, "+Commons.CUSTOMER_NAME+" text, "+Commons.CUSTOMER_TYPE+" text, "+Commons.CUSTOMER_DESCRIPTION+" text, "+Commons.CUSTOMER_OUTLET_TYPE+" text, "+Commons.CUSTOMER_LOCATION+" text, "+Commons.CUSTOMER_GEO_COORDINATES+" text, "+Commons.CUSTOMER_DATE_CREATED+" text, "+Commons.CUSTOMER_TAXABLE+" integer, "+Commons.CUSTOMER_EMAIL+" text, "+Commons.CUSTOMER_PHONE+" text, "+Commons.CUSTOMER_CONTACT_PERSON+" text, "+Commons.CUSTOMER_CONTACT_EMAIL+" text, "+Commons.CUSTOMER_CONTACT_PHONE+" text, "+Commons.CUSTOMER_CREDIT_LIMIT+" real, "+Commons.CUSTOMER_CREDIT_BALANCE+" real, "+Commons.CUSTOMER_STATUS+" integer, "+Commons.CUSTOMER_SYNC+" integer)";
    public  static final String journey_plan             =   "create table "+Commons.JOURNEY_PLAN+"("+Commons.PLAN_ID+" integer primary key autoincrement not null, "+Commons.PLAN_CUSTOMER_CODE+" text, "+Commons.PLAN_DATE+" text, "+Commons.PLAN_SEQUENCE+" integer, "+Commons.PLAN_SYNC+" integer)";
    public  static final String tender_types             =   "create table "+Commons.TENDER_TYPES+"("+Commons.TENDER_CODE+" text, "+Commons.TENDER_NAME+" text, "+Commons.TENDER_TYPE+" text)";
    public  static final String daily_check              =   "create table "+Commons.DAILY_CHECK+"("+Commons.DAILY_MODE_OF_TRANSPORT+" text, "+Commons.DAILY_REGISTRATION_NUMBER+" text, "+Commons.DAILY_ODOMETER_READING_START+" real, "+Commons.DAILY_ODOMETER_READING_END+" real, "+Commons.DAILY_COMMENT+" text, "+Commons.DAILY_DATE+" text, "+Commons.DAILY_TIME_START+" text, "+Commons.DAILY_TIME_END+" text, "+Commons.DAILY_IS_TRIP_CLOSED+" integer, "+Commons.DAILY_COMENT_END+" text)";
    public  static final String sales_trans_header       =   "create table "+Commons.SALES_TRANSACTION_HEADER+"("+Commons.HEADER_ID+" integer primary key autoincrement, "+Commons.SALES_TRANS_HEADER_NO+" text, "+Commons.SALES_TRANS_HEADER_CUSTOMER_CODE+" text, "+Commons.SALES_TRANS_HEADER_DATE_TIME+" text, "+Commons.SALES_TRANS_HEADER_TOTAL_AMOUNT+" real, "+Commons.SALES_TRANS_HEADER_CREATED_BY+" text,"+Commons.SALES_TRANS_HEADER_STATUS+" text)";
    public  static final String sales_trans_lines        =   "create table "+Commons.SALES_TRANSACTION_LINES+"("+Commons.LINE_ID+" integer,"+Commons.SALES_TRANS_LINES_HEADER_ID+" text,"+Commons.SALES_TRANS_LINE_NO+" text,"+Commons.SALES_TRANS_ITEM_LINES_CODE+" text, "+Commons.SALES_TRANS_ITEM_LINES_NAME+" text, "+Commons.SALES_TRANS_LINE_ITEM_DESCRIPTION+" text, "+Commons.SALES_TRANS_LINE_QTY_ORDERED+" real,"+ Commons.SALES_TRANS_LINE_QTY_DELIVERED+" real, "+Commons.SALES_TRANS_LINE_UNIT_PRICE+" real,"+Commons.SALES_TRANS_LINE_TOTAL_PRICE+" real, "+Commons.SALES_TRANS_LINE_DATE_AND_TIME+" text,"+Commons.SALES_TRANS_LINE_STATUS+" text, "+Commons.SALES_TRANS_LINE_COMMENTS+" text)";
    public  static final String transaction_tender_types =   "create table "+Commons.TRANSACTION_TENDER_TYPES+"("+Commons.TRANS_TENDER_TRANS_ID+" text, "+Commons.TRANS_TENDER_TENDER_CODE+" text, "+Commons.TRANSACTION_TENDER_TYPES+" text, "+Commons.TRANS_TENDER_AMOUNT+" real)";
    public  static final String bad_stock                =   "create table "+Commons.BAD_STOCK+"("+Commons.BAD_STOCK_ITEM_CODE+" text,"+Commons.BAD_STOCK_ITEM_NAME+" text, "+Commons.BAD_STOCK_CUSTOMER_CODE+" text, "+Commons.BAD_STOCK_STATUS+" text, "+Commons.BAD_STOCK_QTY+" real, "+Commons.BAD_STOCK_DAMAGED+" real,"+Commons.BAD_STOCK_EXPIRED+",real "+Commons.BAD_STOCK_DATE+" text, "+Commons.BAD_STOCK_REASON+" text)";
    public  static final String stock_request            =   "create table "+Commons.STOCK_REQUEST+"("+Commons.STOCK_REQUEST_ITEM_CODE+" text, "+Commons.STOCK_REQUEST_QTY+" real)";
    public  static final String feedback                 =   "create table "+Commons.FEEDBACK+"("+Commons.FEEDBACK_SOURCE+" text, "+Commons.FEEDBACK_DATE+" text, "+Commons.FEEDBACK_NOTE+" text)";
    public  static final String customer_check_in_out    =   "create table "+Commons.CUSTOMER_CHECK_IN_OUT+"("+Commons.CUSTOMER_CHECK_IN_ID+" integer primary key autoincrement not null, "+Commons.CUSTOMER_CHECK_IN_CUSTOMER_ID+" text, "+Commons.CUSTOMER_CHECK_IN_DATE_AND_TIME+" text, "+Commons.CUSTOMER_CHECK_IN_ACTIVITY+" text)";
    public  static final String sales_order_header       =   "create table "+Commons.SALES_ORDER_HEADER+"("+Commons.SALES_ORDER_HEADER_ID+" integer primary key autoincrement not null ,"+Commons.SALES_ORDER_HEADER_NO+" text, "+Commons.SALES_ORDER_HEADER_CUSTOMER_CODE+" text, "+Commons.SALES_ORDER_HEADER_AMOUNT+" real, "+Commons.SALES_ORDER_HEADER_DATE_AND_TIME+" text,"+Commons.SALES_ORDER_HEADER_CREATED_BY+" text)";
    public  static final String sales_order_lines        =   "create table "+Commons.SALES_ORDER_LINES+"("+Commons.SALES_ORDER_LINE_ID+" integer ,"+Commons.SALES_ORDER_LINE_NO+" text,"+Commons.SALES_ORDER_LINE_HEADER+" text ,"+Commons.SALES_ORDER_LINE_ITEM_CODE+" text, "+Commons.SALES_ORDER_LINE_ITEM_NAME+" text, "+Commons.SALES_ORDER_LINE_ITEM_DESCRIPTION+" text, "+Commons.SALES_ORDER_LINE_ITEM_QTY+" real, "+Commons.SALES_ORDER_LINE_ITEM_COST+" real, "+Commons.SALES_ORDER_LINE_UNIT_PRICE+" real, "+Commons.SALES_ORDER_LINE_TOTAL_AMOUNT+" real, "+Commons.SALES_ORDER_LINE_DATE_AND_TIME+" text)";
    public  static final String routes                   =   "create table "+Commons.ROUTES+"("+Commons.ROUTE_ID+" integer, "+Commons.ROUTE_CODE+" text,  "+Commons.ROUTE_NAME+" text, "+Commons.ROUTE_DESCRIPTION+" text, "+Commons.ROUTE_TERRITORY+" text, "+Commons.ROUTE_REGION+" text, "+Commons.ROUTE_IS_ACTIVE+" integer)";
    public  static final String notificatiions           =   "create table "+Commons.NOTIFICATIONS+"("+Commons.NOTIFICATIONS_ID+" integer, "+Commons.NOTIFICATIONS_SUBJECT+" text, "+Commons.NOTIFICATIONS_CONTENT+" text,"+Commons.NOTIFICATIONS_STATUS+" integer, "+Commons.NOTIFICATIONS_DATE+" text)";
    public  static final String assets                   =   "create table "+Commons.ASSETS +"(" + Commons.ASSETS_NO+" integer, "+Commons.ASSETS_NAME+" text,"+Commons.ASSETS_ON_SITE+" text, "+Commons.ASSETS_CONDITION+" text, "+Commons.ASSETS_REASON+" text, "+Commons.ASSETS_LAST_SERVICE_DATE+" date, "+Commons.ASSETS_DATE+" date,"+Commons.ASSETS_NEXT_SERVICE_DATE+" date, "+Commons.ASSETS_COMMENTS+" text)" ;


    public  static final String product_master_drop      =   "drop table if exists "+Commons.PRODUCT_MASTER;
    public  static final String customer_master_drop     =   "drop table if exists "+Commons.CUSTOMER_MASTER;
    public  static final String journey_plan_drop        =   "drop table if exists "+Commons.JOURNEY_PLAN;
    public  static final String tender_types_drop        =   "drop table if exists "+Commons.TENDER_TYPES;
    public  static final String daily_check_drop         =   "drop table if exists "+Commons.DAILY_CHECK;
    public  static final String sales_trans_header_drop  =   "drop table if exists "+Commons.SALES_TRANSACTION_HEADER;
    public  static final String sales_trans_lines_drop   =   "drop table if exists "+Commons.SALES_TRANSACTION_LINES;
    public  static final String transaction_tender_types_drop    =   "drop table if exists "+Commons.TRANSACTION_TENDER_TYPES;
    public  static final String bad_stock_drop                   =   "drop table if exists "+Commons.BAD_STOCK;
    public  static final String stock_request_drop               =   "drop table if exists "+Commons.STOCK_REQUEST;
    public  static final String feedback_drop                    =   "drop table if exists "+Commons.FEEDBACK;
    public  static final String customer_check_in_out_drop       =   "drop table if exists "+Commons.CUSTOMER_CHECK_IN_OUT;
    public  static final String sales_order_header_drop          =   "drop table if exists "+Commons.SALES_ORDER_HEADER;
    public  static final String sales_order_lines_drop           =   "drop table if exists "+Commons.SALES_ORDER_LINES;
    public  static final String routes_drop                      =   "drop table if exists "+Commons.ROUTES;
    public  static final String notification_drop                =   "drop table if exists "+Commons.NOTIFICATIONS;
    public  static final String assets_drop                      =   "drop table if exists "+Commons.ASSETS;


}