package database;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement(
                "INSERT INTO seller "
                +"(Name, Email, BirthDate, BaseSalary, DepartmentId) "
                +"VALUES "
                +"(?, ?, ?, ?, ?)", 
                Statement.RETURN_GENERATED_KEYS
                );
            preparedStatement.setString(1, obj.getName());
            preparedStatement.setString(2, obj.getEmail());
            preparedStatement.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            preparedStatement.setDouble(4, obj.getBaseSalary());
            preparedStatement.setInt(5, obj.getDepartment().getId());
            int rows = preparedStatement.executeUpdate();
            if(rows > 0){
                ResultSet result = preparedStatement.getGeneratedKeys();
                if(result.next()){
                    int id = result.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(result);
            }else{
                throw new DbException("Erro inesperado! nenhuma linha foi afetada");
            }
        }catch(SQLException e){
            throw new DbException(e.getMessage());
        }finally{
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public void update(Seller obj) {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement(
                "UPDATE seller "
                +"SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
                +"WHERE Id = ? "
                );
            preparedStatement.setString(1, obj.getName());
            preparedStatement.setString(2, obj.getEmail());
            preparedStatement.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
            preparedStatement.setDouble(4, obj.getBaseSalary());
            preparedStatement.setInt(5, obj.getDepartment().getId());
            preparedStatement.setInt(6, obj.getId());
            preparedStatement.executeUpdate();
            
        }catch(SQLException e){
            throw new DbException(e.getMessage());
        }finally{
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement preparedStatement = null;
        try{
            preparedStatement = connection.prepareStatement(
                "DELETE FROM seller "
                +"WHERE Id = ? "
                );
            preparedStatement.setInt(1, id);
            int linhas = preparedStatement.executeUpdate();
            if(linhas == 0){
                throw new DbException("Não existe usuário com este id");
            }
        }catch(SQLException e){
            throw new DbException(e.getMessage());
        }finally{
            DB.closeStatement(preparedStatement);
        }
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
                    Department department = instanciateDepartment(result);
                    Seller seller = instanciateSeller(result, department);
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

    private Seller instanciateSeller(ResultSet result, Department department) throws SQLException{
        Seller seller = new Seller();
        seller.setId(result.getInt("Id"));
        seller.setName(result.getString("Name"));
        seller.setEmail(result.getString("Email"));
        seller.setBaseSalary(result.getDouble("BaseSalary"));
        seller.setBirthDate(result.getDate("BirthDate"));
        seller.setDepartment(department);
        return seller;
    }

    private Department instanciateDepartment(ResultSet result) throws SQLException{
        Department department = new Department();
        department.setId(result.getInt("DepartmentId"));
        department.setName(result.getString("DepName"));
        return department;
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement preparableStatement = null;
        ResultSet result = null;
        try{
            preparableStatement = connection.prepareStatement(
                "SELECT seller.*,department.Name as DepName "
                +"FROM seller INNER JOIN department "
                +"ON seller.DepartmentId = department.Id "
                +"ORDER BY Name "
                );
                result = preparableStatement.executeQuery();
                List<Seller> list = new ArrayList<>();
                Map<Integer, Department> map =  new HashMap<>();
                while(result.next()){
                    Department dep = map.get(result.getInt("DepartmentId"));
                    if(dep == null){
                        dep = instanciateDepartment(result);
                        map.put(result.getInt("DepartmentId"), dep);
                    }
                    Seller seller = instanciateSeller(result, dep);
                    list.add(seller);
                }
                return list;
        }catch(SQLException e){
            throw new DbException(e.getMessage());
        }finally{
            DB.closeStatement(preparableStatement);
            DB.closeResultSet(result);
        }
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement preparableStatement = null;
        ResultSet result = null;
        try{
            preparableStatement = connection.prepareStatement(
                "SELECT seller.*,department.Name as DepName "
                +"FROM seller INNER JOIN department "
                +"ON seller.DepartmentId = department.Id "
                +"WHERE DepartmentId = ? "
                +"ORDER BY Name "
                );
                preparableStatement.setInt(1, department.getId());
                result = preparableStatement.executeQuery();
                List<Seller> list = new ArrayList<>();
                Map<Integer, Department> map =  new HashMap<>();
                while(result.next()){
                    Department dep = map.get(result.getInt("DepartmentId"));
                    if(dep == null){
                        dep = instanciateDepartment(result);
                        map.put(result.getInt("DepartmentId"), dep);
                    }
                    Seller seller = instanciateSeller(result, dep);
                    list.add(seller);
                    return list;
                }
                return null;
        }catch(SQLException e){
            throw new DbException(e.getMessage());
        }finally{
            DB.closeStatement(preparableStatement);
            DB.closeResultSet(result);
        }
    }
    
}
