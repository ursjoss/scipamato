/*
 * MethodTypeMapping.java
 * 
 * 22 apr 2017
 */
package com.gmail.gcolaianni5.jris.engine;

/**
 * 
 *
 * @author Gianluca Colaianni -- g.colaianni5@gmail.com
 * @version 1.0
 * @since 22 apr 2017
 */
class MethodTypeMapping implements Comparable<MethodTypeMapping>{

	private String method;
	private Class<?> clazz;
	
	/**
	 * @param method
	 * @param clazz
	 */
	public MethodTypeMapping(String method, Class<?> clazz) {
		super();
		this.method = method;
		this.clazz = clazz;
	}

	/**
	 * Return method value or reference.
	 * @return method value or reference.
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * Return clazz value or reference.
	 * @return clazz value or reference.
	 */
	public Class<?> getClazz() {
		return clazz;
	}

	/* (non-Jsdoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(MethodTypeMapping o) {
		return this.getMethod().compareTo(o.getMethod());
	}
	
	/* (non-Jsdoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MethodTypeMapping) {
			return this.compareTo((MethodTypeMapping) obj) == 0; 
		}
		return false;
	}

	/* (non-Jsdoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.method.hashCode();
	}

	/* (non-Jsdoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MethodTypeMapping [method=");
		builder.append(method);
		builder.append(", clazz=");
		builder.append(clazz);
		builder.append("]");
		return builder.toString();
	}
	
}
