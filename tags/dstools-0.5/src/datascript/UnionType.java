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
package datascript;

import java.util.Iterator;

public class UnionType extends CompoundType
{

    UnionType(String name, String doc, CompoundType parent)
    {
        super(name, doc, parent);
    }

    public IntegerValue sizeof(Context ctxt)
    {
        IntegerValue size = ((Field) fields.elementAt(0)).sizeof(ctxt);
        for (int i = 1; i < fields.size(); i++)
        {
            if (size.compareTo(((Field) fields.elementAt(i)).sizeof(ctxt)) != 0)
            {
                throw new ComputeError("sizeof is unknown");
            }
        }
        return size;
    }

    public boolean isMember(Context ctxt, Value val)
    {
        // do something like
        // if val.getType() == this
        throw new ComputeError("isMember not implemented");
    }

    void computeBitFieldOffsets(int offset)
    {
        if (bfoComputed)
        {
            return;
        }
        bfoComputed = true;
        Main.debug("computing bfo for " + this + " offset=" + offset);

        for (Iterator i = getFields(); i.hasNext();)
        {
            Field f = (Field) i.next();

            if (f.isBitField())
            {
                TypeInterface ftype = BuiltinType.getBuiltinType(f.getType());
                BitFieldType bftype = (BitFieldType) ftype;
                f.bitFieldStart = f;
                f.bitFieldOffset = offset;
                f.totalBitFieldLength = bftype.getLength();
            }
        }
    }

    /**
     * return lookahead mapping
     */
    public Field.LookAhead getLookAhead()
    {
        Field.LookAhead res = null;
        for (int i = 0; i < fields.size(); i++)
        {
            Field f = (Field) fields.get(i);
            Field.LookAhead li = f.getLookAhead();
            if (li == null) // if any field is without lookahead, so is this
                            // union
                return null;

            if (res == null)
                res = li;
            else
            {
                res = res.symDifference(li);
                if (res == null)
                    return null;
            }
        }
        return res;
    }
}
