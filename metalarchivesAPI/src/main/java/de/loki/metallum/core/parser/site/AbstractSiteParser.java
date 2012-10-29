package de.loki.metallum.core.parser.site;

import java.util.concurrent.ExecutionException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.loki.metallum.core.util.net.downloader.Downloader;
import de.loki.metallum.entity.AbstractEntity;

public abstract class AbstractSiteParser<T extends AbstractEntity> {

	protected final String	html;
	protected final boolean	loadImage;
	protected final boolean	loadLinks;
	protected final T		entity;

	public AbstractSiteParser(final T entity, final boolean loadImage, final boolean loadLinks) throws ExecutionException {
		this.entity = entity;
		this.loadImage = loadImage;
		this.loadLinks = loadLinks;
		this.html = Downloader.getHTML(getSiteURL());
	}

	public abstract T parse();

	/**
	 * To download the Specific Site as HTML content, only used in the Abstract Constructor.
	 * 
	 * @return the URL for the specific site
	 */
	protected abstract String getSiteURL();

	protected T parseModfications(final T entity) {
		final Document document = Jsoup.parse(this.html);
		final Element footer = document.getElementById("auditTrail");
		final Elements elements = footer.select("td");
		entity.setAddedBy(modificationElementToString(elements.get(0)));
		entity.setModifiedBy(modificationElementToString(elements.get(1)));
		entity.setAddedOn(modificationElementToString(elements.get(2)));
		entity.setLastModifiedOn(modificationElementToString(elements.get(3)));
		return entity;
	}

	/**
	 * 
	 * @param element
	 * @return the parsed element behind the ":", N/A and (Unknown User) is also possible
	 */
	private final String modificationElementToString(final Element element) {
		String text = element.text();
		text = text.replaceAll(".*?:\\s", "");
		return text;
	}

}
