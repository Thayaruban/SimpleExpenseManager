package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;





public class PersistentTransactionDAO implements TransactionDAO {





    SQLiteDatabase Db;

    public PersistentTransactionDAO(SQLiteDatabase Db){

      this.Db=Db;
    }


    @Override
    //insert values into transaction table
    public void logTransaction(Date date_, String accountNo, ExpenseType expenseType_, double amount_){



            String query = "INSERT INTO Account_Transaction (accountNo,expenseType,amount,date) VALUES (?,?,?,?)";
            SQLiteStatement statement = Db.compileStatement(query);

            statement.bindString(1,accountNo);
            statement.bindLong(2,(expenseType_ == ExpenseType.EXPENSE) ? 0 : 1);
            statement.bindDouble(3,amount_);
            statement.bindLong(4,date_.getTime());

            statement.executeInsert();



    }

    @Override
    //get all transactions
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionsAll = new ArrayList<>();

        String query = "SELECT * FROM Account_Transaction";
        Cursor cursor = Db.rawQuery(query, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Transaction transactions=new Transaction(
                            new Date(cursor.getLong(cursor.getColumnIndex("date"))),
                            cursor.getString(cursor.getColumnIndex("accountNo")),
                            (cursor.getInt(cursor.getColumnIndex("expenseType")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                   cursor.getDouble(cursor.getColumnIndex("amount")));




                    transactionsAll.add(transactions);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }


        return transactionsAll;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {


        List<Transaction> allTransDetail = new ArrayList<>();

        String query = "SELECT * FROM Account_Transaction LIMIT"+limit;


        Cursor cursor = Db.rawQuery(query, null);


            if (cursor.moveToFirst()) {
                do {
                    Transaction transactionDetail=new Transaction(
                            new Date(cursor.getLong(cursor.getColumnIndex("date"))),
                            cursor.getString(cursor.getColumnIndex("accountNo")),
                            (cursor.getInt(cursor.getColumnIndex("expenseType")) == 0) ? ExpenseType.EXPENSE : ExpenseType.INCOME,
                            cursor.getDouble(cursor.getColumnIndex("amount")));


                    allTransDetail.add(transactionDetail);

                } while (cursor.moveToNext());
            }

        return  allTransDetail;
    }





}

