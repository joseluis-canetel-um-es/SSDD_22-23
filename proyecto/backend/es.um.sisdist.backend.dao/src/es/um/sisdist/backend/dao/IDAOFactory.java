/**
 *
 */
package es.um.sisdist.backend.dao;

import es.um.sisdist.backend.dao.database.IDatabaseDAO;
import es.um.sisdist.backend.dao.user.IUserDAO;


/**
 *
 */
public interface IDAOFactory
{
    public IUserDAO createSQLUserDAO();

    public IUserDAO createMongoUserDAO();
    /** nuevos metodos -- kholoud */
    // añado los metodos de eliminacion de usuario
    
    public boolean removeSQLUserDAO();
    public boolean removeMongoUserDAO();
    
    // metodos de la base de datos 
    // creacion
    public IDatabaseDAO createSQLDatabaseDAO();
    public IDatabaseDAO createMongoDatabaseDAO();
    // eliminacion
    public void deleteSQLDatabaseDAO();
    public void deleteMongoDatabaseDAO();


}
