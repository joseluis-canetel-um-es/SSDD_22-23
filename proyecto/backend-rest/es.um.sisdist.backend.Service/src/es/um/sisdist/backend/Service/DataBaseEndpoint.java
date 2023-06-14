package es.um.sisdist.backend.Service;

import es.um.sisdist.backend.Service.impl.AppLogicImpl;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/u/{id}/db")
// endpoint de bases de datos
/**
 * 
 * En el cuerpo de la petición se incluirá el nombre de la base de datos y otros
 * parámetros que se necesiten, así como unos datos iniciales que introducir en
 * la base de datos, recordad, cada entrada tiene una clave y un valor
 */
public class DataBaseEndpoint {
	private AppLogicImpl impl = AppLogicImpl.getInstance();


	// metodo para que el usuario pueda crear bases de datos
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createDatabase(@PathParam("id") String userId, String databaseName) {
		// obtener los datos iniciales de databaseRequest.getD()

		// crear la base de datos
		impl.createDatabase(databaseName, userId);
		// Construye la URL de la base de datos
		String databaseUrl = "/u/" + userId + "/db/" + databaseName;

		// Construye la respuesta con el código HTTP 201 Created y la cabecera Location
		return Response.status(Response.Status.CREATED).header("Location", databaseUrl).build();
	}

	// metodo consulta de bases de datos
	@GET
    @Path("/{name}")
    public Response getDatabase(@PathParam("id") String userId, @PathParam("name") String databaseName) {
        impl.getDatabase(databaseName);

        return Response.ok().build();
    }
	
	
	@DELETE
	@Path("/{dbName}")
	public Response deleteDatabase(@PathParam("id") String userId, @PathParam("dbName") String databaseName) {
	    // Verificar si la base de datos existe y pertenece al usuario
	    //if (impl.isDatabaseOwnedByUser(databaseName, userId)) {
	        // Eliminar la base de datos
	        boolean deleted = impl.deleteDatabase(userId, databaseName);

	        if (deleted) {
	            return Response.ok().build(); // Respuesta HTTP 200 OK si se eliminó correctamente
	        } else {
	            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build(); // Respuesta HTTP 500 Internal Server Error si hubo un error al eliminar
	        }
	    
	}

}
