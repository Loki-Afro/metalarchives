package de.loki.metallum.core.parser.site.helper.band;

import java.util.concurrent.ExecutionException;

import de.loki.metallum.core.util.net.MetallumURL;
import de.loki.metallum.core.util.net.downloader.Downloader;
import de.loki.metallum.entity.Disc;
import de.loki.metallum.enums.DiscType;

public final class DiscParser {
	public final Disc[] parse(final long bandId) throws ExecutionException {
		final String tmpHtml = Downloader.getHTML(MetallumURL.assembleDiscographyURL(bandId));
		final String[] discHtml = tmpHtml.substring(tmpHtml.indexOf("<tbody>") + 7, tmpHtml.indexOf("</tbody>")).split("<tr>");

		final Disc[] discs = new Disc[discHtml.length - 1];
		// Don't know what that means?!
		if (discHtml.length == 1) {
			return new Disc[0];
		}

		for (int i = 1; i < discHtml.length; i++) {
			final Disc disc;
			final String[] parts = discHtml[i].split("<td");

			disc = new Disc(parseDiscId(parts[1]));
			disc.setName(parseDiscName(parts[1]));
			disc.setDiscType(parseDiscType(parts[2]));
			disc.setReleaseDate(parseDiscDate(parts[3]));

			discs[i - 1] = disc;
		}
		return discs;
	}

	private final String parseDiscDate(final String htmlPart) {
		final String discDate = htmlPart.substring(htmlPart.indexOf(">") + 1, htmlPart.indexOf("<"));
		return discDate;
	}

	private final long parseDiscId(final String htmlPart) {
		String id = htmlPart.substring(htmlPart.indexOf("/albums/") + 8);
		id = id.substring(0, id.indexOf("\" class=\""));
		id = id.substring(id.lastIndexOf("/") + 1, id.length());
		return Long.parseLong(id);
	}

	private final DiscType parseDiscType(final String htmlPart) {
		final String discType = htmlPart.substring(htmlPart.indexOf(">") + 1, htmlPart.indexOf("<"));
		return DiscType.getTypeDiscTypeForString(discType);

	}

	private final String parseDiscName(final String htmlPart) {
		final String name = htmlPart.substring(htmlPart.indexOf(">", 10) + 1, htmlPart.indexOf("</a>"));
		return name;
	}

}
