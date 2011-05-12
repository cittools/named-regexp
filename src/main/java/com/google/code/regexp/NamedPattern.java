/************************************************************************************
 * Copyright (c) 2004-2011,  Thales Corporate Services SAS                          *
 * Author: Robin Jarry                                                              *
 *                                                                                  *
 * The MIT License                                                                  *
 *                                                                                  *
 * Permission is hereby granted, free of charge, to any person obtaining a copy     *
 * of this software and associated documentation files (the "Software"), to deal    *
 * in the Software without restriction, including without limitation the rights     *
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell        *
 * copies of the Software, and to permit persons to whom the Software is            *
 * furnished to do so, subject to the following conditions:                         *
 *                                                                                  *
 * The above copyright notice and this permission notice shall be included in       *
 * all copies or substantial portions of the Software.                              *
 *                                                                                  *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR       *
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,         *
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE      *
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER           *
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,    *
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN        *
 * THE SOFTWARE.                                                                    *
 ************************************************************************************/
package com.google.code.regexp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamedPattern {

    private static final Pattern NAMED_GROUP_PATTERN = Pattern.compile("\\(\\?P<(\\w+)>");

    private Pattern pattern;
    private String namedPattern;
    private List<String> groupNames;

    public static NamedPattern compile(String regex) {
        return new NamedPattern(regex, 0);
    }

    public static NamedPattern compile(String regex, int flags) {
        return new NamedPattern(regex, flags);
    }

    private NamedPattern(String regex, int flags) {
        namedPattern = regex;
        pattern = buildStandardPattern(regex, flags);
        groupNames = extractGroupNames(regex);
    }

    public int flags() {
        return pattern.flags();
    }

    public NamedMatcher matcher(CharSequence input) {
        return new NamedMatcher(this, input);
    }

    Pattern pattern() {
        return pattern;
    }

    public String standardPattern() {
        return pattern.pattern();
    }

    public String namedPattern() {
        return namedPattern;
    }

    public List<String> groupNames() {
        return groupNames;
    }

    public String[] split(CharSequence input, int limit) {
        return pattern.split(input, limit);
    }

    public String[] split(CharSequence input) {
        return pattern.split(input);
    }

    public String toString() {
        return namedPattern;
    }

    static List<String> extractGroupNames(String namedPattern) {
        List<String> groupNames = new ArrayList<String>();
        Matcher matcher = NAMED_GROUP_PATTERN.matcher(namedPattern);
        while (matcher.find()) {
            groupNames.add(matcher.group(1));
        }
        return groupNames;
    }

    static Pattern buildStandardPattern(String namedPattern, int flags) {
        String standardPattern = NAMED_GROUP_PATTERN.matcher(namedPattern).replaceAll("(");
        return Pattern.compile(standardPattern, flags);
    }

}
