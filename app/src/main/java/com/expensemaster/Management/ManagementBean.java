package com.expensemaster.Management;

import android.content.Context;
import com.expensemaster.DAO.SQLiteDAO;
import com.expensemaster.DAO.SQLiteDAOImpl;

/**
 * Created by Aditya on 1/7/2017.
 */
public class ManagementBean {

    public SQLiteDAO getDAOFactory(Context context){
        return new SQLiteDAOImpl(context);
    }

}
