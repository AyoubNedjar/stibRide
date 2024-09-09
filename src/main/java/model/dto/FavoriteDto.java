package model.dto;

public class FavoriteDto extends Dto<String> {

    private final StationDto source;
    private final StationDto destination;

    /**
     * Creates a new instance of <code>Dto</code> with the key of the data.
     *
     * @param key         key of the data.
     * @param source
     * @param destination
     */
    public FavoriteDto(String key, StationDto source, StationDto destination) {
        super(key);
        this.source = source;
        this.destination = destination;
    }

    public String getKey() {
        return super.key;
    }

    public StationDto getSource() {
        return source;
    }

    public StationDto getDestination() {
        return destination;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoriteDto that)) return false;
        if (!super.equals(o)) return false;

        if (getSource() != null ? !getSource().equals(that.getSource()) : that.getSource() != null) return false;
        return getDestination() != null ? getDestination().equals(that.getDestination()) : that.getDestination() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getSource() != null ? getSource().hashCode() : 0);
        result = 31 * result + (getDestination() != null ? getDestination().hashCode() : 0);
        return result;
    }
}
