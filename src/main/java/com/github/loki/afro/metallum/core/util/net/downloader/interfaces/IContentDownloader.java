package com.github.loki.afro.metallum.core.util.net.downloader.interfaces;

import java.util.concurrent.Callable;

/**
 * @param <E> the return value of the downloader.
 */
public interface IContentDownloader<E> extends Callable<E> {
}
