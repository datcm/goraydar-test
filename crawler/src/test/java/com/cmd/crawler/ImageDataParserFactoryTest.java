package com.cmd.crawler;

import org.jsoup.Jsoup;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by caomanhdat on 6/26/16.
 */
public class ImageDataParserFactoryTest {
    @Test
    public void testTookapic() throws IOException, ImageDataParserFactory.ParserException {
        ImageDataParserFactory dataParserFactory = ImageDataParserFactory.create("https://tookapic.com/photos/74987");
        ImageData imageData = dataParserFactory.parse(Jsoup.connect("https://tookapic.com/photos/74987").timeout(0).get());
        assertEquals("Another scene", imageData.title);
        assertEquals("This is the final image for the tutorial I'm working on. It should be ready next week.", imageData.description);
        assertTrue(Arrays.asList(imageData.tags).contains("studio"));
    }
}
