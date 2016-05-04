package com.globalforge.infix;

import java.util.Collections;
import java.util.List;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*-
 The MIT License (MIT)

 Copyright (c) 2016 Global Forge LLC

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */
/**
 * Extension of antr bases class allowing for more control over parser error
 * reporting.
 *
 * @see BaseErrorListener
 * @author Michael
 */
public class FixRulesParserErrorListener extends BaseErrorListener {
    /** logger */
    final static Logger logger = LoggerFactory.getLogger(FixRulesParserErrorListener.class);
    public static final FixRulesParserErrorListener INSTANCE = new FixRulesParserErrorListener();

    /**
     * @see BaseErrorListener#reportAmbiguity
     */
    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
        int charPositionInLine, String msg, RecognitionException e) {
        List<String> stack = ((Parser) recognizer).getRuleInvocationStack();
        Collections.reverse(stack);
        String logMsg = "Parser ERROR: line " + line + ":" + charPositionInLine + " at "
            + offendingSymbol + ": " + msg;
        CommonToken tok = (CommonToken) offendingSymbol;
        String s = tok.getText();
        logMsg += ": offending token " + s;
        if (s.equals("<EOF>")) {
            logMsg += ". Look for tag=(null or empty).";
        } else {
            try {
                Integer.parseInt(s);
                logMsg += " may be tag missing '&'.";
            } catch (NumberFormatException ex) {
                logMsg += " not a number. ";
            }
        }
        FixRulesParserErrorListener.logger.error(logMsg + " Tree = {}", stack);
        throw new RuntimeException(logMsg);
    }
}
