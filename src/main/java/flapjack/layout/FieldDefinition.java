package flapjack.layout;

public interface FieldDefinition {
    /**
     * The location of the field in the record
     * <b>Zero based</b>
     *
     * @return
     */
    int getPosition();

    /**
     * The length of the field in the record
     *
     * @return
     */
    int getLength();

    /**
     * Documentary value
     *
     * @return
     */
    String getName();
}
