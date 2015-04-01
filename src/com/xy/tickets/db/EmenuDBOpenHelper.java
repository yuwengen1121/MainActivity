package com.xy.tickets.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class EmenuDBOpenHelper extends OrmLiteSqliteOpenHelper {

	public static final int DATABASE_VERSION = 1;

	public static final String DATABASE_NAME = "tickets.db";

	// we do this so there is only one helper
	private static EmenuDBOpenHelper helper = null;

	private Dao<SMSMessageModel, Long> smsDao;

	public EmenuDBOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static synchronized EmenuDBOpenHelper getHelper(Context context) {
		if (helper == null) {
			helper = new EmenuDBOpenHelper(context);
			helper.getWritableDatabase();
		}
		return helper;
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(EmenuDBOpenHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, SMSMessageModel.class);
		} catch (SQLException e) {
			Log.e(EmenuDBOpenHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			Log.i(EmenuDBOpenHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, SMSMessageModel.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(EmenuDBOpenHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}

	}

    public Dao<SMSMessageModel, Long> getSMSDAO() throws SQLException {
		if (smsDao == null) {
            smsDao = getDao(SMSMessageModel.class);
		}
		return smsDao;
	}

	@Override
	public void close() {
		super.close();
        smsDao = null;
	}

}
