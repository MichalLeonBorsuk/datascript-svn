//
// Generated by JTB 1.2.2
//

package datascript.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeToken -> "["
 * nodeOptional -> [ RangeExpression() ]
 * nodeToken1 -> "]"
 * </PRE>
 */
public class ArrayRange implements Node {
   public NodeToken nodeToken;
   public NodeOptional nodeOptional;
   public NodeToken nodeToken1;

   public ArrayRange(NodeToken n0, NodeOptional n1, NodeToken n2) {
      nodeToken = n0;
      nodeOptional = n1;
      nodeToken1 = n2;
   }

   public ArrayRange(NodeOptional n0) {
      nodeToken = new NodeToken("[");
      nodeOptional = n0;
      nodeToken1 = new NodeToken("]");
   }

   public void accept(datascript.visitor.Visitor v) {
      v.visit(this);
   }
   public Object accept(datascript.visitor.ObjectVisitor v, Object argu) {
      return v.visit(this,argu);
   }
}
