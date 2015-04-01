package com.xy.tickets.service;

import android.content.Context;
import android.util.Log;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.xy.tickets.db.EmenuDBOpenHelper;
import com.xy.tickets.db.SMSMessageModel;

import java.sql.SQLException;
import java.util.*;

public class DBServiceImpl implements IDBService {

    private static final String LOG_TAG = "DBServiceImpl";

    private EmenuDBOpenHelper cmsHelper;

    private DBServiceImpl(Context context) {
        // this.commonHelper = CommonDBOpenHelper.getHelper(context);
        this.cmsHelper = EmenuDBOpenHelper.getHelper(context);
    }

    public static IDBService getInstance(Context context) {
        return new DBServiceImpl(context);
    }

    @Override
    public void close() {
        if (cmsHelper != null && cmsHelper.isOpen()) {
            OpenHelperManager.releaseHelper();
            cmsHelper = null;
        }
    }

    @Override
    public void updateSMSMessageModel(SMSMessageModel sms) {
        try {
            Dao<SMSMessageModel, Long> udao = cmsHelper.getSMSDAO();

            SMSMessageModel app = querySMSMessageByContent(sms.getContent());
            if (app == null) {
                //create a new app
                udao.create(sms);
            } else {
                udao.update(sms);
            }

        } catch (SQLException e) {

            Log.e(LOG_TAG, e.getMessage());
        }
    }

    public SMSMessageModel querySMSMessageByContent(String content) {
        try {
            Dao<SMSMessageModel, Long> dba = cmsHelper.getSMSDAO();

            QueryBuilder<SMSMessageModel, Long> queryBuilder = dba.queryBuilder();

            Where<SMSMessageModel, Long> where = queryBuilder.where();
            where.eq("content", content);

            List<SMSMessageModel> list = dba.query(queryBuilder.prepare());
            if(list != null && list.size() > 0){
                return list.get(0);
            }

        } catch (SQLException e) {

            Log.e(LOG_TAG, e.getMessage());
        }
        return null;
    }

    @Override
    public List<SMSMessageModel> getSMSMessageModel() {
        try {
            Dao<SMSMessageModel, Long> dba = cmsHelper.getSMSDAO();
            QueryBuilder<SMSMessageModel, Long> queryBuilder = dba.queryBuilder();
            queryBuilder.orderBy("dueToDate", false);

            Where<SMSMessageModel, Long> where = queryBuilder.where();
            where.eq("deleteFlag", false);

            return dba.query(queryBuilder.prepare());

        } catch (SQLException e) {

            Log.e(LOG_TAG, e.getMessage());
        }
        return new ArrayList<SMSMessageModel>();
    }

}
