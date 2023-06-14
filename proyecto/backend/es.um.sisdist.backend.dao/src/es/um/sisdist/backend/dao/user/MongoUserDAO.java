/**
 *
 */
package es.um.sisdist.backend.dao.user;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static java.util.Arrays.*;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;


import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import org.bson.Document;



import com.mysql.cj.xdevapi.UpdateResult;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import es.um.sisdist.backend.dao.models.User;
import es.um.sisdist.backend.dao.utils.Lazy;

/**
 * @author dsevilla
 *
 */
public class MongoUserDAO implements IUserDAO
{
    private Supplier<MongoCollection<User>> collection;

    public MongoUserDAO()
    {
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().conventions(asList(Conventions.ANNOTATION_CONVENTION)).automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));

        // Replace the uri string with your MongoDB deployment's connection string
        String uri = "mongodb://root:root@" 
        		+ Optional.ofNullable(System.getenv("MONGO_SERVER")).orElse("localhost")
                + ":27017/ssdd?authSource=admin";

        collection = Lazy.lazily(() -> 
        {
        	MongoClient mongoClient = MongoClients.create(uri);
        	MongoDatabase database = mongoClient
        		.getDatabase(Optional.ofNullable(System.getenv("DB_NAME")).orElse("ssdd"))
        		.withCodecRegistry(pojoCodecRegistry);
        	return database.getCollection("users", User.class);
        });
    }

    @Override
    public Optional<User> getUserById(String id)
    {
        Optional<User> user = Optional.ofNullable(collection.get().find(eq("id", id)).first());
        return user;
    }

    @Override
    public Optional<User> getUserByEmail(String id)
    {
        Optional<User> user = Optional.ofNullable(collection.get().find(eq("email", id)).first());
        return user;
    }
    
    /** modificado por kholoud*/
    // inserta usuario en BD
    
    // FALTA MODIFICAR USUARIO PASSWORD
    @Override
   public boolean insertUser(String email, String name, String password) {
        try {
            User user = new User(email, password, name, UUID.randomUUID().toString(),0); // Crear objeto User con los datos proporcionados
            collection.get().insertOne(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** modificado por kholoud*/
    // elimina usuario de BD dado un ID
    @Override
    public boolean deleteUser(String id) {
        try {
            collection.get().deleteOne(eq("_id", id));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /** modificado por kholoud*/
    // actualiza datos del usuario en BD 
    @Override
    public boolean updateUser(User user) {
        try {
            Document filter = new Document("_id", user.getId());
            Document update = new Document("$set", new Document()
                .append("email", user.getEmail())
                .append("password_hash", user.getPassword_hash())
                .append("name", user.getName())
                .append("token", user.getToken())
                .append("visits", user.getVisits())
            );
            com.mongodb.client.result.UpdateResult result = collection.get().updateOne(filter, update);
            return result.getModifiedCount() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**modificado por kholoud*/
	@Override
	public void addVisits(String id) {
		// TODO Auto-generated method stub
		// obtener usuario y modificar su numero de visitas en la base de datos
		try {
	        Document filter = new Document("_id", id);
	        Document update = new Document("$inc", new Document("visits", 1));
	        collection.get().updateOne(filter, update);
	        //return result.getModifiedCount() > 0;
	    } catch (Exception e) {
	        //return false;
	    }
	}
    
    
}
