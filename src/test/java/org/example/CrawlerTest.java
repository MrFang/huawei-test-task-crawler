package org.example;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CrawlerTest {
    @Test
    public void testCrawler() {
        Crawler c = new Crawler();
        c.addUrl("1");
        c.addUrl("2");
        c.addUrl("3");
        c.addUrl("123");
        c.addUrl("111");
        c.addHyperLink("1", "2");
        c.addHyperLink("1", "123");
        c.addHyperLink("2", "123");

        Assertions.assertEquals("123", c.queryTopFive("1").get(0));
        Assertions.assertEquals("2", c.queryTopFive("1").get(1));
        Assertions.assertEquals(3, c.queryListOfPagesWithGivenPrefix("1").size());
        Assertions.assertEquals(0, c.queryTopFive("123").size());
        Assertions.assertThrows(IllegalArgumentException.class, () -> c.addHyperLink("1234", "1"));
        Assertions.assertThrows(IllegalArgumentException.class, () -> c.addUrl("1"));
    }
}
