//
// Generated by JTB 1.2.2
//

package datascript.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeListOptional -> ( Declaration() )*
 * </PRE>
 */
public class DeclarationList implements Node {
   public NodeListOptional nodeListOptional;

   public DeclarationList(NodeListOptional n0) {
      nodeListOptional = n0;
   }

   public void accept(datascript.visitor.Visitor v) {
      v.visit(this);
   }
   public Object accept(datascript.visitor.ObjectVisitor v, Object argu) {
      return v.visit(this,argu);
   }
}

