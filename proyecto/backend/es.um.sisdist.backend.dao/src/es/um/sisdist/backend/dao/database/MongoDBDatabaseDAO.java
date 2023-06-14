package es.um.sisdist.backend.dao.database;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static java.util.Arrays.*;
import java.util.ArrayList;

import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import es.um.sisdist.backend.dao.models.DataBase;
import es.um.sisdist.backend.dao.utils.Lazy;

public class MongoDBDatabaseDAO implements IDatabaseDAO {

	private Supplier<MongoCollection<DataBase>> collection;

	public MongoDBDatabaseDAO() {
		CodecProvider pojoCodecProvider = PojoCodecProvider.builder()
				.conventions(asList(Conventions.ANNOTATION_CONVENTION)).automatic(true).build();
		CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));

		// falta mirar URL !!!!!!!!!!!!!!!!!!!!!!!!!!!!
		String uri = "mongodb://root:root@" + Optional.ofNullable(System.getenv("MONGO_SERVER")).orElse("localhost")
				+ ":27017/ssdd?authSource=admin";

		collection = Lazy.lazily(() -> {
			MongoClient mongoClient = MongoClients.create(uri);
			MongoDatabase database = mongoClient
					.getDatabase(Optional.ofNullable(System.getenv("DB_NAME")).orElse("ssdd"))
					.withCodecRegistry(pojoCodecRegistry);
			return database.getCollection("databases", DataBase.class);
		});
	}

	@Override
	public boolean deleteDatabase(String databaseName) {
		try {
			collection.get().deleteOne(eq("name", databaseName));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// retorna db insertada con anterioridad
	@Override
	public DataBase getDatabase(String databaseName) {
		try {
			MongoCollection<DataBase> mongoCollection = collection.get(); // Obtener la colección de la base de datos
			DataBase result = mongoCollection.find(eq("name", databaseName)).first();

			return result;
		} catch (Exception e) {
		}

		return null;
	}

	// añade una nueva entrada clave valor al mapa de pares<k,v>
	@Override
	public void addClaveValor(String db, String clave, String valor) {
		try {
			DataBase database = getDatabase(db);
			if (database != null) {
				// Obtener el mapa de pares clave-valor de la base de datos
				HashMap<String, String> paresDb = database.getPares();
				paresDb.put(clave, valor);
				StringBuilder sb = new StringBuilder();

				for (Map.Entry<String, String> entry : paresDb.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					sb.append(key).append(":").append(value).append(",");
				}

				// Eliminar la última coma
				if (sb.length() > 0) {
					sb.deleteCharAt(sb.length() - 1);
				}

				// Crear el filtro para actualizar el documento
				Bson filter = Filters.eq("name", db);

				// Update para el campo pares con la cadena de pares actualizada
				Bson update = Updates.set("pares", sb.toString());

				// com.mongodb.client.result.UpdateResult result =
				// collection.get().updateOne(filter, update);
				collection.get().updateOne(filter, update);
			}
		} catch (Exception e) {
		}
	}

	// eliminar un par<k,v>
	@Override
	public void deleteClaveValor(String db, String clave) {
		try {
			// Obtener la instancia de la base de datos
			DataBase database = getDatabase(db);

			if (database != null) {
				// Obtener el mapa de pares clave-valor de la base de datos
				HashMap<String, String> pares = database.getPares();

				// Eliminar la clave especificada del mapa de pares
				pares.remove(clave);

				// Convertir el mapa a una representación de cadena
				StringBuilder sb = new StringBuilder();

				for (Map.Entry<String, String> entry : pares.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					sb.append(key).append(":").append(value).append(",");
				}

				// Eliminar la última coma
				if (sb.length() > 0) {
					sb.deleteCharAt(sb.length() - 1);
				}

				Bson filter = Filters.eq("_id", database.getId());

				Bson update = Updates.set("pares", sb.toString());

				collection.get().updateOne(filter, update);

			}
		} catch (Exception e) {
		}
	}

	@Override
	public void getValues() {
		// Obtiene lista de todos los pares Clave,Valor de la base de datos

		// TODO Auto-generated method stub

	}

	@Override
	public boolean insertDatabase(String db, String idUser) {
		try {
			DataBase database = new DataBase(db); // Crear objeto DataBase con el nombre db
			database.setId(UUID.randomUUID().toString()); // Generar un ID único para la base de datos
			database.setIdUser(idUser); // Asignar el ID del usuario asociado

			collection.get().insertOne(database);

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// obtener todas las db que esten relacionados con un usuario
	@Override
	public ArrayList<DataBase> getDatabases(String userId) {
		try {
	        MongoCollection<DataBase> mongoCollection = collection.get(); // Obtener la colección de la base de datos
	        ArrayList<DataBase> result = new ArrayList<DataBase>();

	        mongoCollection.find(eq("idUser", userId)).forEach(db -> {
	            result.add(db);
	        });

	        return result;
	    } catch (Exception e) {
	    }

	    return null;
	}	
	

}
