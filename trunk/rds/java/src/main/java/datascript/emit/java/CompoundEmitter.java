/* BSD License
 *
 * Copyright (c) 2006, Harald Wellmann, Henrik Wedekind Harman/Becker Automotive Systems
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

package datascript.emit.java;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import datascript.antlr.DataScriptParserTokenTypes;
import datascript.ast.ArrayType;
import datascript.ast.BitFieldType;
import datascript.ast.CompoundType;
import datascript.ast.Container;
import datascript.ast.DataScriptException;
import datascript.ast.EnumType;
import datascript.ast.Expression;
import datascript.ast.Field;
import datascript.ast.FunctionType;
import datascript.ast.IntegerType;
import datascript.ast.Parameter;
import datascript.ast.SignedBitFieldType;
import datascript.ast.StdIntegerType;
import datascript.ast.StringType;
import datascript.ast.TypeInstantiation;
import datascript.ast.TypeInterface;
import datascript.ast.TypeReference;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

abstract public class CompoundEmitter extends IntegerTypeEmitter
{
    protected final List<CompoundParameterEmitter> params = new ArrayList<CompoundParameterEmitter>();

    private StringBuilder buffer;

    private String formalParams;

    private String actualParams;

    public static class CompoundFunctionEmitter
    {
        private static Template tpl;

        private FunctionType func;

        public CompoundFunctionEmitter(FunctionType func)
        {
            this.func = func;
        }

        public void emit(PrintWriter writer, Configuration cfg)
                throws IOException, TemplateException
        {
            if (tpl == null)
                tpl = cfg.getTemplate("java/FunctionTmpl.ftl");
            tpl.process(this, writer);
        }

        public String getName()
        {
            return func.getName();
        }

        public String getResult()
        {
            ExpressionEmitter ee = new ExpressionEmitter();
            return ee.emit(func.getResult());
        }

        public String getReturnType()
        {
            TypeInterface returnType = func.getReturnType();
            return TypeNameEmitter.getTypeName(returnType);
        }
    }

    public static class CompoundParameterEmitter
    {
        private static Template tpl;

        private CompoundEmitter global;

        private TypeInterface type;

        private Parameter param;

        public CompoundParameterEmitter(Parameter param, CompoundEmitter j)
        {
            global = j;
            this.param = param;
            type = TypeReference.getBaseType(param.getType());
        }

        public void emit(PrintWriter writer, Configuration cfg)
                throws IOException, TemplateException
        {
            if (tpl == null)
                tpl = cfg.getTemplate("java/ParameterAccessor.ftl");
            tpl.process(this, writer);
        }

        public String getName()
        {
            return param.getName();
        }

        public String getJavaTypeName()
        {
            return TypeNameEmitter.getTypeName(param.getType());
        }

        public String getGetterName()
        {
            return AccessorNameEmitter.getGetterName(param);
        }

        public String getSetterName()
        {
            return AccessorNameEmitter.getSetterName(param);
        }

        public String getTypeName()
        {
            String typeName = TypeNameEmitter.getTypeName(type);
            typeName = typeName.replaceAll("&lt;", "<").replaceAll("&gt;", ">");
            return typeName;
        }

        public boolean getIsSimpleIntegerType()
        {
            boolean result = false;
            if (type instanceof StdIntegerType)
            {
                result = (((StdIntegerType) type).getType() != datascript.antlr.DataScriptParserTokenTypes.UINT64);
            }
            else if (type instanceof BitFieldType)
            {
                BitFieldType bitField = (BitFieldType) type;
                result = 0 < bitField.getLength() && bitField.getLength() < 64;
            }
            return result;
        }

        public long getMinVal()
        {
            if (type instanceof SignedBitFieldType)
            {
                BitFieldType bitField = (BitFieldType) type;
                return -(1 << bitField.getLength() - 1);
            }
            if (type instanceof BitFieldType)
            {
                return 0;
            }
            if (type instanceof StdIntegerType)
            {
                StdIntegerType integerType = (StdIntegerType) type;
                return integerType.getLowerBound().longValue();
            }
            throw new RuntimeException("type of field '" + param.getName()
                    + "' is not a simple type");
        }

        public long getMaxVal()
        {
            if (type instanceof SignedBitFieldType)
            {
                BitFieldType bitField = (BitFieldType) type;
                return (1 << bitField.getLength() - 1) - 1;
            }
            if (type instanceof BitFieldType)
            {
                BitFieldType bitField = (BitFieldType) type;
                return (1 << bitField.getLength()) - 1;
            }
            if (type instanceof StdIntegerType)
            {
                StdIntegerType integerType = (StdIntegerType) type;
                return integerType.getUpperBound().longValue();
            }
            throw new RuntimeException("type of field '" + param.getName()
                    + "' is not a simple type");
        }

        public boolean getEqualsCanThrowExceptions()
        {
            return global.getEqualsCanThrowExceptions();
        }
    }

    public class ArrayEmitter
    {
        private final ExpressionEmitter ee = new ExpressionEmitter();

        private final Field field;

        private final ArrayType array;

        private final CompoundEmitter global;

        public ArrayEmitter(Field field, ArrayType array, CompoundEmitter j)
        {
            this.field = field;
            this.array = array;
            this.global = j;
        }

        public String getElType()
        {
            return TypeNameEmitter.getTypeName(array.getElementType());
        }

        public boolean getIsVariable()
        {
            return array.isVariable();
        }

        public String getGetterName()
        {
            return AccessorNameEmitter.getGetterName(field);
        }

        public String getSetterName()
        {
            return AccessorNameEmitter.getSetterName(field);
        }

        public String getActualParameterList()
        {
            StringBuilder builder = new StringBuilder();
            TypeInterface elType = array.getElementType();
            if (elType instanceof TypeInstantiation)
            {
                TypeInstantiation inst = (TypeInstantiation) elType;
                appendArguments(builder, inst);
            }
            return builder.toString();
        }

        public String getLengthExpr()
        {
            return ee.emit(array.getLengthExpression());
        }

        public String getCurrentElement()
        {
            StringBuilder builder = new StringBuilder();
            TypeInterface elType = array.getElementType();
            if (elType instanceof EnumType)
            {
                EnumType enumType = (EnumType) elType;
                IntegerType baseType = (IntegerType) enumType.getBaseType();
                builder.append(elType.getName());
                builder.append(".toEnum(");
                readIntegerValue(builder, baseType);
                builder.append(")");
            }
            else
            {
                builder.append("new ");
                builder.append(getElType());
                builder.append("(__in, __cc");
                builder.append(getActualParameterList());
                builder.append(")");
            }
            return builder.toString();
        }

        public boolean getEqualsCanThrowExceptions()
        {
            return global.getEqualsCanThrowExceptions();
        }
    }

    public CompoundEmitter(JavaDefaultEmitter j)
    {
        super(j);
    }

    abstract public CompoundType getCompoundType();

    protected void reset()
    {
        formalParams = null;
        actualParams = null;
    }

    public String readField(Field field)
    {
        buffer = new StringBuilder();
        TypeInterface type = field.getFieldType();
        type = TypeReference.getBaseType(type);

        if (type instanceof IntegerType)
        {
            readIntegerField(field, (IntegerType) type);
        }
        else if (type instanceof CompoundType)
        {
            readCompoundField(field, (CompoundType) type);
        }
        else if (type instanceof ArrayType)
        {
            readArrayField(field, (ArrayType) type);
        }
        else if (type instanceof EnumType)
        {
            readEnumField(field, (EnumType) type);
        }
        else if (type instanceof TypeInstantiation)
        {
            TypeInstantiation inst = (TypeInstantiation) type;
            readInstantiatedField(field, inst);
        }
        else if (type instanceof StringType)
        {
            StringType inst = (StringType) type;
            readStringField(field, inst);
        }
        else
        {
            throw new InternalError("unhandled type: "
                    + type.getClass().getName());
        }
        return buffer.toString();
    }

    private void readIntegerField(Field field, IntegerType type)
    {
        buffer.append(AccessorNameEmitter.getSetterName(field));
        buffer.append("(");
        readIntegerValue(buffer, type);
        buffer.append(");");
    }

    private void readStringField(Field field, StringType type)
    {
        buffer.append(AccessorNameEmitter.getSetterName(field));
        buffer.append("(__in.readString());");
    }

    private void readCompoundField(Field field, CompoundType type)
    {
        buffer.append(AccessorNameEmitter.getSetterName(field));
        buffer.append("(new ");
        buffer.append(type.getName());
        buffer.append("(__in, __cc));");
    }

    private void readInstantiatedField(Field field, TypeInstantiation inst)
    {
        CompoundType compound = inst.getBaseType();

        buffer.append(AccessorNameEmitter.getSetterName(field));
        buffer.append("(new ");
        buffer.append(compound.getName());
        buffer.append("(__in, __cc");
        appendArguments(buffer, inst);
        buffer.append("));");
    }

    static void appendArguments(StringBuilder buffer, TypeInstantiation inst)
    {
        CompoundType compound = inst.getBaseType();
        Iterable<Expression> arguments = inst.getArguments();
        if (arguments != null)
        {
            ExpressionEmitter ee = new ExpressionEmitter();
            int argIndex = 0;
            for (Expression arg : arguments)
            {
                buffer.append(", ");
                boolean cast = emitTypeCast(buffer, compound, arg, argIndex);
                String javaArg = ee.emit(arg);
                buffer.append(javaArg);
                if (cast)
                {
                    buffer.append(")");
                }
                argIndex++;
            }
        }
    }

    /**
     * Emits a type cast for passing an argument to a parameterized type.
     * 
     * @param type
     *            compound type with parameters
     * @param expr
     *            argument expression in type instantiation
     * @param paramIndex
     *            index of argument in argument list
     */
    private static boolean emitTypeCast(StringBuilder buffer,
            CompoundType type, Expression expr, int paramIndex)
    {
        boolean cast = false;
        Parameter param = type.getParameterAt(paramIndex);
        TypeInterface paramType = TypeReference.getBaseType(param.getType());

        if (paramType instanceof StdIntegerType)
        {
            StdIntegerType intType = (StdIntegerType) paramType;
            switch (intType.getType())
            {
                case DataScriptParserTokenTypes.INT8:
                    buffer.append("(byte)(");
                    cast = true;
                    break;

                case DataScriptParserTokenTypes.UINT8:
                case DataScriptParserTokenTypes.INT16:
                    buffer.append("(short)(");
                    cast = true;
                    break;
                default:
                    // nothing
            }
        }
        return cast;
    }

    private void readArrayField(Field field, ArrayType array)
    {
        String elTypeJavaName = TypeNameEmitter.getTypeName(array);
        if (elTypeJavaName.startsWith("ObjectArray"))
        {
            try
            {
                ArrayEmitter ae = new ArrayEmitter(field, array, this);
                Template tpl = global.getTemplateConfig().getTemplate(
                        "java/ArrayRead.ftl");
                tpl.process(ae, writer);
            }
            catch (TemplateException exc)
            {
                throw new DataScriptException(exc);
            }
            catch (IOException exc)
            {
                throw new DataScriptException(exc);
            }
        }
        else
        {
            Expression length = array.getLengthExpression();
            TypeInterface elType = array.getElementType();
            if (elType instanceof EnumType)
            {
                EnumType enumType = (EnumType) elType;
                readEnumField(field, enumType);
            }
            else
            {
                buffer.append(AccessorNameEmitter.getSetterName(field));
                buffer.append("(new ");
                buffer.append(elTypeJavaName);
                buffer.append("(__in, (int)(");
                buffer.append(getLengthExpression(length));
                buffer.append(")");
                if (elType instanceof BitFieldType)
                {
                    BitFieldType bitField = (BitFieldType) elType;
                    Expression numBits = bitField.getLengthExpression();
                    buffer.append(", ");
                    buffer.append(getLengthExpression(numBits));
                }
            }
            buffer.append("));");
        }
    }

    private void readEnumField(Field field, EnumType type)
    {
        IntegerType baseType = (IntegerType) type.getBaseType();
        buffer.append(AccessorNameEmitter.getSetterName(field));
        buffer.append("(");
        buffer.append(type.getName());
        buffer.append(".toEnum(");
        readIntegerValue(buffer, baseType);
        buffer.append("));");
    }

    private String getLengthExpression(Expression expr)
    {
        if (expr == null)
        {
            // TODO handle variable length
            // throw new
            // InternalError("Variable length arrays are not implemented now!");
            return "-1";
        }
        return exprEmitter.emit(expr);
    }

    public String getConstraint(Field field)
    {
        String result = null;
        Expression expr = field.getCondition();
        if (expr != null)
        {
            result = exprEmitter.emit(expr);
        }
        else
        {
            expr = field.getInitializer();
            if (expr != null)
            {
                result = field.getName() + " == " + exprEmitter.emit(expr);
            }
        }
        return result;
    }

    public String getOptionalClause(Field field)
    {
        String result = null;
        Expression expr = field.getOptionalClause();
        if (expr != null)
        {
            result = exprEmitter.emit(expr);
        }
        return result;
    }

    public void buildParameterLists()
    {
        StringBuilder formal = new StringBuilder();
        StringBuilder actual = new StringBuilder();
        CompoundType compound = getCompoundType();
        for (Parameter param : compound.getParameters())
        {
            String paramName = param.getName();
            TypeInterface paramType = TypeReference
                    .getBaseType(param.getType());

            String typeName = TypeNameEmitter.getTypeName(paramType);
            formal.append(", ");
            formal.append(typeName);
            formal.append(" ");
            formal.append(paramName);

            actual.append(", ");
            actual.append(paramName);
        }
        formalParams = formal.toString();
        actualParams = actual.toString();
    }

    public String getFormalParameterList()
    {
        if (formalParams == null)
        {
            buildParameterLists();
        }
        return formalParams;
    }

    public String getActualParameterList()
    {
        if (actualParams == null)
        {
            buildParameterLists();
        }
        return actualParams;
    }

    public String writeField(Field field)
    {
        buffer = new StringBuilder();
        TypeInterface type = field.getFieldType();
        type = TypeReference.getBaseType(type);

        if (type instanceof IntegerType)
        {
            writeIntegerField(field, (IntegerType) type);
        }
        else if (type instanceof CompoundType)
        {
            writeCompoundField(field, (Container) type);
        }
        else if (type instanceof ArrayType)
        {
            writeArrayField(field, (ArrayType) type);
        }
        else if (type instanceof EnumType)
        {
            writeEnumField(field, (EnumType) type);
        }
        else if (type instanceof TypeInstantiation)
        {
            writeInstantiatedField(field, (TypeInstantiation) type);
        }
        else if (type instanceof StringType)
        {
            writeStringType(field, (StringType) type);
        }
        else
        {
            throw new InternalError("unhandled type: "
                    + type.getClass().getName());
        }
        return buffer.toString();
    }

    private void writeIntegerField(Field field, IntegerType type)
    {
        writeIntegerValue(buffer, AccessorNameEmitter.getGetterName(field)
                + "()", type);
    }

    private void writeStringType(Field field, StringType type)
    {
        buffer.append("__out.writeString(");
        buffer.append(AccessorNameEmitter.getGetterName(field));
        buffer.append("());");
    }

    private void writeCompoundField(Field field, Container type)
    {
        buffer.append(AccessorNameEmitter.getGetterName(field));
        buffer.append("().write(__out, __cc);");
    }

    private void writeInstantiatedField(Field field, TypeInstantiation inst)
    {
        String getter = AccessorNameEmitter.getGetterName(field);
        setParameters(getter + "()", inst);
        buffer.append(getter);
        buffer.append("().write(__out, __cc);");
    }

    private void setParameters(String lhs, TypeInstantiation inst)
    {
        CompoundType compound = inst.getBaseType();
        Iterable<Expression> arguments = inst.getArguments();
        if (arguments != null)
        {
            int argIndex = 0;
            for (Expression arg : arguments)
            {
                Parameter param = compound.getParameterAt(argIndex);
                String setter = AccessorNameEmitter.getSetterName(param);
                buffer.append(lhs);
                buffer.append(".");
                buffer.append(setter);
                buffer.append("(");
                boolean cast = emitTypeCast(buffer, compound, arg, argIndex);
                String javaArg = exprEmitter.emit(arg);
                buffer.append(javaArg);
                if (cast)
                {
                    buffer.append(")");
                }
                buffer.append(");\n                ");
                argIndex++;
            }
        }
    }

    private void writeArrayField(Field field, ArrayType array)
    {
        String elTypeJavaName = TypeNameEmitter.getTypeName(array);
        if (elTypeJavaName.startsWith("ObjectArray"))
        {
            try
            {
                ArrayEmitter ae = new ArrayEmitter(field, array, this);
                Template tpl = global.getTemplateConfig().getTemplate(
                        "java/ArrayWrite.ftl");
                tpl.process(ae, writer);
            }
            catch (IOException exc)
            {
                throw new DataScriptException(exc);
            }
            catch (TemplateException exc)
            {
                throw new DataScriptException(exc);
            }
        }
        else
        {
            buffer.append(AccessorNameEmitter.getGetterName(field));
            buffer.append("().write(__out, __cc);");
        }

    }

    private void writeEnumField(Field field, EnumType type)
    {
        IntegerType baseType = (IntegerType) type.getBaseType();
        writeIntegerValue(buffer, AccessorNameEmitter.getGetterName(field)
                + "().getValue()", baseType);
    }

    public String getClassName()
    {
        return getCompoundType().getName();
    }
}
