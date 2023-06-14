package es.um.sisdist.backend.dao.user;

import java.util.Optional;

import es.um.sisdist.backend.dao.models.User;

public interface IUserDAO
{
    public Optional<User> getUserById(String id);

    public Optional<User> getUserByEmail(String id);
 /** modificado por kholoud*/
    
    public void addVisits(String id); // incrementa el numero de visitas
    public boolean insertUser(String email, String name, String password);
    public boolean deleteUser(String id);
    public boolean updateUser(User user);
 
}
