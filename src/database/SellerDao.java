package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import error.DB;
import error.DbException;
import interfaces.ISellerDao;
import model.Department;
import model.Seller;

public class SellerDao implements ISellerDao {
    private Connection connection;

    public SellerDao(Connection connection){
        this.connection = connection;
    }

    @Override
    public void insert(Seller obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'insert'");
    }

    @Override
    public void update(Seller obj) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void deleteById(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteById'");
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement preparableStatement = null;
        ResultSet result = null;
        try{
            preparableStatement = connection.prepareStatement(
                "SELECT seller.*,department.Name as DepName "
                +"FROM seller INNER JOIN department "
                +"ON seller.DepartmentId = department.Id "
                +"WHERE seller.Id = ? "
                );
                preparableStatement.setInt(1, id);
                result = preparableStatement.executeQuery();
                if(result.next()){
                    Department department = new Department();
                    Seller seller = new Seller();
                    department.setId(result.getInt("DepartmentId"));
                    department.setName(result.getString("Name"));

                    seller.setId(result.getInt("Id"));
                    seller.setName(result.getString("Name"));
                    seller.setEmail(result.getString("Email"));
                    seller.setBaseSalary(result.getDouble("BaseSalary"));
                    seller.setBirthDate(result.getDate("BirthDate"));
                    seller.setDepartment(department);
                    return seller;
                }
                return null;
        }catch(SQLException e){
            throw new DbException(e.getMessage());
        }finally{
            DB.closeStatement(preparableStatement);
            DB.closeResultSet(result);
        }
    }

    @Override
    public List<Seller> findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }
    
}
