//
// Generated by JTB 1.2.2
//

package datascript.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * exclusiveOrExpression -> ExclusiveOrExpression()
 * nodeOptional -> [ InclusiveOrOperand() ]
 * </PRE>
 */
public class InclusiveOrExpression implements Node {
   public ExclusiveOrExpression exclusiveOrExpression;
   public NodeOptional nodeOptional;

   public InclusiveOrExpression(ExclusiveOrExpression n0, NodeOptional n1) {
      exclusiveOrExpression = n0;
      nodeOptional = n1;
   }

   public void accept(datascript.visitor.Visitor v) {
      v.visit(this);
   }
   public Object accept(datascript.visitor.ObjectVisitor v, Object argu) {
      return v.visit(this,argu);
   }
}
