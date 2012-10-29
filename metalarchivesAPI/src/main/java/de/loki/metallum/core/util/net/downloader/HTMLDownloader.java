package de.loki.metallum.core.util.net.downloader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.loki.metallum.core.util.net.downloader.interfaces.IHTMLDowloader;

public final class HTMLDownloader extends AbstractDownloader implements IHTMLDowloader {

	protected HTMLDownloader(String urlString) {
		super(urlString);
	}

	@Override
	public final String call() throws Exception {
		InputStream in = getDownloadEntity().getContent();
		BufferedReader rd = new BufferedReader(new InputStreamReader(in, HTML_CHARSET));
		StringBuilder htmlStringBuilder = new StringBuilder();
		String line;
		while ((line = rd.readLine()) != null) {
			htmlStringBuilder.append(line);
		}
		rd.close();
		in.close();

		return htmlStringBuilder.toString();
	}

}
