/**
 * 
 */
package org.dgfoundation.amp.newreports;

import java.util.Objects;


/**
 * A report element that can be either a NamedTypedEntity or an element of type YEAR, QUARTER, MONTH
 * @author Nadejda Mandrescu
 */
public class ReportElement {
	public enum ElementType {
		ENTITY,
		YEAR,
		QUARTER,
		MONTH,
		DATE;
	};
	
	/** Report element type. If it is ENTITY type, then {@link #entity} is specified */
	public final ElementType type;
	/** Report column or measure, if {@link #type} is ENTITY. Otherwise null. */
	public final NamedTypedEntity entity;
	
	/**
	 * Constructs a report element as a NamedTypedEntity
	 * @param entity
	 */
	public ReportElement(NamedTypedEntity entity) {
		this(entity, ElementType.ENTITY);
	}
	
	/**
	 * No entity report element
	 * @param type - anything, except ENTITY
	 */
	public ReportElement(ElementType type) {
		this(null, type);
	}
	
	private ReportElement(NamedTypedEntity entity, ElementType type) {
		this.entity = entity;
		this.type = type;
	}

	@Override
	public boolean equals(Object o) {
		return 	Objects.deepEquals(this, o);
	}
	
	@Override
	public String toString() {
		return "ElementType = " + type + ", NamedTypedEntity =[" + (entity == null ? "null]" : entity.getEntityName() + "] of type " + entity.getEntityType());  
	}
}
