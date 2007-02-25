/*
 * PrimitiveLiteral.java
 * 
 * Copyright 2002 Christoph Csallner and Yannis Smaragdakis.
 */
package edu.gatech.cc.jcrasher.plans.expr.literals;

import static edu.gatech.cc.jcrasher.Assertions.notNull;
import edu.gatech.cc.jcrasher.plans.expr.Expression;


/**
 * Represents how to instantiate a wrapper-object for a java primtive type.
 * 
 * <p>
 * Unless otherwise noted:
 * <ul>
 * <li>Each reference parameter of every method must be non-null.
 * <li>Each reference return value must be non-null.
 * 
 * @param <T> boxed version of this literal's type.
 * 
 * @author csallner@gatech.edu (Christoph Csallner)
 * http://java.sun.com/docs/books/jls/third_edition/html/lexical.html#3.10
 */
public abstract class PrimitiveLiteral<T> implements Expression<T> {

  protected T value = null;     // result of plan execution
  
  /**
   * Constructor
   * 
   * @param pValue hardcoded primitive value, not via java-wrapper-constructor -
   *          never null
   */
  public PrimitiveLiteral(final T pValue) {   
    value = notNull(pValue);
  }

  
  public T execute() { // never returns null
    return notNull(value);
  }
  
  @Override
  public String toString() {
    return value.toString();
  }

  /**
   * How to reproduce this value=object?
   */
  public String toString(final Class<?> testee) {
  	notNull(testee);
    return toString();
  }
}