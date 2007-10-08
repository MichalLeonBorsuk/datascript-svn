<#--
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
-->
<#include "FileHeader.inc.ftl">
// DS-Import
${packageImports}


public class ${className} implements ${rootPackageName}.__Visitor.Acceptor, Writer, SizeOf
{
    long __fpos;
    Object __objectChoice;
    int __choiceTag = -1;


    public void accept(${rootPackageName}.__Visitor visitor, Object arg)
    {
        visitor.visit(this, arg);
    }


    public int sizeof() 
    {
        return ${rootPackageName}.__SizeOf.sizeof(this);
    }


    public int getChoiceTag()
    {
        return __choiceTag;
    }


<#list fields as field>
    public static final int CHOICE_${field.name} = ${field_index};
</#list>


    public boolean equals(Object obj)
    {
        if (obj instanceof ${className})
        {
            ${className} that = (${className}) obj;

<#if equalsCanThrowExceptions>
            if (this.__choiceTag != that.__choiceTag)
                throw new RuntimeException("Field '__choiceTag' is not equal!"); 
            if (!this.__objectChoice.equals(that.__objectChoice))
                throw new RuntimeException("Field '__objectChoice' is not equal!");
            return true;
<#else>
            return (this.__choiceTag == that.__choiceTag) && 
                this.__objectChoice.equals(that.__objectChoice);
</#if>
        }
        return super.equals(obj);
    }

