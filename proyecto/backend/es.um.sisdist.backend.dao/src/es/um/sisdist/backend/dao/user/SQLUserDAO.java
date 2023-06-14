/**
 *
 */
package es.um.sisdist.backend.dao.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import es.um.sisdist.backend.dao.models.User;
import es.um.sisdist.backend.dao.utils.Lazy;

/**
 * @author dsevilla
 *
 */
public class SQLUserDAO implements IUserDAO
{
    Supplier<Connection> conn;

    public SQLUserDAO()
    {
    	conn = Lazy.lazily(() -> 
    	{
    		try
    		{
    			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();

    			// Si el nombre del host se pasa por environment, se usa aquí.
    			// Si no, se usa localhost. Esto permite configurarlo de forma
    			// sencilla para cuando se ejecute en el contenedor, y a la vez
    			// se pueden hacer pruebas locales
    			String sqlServerName = Optional.ofNullable(System.getenv("SQL_SERVER")).orElse("localhost");
    			String dbName = Optional.ofNullable(System.getenv("DB_NAME")).orElse("ssdd");
    			return DriverManager.getConnection(
                    "jdbc:mysql://" + sqlServerName + "/" + dbName + "?user=root&password=root");
    		} catch (Exception e)
    		{
    			// TODO Auto-generated catch block
    			e.printStackTrace();
            
    			return null;
    		}
    	});
    }

 // modificada por kholoud
    @Override
    public Optional<User> getUserById(String id)
    {
        PreparedStatement stm;
        try {
            stm = conn.get().prepareStatement("SELECT * FROM users WHERE id = ?");
            stm.setString(1, id);
            ResultSet result = stm.executeQuery();
            if (result.next()) {
                return createUser(result);
            }
        } catch (SQLException e) {
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserByEmail(String id)
    {
        PreparedStatement stm;
        try
        {
            stm = conn.get().prepareStatement("SELECT * from users WHERE email = ?");
            stm.setString(1, id);
            ResultSet result = stm.executeQuery();
            if (result.next())
                return createUser(result);
        } catch (SQLException e)
        {
            // Fallthrough
        }
        return Optional.empty();
    }

    private Optional<User> createUser(ResultSet result)
    {
        try
        {
            return Optional.of(new User(result.getString(1), // id
                    result.getString(2), // email
                    result.getString(3), // pwhash
                    result.getString(4), // name
                    result.getString(5), // token
                    result.getInt(6))); // visits
        } catch (SQLException e)
        {
            return Optional.empty();
        }
    }
    
    /** modificado por kholoud*/
    // recibe un objeto User que contiene la información actualizada 
    // del usuario que se desea modificar en la base de datos
    public boolean updateUser(User user) {
        PreparedStatement stm;
        try {
            stm = conn.get().prepareStatement("UPDATE users SET id = ?, email = ?, password_hash = ?, name = ?, token = ?, visits = ? WHERE id = ?");
            stm.setString(1, user.getId());
            stm.setString(2, user.getEmail());
            stm.setString(3, user.getPassword_hash());
            stm.setString(4, user.getName());
            stm.setString(5, user.getToken());
            stm.setInt(6, user.getVisits());
            //Se ejecuta la consulta de actualización
            int rowsAffected = stm.executeUpdate();
            return rowsAffected > 0; // Se verifica si se modificó al menos una fila en la base de datos 
        } catch (SQLException e) {
        }
        return false;
    }
    
    /** modificada por kholoud*/
    // inserta el user en la base de datos 
    // falta modificar password ID Y TOKEN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
    public boolean insertUser(String email, String name, String password) {
        PreparedStatement stm;
        try {
            stm = conn.get().prepareStatement("INSERT INTO users (id, email, password_hash, name, token, visits) VALUES (?, ?, ?, ?, ?, ?)");
            stm.setString(1, UUID.randomUUID().toString()); 
            stm.setString(2, email);
            stm.setString(3, password);
            stm.setString(4, name);
            stm.setString(5, UUID.randomUUID().toString());
            stm.setInt(6, 0);
            int rowsAffected = stm.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Manejo de excepciones: capturar y manejar la excepción según sea necesario
        }
        return false;
    }
    
    /** modificada por kholoud*/
    // elimina el user en la base de datos 
    // recibe el ID DEL USUARIO
    public boolean deleteUser(String id) {
        PreparedStatement stm;
        try {
            stm = conn.get().prepareStatement("DELETE FROM users WHERE id = ?");
            stm.setString(1, id);
            int rowsAffected = stm.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // Manejo de excepciones: capturar y manejar la excepción según sea necesario
        }
        return false;
    }
    
    /**modificado por kholoud*/
    
	@Override
	public void addVisits(String id) {
		// TODO Auto-generated method stub
		// obtener usuario y modificar su numero de visitas en la base de datos
		 try {
		        PreparedStatement stm = conn.get().prepareStatement("UPDATE users SET visits = visits + 1 WHERE id = ?");
		        stm.setString(1, id);
		        stm.executeUpdate();
		        //return rowsAffected > 0;
		    } catch (SQLException e) {
		        // Manejo de excepciones: capturar y manejar la excepción según sea necesario
		       // return false;
		    }
		

	}

}
