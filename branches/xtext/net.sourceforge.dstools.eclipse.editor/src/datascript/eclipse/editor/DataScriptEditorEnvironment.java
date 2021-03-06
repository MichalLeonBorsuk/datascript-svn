/* BSD License
 *
 * Copyright (c) 2007, Harald Wellmann, Harman/Becker Automotive Systems
 * All rights reserved.
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
 *     * Neither the name of Harman/Becker Automotive Systems 
 *       nor the names of their contributors may be used to
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
package datascript.eclipse.editor;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.Token;

public class DataScriptEditorEnvironment
{
    private static DataScriptColorProvider colorProvider;

    private static DataScriptCodeScanner codeScanner;

    private static int refCount = 0;

    /**
     * A connection has occured - initialize the receiver if it is the first
     * activation.
     */
    public static void connect()
    {
        if (++refCount == 1)
        {
            colorProvider = new DataScriptColorProvider();
            codeScanner = new DataScriptCodeScanner(colorProvider);
            codeScanner.setDefaultReturnToken(new Token(new TextAttribute(
                    colorProvider.getColor(DataScriptColorConstants.DEFAULT))));
        }
    }

    /**
     * A disconnection has occured - clear the receiver if it is the last
     * deactivation.
     */
    public static void disconnect()
    {
        if (--refCount == 0)
        {
            colorProvider.dispose();
            colorProvider = null;
            codeScanner = null;
        }
    }

    /**
     * Returns the singleton color provider.
     */
    public static DataScriptColorProvider getColorProvider()
    {
        return colorProvider;
    }

    /**
     * Returns the singleton scanner.
     */
    public static DataScriptCodeScanner getCodeScanner()
    {
        return codeScanner;
    }
}
