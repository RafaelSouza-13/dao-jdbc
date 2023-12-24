package database;

import error.DB;

public class FactoreDao {
    public static SellerDao createSellerDao(){
        return new SellerDao(DB.getConnection());
    }
}
