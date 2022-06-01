package com.github.loki.afro.metallum.core.util.net.downloader;

import com.github.loki.afro.metallum.MetallumException;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DownloaderTest {

    @Test
    public void downloadHtml() {
        final String downloadedHtml = Downloader.getHTML("http://pastebin.com/raw.php?i=iEjw3swD").replaceAll("[\r|\n]", "");
        assertThat(downloadedHtml).isEqualTo("<html><head><title>Riesenzwerg</title></head><body><h1>Hallo Welt!</h1><strong>Fett</strong><em>Kursiv</em></body></html>");
        assertThat(Jsoup.parse(downloadedHtml).text()).isEqualTo("Riesenzwerg Hallo Welt!FettKursiv");
    }

    @Test
    public void downloadExceptionTest() {
        assertThatThrownBy(() -> Downloader.getHTML("http://past7878787877878787ebin.com/raw.php?i=iEjw3swD"))
                .isInstanceOf(MetallumException.class);
    }

}
