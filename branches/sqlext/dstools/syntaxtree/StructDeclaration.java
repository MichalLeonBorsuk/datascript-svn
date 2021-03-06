//
// Generated by JTB 1.2.2
//

package datascript.syntaxtree;

/**
 * Grammar production:
 * <PRE>
 * nodeOptional -> [ ByteOrderModifier() ]
 * nodeOptional1 -> [ &lt;UNION&gt; | &lt;DATABASE&gt; | &lt;TABLE&gt; | &lt;SQLINTEGER&gt; | &lt;SQLTEXT&gt; ]
 * nodeOptional2 -> [ &lt;IDENTIFIER&gt; ]
 * nodeOptional3 -> [ "(" ParameterDefinition() ( "," ParameterDefinition() )* ")" ]
 * nodeToken -> "{"
 * declarationList -> DeclarationList()
 * nodeToken1 -> "}"
 * </PRE>
 */
public class StructDeclaration implements Node {
   public NodeOptional nodeOptional;
   public NodeOptional nodeOptional1;
   public NodeOptional nodeOptional2;
   public NodeOptional nodeOptional3;
   public NodeToken nodeToken;
   public DeclarationList declarationList;
   public NodeToken nodeToken1;

   public StructDeclaration(NodeOptional n0, NodeOptional n1, NodeOptional n2, NodeOptional n3, NodeToken n4, DeclarationList n5, NodeToken n6) {
      nodeOptional = n0;
      nodeOptional1 = n1;
      nodeOptional2 = n2;
      nodeOptional3 = n3;
      nodeToken = n4;
      declarationList = n5;
      nodeToken1 = n6;
   }

   public StructDeclaration(NodeOptional n0, NodeOptional n1, NodeOptional n2, NodeOptional n3, DeclarationList n4) {
      nodeOptional = n0;
      nodeOptional1 = n1;
      nodeOptional2 = n2;
      nodeOptional3 = n3;
      nodeToken = new NodeToken("{");
      declarationList = n4;
      nodeToken1 = new NodeToken("}");
   }

   public void accept(datascript.visitor.Visitor v) {
      v.visit(this);
   }
   public Object accept(datascript.visitor.ObjectVisitor v, Object argu) {
      return v.visit(this,argu);
   }
}

