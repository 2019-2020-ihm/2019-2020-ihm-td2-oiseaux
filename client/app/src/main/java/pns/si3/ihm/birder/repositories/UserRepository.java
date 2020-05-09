package pns.si3.ihm.birder.repositories;

public interface UserRepository {
	void getUsers();
	void getUser(String id);
	void createUser(String firstName, String lastName, String email, String password);
	void updateUser(String id, String firstName, String lastName, String email, String password);
	void removeUser(String id);
}
