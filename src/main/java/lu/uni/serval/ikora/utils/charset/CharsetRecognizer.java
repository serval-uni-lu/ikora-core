// © 2016 and later: Unicode, Inc. and others.
// License & terms of use: http://www.unicode.org/copyright.html#License
/**
 * ******************************************************************************
 * Copyright (C) 2005-2012, International Business Machines Corporation and    *
 * others. All Rights Reserved.                                                *
 * ******************************************************************************
 */
package lu.uni.serval.ikora.utils.charset;

/**
 * Abstract class for recognizing a single charset.
 * Part of the implementation of ICU's CharsetDetector.
 * <p>
 * Each specific charset that can be recognized will have an instance
 * of some subclass of this class.  All interaction between the overall
 * CharsetDetector and the stuff specific to an individual charset happens
 * via the interface provided here.
 * <p>
 * Instances of CharsetDetector DO NOT have or maintain
 * state pertaining to a specific match or detect operation.
 * The WILL be shared by multiple instances of CharsetDetector.
 * They encapsulate const charset-specific information.
 */
abstract class CharsetRecognizer {

    abstract String getName();
    public String getLanguage() {
        return null;
    }
    abstract CharsetMatch match(CharsetDetector det);

}