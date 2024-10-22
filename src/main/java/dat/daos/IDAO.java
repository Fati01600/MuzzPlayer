package dat.daos;

import java.util.List;

public interface IDAO<T> {

    // Fetch all objects of type T
    List<T> getAll();

    // Fetch a single object by its ID
    T getById(int id);

    // Create a arraylist of new object of type T
    List<T> createFromList(T[] entityArray);

    // Create a new object of type T
    T create(T entity);

    // Update an existing object of type T
    T update(int id, T entity);

    // Delete an object by its ID
    void delete(int id);
}
