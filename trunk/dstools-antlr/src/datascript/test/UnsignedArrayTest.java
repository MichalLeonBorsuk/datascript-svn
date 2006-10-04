/**
 * 
 */
package datascript.test;

import java.io.File;
import java.io.IOException;

import javax.imageio.stream.FileImageOutputStream;

import junit.framework.TestCase;
import datascript.runtime.ShortArray;
import datascript.runtime.UnsignedShortArray;

/**
 * @author HWellmann
 *
 */
public class UnsignedArrayTest extends TestCase
{
    private FileImageOutputStream os;
    private String fileName = "unsignedarraytest.bin";
    private File file = new File(fileName);

    /**
     * Constructor for BitStreamReaderTest.
     * @param name
     */
    public UnsignedArrayTest(String name)
    {
        super(name);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        file.delete();
    }

    /*
     * Test method for 'datascript.library.BitStreamReader.readByte()'
     */
    private void writeArray(int numElems, int startSigned, int startUnsigned) throws IOException
    {
        file.delete();
        os = new FileImageOutputStream(file);
        os.writeShort(numElems);

        for (int i = startSigned; i < startSigned+numElems; i++)
        {
            os.writeShort(i);
        }
        for (int i = startUnsigned; i < startUnsigned+numElems; i++)
        {
            os.writeShort(i);
        }
        os.close();

        bits.IntegerArray array = new bits.IntegerArray(fileName);
        assertEquals(numElems, array.getNumElems());
        ShortArray signed = array.getIntList();
        for (int i = 0; i < numElems; i++)
        {            
            assertEquals(startSigned+i, signed.elementAt(i));
        }
        UnsignedShortArray unsigned = array.getUintList();
        for (int i = 0; i < numElems; i++)
        {            
            assertEquals(startUnsigned+i, unsigned.elementAt(i));
        }
    }

    public void testArray1() throws IOException
    {
        writeArray(5, 2, 3);
    }

    public void testArray2() throws IOException
    {
        writeArray(5, -100, 100);
    }

    public void testArray3() throws IOException
    {
        writeArray(10, -30000, 30000);
    }
    public void testArray4() throws IOException
    {
        writeArray(10, -30000, 60000);
    }
}
