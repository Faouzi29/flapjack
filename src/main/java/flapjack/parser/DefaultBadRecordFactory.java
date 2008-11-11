package flapjack.parser;

public class DefaultBadRecordFactory implements BadRecordFactory {
    public BadRecord build(byte[] record) {
        return new BadRecord(record);
    }

    public BadRecord build(byte[] record, ParseException err) {
        return new BadRecord(record, err);
    }
}
