/* BSD License
 *
 * Copyright (c) 2006, Harald Wellmann, Harman/Becker Automotive Systems
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
package datascript.emit;

import antlr.collections.AST;
import datascript.antlr.DataScriptParserTokenTypes;
import datascript.ast.ArrayType;
import datascript.ast.BitFieldType;
import datascript.ast.EnumType;
import datascript.ast.Expression;
import datascript.ast.Field;
import datascript.ast.IntegerType;
import datascript.ast.SequenceType;
import datascript.ast.StringType;
import datascript.ast.TypeInterface;
import datascript.ast.TypeReference;
import datascript.ast.UnionType;
import datascript.emit.java.AccessorNameEmitter;
import datascript.emit.java.DepthFirstVisitorEmitter;
import datascript.jet.java.XmlDumper;
import datascript.jet.java.XmlDumperEnumeration;


public class XmlDumperEmitter extends DepthFirstVisitorEmitter
{
    private XmlDumper dumperTmpl = new XmlDumper();
    private XmlDumperEnumeration enumerationTmpl = new XmlDumperEnumeration();



    public XmlDumperEmitter(String outPathName, String defaultPackageName, AST rootNode)
    {
        super(outPathName, defaultPackageName, rootNode);
    }


    public void beginTranslationUnit()
    {
        openOutputFile(dir, "__XmlDumper.java");
    }


    public void beginImport(AST r)
    {
        rootNode.push(r);
        if (!TmplGenerated)
        {
            String result = dumperTmpl.generate(this);
            out.print(result);
            TmplGenerated = true;
        }
        rootNode.pop();
    }


    public void beginSequence(AST s)
    {
        if (!TmplGenerated)
        {
            String result = dumperTmpl.generate(this);
            out.print(result);
            TmplGenerated = true;
        }
        sequence = (SequenceType) s;
        String result = sequenceTmpl.generate(this);
        out.print(result);
    }


    public void beginUnion(AST u)
    {
        if (!TmplGenerated)
        {
            String result = dumperTmpl.generate(this);
            out.print(result);
            TmplGenerated = true;
        }
        union = (UnionType) u;
        String result = unionTmpl.generate(this);
        out.print(result);
    }


    public void beginEnumeration(AST e)
    {
        if (!TmplGenerated)
        {
            String result = dumperTmpl.generate(this);
            out.print(result);
            TmplGenerated = true;
        }
        enumeration = (EnumType) e;
        String result = enumerationTmpl.generate(this);
        out.print(result);
    }


    public String getVisitor(Field field)
    {
        TypeInterface type = field.getFieldType();
        return getVisitor(type, "node." + AccessorNameEmitter.getGetterName(field) + "()",
                field.getName());
    }


    public String getVisitor(TypeInterface type, String nodeName, String fieldName)
    {
        type = TypeReference.resolveType(type);
        Expression length = null;
        StringBuilder buffer = new StringBuilder();
        if (type instanceof IntegerType)
        {
            buffer.append("visit");
            IntegerType itype = (IntegerType)type;
            switch (itype.getType())
            {
                case DataScriptParserTokenTypes.INT8:
                    buffer.append("Int8");
                    break;
                case DataScriptParserTokenTypes.UINT8:
                    buffer.append("UInt8");
                    break;
                case DataScriptParserTokenTypes.INT16:
                    buffer.append("Int16");
                    break;
                case DataScriptParserTokenTypes.UINT16:
                    buffer.append("UInt16");
                    break;
                case DataScriptParserTokenTypes.INT32:
                    buffer.append("Int32");
                    break;
                case DataScriptParserTokenTypes.UINT32:
                    buffer.append("UInt32");
                    break;
                case DataScriptParserTokenTypes.INT64:
                    buffer.append("Int64");
                    break;
                case DataScriptParserTokenTypes.UINT64:
                    buffer.append("UInt64");
                    break;
                case DataScriptParserTokenTypes.BIT:
                    BitFieldType bftype = (BitFieldType)itype;
                    length = bftype.getLengthExpression();
                    buffer.append("BitField");
                    break;
            }
            buffer.append("(");
            buffer.append(nodeName);
            if (length != null)
            {
                buffer.append(", ");
                buffer.append(exprEmitter.emit(length, "node"));                    
            }
            buffer.append(", \"");
            buffer.append(fieldName);
            buffer.append("\")");
        }
        else if (type instanceof StringType)
        {
            buffer.append("visitString(" + nodeName + ", arg)");
        }
        else if (type instanceof ArrayType)
        {
            buffer.append("visitArray(" + nodeName + ", arg)");
        }
        else
        {
            buffer.append(nodeName);
            buffer.append(".accept(this, \"");
            buffer.append(fieldName);
            buffer.append("\")");
        }
        return buffer.toString();
    }


    public String getElementVisitor(Field field)
    {
        String result = null;
        TypeInterface type = field.getFieldType();
        if (type instanceof ArrayType)
        {
            ArrayType array = (ArrayType)type;            
            TypeInterface elemType = array.getElementType();
            result = getVisitor(elemType, "__elem", field.getName()); 
        }
        
        return result;
    }


    public String startType()
    {
        return "startElement(arg);";
    }


    public String endType()
    {
        return "endElement(arg);";
    }
    
}
