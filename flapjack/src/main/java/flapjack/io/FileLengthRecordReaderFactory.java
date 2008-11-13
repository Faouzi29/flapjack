package flapjack.io;

import java.io.File;

/**
 * At runtime determines what implementation of the RecordReader should be used based on the file to be processed.
 * <p/>
 * If the record is large it will used the MappedRecordReader otherwise it will use the NioRecordReader.
 * <p/>
 * The default small file limit is 50 Mb.
 */
public class FileLengthRecordReaderFactory implements RecordReaderFactory {
    private static final int DEFAULT_SMALL_FILE_LIMIT = 50;
    private static final long MEGABYTE = 1024L * 1024L;

    private NioRecordReaderFactory nioFactory;
    private MappedRecordReaderFactory mappedFactory;
    private int smallFileLimit = DEFAULT_SMALL_FILE_LIMIT;

    public FileLengthRecordReaderFactory(int recordLength) {
        nioFactory = new NioRecordReaderFactory(recordLength);
        mappedFactory = new MappedRecordReaderFactory(recordLength);
    }

    public RecordReader build(File file) {
        if (getFileLength(file) <= smallFileLimit) {
            return nioFactory.build(file);
        }
        return mappedFactory.build(file);
    }

    protected int getFileLength(File file) {
        return convertToMegaBytes(file.length());
    }


    private int convertToMegaBytes(long bytes) {
        return (int) (bytes / MEGABYTE);
    }

    /**
     * The limit on file size to determine what implementation should be used for RecordReader.
     *
     * @param smallFileLimit - number of MegaBytes
     */
    public void setSmallFileLimit(int smallFileLimit) {
        this.smallFileLimit = smallFileLimit;
    }
}