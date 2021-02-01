package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;







public class PersistentDemoExpenseManager extends ExpenseManager{


    Context context;
    public PersistentDemoExpenseManager(Context con)  {
        context=con;
        try {

            setup();
        } catch (ExpenseManagerException e) {
            e.printStackTrace();
        }
    }
    @Override

    public void setup() throws ExpenseManagerException {

        SQLiteDatabase db = context.openOrCreateDatabase("180638L", context.MODE_PRIVATE, null);

        // create the databases.
        db.execSQL("CREATE TABLE IF NOT EXISTS Account(" +
                "accountNo VARCHAR PRIMARY KEY," +
                "bankName VARCHAR," +
                "accountHolderName VARCHAR," +
                "balance REAL" +
                " );");


        db.execSQL("CREATE TABLE IF NOT EXISTS Account_Transaction(" +
                "Transaction_id INTEGER PRIMARY KEY," +
                "accountNo VARCHAR," +
                "expenseType INT," +
                "amount REAL," +
                "date DATE," +
                "FOREIGN KEY (accountNo) REFERENCES Account(accountNo)" +
                ");");




        PersistentAccountDAO accountDAO = new PersistentAccountDAO(db);

        setAccountsDAO(accountDAO);

        setTransactionsDAO(new PersistentTransactionDAO(db));
        // dummy data
      /* Account dumAcct1 = new Account("00001A", "BOC","Ruban", 10000.0);
        Account dummyAcct2 = new Account("00012L", "HSBC", "Thaya", 300000.0);
        getAccountsDAO().addAccount(dumAcct1);
        getAccountsDAO().addAccount(dummyAcct2);*/


    }
}
