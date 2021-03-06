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
package datascript.ast;

import java.math.BigInteger;

public class IntegerValue extends Value implements Comparable
{
    private BigInteger ival;

    public IntegerValue(String ival)
    {
        this.ival = new BigInteger(ival);
    }

    public IntegerValue(BigInteger ival)
    {
        this.ival = ival;
    }

    public IntegerValue(int ival)
    {
        this.ival = new BigInteger(new Integer(ival).toString());
    }

    public BigInteger integerValue()
    {
        return (ival);
    }

    public IntegerValue add(IntegerValue v2)
    {
        return new IntegerValue(ival.add(v2.ival));
    }

    public IntegerValue subtract(IntegerValue v2)
    {
        return new IntegerValue(ival.subtract(v2.ival));
    }

    public IntegerValue multiply(IntegerValue v2)
    {
        return new IntegerValue(ival.multiply(v2.ival));
    }

    public IntegerValue divide(IntegerValue v2)
    {
        return new IntegerValue(ival.divide(v2.ival));
    }

    public IntegerValue remainder(IntegerValue v2)
    {
        return new IntegerValue(ival.remainder(v2.ival));
    }

    public IntegerValue and(IntegerValue v2)
    {
        return new IntegerValue(ival.and(v2.ival));
    }

    public IntegerValue or(IntegerValue v2)
    {
        return new IntegerValue(ival.or(v2.ival));
    }

    public IntegerValue xor(IntegerValue v2)
    {
        return new IntegerValue(ival.xor(v2.ival));
    }

    public IntegerValue shiftLeft(IntegerValue v2)
    {
        return new IntegerValue(ival.shiftLeft(v2.ival.intValue()));
    }

    public IntegerValue shiftRight(IntegerValue v2)
    {
        return new IntegerValue(ival.shiftRight(v2.ival.intValue()));
    }

    public IntegerValue not()
    {
        return new IntegerValue(ival.not());
    }

    public IntegerValue negate()
    {
        return new IntegerValue(ival.negate());
    }

    public int compareTo(Object obj)
    {
        IntegerValue ivalobject = (IntegerValue) obj;
        return this.ival.compareTo(ivalobject.ival);
    }

    public String toString()
    {
        return ival.toString() + "L";
    }

    public TypeInterface getType()
    {
        return IntegerType.integerType;
    }
}
