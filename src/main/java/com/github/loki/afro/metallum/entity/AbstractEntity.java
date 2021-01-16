package com.github.loki.afro.metallum.entity;

public abstract class AbstractEntity extends AbstractIdentifiable {

    protected String addedBy = "";
    protected String modifiedBy = "";
    protected String addedOn = "";
    protected String lastModifiedOn = "";

    public AbstractEntity(final long id, final String name) {
        super(id, name);
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
