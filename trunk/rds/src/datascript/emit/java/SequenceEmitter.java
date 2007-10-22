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


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import datascript.ast.CompoundType;
import datascript.ast.DataScriptException;
import datascript.ast.Field;
import datascript.ast.FunctionType;
import datascript.ast.Parameter;
import datascript.ast.SequenceType;
import freemarker.template.Configuration;
import freemarker.template.Template;



public class SequenceEmitter extends CompoundEmitter
{
    private final List<CompoundFunctionEmitter> functions = 
        new ArrayList<CompoundFunctionEmitter>();
    private final List<SequenceFieldEmitter> fields = 
        new ArrayList<SequenceFieldEmitter>();

    private SequenceType seq;


    public static class SequenceFieldEmitter extends FieldEmitter
    {
        private ExpressionEmitter exprEmitter;
        private static Template tpl = null;


        public SequenceFieldEmitter(Field f, CompoundEmitter j)
        {
            super(f, j);
            exprEmitter = new ExpressionEmitter();
        }


        public void emit(PrintWriter writer, Configuration cfg) throws Exception
        {
            //super.emit(writer, cfg, "java/SequenceFieldAccessor.ftl");
            if (tpl == null)
                tpl = cfg.getTemplate("java/SequenceFieldAccessor.ftl");
            tpl.process(this, writer);
        }


        public boolean getHasAlignment()
        {
            return field.getAlignment() != null;
        }


        public int getAlignmentValue()
        {
            return field.getAlignmentValue();
        }


        public int getBitsizeof()
        {
            int bitSize = field.bitsizeof(null).integerValue().intValue();
//            if (bitSize % 8 != 0)
//                bitSize = ((bitSize / 8) + 1) * 8;
            return bitSize;
        }


        public String getLabel()
        {
            return exprEmitter.emit(field.getLabel());
            //return field.getLabel().getText();
        }


        public String getLabelSetter()
        {
            String name = field.getLabel().getText();
            StringBuffer result = new StringBuffer("set");
            result.append(name.substring(0, 1).toUpperCase());
            result.append(name.substring(1, name.length()));
            return result.toString();
        }


        public String getLabelTypeName()
        {
            return TypeNameEmitter.getTypeName(field.getLabel().getExprType());
        }
    }



    public SequenceEmitter(JavaDefaultEmitter j, SequenceType sequence)
    {
        super(j);
        seq = sequence;
    }


    public SequenceType getSequenceType()
    {
        return seq;
    }


    public CompoundType getCompoundType()
    {
        return seq;
    }



    public void begin(Configuration cfg)
    {
        fields.clear();
        for (Field field : seq.getFields())
        {
            SequenceFieldEmitter fe = new SequenceFieldEmitter(field, this);
            fields.add(fe);
        }
        params.clear();
        for (Parameter param : seq.getParameters())
        {
            CompoundParameterEmitter p = new CompoundParameterEmitter(param);
            params.add(p);
        }
        functions.clear();
        for (FunctionType func : seq.getFunctions())
        {
            CompoundFunctionEmitter f = new CompoundFunctionEmitter(func);
            functions.add(f);
        }

        try
        {
            Template tpl = cfg.getTemplate("java/SequenceBegin.ftl");
            tpl.process(this, writer);

            for (SequenceFieldEmitter field : fields)
            {
                field.emit(writer, cfg);
            }

            for (CompoundParameterEmitter param : params)
            {
                param.emit(writer, cfg);
            }

            for (CompoundFunctionEmitter func : functions)
            {
                func.emit(writer, cfg);
            }

            tpl = cfg.getTemplate("java/SequenceRead.ftl");
            tpl.process(this, writer);

            tpl = cfg.getTemplate("java/SequenceWrite.ftl");
            tpl.process(this, writer);
        }
        catch (Exception e)
        {
            throw new DataScriptException(e);
        }
    }


    public void end(Configuration cfg)
    {
        try
        {
            Template tpl = cfg.getTemplate("java/SequenceEnd.ftl");

            tpl.process(this, writer);
        }
        catch (Exception e)
        {
            throw new DataScriptException(e);
        }
    }



    public List<SequenceFieldEmitter> getFields()
    {
        return fields;
    }

}
