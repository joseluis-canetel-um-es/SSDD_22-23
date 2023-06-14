package es.um.sisdist.backend.Service;

import es.um.sisdist.backend.Service.impl.AppLogicImpl;
import es.um.sisdist.models.UserDTO;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

//verifica el signup de usuario

@Path("/checkSignup")
public class CheckSignupEndpoint {
	private AppLogicImpl impl = AppLogicImpl.getInstance();

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerUser(UserDTO userDTO) {
		// Verificar si el usuario ya existe en la base de datos
		if (impl.getUserByEmail(userDTO.getEmail()).isPresent()) {
			// Usuario ya registrado, devolver estado de conflicto (409)
			return Response.status(Status.CONFLICT).build();
		}

		// Crear un nuevo objeto de usuario
		// User user = new User(userDTO.getEmail(), userDTO.getName(),
		// userDTO.getPassword(), 0);
		// Guardar el usuario en la base de datos
		impl.signup(userDTO.getEmail(), userDTO.getName(), userDTO.getPassword());

		// Devolver una respuesta ok
		return Response.ok().build();
	}

}
