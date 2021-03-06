/**
 * 
 */
package datascript.ast;

/**
 * @author HWellmann
 *
 */
public class SqlIntegerType extends CompoundType
{
    private TokenAST sqlConstraint;
    
    public void setSqlConstraint(TokenAST s)
    {
        sqlConstraint = s;
    }

    public IntegerValue sizeof(Context ctxt)
    {
        throw new UnsupportedOperationException("sizeof not implemented");
    }

    public boolean isMember(Context ctxt, Value val)
    {
        throw new UnsupportedOperationException("isMember not implemented");
    }

    public String toString()
    {
        return "SQL_INTEGER";
    }
}
