//
// Generated by JTB 1.2.2
//

package datascript.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeToken -> ":"
 * expression -> Expression()
 * </PRE>
 */
public class FieldCondition implements Node {
   public NodeToken nodeToken;
   public Expression expression;

   public FieldCondition(NodeToken n0, Expression n1) {
      nodeToken = n0;
      expression = n1;
   }

   public FieldCondition(Expression n0) {
      nodeToken = new NodeToken(":");
      expression = n0;
   }

   public void accept(datascript.visitor.Visitor v) {
      v.visit(this);
   }
   public Object accept(datascript.visitor.ObjectVisitor v, Object argu) {
      return v.visit(this,argu);
   }
}

