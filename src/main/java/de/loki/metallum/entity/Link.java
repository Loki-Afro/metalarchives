package de.loki.metallum.entity;

import de.loki.metallum.enums.LinkCategory;

public class Link {
	private String       url      = "";
	private LinkCategory category = LinkCategory.ANY;
	private String       name     = "";

	public Link() {
	}

	public Link(String url, String name) {
		this.url = url;
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setURL(String link) {
		this.url = link;
	}

	public void setCategory(LinkCategory cat) {
		this.category = cat;
	}

	public LinkCategory getCategory() {
		return this.category;
	}

	public String getURL() {
		return this.url;
	}

	@Override
	public String toString() {
		return this.name + " - " + this.url + " [" + this.category.asCategoryName() + "]";

	}
}
