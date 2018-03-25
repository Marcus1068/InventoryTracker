package de.marcus_deuss.inventorytracker;


import java.util.Date;

/**
 * Created by marcus on 22.03.18.
 */

// TODO try catch
// TODO DATE anstatt String
// TODO AUTOINCREMENT bei Create table

// contains inventory of an household
public class Inventory {

    // inventory table name
    public static final String TABLE_NAME = "inventory";


    // Inventory Table Column names
    public static final String COLUMN_ID = "_id";    // should always be id, needed by some android classes
    public static final String COLUMN_INVENTORYNAME = "inventoryname";
    public static final String COLUMN_DATEOFPURCHASE = "dateofpurchase";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_INVOICE = "invoice";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_WARRANTY = "warranty";
    public static final String COLUMN_SERIALNUMBER = "serialnumber";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_REMARK = "remark";
    public static final String COLUMN_OWNERNAME = "ownername";
    public static final String COLUMN_CATEGORYNAME = "categoryname";
    public static final String COLUMN_BRANDNAME = "brandname";
    public static final String COLUMN_ROOMNAME = "roomname";

    // Create table SQL query string
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_INVENTORYNAME + " TEXT,"
            + COLUMN_DATEOFPURCHASE  + " DATE," + COLUMN_PRICE + " INTEGER,"
            + COLUMN_INVOICE + " BLOB," + COLUMN_TIMESTAMP + " DATE DEFAULT CURRENT_TIMESTAMP,"
            + COLUMN_WARRANTY + " INTEGER," + COLUMN_SERIALNUMBER + " TEXT,"
            + COLUMN_IMAGE + " BLOB," + COLUMN_REMARK + " TEXT,"
            + COLUMN_OWNERNAME + " TEXT," + COLUMN_CATEGORYNAME + " TEXT,"
            + COLUMN_BRANDNAME + " TEXT," + COLUMN_ROOMNAME + " TEXT" + ")";


    // private variables
    private long id;                       // database internal number, must be long!
    private String inventoryName;        // name of inventory
    private String dateOfPurchase;
    private int price;
    private byte[] invoice;               // PDF of already scanned invoice or new invoice rendered vom Jpeg to PDF
    private String timeStamp;              // create date of database entry
    private int warranty;                // warranty in number of months (6,12,18,24,36)
    private String serialNumber;
    private byte[] image;                // link to object image
    private String remark;               // addl. info
    private String ownerName;           // who owns this object in household
    private String categoryName;        // (Technology, Computer, Furniture etc.)
    private String brandName;       // (Apple, Sony, Samsung, IKEA etc.)
    private String roomName;            // location of object

    // Empty constructor
    public Inventory(){

    }

    // constructor
    public Inventory(long id, String inventoryName, String dateOfPurchase,
                     int price, byte[] invoice, String timeStamp, int warranty,
                     String serialNumber, byte[] image, String remark,
                     String ownerName, String categoryName, String brandName,
                     String roomName){
        this.id = id;                           // id autogenerated in database
        this.inventoryName = inventoryName;
        this.dateOfPurchase = dateOfPurchase;
        this.price = price;
        this.invoice = invoice;
        this.timeStamp = timeStamp;             // will be autogenerated in database
        this.warranty = warranty;
        this.serialNumber = serialNumber;
        this.image = image;
        this.remark = remark;
        this.ownerName = ownerName;
        this.categoryName = categoryName;
        this.brandName = brandName;
        this.roomName = roomName;
    }


    // getter methods
    public long getId() {
        return id;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public String getDateOfPurchase() {
        return dateOfPurchase;
    }

    public int getPrice() {
        return price;
    }

    public byte[] getInvoice() {
        return invoice;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public int getWarranty() {
        return warranty;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public byte[] getImage(){
        return image;
    }

    public String getRemark() {
        return remark;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getBrandName() {
        return brandName;
    }

    // setter methods

    public void setId(long id) {
        this.id = id;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public void setDateOfPurchase(String dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setInvoice(byte[] invoice) {
        this.invoice = invoice;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setWarranty(int warranty) {
        this.warranty = warranty;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setImage(byte[] image){
        this.image = image;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}






