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
package datascript.emit.java;

import datascript.ast.CompoundType;
import datascript.ast.Field;
import datascript.ast.Parameter;
import datascript.ast.Scope;
import datascript.ast.SequenceType;
import datascript.ast.TypeInterface;
import datascript.jet.java.SequenceBegin;
import datascript.jet.java.SequenceEnd;
import datascript.jet.java.SequenceRead;

public class SequenceEmitter extends CompoundEmitter
{
    private SequenceType seq;
    private SequenceFieldEmitter fieldEmitter;
    private SequenceBegin beginTmpl = new SequenceBegin();
    private SequenceEnd endTmpl = new SequenceEnd();
    private SequenceRead readTmpl = new SequenceRead();
    private TypeNameEmitter tne = new TypeNameEmitter();
    
    public SequenceEmitter(JavaEmitter j, SequenceType sequence)
    {
        super(j);
        seq = sequence;
        fieldEmitter = new SequenceFieldEmitter(this);
    }
   
    public SequenceType getSequenceType()
    {
        return seq;
    }
    
    public CompoundType getCompoundType()
    {
        return seq;
    }        

    public FieldEmitter getFieldEmitter()
    {
        return fieldEmitter;
    }
    
    public void begin()
    {
        //reset();
        String result = beginTmpl.generate(this);
        out.print(result);
        
        for (Field field : seq.getFields())
        {
            fieldEmitter.emit(field);
        }
        
        for (Parameter param : seq.getParameters())
        {
            String typeName = tne.getTypeName(param.getType());
            out.println("    " + typeName + " " + param.getName() + ";");
        }
        result = readTmpl.generate(this);
        out.print(result);
    }

    public void end()
    {
        String result = endTmpl.generate(this);
        out.print(result);
    }    
}
