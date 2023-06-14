/**
 *
 */
package es.um.sisdist.backend.Service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import es.um.sisdist.backend.grpc.GrpcServiceGrpc;
import es.um.sisdist.backend.grpc.PingRequest;
import es.um.sisdist.backend.dao.DAOFactoryImpl;
import es.um.sisdist.backend.dao.IDAOFactory;
import es.um.sisdist.backend.dao.database.IDatabaseDAO;
import es.um.sisdist.backend.dao.models.DataBase;
import es.um.sisdist.backend.dao.models.User;
import es.um.sisdist.backend.dao.models.utils.UserUtils;
import es.um.sisdist.backend.dao.user.IUserDAO;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * @author dsevilla
 *
 */
public class AppLogicImpl
{
    IDAOFactory daoFactory;
    IUserDAO dao; // usuario DAO
    IDatabaseDAO daodb;

    private static final Logger logger = Logger.getLogger(AppLogicImpl.class.getName());

    private final ManagedChannel channel;
    private final GrpcServiceGrpc.GrpcServiceBlockingStub blockingStub;
    //private final GrpcServiceGrpc.GrpcServiceStub asyncStub;

    static AppLogicImpl instance = new AppLogicImpl();

    private AppLogicImpl()
    {
        daoFactory = new DAOFactoryImpl();
        Optional<String> backend = Optional.ofNullable(System.getenv("DB_BACKEND"));
        
        if (backend.isPresent() && backend.get().equals("mongo"))
            dao = daoFactory.createMongoUserDAO();
        else
            dao = daoFactory.createSQLUserDAO();

        var grpcServerName = Optional.ofNullable(System.getenv("GRPC_SERVER"));
        var grpcServerPort = Optional.ofNullable(System.getenv("GRPC_SERVER_PORT"));

        channel = ManagedChannelBuilder
                .forAddress(grpcServerName.orElse("localhost"), Integer.parseInt(grpcServerPort.orElse("50051")))
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS
                // to avoid needing certificates.
                .usePlaintext().build();
        blockingStub = GrpcServiceGrpc.newBlockingStub(channel);
        //asyncStub = GrpcServiceGrpc.newStub(channel);
    }

    public static AppLogicImpl getInstance()
    {
        return instance;
    }
    
    /**Métodos de la lógica de la aplicación - Kholoud*/


    public Optional<User> getUserByEmail(String userId)
    {
        Optional<User> u = dao.getUserByEmail(userId);
        return u;
    }

    public Optional<User> getUserById(String userId)
    {
        return dao.getUserById(userId);
    }

    public boolean ping(int v)
    {
    	logger.info("Issuing ping, value: " + v);
    	
        // Test de grpc, puede hacerse con la BD
    	var msg = PingRequest.newBuilder().setV(v).build();
        var response = blockingStub.ping(msg);
        
        return response.getV() == v;
    }

    /**modificado por kholoud*/
    // regitra un usuario
    public void signup(String email, String name, String password) {
    	dao.insertUser(email, name, password);
    	//return false;
    	
    }
    
    // crea una base de datos relacionada al id de un usuario
    public boolean createDatabase(String name, String idUser) {
    	return daodb.insertDatabase(name, idUser);
    	//return false;
    }
    
    public boolean deleteDatabase(String idUser, String name) {
    	return daodb.deleteDatabase(name);
    }
    
    
    // devuelve la database dado su id
    public Optional<DataBase> getDatabase(String db) {
    	return Optional.of(daodb.getDatabase(db));
    	//return null;
    }
    
    // dado un id de usuario retorna las bases de datos relacioandos
    public ArrayList<DataBase> getDatabasesByUserId(String userId) {
        try {
            //MongoCollection<DataBase> mongoCollection = collection.get(); // Obtener la colección de la base de datos
           // List<DataBase> result = mongoCollection.find(eq("idUser", userId)).into(new ArrayList<>());
        	ArrayList<DataBase> result = daodb.getDatabases(userId);
            return result;
        } catch (Exception e) {
            // Manejar la excepción según sea necesario
        }

        return null;
    }

    public void insertKeyValue(String dbname, String key, String value) {
    	daodb.addClaveValor(dbname, key, value);
    }
    
    // El frontend, a través del formulario de login,
    // envía el usuario y pass, que se convierte a un DTO. De ahí
    // obtenemos la consulta a la base de datos, que nos retornará,
    // si procede,
    public Optional<User> checkLogin(String email, String pass)
    {
        Optional<User> u = dao.getUserByEmail(email);

        if (u.isPresent())
        {
            String hashed_pass = UserUtils.md5pass(pass);
            if (0 == hashed_pass.compareTo(u.get().getPassword_hash())) {
            	// si login correcto, incrementar numero de visitas: 
            	dao.addVisits(u.get().getId());
                return u;

            }
        }

        return Optional.empty();
    }
}
