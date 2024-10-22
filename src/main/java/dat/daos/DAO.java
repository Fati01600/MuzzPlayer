package dat.daos;

import dat.dtos.RoomDTO;

import java.util.List;

public abstract class DAO<T> implements IDAO<T> {

    @Override
    public List<T> getAll() {
        return List.of();
    }

    @Override
    public T getById(int id) {
        return null;
    }

    @Override
    public List<T> createFromList(T[] entityArray) {
        return List.of();
    }

    @Override
    public T create(T entity) {
        return null;
    }

    @Override
    public T update(int id, T entity) {
        return null;
    }

    @Override
    public void delete(int id) {
    }
}
