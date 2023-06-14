package es.um.sisdist.backend.dao.models;

import java.util.HashMap;

/**
 * 
 * Clase usada para representar las propiedades de una base de datos
 */
public class DataBase {
    private String id;
	private String name;
	private String idUser; // relacionar el id de usuario con la db
	//private ArrayList<Value> table; // valores que almacena la db
	
	/**
	 * Las claves y los valores pueden ser de tres tipos:
		Números enteros, si no tienen decimales
		Números en coma flotante, si tienen decimales
		Cadenas arbitrarias de caracteres
		??????????????????????????????????????????????????

	 */
	private HashMap<String, String> pares; // pares clave valor

	public DataBase(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<String, String> getPares() {
		return pares;
	}

	public void setPares(HashMap<String, String> pares) {
		this.pares = pares;
	}
	
	// Añadir un par clave valor a la base de datos
	public void addPar(String key, String value) {
		this.pares.put(key, value);
	}
	// eliminar un par clave valor de la base de datos
	public void deletePar(String key) {
		this.pares.remove(key);
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	
	

}
