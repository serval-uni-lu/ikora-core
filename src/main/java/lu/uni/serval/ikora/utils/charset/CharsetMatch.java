// © 2016 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html#License
/**
 * ******************************************************************************
 * Copyright (C) 2005-2016, International Business Machines Corporation and    *
 * others. All Rights Reserved.                                                *
 * ******************************************************************************
 */
package lu.uni.serval.ikora.utils.charset;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


public class CharsetMatch implements Comparable<CharsetMatch> {


    //
    //   Private Data
    //
    private int fConfidence;
    private byte[] fRawInput = null;     // Original, untouched input bytes.
    //  If user gave us a byte array, this is it.
    private int fRawLength;           // Length of data in fRawInput array.
    private InputStream fInputStream = null;  // User's input stream, or null if the user
    private String fCharsetName;         // The name of the charset this CharsetMatch
    //   represents.  Filled in by the recognizer.
    private String fLang;                // The language, if one was determined by

    /*
     *  Constructor.  Implementation internal
     */
    CharsetMatch(CharsetDetector det, CharsetRecognizer rec, int conf) {
        fConfidence = conf;

        // The references to the original application input data must be copied out
        //   of the charset recognizer to here, in case the application resets the
        //   recognizer before using this CharsetMatch.
        if (det.fInputStream == null) {
            // We only want the existing input byte data if it came straight from the user,
            //   not if is just the head of a stream.
            fRawInput = det.fRawInput;
            fRawLength = det.fRawLength;
        }
        fInputStream = det.fInputStream;
        fCharsetName = rec.getName();
        fLang = rec.getLanguage();
    }

    /*
     *  Constructor.  Implementation internal
     */
    CharsetMatch(CharsetDetector det, CharsetRecognizer rec, int conf, String csName, String lang) {
        fConfidence = conf;

        // The references to the original application input data must be copied out
        //   of the charset recognizer to here, in case the application resets the
        //   recognizer before using this CharsetMatch.
        if (det.fInputStream == null) {
            // We only want the existing input byte data if it came straight from the user,
            //   not if is just the head of a stream.
            fRawInput = det.fRawInput;
            fRawLength = det.fRawLength;
        }
        fInputStream = det.fInputStream;
        fCharsetName = csName;
        fLang = lang;
    }

    public Reader getReader() {
        InputStream inputStream = fInputStream;

        if (inputStream == null) {
            inputStream = new ByteArrayInputStream(fRawInput, 0, fRawLength);
        }

        try {
            inputStream.reset();
            return new InputStreamReader(inputStream, getName());
        } catch (IOException e) {
            return null;
        }
    }

    public String getString() throws java.io.IOException {
        return getString(-1);

    }

    public String getString(int maxLength) throws java.io.IOException {
        String result = null;
        if (fInputStream != null) {
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[1024];
            Reader reader = getReader();
            int max = maxLength < 0 ? Integer.MAX_VALUE : maxLength;
            int bytesRead = 0;

            while ((bytesRead = reader.read(buffer, 0, Math.min(max, 1024))) >= 0) {
                sb.append(buffer, 0, bytesRead);
                max -= bytesRead;
            }

            reader.close();

            return sb.toString();
        } else {
            String name = getName();
            /*
             * getName() may return a name with a suffix 'rtl' or 'ltr'. This cannot
             * be used to open a charset (e.g. IBM424_rtl). The ending '_rtl' or 'ltr'
             * should be stripped off before creating the string.
             */
            int startSuffix = name.indexOf("_rtl") < 0 ? name.indexOf("_ltr") : name.indexOf("_rtl");
            if (startSuffix > 0) {
                name = name.substring(0, startSuffix);
            }
            result = new String(fRawInput, name);
        }
        return result;

    }

    public int getConfidence() {
        return fConfidence;
    }

    public String getName() {
        return fCharsetName;
    }
    //   gave us a byte array.

    public String getLanguage() {
        return fLang;
    }

    public int compareTo(CharsetMatch other) {
        int compareResult = 0;
        if (this.fConfidence > other.fConfidence) {
            compareResult = 1;
        } else if (this.fConfidence < other.fConfidence) {
            compareResult = -1;
        }
        return compareResult;
    }

    public boolean equals(Object o) {
        if (o instanceof CharsetMatch) {
            CharsetMatch that = (CharsetMatch) o;
            return (this.fConfidence == that.fConfidence);
        }

        return false;
    }

    public int hashCode() {
        return fConfidence;
    }
    //   gave us a byte array.

    public String toString() {
        String s = "Match of " + fCharsetName;
        if (getLanguage() != null) {
            s += " in " + getLanguage();
        }
        s += " with confidence " + fConfidence;
        return s;
    }
}