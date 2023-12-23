package database;

public class FactoreDao {
    public static SellerDao createSellerDao(){
        return new SellerDao();
    }
}
