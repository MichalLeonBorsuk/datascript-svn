/* BSD License
 *
 * Copyright (c) 2007, Henrik Wedekind, Harman/Becker Automotive Systems
 * All rights reserved.
 * 
 * This software is derived from previous work
 * Copyright (c) 2003, Godmar Back.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 * 
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 * 
 *     * Neither the name of Harman/Becker Automotive Systems or
 *       Godmar Back nor the names of their contributors may be used to
 *       endorse or promote products derived from this software without
 *       specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package datascript.emit.html;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import antlr.collections.AST;
import datascript.ast.ArrayType;
import datascript.ast.ChoiceType;
import datascript.ast.CompoundType;
import datascript.ast.Container;
import datascript.ast.DataScriptException;
import datascript.ast.Expression;
import datascript.ast.Field;
import datascript.ast.FunctionType;
import datascript.ast.SequenceType;
import datascript.ast.SqlDatabaseType;
import datascript.ast.SqlIntegerType;
import datascript.ast.SqlMetadataType;
import datascript.ast.SqlPragmaType;
import datascript.ast.SqlTableType;
import datascript.ast.TypeInstantiation;
import datascript.ast.TypeInterface;
import datascript.ast.TypeReference;
import datascript.ast.UnionType;
import freemarker.template.Template;



public class CompoundEmitter extends DefaultHTMLEmitter
{
    private CompoundType compound;

    private final List<FieldEmitter> fields = new ArrayList<FieldEmitter>();
    private final ExpressionEmitter exprEmitter = new ExpressionEmitter();
    private final List<FunctionEmitter> functions = new ArrayList<FunctionEmitter>();


    public CompoundEmitter()
    {
        super();
        directory = new File(directory, contentFolder);
    }


    public CompoundEmitter(CompoundType cc)
    {
        this.compound = cc;
    }


    public String getName()
    {
        return compound == null ? "" : compound.getName();
    }


    public LinkedType getLinkedType()
    {
    	if (compound == null)
    		return null;

        TypeInterface type = compound;
        type = TypeReference.resolveType(type);
        LinkedType linkedType = new LinkedType(type);
        return linkedType;
    }



    public class FunctionEmitter
    {
        private final FunctionType function;
        private final ExpressionEmitter ee = new ExpressionEmitter();


        public FunctionEmitter(FunctionType fctn)
        {
            function = fctn;
        }


        public FunctionType getFuntionType()
        {
            return function;
        }


        public String getReturnTypeName()
        {
            String returnTypeName = TypeNameEmitter.getTypeName(function.getReturnType());
            return returnTypeName;
        }


        public String getResult()
        {
            return ee.emit(function.getResult());
        }
    }



    public static class FieldEmitter
    {
        private final Field field;
        private static final TypeNameEmitter tne = new TypeNameEmitter();


        public FieldEmitter(Field f)
        {
            this.field = f;
        }


        public String getName()
        {
            return field.getName();
        }


        public LinkedType getType()
        {
            TypeInterface type = field.getFieldType();
            type = TypeReference.resolveType(type);
            if (type instanceof ArrayType)
                type = TypeReference.resolveType(((ArrayType)type).getElementType());
            LinkedType linkedType = new LinkedType(type);
            return linkedType;
        }


        public String getConstraint()
        {
            return tne.getConstraint(field);
        }


        public String getArrayRange()
        {
            return tne.getArrayRange(field);
        }


        public String getOptionalClause()
        {
            return tne.getOptionalClause(field);
        }


        public String getOffsetLabel()
        {
            return tne.getLabel(field);
        }


        public Comment getDocumentation()
        {
            Comment comment = new Comment();
            String doc = field.getDocumentation();
            if (doc != null && doc.length() > 0) comment.parse(doc);
            return comment;
        }


        public List<Expression> getArguments()
        {
            TypeInterface type = field.getFieldType();
            type = TypeReference.resolveType(type);
            while (type instanceof ArrayType)
                type = TypeReference.resolveType(((ArrayType)type).getElementType());
            if (type instanceof TypeInstantiation)
            {
                TypeInstantiation inst = (TypeInstantiation) type;
                return inst.getArguments();
            }
            else
            {
                return null;
            }
        }
    }



    public void emit(CompoundType compnd)
    {
        this.compound = compnd;
        functions.clear();
        for (FunctionType fctn : compnd.getFunctions())
        {
            FunctionEmitter fe = new FunctionEmitter(fctn);
            functions.add(fe);
        }
        containers.clear();
        for (Container compund : compnd.getContainers())
        {
            CompoundEmitter ce = new CompoundEmitter((CompoundType)compund);
            containers.add(ce);
        }

        if (compnd instanceof ChoiceType)
            emitChoiceType();
        else
            emitCompoundType();
    }


    private void emitChoiceType()
    {
        try
        {
            Template tpl = cfg.getTemplate("html/choice.html.ftl");

            setCurrentFolder(contentFolder);

            File outputDir = new File(directory, compound.getPackage().getPackageName());
            openOutputFile(outputDir, compound.getName() + HTML_EXT);

            tpl.process(this, writer);
            writer.close();
        }
        catch (Exception exc)
        {
            throw new DataScriptException(exc);
        }
    }


    private void emitCompoundType()
    {
        fields.clear();
        for (Field field : compound.getFields())
        {
            FieldEmitter fe = new FieldEmitter(field);
            fields.add(fe);
        }
        
        try
        {
            Template tpl = cfg.getTemplate("html/compound.html.ftl");

            setCurrentFolder(contentFolder);

            File outputDir = new File(directory, compound.getPackage().getPackageName());
            openOutputFile(outputDir, compound.getName() + HTML_EXT);

            tpl.process(this, writer);
            writer.close();
        }
        catch (Exception exc)
        {
            throw new DataScriptException(exc);
        }
    }


    public String getCategoryPlainText()
    {
        if (compound instanceof SequenceType)
        {
            return "Sequence";
        }
        else if (compound instanceof UnionType)
        {
            return "Union";
        }
        else if (compound instanceof ChoiceType)
        {
            return "Choice";
        }
        else if (compound instanceof SqlDatabaseType)
        {
            return "SQL Database";
        }
        else if (compound instanceof SqlMetadataType)
        {
            return "SQL Matadata";
        }
        else if (compound instanceof SqlTableType)
        {
            return "SQL Table";
        }
        else if (compound instanceof SqlPragmaType)
        {
            return "SQL Pragma";
        }
        else if (compound instanceof SqlIntegerType)
        {
            return "SQL Integer";
        }
        throw new RuntimeException("unknown category "
                + compound.getClass().getName());
    }


    public String getCategoryKeyword()
    {
        if (compound instanceof SequenceType)
        {
            return "";
        }
        else if (compound instanceof ChoiceType)
        {
            return "choice ";
        }
        else if (compound instanceof UnionType)
        {
            return "union ";
        }
        else if (compound instanceof SqlDatabaseType)
        {
            return "sql_database ";
        }
        else if (compound instanceof SqlMetadataType)
        {
            return "sql_metadata ";
        }
        else if (compound instanceof SqlTableType)
        {
            return "sql_table ";
        }
        else if (compound instanceof SqlPragmaType)
        {
            return "sql_pragma ";
        }
        else if (compound instanceof SqlIntegerType)
        {
            return "sql_integer ";
        }
        throw new RuntimeException("unknown category "
                + compound.getClass().getName());
    }


    @Override
    public String getPackageName()
    {
        return compound.getScope().getPackage().getPackageName();
    }


    public Container getType()
    {
        return compound;
    }


    public ChoiceType getChoiceType()
    {
        return (ChoiceType) compound;
    }


    public String getSelector()
    {
        String selector = null;

        AST node = ((ChoiceType)compound).getSelectorAST();
        if (node != null)
        {
            ExpressionEmitter ee = new ExpressionEmitter();
            selector = ee.emit((Expression) node);
        }

//        if (selector == null)
//            throw new ComputeError("missing selector");
        return selector;
    }


    public Comment getDocumentation()
    {
        Comment comment = new Comment();
        String doc = compound.getDocumentation();
        if (doc != null && doc.length() > 0)
            comment.parse(doc);
        return comment;
    }


    public List<FieldEmitter> getFields()
    {
        return fields;
    }


    public List<FunctionEmitter> getFunctions()
    {
        return functions;
    }


    public LinkedType toLinkedType(TypeInterface type1)
    {
        TypeInterface type2 = TypeReference.resolveType(type1);
        LinkedType linkedType = new LinkedType(type2);
        return linkedType;
    }


    public Comment parseDocumentation(Field f)
    {
        Comment comment = new Comment();
        String doc = f.getDocumentation();
        if (doc != null && doc.length() > 0)
            comment.parse(doc);
        return comment;
    }


    public String emitExpression(Expression expr)
    {
        return exprEmitter.emit(expr);
    }
}
