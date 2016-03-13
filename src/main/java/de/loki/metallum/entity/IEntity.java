package de.loki.metallum.entity;

public interface IEntity extends Cloneable {
	long getId();

	void setId(long id);

	void setName(final String name);

	String getName();

	@Override
	boolean equals(Object obj);

	@Override
	int hashCode();

}
