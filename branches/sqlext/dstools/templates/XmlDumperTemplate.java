package datascript.templates;

import datascript.*;
import java.util.Iterator;

public class XmlDumperTemplate
{
  protected static String nl;
  public static synchronized XmlDumperTemplate create(String lineSeparator)
  {
    nl = lineSeparator;
    XmlDumperTemplate result = new XmlDumperTemplate();
    nl = null;
    return result;
  }

  protected final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "package ";
  protected final String TEXT_2 = ";" + NL + "" + NL + "" + NL + "import datascript.runtime.*;" + NL + "" + NL + "import java.math.BigInteger;" + NL + "import java.util.Stack;" + NL + "" + NL + "import org.xml.sax.ContentHandler;" + NL + "import org.xml.sax.InputSource;" + NL + "import org.xml.sax.SAXException;" + NL + "import org.xml.sax.helpers.AttributesImpl;" + NL + "import org.xml.sax.helpers.XMLFilterImpl;" + NL + "" + NL + "public class __XmlDumper extends XMLFilterImpl implements __Visitor" + NL + "{" + NL + "    private static String NO_LITTLE_ENDIAN_TYPES = " + NL + "    \t\"Little endian types not supported\";" + NL + "" + NL + "    private ContentHandler handler;" + NL + "    private AttributesImpl noAttr = new AttributesImpl();" + NL + "    private Stack<String> nameStack = new Stack<String>();" + NL + "    private __Visitor.Acceptor acceptor;" + NL + "    " + NL + "    public __XmlDumper(__Visitor.Acceptor acceptor)" + NL + "    {" + NL + "        this.acceptor = acceptor;" + NL + "    }" + NL + "    " + NL + "    private void startElement() throws SAXException" + NL + "    {" + NL + "        String tag = nameStack.peek();" + NL + "        handler.startElement(\"\", tag, tag, noAttr);" + NL + "    }" + NL + "    " + NL + "    private void endElement() throws SAXException" + NL + "    {" + NL + "        String tag = nameStack.pop();" + NL + "        handler.endElement(\"\", tag, tag);" + NL + "    }" + NL + "    " + NL + "    private void text(String s) throws SAXException" + NL + "    {" + NL + "        handler.characters(s.toCharArray(), 0, s.length());" + NL + "    }" + NL + "    " + NL + "    public void parse(InputSource is) throws SAXException" + NL + "    {" + NL + "        handler = getContentHandler();" + NL + "        handler.startDocument();" + NL + "        nameStack.push(\"";
  protected final String TEXT_3 = "\");" + NL + "        acceptor.accept(this);" + NL + "        handler.endDocument();" + NL + "    }" + NL + "    ";
  protected final String TEXT_4 = NL;
  protected final String TEXT_5 = NL + "    public void visitArray(ObjectArray n)" + NL + "    {" + NL + "    \tString tag = nameStack.peek();" + NL + "    \tint last = n.length()-1;" + NL + "        for (int i = 0; i < last; i++) " + NL + "        {" + NL + "      \t    ((__Visitor.Acceptor)(n.elementAt(i))).accept(this);" + NL + "      \t    nameStack.push(tag);" + NL + "      \t}" + NL + "      \t((__Visitor.Acceptor)(n.elementAt(last))).accept(this);" + NL + "    }" + NL + "\t" + NL + "    public void visitArray(ByteArray n)" + NL + "    {" + NL + "    \tString tag = nameStack.peek();" + NL + "    \tint last = n.length()-1;" + NL + "        for (int i = 0; i < last; i++) " + NL + "        {" + NL + "            visitint8(n.elementAt(i));\t" + NL + "      \t    nameStack.push(tag);" + NL + "      \t}" + NL + "        visitint8(n.elementAt(last));" + NL + "    }" + NL + "\t" + NL + "    public void visitArray(ShortArray n)" + NL + "    {" + NL + "    \tString tag = nameStack.peek();" + NL + "    \tint last = n.length()-1;" + NL + "        for (int i = 0; i < last; i++) " + NL + "        {" + NL + "            visitint16(n.elementAt(i));\t" + NL + "      \t    nameStack.push(tag);" + NL + "      \t}" + NL + "        visitint16(n.elementAt(last));" + NL + "    }" + NL + "\t" + NL + "    public void visitArray(IntArray n)" + NL + "    {" + NL + "    \tString tag = nameStack.peek();" + NL + "    \tint last = n.length()-1;" + NL + "        for (int i = 0; i < last; i++) " + NL + "        {" + NL + "            visitint32(n.elementAt(i));\t" + NL + "      \t    nameStack.push(tag);" + NL + "      \t}" + NL + "        visitint32(n.elementAt(last));" + NL + "    }" + NL + "\t" + NL + "    public void visitArray(LongArray n)" + NL + "    {" + NL + "    \tString tag = nameStack.peek();" + NL + "    \tint last = n.length()-1;" + NL + "        for (int i = 0; i < last; i++) " + NL + "        {" + NL + "            visitint64(n.elementAt(i));\t" + NL + "      \t    nameStack.push(tag);" + NL + "      \t}" + NL + "        visitint64(n.elementAt(last));" + NL + "    }" + NL + "\t" + NL + "    public void visitArray(BitFieldArray n)" + NL + "    {" + NL + "    \tString tag = nameStack.peek();" + NL + "    \tint last = n.length()-1;" + NL + "        for (int i = 0; i < last; i++) " + NL + "        {" + NL + "            visitint64(n.elementAt(i));\t" + NL + "      \t    nameStack.push(tag);" + NL + "      \t}" + NL + "        visitint64(n.elementAt(last));" + NL + "    }" + NL + "\t" + NL + "    public void visitint8(byte n)" + NL + "    {" + NL + "        try" + NL + "        {" + NL + "            startElement();" + NL + "            text(Short.toString(n));" + NL + "            endElement();" + NL + "        }" + NL + "        catch(SAXException exc)" + NL + "        {" + NL + "            exc.printStackTrace();" + NL + "        }" + NL + "    " + NL + "    }" + NL + "" + NL + "    public void visitint16(short n)" + NL + "    {" + NL + "        try" + NL + "        {" + NL + "            startElement();" + NL + "            text(Short.toString(n));" + NL + "            endElement();" + NL + "        }" + NL + "        catch(SAXException exc)" + NL + "        {" + NL + "            exc.printStackTrace();" + NL + "        }" + NL + "    }" + NL + "" + NL + "    public void visitint32(int n)" + NL + "    {" + NL + "        try" + NL + "        {" + NL + "            startElement();" + NL + "            text(Integer.toString(n));" + NL + "            endElement();" + NL + "        }" + NL + "        catch(SAXException exc)" + NL + "        {" + NL + "            exc.printStackTrace();" + NL + "        }" + NL + "    }" + NL + "" + NL + "    public void visitint64(long n)" + NL + "    {" + NL + "        try" + NL + "        {" + NL + "            startElement();" + NL + "            text(Long.toString(n));" + NL + "            endElement();" + NL + "        }" + NL + "        catch(SAXException exc)" + NL + "        {" + NL + "            exc.printStackTrace();" + NL + "        }" + NL + "    }" + NL + "" + NL + "    public void visitleint16(short n)" + NL + "    {" + NL + "    }" + NL + "" + NL + "    public void visitleint32(int n)" + NL + "    {" + NL + "    }" + NL + "" + NL + "    public void visitleint64(long n)" + NL + "    {" + NL + "    }" + NL + "" + NL + "    public void visituint8(short n)" + NL + "    {" + NL + "        try" + NL + "        {" + NL + "            startElement();" + NL + "            text(Short.toString(n));" + NL + "            endElement();" + NL + "        }" + NL + "        catch(SAXException exc)" + NL + "        {" + NL + "            exc.printStackTrace();" + NL + "        }" + NL + "    }" + NL + "" + NL + "    public void visituint16(int n)" + NL + "    {" + NL + "        try" + NL + "        {" + NL + "            startElement();" + NL + "            text(Integer.toString(n));" + NL + "            endElement();" + NL + "        }" + NL + "        catch(SAXException exc)" + NL + "        {" + NL + "            exc.printStackTrace();" + NL + "        }" + NL + "    }" + NL + "" + NL + "    public void visituint32(long n)" + NL + "    {" + NL + "        try" + NL + "        {" + NL + "            startElement();" + NL + "            text(Long.toString(n));" + NL + "            endElement();" + NL + "        }" + NL + "        catch(SAXException exc)" + NL + "        {" + NL + "            exc.printStackTrace();" + NL + "        }" + NL + "    }" + NL + "" + NL + "    public void visituint64(BigInteger n)" + NL + "    {" + NL + "        try" + NL + "        {" + NL + "            startElement();" + NL + "            text(n.toString());" + NL + "            endElement();" + NL + "        }" + NL + "        catch(SAXException exc)" + NL + "        {" + NL + "            exc.printStackTrace();" + NL + "        }" + NL + "    }" + NL + "" + NL + "    public void visitleuint16(int n)" + NL + "    {" + NL + "        throw new IllegalArgumentException(NO_LITTLE_ENDIAN_TYPES);" + NL + "    }" + NL + "" + NL + "    public void visitleuint32(long n)" + NL + "    {" + NL + "        throw new IllegalArgumentException(NO_LITTLE_ENDIAN_TYPES);" + NL + "    }" + NL + "" + NL + "    public void visitleuint64(BigInteger n)" + NL + "    {" + NL + "        throw new IllegalArgumentException(NO_LITTLE_ENDIAN_TYPES);" + NL + "    }" + NL + "" + NL + "    public void visitBitField(byte n, int length)" + NL + "    {" + NL + "        visitint64(n);" + NL + "    }" + NL + "" + NL + "    public void visitBitField(short n, int length)" + NL + "    {" + NL + "        visitint64(n);" + NL + "    }" + NL + "" + NL + "    public void visitBitField(int n, int length)" + NL + "    {" + NL + "        visitint64(n);" + NL + "    }" + NL + "" + NL + "    public void visitBitField(long n, int length)" + NL + "    {" + NL + "        visitint64(n);" + NL + "    }" + NL + "" + NL + "    public void visitBitField(java.math.BigInteger n, int length)" + NL + "    {" + NL + "    \tvisitBitField(n);" + NL + "    }" + NL + "" + NL + "    public void visitBitField(java.math.BigInteger n)" + NL + "    {" + NL + "        try" + NL + "        {" + NL + "            startElement();" + NL + "            text(n.toString());" + NL + "            endElement();" + NL + "        }" + NL + "        catch(SAXException exc)" + NL + "        {" + NL + "            exc.printStackTrace();" + NL + "        }    " + NL + "    }" + NL + "" + NL + "    public void visitString(String n)" + NL + "    {" + NL + "        try" + NL + "        {" + NL + "            startElement();" + NL + "            text(n);" + NL + "            endElement();" + NL + "        }" + NL + "        catch(SAXException exc)" + NL + "        {" + NL + "            exc.printStackTrace();" + NL + "        }    " + NL + "    }" + NL + "}";
  protected final String TEXT_6 = NL;

  public String generate(Object argument)
  {
    StringBuffer stringBuffer = new StringBuffer();
     XmlDumperEmitter c = (XmlDumperEmitter) argument; 
    stringBuffer.append(TEXT_1);
    stringBuffer.append(c.getPackage());
    stringBuffer.append(TEXT_2);
    stringBuffer.append(c.getRoot());
    stringBuffer.append(TEXT_3);
    stringBuffer.append(TEXT_4);
    stringBuffer.append(c.emitTypes());
    stringBuffer.append(TEXT_5);
    stringBuffer.append(TEXT_6);
    return stringBuffer.toString();
  }
}