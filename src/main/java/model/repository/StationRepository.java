package model.repository;

import model.dao.StationDao;
import model.dao.StationNlDao;
import model.dto.StationDto;
import model.exception.RepositoryException;

import java.util.List;

public class StationRepository implements Repository<Integer, StationDto> {


    private StationDao daoFr;
    private StationNlDao daoNl;


    public StationRepository() throws RepositoryException{
        daoFr = StationDao.getInstance();
        daoNl = StationNlDao.getInstance();
    }

    @Override
    public Integer add(StationDto item) throws RepositoryException {
        return null;
    }

    @Override
    public void remove(Integer key) throws RepositoryException {

    }

    @Override
    public List<StationDto> getAll() throws RepositoryException {
        return daoFr.selectAll();
    }



    public List<StationDto> getAllNl() throws RepositoryException {
        return daoNl.selectAll();
    }


    public StationDto completeStation(Integer key) throws RepositoryException{
         return daoFr.completeStation(key);
    }

    public StationDto completeStationNl(Integer key) throws RepositoryException{
        return daoNl.completeStation(key);
    }

    @Override
    public StationDto get(Integer key) throws RepositoryException {
        return daoFr.select(key);
    }

    public StationDto getNl(Integer key) throws RepositoryException {
        return daoNl.select(key);
    }


    @Override
    public boolean contains(Integer key) throws RepositoryException {
        return false;
    }
}
