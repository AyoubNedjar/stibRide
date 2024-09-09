package model.dto;

import javafx.util.Pair;

public class StopDto extends Dto<Pair<Integer, Integer>> {

    private int order;


    /**
     * Creates a new instance of <code>Dto</code> with the key of the data.
     *
     * @param idLine & idStation key of the data.
     */
    public StopDto(Integer idLine, Integer idStation, int order) {
        super(new Pair<>(idLine, idStation));
        this.order = order;
    }

    public int getIdLine(){
        return this.getKey().getKey();
    }

    public int getIdStation(){
        return this.getKey().getValue();
    }

    public int getOrder() {
        return order;
    }


    @Override
    public String toString() {
        return "StopDto{" +
                "order=" + order +
                ", key=" + key +
                '}';
    }
}
