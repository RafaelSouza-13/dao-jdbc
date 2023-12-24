import database.FactoreDao;
import database.SellerDao;
import model.Seller;

public class App {
    public static void main(String[] args) throws Exception {
        SellerDao sellerDao = new FactoreDao().createSellerDao();
        Seller seller  = sellerDao.findById(3);
        System.out.println(seller);
    }
}
