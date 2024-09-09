package model.dto;

import java.util.ArrayList;
import java.util.List;

public class StationDto extends Dto<Integer>{

    private String name;
    private List<StopDto> stops;

    /**
     * Creates a new instance of <code>Dto</code> with the key of the data.
     *
     * @param key    key of the data.
     * @param name
     */
    public StationDto(Integer key, String name) {
        super(key);
        this.name = name;
        stops = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<StopDto> getStops() {
        return stops;
    }

    public void setStops(List<StopDto> stops) {
        this.stops = stops;
    }

    public void addStop(StopDto stop){
        stops.add(stop);
    }

    public String getLines() {
        if (stops == null || stops.isEmpty()) {
            return "[]";
        }

        StringBuilder stationLines = new StringBuilder("[");
        for (int i = 0; i < stops.size(); i++) {
            stationLines.append(stops.get(i).getIdLine());
            if (i < stops.size() - 1) { // Check if not the last item
                stationLines.append("-");
            }
        }
        stationLines.append("]");
        return stationLines.toString();
    }
}
