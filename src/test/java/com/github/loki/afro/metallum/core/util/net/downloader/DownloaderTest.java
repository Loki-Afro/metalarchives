package com.github.loki.afro.metallum.core.util.net.downloader;

import com.github.loki.afro.metallum.MetallumException;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DownloaderTest {

    @Disabled
    @Test
    public void downloadImage() throws IOException, ExecutionException {
        final BufferedImage testImage = Downloader.getImage("http://i.imgur.com/4Hkmk.png");
        final BufferedImage controlimage = ImageIO.read(getClass().getClassLoader().getResource("de/loki/metallum/core/util/net/downloader/gnu.png"));
        assertThat(compareImages(controlimage, testImage)).isTrue();
    }

    private boolean compareImages(final BufferedImage controlImage, final BufferedImage testImage) {
        final int columns = controlImage.getWidth();
        final int rows = controlImage.getHeight();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int controlRgb = controlImage.getRGB(col, row);
                int testRgb = testImage.getRGB(col, row);
                if (controlRgb != testRgb) {
                    return false;
                }
            }
        }
        return true;
    }

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
