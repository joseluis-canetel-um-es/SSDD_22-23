package es.um.sisdist.models;

import es.um.sisdist.backend.dao.models.DataBase;

public class DatabaseDTOUtils {
	
	// get DataBase Object
	public static DataBase fromDTO(DatabaseDTO dto) {
		DataBase database = new DataBase(dto.getName());
		database.setId(dto.getId());
		database.setPares(dto.getPares());
		return database;
	}

	// get DatabaseDTO
	public static DatabaseDTO toDTO(DataBase database) {
		DatabaseDTO dto = new DatabaseDTO();
		dto.setId(database.getId());
		dto.setName(database.getName());
		dto.setPares(database.getPares());
		return dto;
	}


}
