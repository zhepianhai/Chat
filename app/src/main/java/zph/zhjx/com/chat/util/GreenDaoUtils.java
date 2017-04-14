package zph.zhjx.com.chat.util;
import android.database.sqlite.SQLiteDatabase;
import zph.zhjx.com.chat.app.App;
import zph.zhjx.com.chat.db.DaoMaster;
import zph.zhjx.com.chat.db.DaoSession;
/**
 * Created by adminZPH on 2017/3/8.
 * 这里是封装GreenDao调用方法
 * */
public class GreenDaoUtils {
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static GreenDaoUtils greenDaoUtils;
    private GreenDaoUtils(){}
    public static GreenDaoUtils getSingleTon(){
        if (greenDaoUtils==null){
            greenDaoUtils=new GreenDaoUtils();
        }
        return greenDaoUtils;
    }
    private void initGreenDao(){
        mHelper=new DaoMaster.DevOpenHelper(App.getIntance(),"chat_db",null);
        db=mHelper.getWritableDatabase();
        mDaoMaster=new DaoMaster(db);
        mDaoSession=mDaoMaster.newSession();
    }
    public DaoSession getmDaoSession() {
        if (mDaoMaster==null){
            initGreenDao();
        }
        return mDaoSession;
    }
    public SQLiteDatabase getDb() {
        if (db==null){
            initGreenDao();
        }
        return db;
    }
}

