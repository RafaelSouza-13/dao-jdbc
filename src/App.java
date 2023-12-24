import java.util.List;

import database.FactoreDao;
import database.SellerDao;
import model.Department;
import model.Seller;

public class App {
    public static void main(String[] args) throws Exception {
        Department department = new Department(2, null);
        SellerDao sellerDao = FactoreDao.createSellerDao();
        Seller seller  = sellerDao.findById(3);
        List<Seller> list = sellerDao.findByDepartment(department);
        System.out.println("-----Teste 1-----");
        System.out.println(seller);
        System.out.println("-----Teste 2-----");
        for(Seller s : list){
            System.out.println(s);
        }
    }
}
