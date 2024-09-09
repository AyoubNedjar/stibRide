package model.repository;

import javafx.util.Pair;
import model.dao.StopDao;
import model.dto.StopDto;
import model.exception.RepositoryException;

import java.util.List;

public class StopRepository implements Repository<Pair<Integer, Integer>, StopDto>{

    private StopDao dao;

    public StopRepository() throws RepositoryException {
        dao = StopDao.getInstance();
    }


    @Override
    public Pair<Integer, Integer> add(StopDto item) throws RepositoryException {
        return null;
    }

    @Override
    public void remove(Pair<Integer, Integer> key) throws RepositoryException {

    }

    @Override
    public List<StopDto> getAll() throws RepositoryException {
        return dao.selectAll();
    }

    @Override
    public StopDto get(Pair<Integer, Integer> key) throws RepositoryException {
        return dao.select(key);
    }

    public List<StopDto> getStopsLine(Integer line) throws RepositoryException{
        return dao.selectLine(line);
    }

    @Override
    public boolean contains(Pair<Integer, Integer> key) throws RepositoryException {
        return false;
    }

    public List<StopDto> getAdjStop(Pair<Integer, Integer> key) throws RepositoryException {
        var list = dao.getAdjPreviousStop(key);
        list.addAll(dao.getAdjNextStop(key));
        return list;
    }
}
