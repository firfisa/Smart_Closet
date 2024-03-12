package com.example.smart_closet.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.smart_closet.domain.Administrator;
import com.example.smart_closet.domain.Clothes;
import com.example.smart_closet.domain.Room;
import com.example.smart_closet.domain.User;
import com.example.smart_closet.domain.Wardrobe;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "smart_closet.db";
    private static final int DB_VERSION = 1;
    private static DBOpenHelper mHelper = null;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;

    public DBOpenHelper(Context context) {
        super(context, "smart_closet.db", null, DB_VERSION);
    }

    // 利用单例模式获取数据库的操作对象
    public static DBOpenHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new DBOpenHelper(context);
        }
        return mHelper;
    }

    // 打开数据库的读连接
    public SQLiteDatabase openReadLink() {
        if (mRDB == null || !mRDB.isOpen()) {
            mRDB = mHelper.getReadableDatabase();
        }
        return mRDB;
    }

    // 打开数据库的写连接
    public SQLiteDatabase openWriteLink() {
        if (mWDB == null || !mWDB.isOpen()) {
            mWDB = mHelper.getWritableDatabase();
        }
        return mWDB;
    }

    // 关闭数据库连接
    public void closeLink() {
        if (mRDB != null && mRDB.isOpen()) {
            mRDB.close();
            mRDB = null;
        }
        if (mWDB != null && mWDB.isOpen()) {
            mWDB.close();
            mWDB = null;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //用户信息表 包含id,username,password
        String sql_student = "create table if not exists user  (" +
                "id integer primary key AUTOINCREMENT  not null ," +
                "username varchar(20) not null," +
                "password varchar(20) not null," +
                "height varchar(20)," +
                "weight varchar(20)," +
                "city varchar(20)," +
                "age integer," +
                "sex varchar(4))";

                //管理员表
        String sql_admin = "create table if not exists administrator  (" +
                "account varchar(20) not null," +
                "password varchar(20) not null)";

        String sql = "CREATE TABLE clothes (" +
                "c_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "c_name VARCHAR(20) NOT NULL," +
                "c_wardrobe_id INTEGER NOT NULL," +
                "c_type VARCHAR(20) NOT NULL," +
                "c_size VARCHAR(3) DEFAULT 'M',"+
                "c_scene VARCHAR(20),"+
                "c_description VARCHAR(100),"+
                "c_cleaned_date DATE," +
                "c_color VARCHAR(20)," +
                "c_season VARCHAR(10)," +
                "c_brand VARCHAR(20)," +
                "c_status INTEGER DEFAULT 0," +
                "c_picture_path VARCHAR(100)," +
                "c_image BLOB," +
                "c_purchase_date DATE" +
                ");";
        sqLiteDatabase.execSQL(sql);

        sql = "CREATE TABLE room (" +
                "r_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "r_name VARCHAR NOT NULL," +
                "r_description TEXT," +
                "r_clothes_num INTEGER" +
                ");";
        sqLiteDatabase.execSQL(sql);

        sql = "CREATE TABLE wardrobe (" +
                "w_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "w_name VARCHAR NOT NULL," +
                "w_description TEXT," +
                "w_clothes_num INTEGER," +
                "w_room_id INTEGER NOT NULL," +
                "FOREIGN KEY (w_room_id) REFERENCES room(r_id)" +
                ");";

        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(sql_student);
        sqLiteDatabase.execSQL(sql_admin);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        Cursor cursor = mRDB.query("room", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Room room = new Room();
                room.setId(cursor.getInt(cursor.getColumnIndex("r_id")));
                room.setName(cursor.getString(cursor.getColumnIndex("r_name")));
                room.setDescription(cursor.getString(cursor.getColumnIndex("r_description")));
                room.setRoomClothesNum(cursor.getInt(cursor.getColumnIndex("r_clothes_num")));
                rooms.add(room);
            } while (cursor.moveToNext());
        }

        return rooms;
    }

    public long addRoom(Room room) {
        ContentValues values = new ContentValues();
        values.put("r_name", room.getName());
        values.put("r_description", room.getDescription());
        values.put("r_clothes_num", room.getRoomClothesNum());
        values.put("r_clothes_num", 0);
        return mWDB.insert("room", null, values);
    }

    public int deleteRoom(int id) {
        return mWDB.delete("room", "r_id=?", new String[]{String.valueOf(id)});
    }

    public int updateRoom(Room room) {
        ContentValues values = new ContentValues();
        values.put("r_name", room.getName());
        values.put("r_description", room.getDescription());
        values.put("r_clothes_num", room.getRoomClothesNum());
        return mWDB.update("room", values, "r_id=?", new String[]{String.valueOf(room.getId())});
    }

    public long addWardrobe(Wardrobe wardrobe) {
        ContentValues values = new ContentValues();
        values.put("w_name", wardrobe.getName());
        values.put("w_description", wardrobe.getDescription());
        values.put("w_clothes_num", wardrobe.getClothesNum());
        values.put("w_room_id", wardrobe.getRoomId());
        values.put("w_clothes_num", 0);
        return mWDB.insert("wardrobe", null, values);
    }

    public int deleteWardrobe(int id) {
        return mWDB.delete("wardrobe", "w_id=?", new String[]{String.valueOf(id)});
    }

    public List<Wardrobe> getWareDrobesByRoomId(int roomId) {
        List<Wardrobe> wardrobes = new ArrayList<>();
        Cursor cursor = mRDB.query("wardrobe", null, "w_room_id=?", new String[]{String.valueOf(roomId)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Wardrobe wardrobe = new Wardrobe();
                wardrobe.setId(cursor.getInt(cursor.getColumnIndex("w_id")));
                wardrobe.setName(cursor.getString(cursor.getColumnIndex("w_name")));
                wardrobe.setDescription(cursor.getString(cursor.getColumnIndex("w_description")));
                wardrobe.setClothesNum(cursor.getInt(cursor.getColumnIndex("w_clothes_num")));
                wardrobe.setRoomId(cursor.getInt(cursor.getColumnIndex("w_room_id")));
                wardrobes.add(wardrobe);
            } while (cursor.moveToNext());
        }

        return wardrobes;
    }

    public List<Clothes> getClothesByWardrobeId(int wardrobe_id) {
        List<Clothes> clothes = new ArrayList<>();
        Cursor cursor = mRDB.query("clothes", null, "c_wardrobe_id=?", new String[]{String.valueOf(wardrobe_id)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Clothes cloth = new Clothes();
                cloth.setName(cursor.getString(cursor.getColumnIndex("c_name")));
                cloth.setId(cursor.getInt(cursor.getColumnIndex("c_id")));
                cloth.setWardrobeId(cursor.getInt(cursor.getColumnIndex("c_wardrobe_id")));
                cloth.setType(cursor.getString(cursor.getColumnIndex("c_type")));
//                cloth.setSize(cursor.getString(cursor.getColumnIndex("c_size")));
//                cloth.setCleanedDate(Date.valueOf(cursor.getString(cursor.getColumnIndex("c_cleaned_date"))));
                cloth.setColor(cursor.getString(cursor.getColumnIndex("c_color")));
                cloth.setImage(cursor.getBlob(cursor.getColumnIndex("c_image")));
//                cloth.setSeason(cursor.getString(cursor.getColumnIndex("c_season")));
//                cloth.setBrand(cursor.getString(cursor.getColumnIndex("c_brand")));
//                cloth.setPurchaseDate(Date.valueOf(cursor.getString(cursor.getColumnIndex("c_purchase_date"))));
//                cloth.setPicturePath(cursor.getString(cursor.getColumnIndex("c_picture_path")));

                clothes.add(cloth);
            } while (cursor.moveToNext());
        }

        return clothes;
    }

    public long addClothes(Clothes clothes) {
        ContentValues values = new ContentValues();
        values.put("c_wardrobe_id", clothes.getWardrobeId());
        values.put("c_name", clothes.getName());
        values.put("c_type", clothes.getType());
        values.put("c_scene", clothes.getScene());
//        values.put("c_size", clothes.getSize());
//        values.put("c_cleaned_date", clothes.getCleanedDate().toString());
        values.put("c_color", clothes.getColor());
        values.put("c_season", clothes.getSeason());
        values.put("c_image", clothes.getImage());
//        values.put("c_brand", clothes.getBrand());
//        values.put("c_purchase_date", clothes.getPurchaseDate().toString());
//        values.put("c_picture_path", clothes.getPicturePath());

        System.out.println("addClothes: " + clothes.getWardrobeId() + " " + clothes.getName() + " " + clothes.getType() + " " + clothes.getScene() + " " + clothes.getColor() + " " + clothes.getSeason());
        System.out.println("--------------------");

        return mWDB.insert("clothes", null, values);
    }

    public void addClothesToWear(int id) {
        ContentValues values = new ContentValues();
        values.put("c_status", 1);
        mWDB.update("clothes", values, "c_id=?", new String[]{String.valueOf(id)});
    }

    public void cancelClothesToWear(int id) {
        ContentValues values = new ContentValues();
        values.put("c_status", 0);
        mWDB.update("clothes", values, "c_id=?", new String[]{String.valueOf(id)});
    }


    public int deleteClothes(int id) {
        return mWDB.delete("clothes", "c_id=?", new String[]{String.valueOf(id)});
    }


    public String getWardrobeNameByID(int wardrobe_id) {
        Cursor cursor = mRDB.query("wardrobe", null, "w_id=?", new String[]{String.valueOf(wardrobe_id)}, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex("w_name"));
        }
        return null;
    }

    public List<Clothes> getClothesBySeason(String season, int mode) {

        Cursor cursor;
        List<Clothes> clothes = new ArrayList<>();

        if (mode == 0) {
            cursor = mRDB.query("clothes", null, "c_season=? AND c_status=0", new String[]{season}, null, null, null);
        } else {
            cursor = mRDB.query("clothes", null, "c_season=? AND c_status=1", new String[]{season}, null, null, null);
        }

        if (cursor.moveToFirst()) {

            do {
                Clothes cloth = new Clothes();
                cloth.setName(cursor.getString(cursor.getColumnIndex("c_name")));
                cloth.setId(cursor.getInt(cursor.getColumnIndex("c_id")));
                cloth.setWardrobeId(cursor.getInt(cursor.getColumnIndex("c_wardrobe_id")));
                cloth.setType(cursor.getString(cursor.getColumnIndex("c_type")));
                cloth.setStatus(cursor.getInt(cursor.getColumnIndex("c_status")));
                cloth.setImage(cursor.getBlob(cursor.getColumnIndex("c_image")));
                clothes.add(cloth);
            } while (cursor.moveToNext());
        }
        return clothes;
    }

    public List<Clothes> getClothesByScene(String scene) {

        System.out.println("getClothesByScene: " + scene + "====================");
        List<Clothes> clothes = new ArrayList<>();
        if ("".equals(scene) || scene == null) {
            return clothes;
        }

        Cursor cursor = mRDB.query("clothes", null, "c_scene=?", new String[]{scene}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Clothes cloth = new Clothes();
                cloth.setName(cursor.getString(cursor.getColumnIndex("c_name")));
                cloth.setId(cursor.getInt(cursor.getColumnIndex("c_id")));
                cloth.setWardrobeId(cursor.getInt(cursor.getColumnIndex("c_wardrobe_id")));
                cloth.setImage(cursor.getBlob(cursor.getColumnIndex("c_image")));
//                cloth.setDescription(cursor.getString(cursor.getColumnIndex("c_description")));
                clothes.add(cloth);
            } while (cursor.moveToNext());
        }
        return clothes;
    }

    public void updateClothesNumInRoom(int room_id, int clothes_num) {
        ContentValues values = new ContentValues();
        values.put("r_clothes_num", clothes_num);
        mWDB.update("room", values, "r_id=?", new String[]{String.valueOf(room_id)});
    }

    public void updateClothesNumInWardrobe(int wardrobe_id, int clothes_num) {
        ContentValues values = new ContentValues();
        values.put("w_clothes_num", clothes_num);
        mWDB.update("wardrobe", values, "w_id=?", new String[]{String.valueOf(wardrobe_id)});
    }

    public int getClothesNumInWardrobe(int wardrobe_id) {
        Cursor cursor = mRDB.query("wardrobe", null, "w_id=?", new String[]{String.valueOf(wardrobe_id)}, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex("w_clothes_num"));
        }
        return 0;
    }

    public int getClothesNumInRoom(int room_id) {
        Cursor cursor = mRDB.query("room", null, "r_id=?", new String[]{String.valueOf(room_id)}, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex("r_clothes_num"));
        }
        return 0;
    }

    public boolean queryUser(String username,String password){
        boolean res=false;
        Cursor cursor = mRDB.query("user", null, "username=?", new String[]{username}, null, null, null);
        while (cursor.moveToNext()) {
            if ( password.equals(cursor.getString(2))){
                res=true;
            }
        }
        return res;
    }
    public long addUser(User user){
        ContentValues values = new ContentValues();
        values.put("username",user.username);
        values.put("password",user.password);
        long res = mWDB.insert("user", null, values);
        return res;
    }

    public int getUserId(String username){
        Cursor cursor = mRDB.query("user", null, "username=?", new String[]{username}, null, null, null);
        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex("id"));
        }
        return 0;
    }

    public User getUserInfo(int id){
        System.out.println(id + "===============");
        Cursor cursor = mRDB.query("user", null, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {

            System.out.println("找到了用户信息=================");
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex("id")));
            user.setCity(cursor.getString(cursor.getColumnIndex("city")));
            user.setAge(cursor.getInt(cursor.getColumnIndex("age")));
            user.setHeight(cursor.getString(cursor.getColumnIndex("height")));
            user.setWeight(cursor.getString(cursor.getColumnIndex("weight")));
            user.setSex(cursor.getString(cursor.getColumnIndex("sex")));
            return user;
        }
        System.out.println("没找到用户信息=================");
        return null;
    }

    public long updateUserInfo(int id, String city, int age, String body_height, String body_weight, String sex) {
        ContentValues values = new ContentValues();
        values.put("city", city);
        values.put("age", age);
        values.put("height", body_height);
        values.put("weight", body_weight);
        values.put("sex", sex);
        long res = mWDB.update("user", values, "id=?", new String[]{String.valueOf(id)});
        return res;
    }

    public List<User> findAllUser(){
        List<User> users=new ArrayList<>();
        Cursor cursor = mRDB.query("user", null, null, null, null, null, null);
        while(cursor.moveToNext()){
            User user = new User();
            user.setId(cursor.getInt(0));
            user.setUsername(cursor.getString(1));
            user.setPassword(cursor.getString(2));
            users.add(user);
        }
        return users;
    }

    //        管理员表
    public long insertAdministrator(Administrator administrator){
        ContentValues values = new ContentValues();
        values.put("account",administrator.account);
        values.put("password",administrator.password);
        return mWDB.insert("administrator",null,values);
    }
    public boolean queryAdministrator(String account,String password){
        boolean res=false;
        Cursor cursor = mRDB.query("administrator", null, "account=?", new String[]{account}, null, null, null);
        while (cursor.moveToNext()) {
            if ( password.equals(cursor.getString(1))){
                res=true;
            }
        }
        return res;
    }
    public long addAdministrator(Administrator administrator){
        ContentValues values = new ContentValues();
        values.put("account",administrator.account);
        values.put("password",administrator.password);
        long res = mWDB.insert("administrator", null, values);
        return res;
    }
    public List<Administrator> findAllAdmin(){
        List<Administrator> administrators=new ArrayList<>();
        Cursor cursor = mRDB.query("administrator", null, null, null, null, null, null);
        while(cursor.moveToNext()){
            Administrator administrator = new Administrator();
            administrator.setAccount(cursor.getString(0));
            administrator.setPassword(cursor.getString(1));
            administrators.add(administrator);
        }
        return administrators;
    }

}
