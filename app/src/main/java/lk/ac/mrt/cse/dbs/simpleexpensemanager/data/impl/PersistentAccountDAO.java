package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;


import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import java.util.ArrayList;
import java.util.List;





public class PersistentAccountDAO implements AccountDAO {


    SQLiteDatabase Db;

   public PersistentAccountDAO(SQLiteDatabase Db){
       this.Db = Db;
   }
    //get all account numbers
    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNums = new ArrayList<>();

        String ALL_ACCOUNT_NUMBERS = "SELECT accountNo FROM Account";


        Cursor cursor = Db.rawQuery(ALL_ACCOUNT_NUMBERS, null);
        try {
            if (cursor.moveToFirst()) {
                do {

                    String test=cursor.getString(cursor
                            .getColumnIndex("accountNo"));

                    accountNums.add(test);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return accountNums;
    }

    @Override

    //get  all account details
    public List<Account> getAccountsList() {
        List<Account> allAccountDetail = new ArrayList<>();

        String ALL_ACCOUNT_DETAIL_SELECT_QUERY = "SELECT * FROM Account";


        Cursor cursor = Db.rawQuery(ALL_ACCOUNT_DETAIL_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Account account=new Account(
                            cursor.getString(cursor.getColumnIndex("accountNo")),
                            cursor.getString(cursor.getColumnIndex("bankName")),
                            cursor.getString(cursor.getColumnIndex("accountHolderName")),
                                    cursor.getDouble(cursor.getColumnIndex("balance")));




                    allAccountDetail.add(account);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return allAccountDetail;

    }

    //get account details for a specific account number
    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        String ACCOUNT_QUERY = "SELECT * FROM Account where accountNo = "+accountNo;

        Cursor cursor = Db.rawQuery(ACCOUNT_QUERY, null);
        if (cursor != null)
            cursor.moveToFirst();

        Account accountSpecific = new Account(
                cursor.getString(cursor.getColumnIndex("accountNo")),
                cursor.getString(cursor.getColumnIndex("bankName")),
                cursor.getString(cursor.getColumnIndex("accountHolderName")),
                cursor.getDouble(cursor.getColumnIndex("balance")));


        return accountSpecific;

    }

    //add account
    @Override
    public void addAccount(Account account) {
        String sql = "INSERT INTO Account (accountNo,bankName,accountHolderName,balance) VALUES (?,?,?,?)";
        SQLiteStatement statement = Db.compileStatement(sql);



        statement.bindString(1, account.getAccountNo());
        statement.bindString(2, account.getBankName());
        statement.bindString(3, account.getAccountHolderName());
        statement.bindDouble(4, account.getBalance());


        statement.executeInsert();
    }


    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

        String query = "DELETE FROM Account WHERE accountNo = ?";
        SQLiteStatement statement = Db.compileStatement(query);

        statement.bindString(1,accountNo);

        statement.executeUpdateDelete();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expense_Type, double _amount) throws InvalidAccountException {

        String query = "UPDATE Account SET balance = balance + ?";
        SQLiteStatement statement = Db.compileStatement(query);
        if(expense_Type == ExpenseType.EXPENSE){
            statement.bindDouble(1,-_amount);
        }else{
            statement.bindDouble(1,_amount);
        }

        statement.executeUpdateDelete();
    }
}

