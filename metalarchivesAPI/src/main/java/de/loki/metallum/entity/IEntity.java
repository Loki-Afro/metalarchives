package de.loki.metallum.entity;

public interface IEntity extends Cloneable {
	public abstract long getId();

	public abstract void setId(long id);

	public abstract void setName(final String name);

	public abstract String getName();

	public abstract Object clone() throws CloneNotSupportedException;

	public abstract String toXml(final String... omitFields);

	@Override
	public abstract boolean equals(Object obj);

	@Override
	public abstract int hashCode();

}
