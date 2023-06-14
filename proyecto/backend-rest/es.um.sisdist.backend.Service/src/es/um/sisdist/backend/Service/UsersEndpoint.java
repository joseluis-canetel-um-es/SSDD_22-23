package es.um.sisdist.backend.Service;

import java.util.ArrayList;

import jakarta.ws.rs.core.Response.Status;

import es.um.sisdist.backend.Service.impl.AppLogicImpl;
import es.um.sisdist.backend.dao.models.DataBase;
import es.um.sisdist.models.DatabaseDTO;
import es.um.sisdist.models.DatabaseDTOUtils;
import es.um.sisdist.models.UserDTO;
import es.um.sisdist.models.UserDTOUtils;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/u")
public class UsersEndpoint
{
    private AppLogicImpl impl = AppLogicImpl.getInstance();

    /** punto de entrada para una solicitud GET a la ruta "/u/{username}",
     * donde "{username}" es un parámetro de ruta que representa el nombre de usuario 
     del usuario del que se desea obtener información.
    */
    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public UserDTO getUserInfo(@PathParam("username") String username)
    {
        return UserDTOUtils.toDTO(impl.getUserByEmail(username).orElse(null));
    }
    
    @GET
    @Path("/{id}/db")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDatabasesUser(@PathParam ("id") String userID) {
    	ArrayList<DataBase> databases = impl.getDatabasesByUserId(userID);
    	if(databases != null) {
    		ArrayList<DatabaseDTO> databasesDTO = new ArrayList<DatabaseDTO>();
    		
    		for( DataBase db : databases ) {
    			databasesDTO.add( DatabaseDTOUtils.toDTO(db) );
    		}
    		
    		return Response.ok(databasesDTO).build();  
    	}else {
    		// contenido vacio
    		return Response.status(Status.NO_CONTENT).build(); 

    	}
    }
}
