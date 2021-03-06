package com.example.chenhao.simpleapp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.chenhao.simpleapp.base.BaseFragment;
import com.example.chenhao.simpleapp.base.UserInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhao on 17/7/15.
 * <p>
 * 这个是一个 专门用于操作用户表（app_user）的操作服务类
 */
public class UserTableDBopenhelerService {

    private static UserTableDBopenhelerService instance = null;
    private DBopenHeler mHeler = null;


    private UserTableDBopenhelerService(Context context) {
        mHeler = DBopenHeler.getInstance(context);
//        mHeler.getWritableDatabase().close();

    }

    /**
     * Gets instance.
     *
     * @param c the c
     * @return the instance
     */
    public static UserTableDBopenhelerService getInstance(Context c) {
        if (instance == null) {
            synchronized (UserTableDBopenhelerService.class) {
                if (instance == null) {
                    instance = new UserTableDBopenhelerService(c);
                }
            }
        }
        return instance;
    }
//    insert（插入）：db.execSQL("insert into person (A,B)  values(?,?)",new Object[]{values1,values2});
//    delete(删除) ：db.execSQL("delete from person where id=? ", new Object[] { id });
//    update（更新）： db.execSQL("update  person set A=? , B=? where id=? ", new Object[] {A,B,id});
//    find(使用id来查找数据) db.rawQuery("select * from person where id=?",new String[] { String.valueOf(Id) });
//    find（多条件查询）select * from person where id=? and A=? and B=?" new String[] { String.valueOf(Id) ,A,B});
//    findAll(获取表中所有数据) db.rawQuery("select * from person",new String[] {  });
//    Sql的语句中，and是&  or 是|  与或 的表示形式。

    /*****************************插入 inser @param bean the bean
     * @return the boolean
     */
    public boolean insert(UserInfoBean bean) {
        try {
            if (!isUserName(bean.getUserName())) {
                Log.e("db", "insert: 账号已存在！");
                return false;
            }
            SQLiteDatabase db = mHeler.getWritableDatabase();
            db.execSQL("insert into app_user" +
                            "(user_name,password,name,email,phone,regist_time,role)" +
                            "values(?,?,?,?,?,?,?)",
                    new Object[]{bean.getUserName(), bean.getPassword(),
                            bean.getName(), bean.getEmail(),
                            bean.getPhone(), bean.getRegistTime(), bean.getRole()});
            Log.e("db", "insert: 插入数据成功！");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("db", "insert: 插入数据失败！");
            return false;
        }
        return true;
    }
/****************************************查询 find*************************************/
    /**
     * 用于判断账户注册 是否重复用户名！ true 表示可以注册 false已注册
     *
     * @param user the user
     * @return boolean
     */
    public boolean isUserName(String user) {
        boolean isUser = false;
        SQLiteDatabase db = mHeler.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from app_user where user_name=?", new String[]{user});
        if (cursor.getCount() == 0) {
            isUser = true;
        }
        cursor.close();
        db.close();
        return isUser;
    }

    /**
     * Is name boolean.
     *
     * @param name the name
     * @return the boolean
     */
    public boolean isName(String name) {
        boolean isName = false;
        SQLiteDatabase db = mHeler.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from app_user where name=?", new String[]{name});
        if (cursor.getCount() == 0) {
            isName = true;
        }
        cursor.close();
        db.close();
        return isName;
    }

    /**
     * Is phone boolean.
     *
     * @param phone the phone
     * @return the boolean
     */
    public boolean isPhone(String phone) {
        boolean isPhone = false;
        SQLiteDatabase db = mHeler.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from app_user where phone=?", new String[]{phone});
        if (cursor.getCount() == 0) {
            isPhone = true;
        }
        cursor.close();
        db.close();
        return isPhone;
    }

    /**
     * Is email boolean.
     *
     * @param email the email
     * @return the boolean
     */
    public boolean isEmail(String email) {
        boolean isPhone = false;
        SQLiteDatabase db = mHeler.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from app_user where email=?", new String[]{email});
        if (cursor.getCount() == 0) {
            isPhone = true;
        }
        cursor.close();
        db.close();
        return isPhone;
    }

//    find（多条件查询）select * from person where id=? and A=? and B=?" new String[] { String.valueOf(Id) ,A,B});

    /**
     * Find user info bean user info bean.
     *
     * @param user the user
     * @param pass the pass
     * @return the user info bean
     */
    public UserInfoBean findUserInfoBean(String user, String pass) {
        UserInfoBean infoBean = null;
        SQLiteDatabase db = mHeler.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from app_user where user_name=? and password=? ",
                new String[]{user, pass});
        if (cursor.getCount() != 0 && cursor.moveToFirst()) {
            infoBean = getCursorContent(cursor);
            Log.e("db", infoBean.toString());
        }
        cursor.close();
        db.close();
        return infoBean;
    }


    /**
     * Find all user info bean list.
     *
     * @return the list
     */
    public List<UserInfoBean> findAllUserInfoBean() {
        List<UserInfoBean> userInfoBeanList = new ArrayList<>();
        SQLiteDatabase db = mHeler.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from app_user",
                new String[]{});
        while (cursor.moveToNext()) {
            UserInfoBean cursorContent = getCursorContent(cursor);
            if (cursorContent != null) {
                userInfoBeanList.add(cursorContent);
                Log.e("db", cursorContent.toString());
            }
        }
        cursor.close();
        db.close();

        return userInfoBeanList;
    }


    /**
     * Gets cursor content.
     *
     * @param cursor the cursor
     * @return the cursor content
     */
    public UserInfoBean getCursorContent(Cursor cursor) {
        int id = 0;
        try {
            id = cursor.getInt(cursor.getColumnIndex("id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String user_name = null;
        try {
            user_name = cursor.getString(cursor.getColumnIndex("user_name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String password = null;
        try {
            password = cursor.getString(cursor.getColumnIndex("password"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String name = null;
        try {
            name = cursor.getString(cursor.getColumnIndex("name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String email = null;
        try {
            email = cursor.getString(cursor.getColumnIndex("email"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String phone = null;
        try {
            phone = cursor.getString(cursor.getColumnIndex("phone"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String regist_time = null;
        try {
            regist_time = cursor.getString(cursor.getColumnIndex("regist_time"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int role = 0;
        try {
            role = cursor.getInt(cursor.getColumnIndex("role"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String carId = null;
        try {
            carId = cursor.getString(cursor.getColumnIndex("carId"));
            String[] split = carId.split(",");
            split = BaseFragment.deleteNull(split);
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < split.length; i++) {
                buffer.append(split[i]);
                if (i < split.length - 1) {
                    buffer.append(",");
                }
            }
            carId = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
            carId = null;
        }
        return new UserInfoBean(id, user_name, password, name, email, phone, regist_time, role, carId);
    }

    /****************更新 -- 改  update @param id the id
     * @param role the role
     * @return the boolean
     */
//    update（更新）： db.execSQL("update  person set A=? , B=? where id=? ", new Object[] {A,B,id});
    public boolean updateRole(int id, int role) {
        SQLiteDatabase db = mHeler.getWritableDatabase();
        try {
            db.execSQL("update  app_user set role=? where id=? ", new Object[]{role, id});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    /**
     * Update car id boolean.
     *
     * @param id    the id
     * @param carId the car id
     * @return the boolean
     */
    public boolean updateCarId(int id, String carId) {
        SQLiteDatabase db = mHeler.getWritableDatabase();
        try {
            db.execSQL("update  app_user set carId=? where id=? ", new Object[]{carId, id});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }
}