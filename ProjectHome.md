# Usage #

The most convenient way to use JCrasher is by running an Ant script like [jcrasher.xml](http://jcrasher.googlecode.com/svn/trunk/examples/jcrasher.xml):

`ant -f jcrasher.xml`

It performs all steps completely automatically.


# Example #

The Ant script performs the following steps. You can follow along with the example files included in the [examples directory](http://jcrasher.googlecode.com/svn/trunk/examples). Download the examples directory, open a command line in your copy of the examples directory, and run our Ant script:

`ant -f jcrasher.xml`

  1. jcrasher.xml compiles the Java source files under test. It looks for a file like [testees.txt](http://jcrasher.googlecode.com/svn/trunk/examples/testees.txt) and interprets every line as the location of a Java source file under test, relative to the src property in jcrasher.xml. Our testees are [trivia/DivByZero.java](http://jcrasher.googlecode.com/svn/trunk/examples/src/trivia/DivByZero.java), [trivia/ManyParameters.java](http://jcrasher.googlecode.com/svn/trunk/examples/src/trivia/ManyParameters.java), and [trivia/NullDeref.java](http://jcrasher.googlecode.com/svn/trunk/examples/src/trivia/NullDeref.java) located in the [src](http://jcrasher.googlecode.com/svn/trunk/examples/src) directory.
  1. jcrasher.xml runs JCrasher on the compiled Java classes under test. JCrasher will generate JUnit test case (as Java source files) for the classes under test. Now we should have the JUnit test cases DivByZeroTest1, DivByZeroTest2, DivByZeroTest3, ManyParametersTest1, etc. Additionally, JCrasher has generated a test suite that aggregates all generated test cases in JUnitAll. (All these files are included in the result zip file generated in step 5.)
  1. jcrasher.xml compiles the generated Java source files (the JUnit test cases).
  1. jcrasher.xml runs JCrasher's extended version of JUnit on the generated test cases. This will enhance the JUnit text output by suppressing redundant warnings.
  1. jcrasher.xml packages the results into a zip file like [examples-jcrasher-2.1.3-2007-03-17-2206.zip](http://jcrasher.googlecode.com/svn/trunk/examples/examples-jcrasher-2.1.3-2007-03-17-2206.zip). This zip file includes the original testee sources, the generated JUnit test case sources, the log output of JCrasher and JUnit, and the configuration files used: jcrasher.xml and testees.txt.

Optionally, you can clean up all generated files (except the zip file) with the following.

`ant -f jcrasher.xml clean`


# Requirements #

JCrasher should run on any machine that has a Java Virtual Machine installed.

The most convenient way to use JCrasher is by running an Ant script like jcrasher.xml. You can [download Ant for free](http://ant.apache.org).

JCrasher generates JUnit test cases. You can [download JUnit for free](http://junit.org).


# Internals #

JCrasher links against (uses) the following:

## Ant-Contrib ##
  * Version: 1.0 b1
  * License: Apache License 2.0

## FB-Crasher ##
  * License: Apache License 2.0

## GNU GetOpt ##
  * Version: 1.0.10
  * License: LGPL

## Jakarta Commons Lang ##
  * Version: 2.3
  * License: Apache License 2.0

## JUnit ##
  * Version: 3.8.1
  * License: Common Public License Version 1.0
  * Not included in the JCrasher distributions. We assume you have JUnit installed.


# Notice #

This product includes software developed by
The Apache Software Foundation (http://www.apache.org/).