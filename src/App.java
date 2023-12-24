import java.util.Date;
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
        System.out.println("-----Teste 1-----");
        System.out.println(seller);
        List<Seller> list = sellerDao.findByDepartment(department);
        System.out.println("-----Teste 2-----");
        for(Seller s : list){
            System.out.println(s);
        }
        list = sellerDao.findAll();
        System.out.println("-----Teste 3-----");
        for(Seller s : list){
            System.out.println(s);
        }
        System.out.println("-----Teste 4-----");
        Seller sellerNovo = new Seller(null, "Shreck", "pantano@email.com", new Date(), 3000.0, department);
        sellerDao.insert(sellerNovo);
        System.out.println("Novo vendedor de id: "+sellerNovo.getId());
    }
}
