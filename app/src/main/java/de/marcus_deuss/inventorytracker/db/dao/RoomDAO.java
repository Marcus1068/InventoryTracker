package de.marcus_deuss.inventorytracker.db.dao;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.marcus_deuss.inventorytracker.R;
import de.marcus_deuss.inventorytracker.db.DatabaseHelper;
import de.marcus_deuss.inventorytracker.db.InventoryDBDAO;
import de.marcus_deuss.inventorytracker.db.entity.Room;

import static de.marcus_deuss.inventorytracker.InventoryApp.resources;


public class RoomDAO extends InventoryDBDAO{
    private static final String TAG = "InventoryTracker";

    private static final String WHERE_ID_EQUALS = DatabaseHelper.COLUMN_ID
            + " =?";

    public RoomDAO(Context context) {
        super(context);
    }

    // insert new room
    public long saveRoom(Room room) {
        Log.d(TAG, "Insert room");

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ROOMNAME, room.getRoomName());

        return database.insert(DatabaseHelper.TABLE_NAME_ROOM, null, values);
    }

    // TODO beta not tested and buggy
    public long updateRoom(Room room) {
        Log.d(TAG, "Update room");

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ROOMNAME, room.getRoomName());

        // values.put(DatabaseHelper.COLUMN_ID, category.getDepartment().getId());

        long result = database.update(DatabaseHelper.TABLE_NAME_ROOM, values,
                WHERE_ID_EQUALS,
                new String[] { String.valueOf(room.getId()) });
        Log.d("Update Result:", "=" + result);

        return result;
    }

    // delete a room
    public int deleteRoom(Room room) {
        return database.delete(DatabaseHelper.TABLE_NAME_ROOM,
                WHERE_ID_EQUALS, new String[] { room.getId() + "" });
    }

    // Getting single room item
    public Room getRoom(long id) {
        Log.d(TAG, "getRoom");

        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME_ROOM,
                new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_ROOMNAME},
                DatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare room object
        Room room = new Room(
                cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ROOMNAME))
        );

        // close the db connection
        cursor.close();

        return room;
    }

    // Getting all room items, sorted by room name
    public List<Room> getRoomList() {
        Log.d(TAG, "getRoomList");

        List<Room> roomList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME_ROOM + " ORDER BY " +
                DatabaseHelper.COLUMN_ROOMNAME + DatabaseHelper.TABLE_SORT_ORDER;


        Cursor cursor = database.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Room room = new Room();
                room.setId(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
                room.setRoomName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ROOMNAME)));

                // Adding  to list
                roomList.add(room);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return list
        return roomList;
    }

    // Getting room Count
    public int getRoomCount() {
        Log.d(TAG, "getRoomCount");

        String countQuery = "SELECT * FROM " + DatabaseHelper.TABLE_NAME_ROOM;

        Cursor cursor = database.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }

    public Cursor createRoomListViewCursor() {
        Log.d(TAG, "createRoomListViewCursor() ...");

        String[] columns = new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_ROOMNAME};
        return  database.query(DatabaseHelper.TABLE_NAME_ROOM, columns,
                null, null, null, null, DatabaseHelper.COLUMN_ROOMNAME);
    }

    // insert some room data into database as initial data
    public void generateRoomData() {
        Log.d(TAG, "generateRoomData");


        Room room1 = new Room((long) 1, resources.getString(R.string.kitchen));
        this.saveRoom(room1);

        Room room2 = new Room((long) 2, resources.getString(R.string.office));
        this.saveRoom(room2);

        Room room3 = new Room((long) 3, resources.getString(R.string.garden));
        this.saveRoom(room3);

        Room room4 = new Room((long) 4, resources.getString(R.string.basement));
        this.saveRoom(room4);

        Room room5 = new Room((long) 5, resources.getString(R.string.livingroom));
        this.saveRoom(room5);

        Room room6 = new Room((long) 6, resources.getString(R.string.bedroom));
        this.saveRoom(room6);

        Room room7 = new Room((long) 6, resources.getString(R.string.unknownRoom));
        this.saveRoom(room7);

    }

}
