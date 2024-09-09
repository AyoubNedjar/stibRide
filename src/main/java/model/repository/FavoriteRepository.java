package model.repository;

import model.dao.FavoriteDao;
import model.dto.FavoriteDto;
import model.exception.RepositoryException;

import java.util.List;

public class FavoriteRepository implements Repository<String, FavoriteDto>{

    private FavoriteDao dao;

    public FavoriteRepository() throws RepositoryException {
        dao = FavoriteDao.getInstance();
    }

    @Override
    public String add(FavoriteDto item) throws RepositoryException {
        return dao.insert(item);
    }

    @Override
    public void remove(String key) throws RepositoryException {
        dao.delete(key);
    }

    @Override
    public List<FavoriteDto> getAll() throws RepositoryException {
        return dao.selectAll();
    }

    @Override
    public FavoriteDto get(String key) throws RepositoryException {
        return dao.select(key);
    }

    @Override
    public boolean contains(String key) throws RepositoryException {
        return false;
    }
}
