package de.loki.metallum.entity;

public abstract class AbstractEntity implements IEntity {

	protected String				name;
	protected long					id				= 0;
	protected String				addedBy			= "";
	protected String				modifiedBy		= "";
	protected String				addedOn			= "";
	protected String				lastModifiedOn	= "";

	public AbstractEntity(final long id, final String name) {
		this.id = id;
		this.name = name;
	}

	public AbstractEntity(final long id) {
		this.id = id;
		this.name = "";
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setId(final long newId) {
		this.id = newId;
	}

	@Override
	public long getId() {
		return this.id;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return ((this.name != null ? this.name : "") + " [id=" + this.id + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (this.id ^ (this.id >>> 32));
		result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AbstractEntity other = (AbstractEntity) obj;
		if (this.id != other.id) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the addedBy
	 */
	public final String getAddedBy() {
		return this.addedBy;
	}

	/**
	 * @return the modifiedBy
	 */
	public final String getModifiedBy() {
		return this.modifiedBy;
	}

	/**
	 * @return the addedOn
	 */
	public final String getAddedOn() {
		return this.addedOn;
	}

	/**
	 * @return the lastModifiedOn
	 */
	public final String getLastModifiedOn() {
		return this.lastModifiedOn;
	}

	/**
	 * @param addedBy the addedBy to set
	 */
	public final void setAddedBy(final String addedBy) {
		this.addedBy = addedBy;
	}

	/**
	 * @param modifiedBy the modifiedBy to set
	 */
	public final void setModifiedBy(final String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	/**
	 * @param addedOn the addedOn to set
	 */
	public final void setAddedOn(final String addedOn) {
		this.addedOn = addedOn;
	}

	/**
	 * @param lastModifiedOn the lastModifiedOn to set
	 */
	public final void setLastModifiedOn(final String lastModifiedOn) {
		this.lastModifiedOn = lastModifiedOn;
	}

}
