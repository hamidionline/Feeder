package com.nononsenseapps.feeder.model;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.InputStream;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNull;

public class FeedParserTest {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void basic() throws Exception {
        SyndFeed feed = FeedParser.parseFeed("https://cowboyprogrammer.org/index.xml", tempFolder.newFolder());
        assertNotNull(feed);

        assertNull(FeedParser.selfLink(feed));

        // TODO this test should not rely on internet
        assertEquals(15, feed.getEntries().size());
    }

    @Test
    public void rss() throws Exception {
        SyndFeed feed = FeedParser.parseFeed(getRSS());

        assertEquals("http://cornucopia.cornubot.se/", feed.getLink());
        assertNull(FeedParser.selfLink(feed));

        assertEquals(25, feed.getEntries().size());
        SyndEntry entry = feed.getEntries().get(0);

        assertEquals("Tredje månaden med överhettad svensk ekonomi - tydlig säljsignal för börsen",
                FeedParser.plainTitle(entry));
        assertEquals("Tredje månaden med överhettad svensk ekonomi - tydlig säljsignal för börsen",
                FeedParser.title(entry));

        assertEquals("För tredje månaden på raken ligger Konjunkturinsitutets barometerindikator (\"konjunkturbarometern\") kvar i överhettat läge. Det råder alltså en klart och tydligt långsiktig säljsignal i enlighet med k",
                FeedParser.snippet(entry));
        assertTrue(FeedParser.description(entry).startsWith("För tredje månaden på raken"));

        assertEquals(null, FeedParser.firstEnclosure(entry));
    }

    @Test
    public void atom() throws Exception {
        SyndFeed feed = FeedParser.parseFeed(getAtom());

        assertEquals("http://cornucopia.cornubot.se/", feed.getLink());
        assertEquals("http://www.blogger.com/feeds/8354057230547055221/posts/default", FeedParser.selfLink(feed));

        assertEquals(25, feed.getEntries().size());
        SyndEntry entry = feed.getEntries().get(0);

        assertEquals("Tredje månaden med överhettad svensk ekonomi - tydlig säljsignal för börsen",
                FeedParser.plainTitle(entry));
        assertEquals("Tredje månaden med överhettad svensk ekonomi - tydlig säljsignal för börsen",
                FeedParser.title(entry));

        assertEquals("För tredje månaden på raken ligger Konjunkturinsitutets barometerindikator (\"konjunkturbarometern\") kvar i överhettat läge. Det råder alltså en klart och tydligt långsiktig säljsignal i enlighet med k",
                FeedParser.snippet(entry));
        assertTrue(FeedParser.description(entry).startsWith("För tredje månaden på raken"));

        assertEquals(null, FeedParser.firstEnclosure(entry));
    }

    private InputStream getAtom() {
        return getClass().getResourceAsStream("atom_cornucopia.xml");
    }

    private InputStream getRSS() {
        return getClass().getResourceAsStream("rss_cornucopia.xml");
    }
}
