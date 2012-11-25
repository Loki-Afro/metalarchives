package de.loki.metallum.core.parser.site;

import java.util.concurrent.ExecutionException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.loki.metallum.core.util.net.downloader.Downloader;
import de.loki.metallum.entity.AbstractEntity;

public abstract class AbstractSiteParser<T extends AbstractEntity> {

	protected final String		html;
	protected final Document	doc;
	protected final boolean		loadImage;
	protected final boolean		loadLinks;
	protected final T			entity;

	public AbstractSiteParser(final T entity, final boolean loadImage, final boolean loadLinks) throws ExecutionException {
		this.entity = entity;
		this.loadImage = loadImage;
		this.loadLinks = loadLinks;
		this.html = Downloader.getHTML(getSiteURL());
//		may we should mark html as deprecated
		this.doc = Jsoup.parse(this.html);
	}

	public abstract T parse();

	/**
	 * To download the Specific Site as HTML content, only used in the Abstract Constructor.
	 * 
	 * @return the URL for the specific site
	 */
	protected abstract String getSiteURL();

	protected T parseModfications(final T entity) {
		final Element footer = this.doc.getElementById("auditTrail");
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

	/**
	 * Convenience Method to extract an image URL from html.
	 * 
	 * @param element the wrapping Element.
	 * @param cssClass where the image link is in.
	 * @return
	 */
	protected final String parseImageURL(final Element element, final String cssClass) {
		String imageURL = null;
		Elements elements = this.doc.getElementsByClass(cssClass);
		if (!elements.isEmpty()) {
			Element imgElement = elements.first().select("img[src~=(?i)\\.(png|jpe?g|gif)]").first();
			imageURL = imgElement.attr("src");
		}
		return imageURL;
	}

}
