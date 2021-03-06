//
// Generated by JTB 1.2.2
//

package datascript.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeToken -> &lt;RANGE&gt;
 * expression -> Expression()
 * </PRE>
 */
public class UpperBoundExpression implements Node {
   public NodeToken nodeToken;
   public Expression expression;

   public UpperBoundExpression(NodeToken n0, Expression n1) {
      nodeToken = n0;
      expression = n1;
   }

   public UpperBoundExpression(Expression n0) {
      nodeToken = new NodeToken("..");
      expression = n0;
   }

   public void accept(datascript.visitor.Visitor v) {
      v.visit(this);
   }
   public Object accept(datascript.visitor.ObjectVisitor v, Object argu) {
      return v.visit(this,argu);
   }
}

