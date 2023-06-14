/**
 *
 */
package es.um.sisdist.backend.dao;

import es.um.sisdist.backend.dao.database.IDatabaseDAO;
import es.um.sisdist.backend.dao.database.MongoDBDatabaseDAO;
import es.um.sisdist.backend.dao.database.SQLDatabaseDAO;
import es.um.sisdist.backend.dao.user.IUserDAO;
import es.um.sisdist.backend.dao.user.MongoUserDAO;
import es.um.sisdist.backend.dao.user.SQLUserDAO;

/**
 * @author dsevilla
 *
 */
public class DAOFactoryImpl implements IDAOFactory
{
    @Override
    public IUserDAO createSQLUserDAO()
    {
        return new SQLUserDAO();
    }

    @Override
    public IUserDAO createMongoUserDAO()
    {
        return new MongoUserDAO();
    }
    // eliminar usuario

    // falta implementar 
	@Override
	public boolean removeSQLUserDAO() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean removeMongoUserDAO() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IDatabaseDAO createSQLDatabaseDAO() {
		// TODO Auto-generated method stub
		return new SQLDatabaseDAO();
	}

	@Override
	public IDatabaseDAO createMongoDatabaseDAO() {
		// TODO Auto-generated method stub
		return new MongoDBDatabaseDAO();
	}

	@Override
	public void deleteSQLDatabaseDAO() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteMongoDatabaseDAO() {
		// TODO Auto-generated method stub
		
	}
   
}
