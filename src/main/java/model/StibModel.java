package model;

import algorithms.Dijkstra;
import algorithms.Graph;
import algorithms.Node;
import model.config.ConfigManager;
import model.dto.FavoriteDto;
import model.dto.StationDto;
import model.exception.RepositoryException;
import model.repository.FavoriteRepository;
import model.repository.StationRepository;
import model.repository.StopRepository;
import model.util.Observable;
import model.util.Observer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StibModel extends Observable {

    private static Graph graph = new Graph();

    private StopRepository stopRepository;

    private Map<Integer, Node> stationsMap;

   // private Map<Integer, Node> stationsMapNl;

    private StationRepository stationRepository;

    private Language language;

    private List<StationDto> listStation;

   // private List<StationDto> listStationNl;

    private FavoriteRepository favoriteRepository;

    private List<StationDto> shortestPath;


    public StibModel() {
        try {
            ConfigManager.getInstance().load();
            String dbUrl = ConfigManager.getInstance().getProperties("db.url");
            System.out.println("Base de données stockée : " + dbUrl);

            stationRepository = new StationRepository();
            stopRepository = new StopRepository();
            favoriteRepository = new FavoriteRepository();

            stationsMap = new HashMap<>();
            //stationsMapNl = new HashMap<>();

            shortestPath = new ArrayList<>();

            listStation = new ArrayList<>();

            initialisationLanguage(Language.FR);

        } catch (IOException ex) {
            System.out.println("Erreur IO " + ex.getMessage());
        } catch (RepositoryException ex) {
            System.out.println("Erreur Repository " + ex.getMessage());
        }
    }

    public void changeLangage() throws RepositoryException {
        var listInter = new ArrayList<>(listStation);
        listStation.clear();

        if(language == Language.FR){
            for(var station : listInter){
               // var s = stationRepository.getNl(station.getKey());
                StationDto completedStation = stationRepository.completeStationNl(station.getKey());
                listStation.add(completedStation);
            }

            var listShortestPath = new ArrayList<>(shortestPath);
            shortestPath.clear();
            for(var station : listShortestPath){
                StationDto completedStation = stationRepository.completeStationNl(station.getKey());
                shortestPath.add(completedStation);
            }

            language = Language.NL;
        }else{
            for(var station : listInter){
                StationDto completedStation = stationRepository.completeStation(station.getKey());
                listStation.add(completedStation);
            }
            var listShortestPath = new ArrayList<>(shortestPath);
            shortestPath.clear();
            for(var station : listShortestPath){
                StationDto completedStation = stationRepository.completeStation(station.getKey());
                shortestPath.add(completedStation);
            }
            language = Language.FR;
        }
        if(!shortestPath.isEmpty()){

        }
        //Mettre a jour le shortestPAth
        //makeGraph(listStation.get(0).getKey());
        notifyObservers();
    }


    public void initialisationLanguage(Language lang) throws RepositoryException {
        listStation.clear();
        if(lang == Language.FR){
           // System.out.println("TEst");
            listStation = stationRepository.getAll();
            for (int i = 0; i < listStation.size(); i++) {
                StationDto station = listStation.get(i);
                StationDto completedStation = stationRepository.completeStation(station.getKey());
                listStation.set(i, completedStation); // Remplace l'objet dans la liste

            }
            language = Language.FR;
        }else{
            System.out.println("initialisation NL");
            listStation = stationRepository.getAllNl();
            for (int i = 0; i < listStation.size(); i++) {
                StationDto station = listStation.get(i);
                StationDto completedStation = stationRepository.completeStationNl(station.getKey());
                listStation.set(i, completedStation); // Remplace l'objet dans la liste

            }
            language = Language.NL;
        }
        makeGraph(listStation.get(0).getKey());
        notifyObservers();

    }

    public void shortestPath(int source, int destination) throws RepositoryException {
        makeGraph(source);

        shortestPath.clear();
        for(var s : stationsMap.get(destination).getShortestPath()){
            shortestPath.add(s.getStationDto());
        }
        shortestPath.add(stationsMap.get(destination).getStationDto());

        notifyObservers();
    }


    public List<StationDto> getStations() {
        System.out.println("get Dans le model");
        return listStation;
    }

    public List<StationDto> getShortestPath() {
        return shortestPath;
    }

    public StationDto getStation(int station){
       return stationsMap.get(station).getStationDto();
    }

    public List<FavoriteDto> getFavorites() throws RepositoryException{
        return favoriteRepository.getAll();
    }

    public Map<Integer, Node> getStationsMap() {
        return stationsMap;
    }

    public void addFavorite(FavoriteDto favoriteDto) throws RepositoryException {
        if(!getFavorites().contains(favoriteDto)){
            favoriteRepository.add(favoriteDto);
            notifyObservers();
        }
    }

    public void deleteFav(String name) throws RepositoryException {
        System.out.println("deleteModel");
        favoriteRepository.remove(name);
        notifyObservers();

    }

    public FavoriteDto getFavorite(String id) throws RepositoryException{
       return favoriteRepository.get(id);
    }

    @Override
    public void notifyObservers() throws RepositoryException {
        super.notifyObservers();
    }

    @Override
    public void addObserver(Observer observer) {
        super.addObserver(observer);
    }

    private void makeGraph(int source) throws RepositoryException {
        //Tous les stops
        var listStops = stopRepository.getAll();

        listStation.forEach(station ->
                stationsMap.put(station.getKey(), new Node(station)));

        /*
        listStationNl.forEach(station ->
                stationsMapNl.put(station.getKey(), new Node(station)));
         */


        for (var stop : listStops) {
            var adjCurrStation = stopRepository.getAdjStop(stop.getKey());
            for (var adj : adjCurrStation) {
                //TODO OPTIMISATION DU CODE ET NE PAS AJOUTER PLUSIEURS FOIS
                stationsMap.get(stop.getIdStation()).addDestination(stationsMap.get(adj.getIdStation()), 1);
                //stationsMapNl.get(stop.getIdStation()).addDestination(stationsMapNl.get(adj.getIdStation()), 1);

            }
        }

        /*
        CAS EXCEPTIONNEL SIMONIS ET ELISABETH
         */
        stationsMap.get(8764).addDestination(stationsMap.get(8472), 0);
        stationsMap.get(8472).addDestination(stationsMap.get(8764), 0);


        //Ajouter toutes les stations au graphe
        for (var i : stationsMap.entrySet()) {
            graph.addNode(i.getValue());
        }

        graph = Dijkstra.calculateShortestPathFromSource(graph, stationsMap.get(source));

    }

    public Language getLanguage() {
        return language;
    }
}
