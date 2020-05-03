package net.tifoha;

import cern.colt.list.LongArrayList;
import net.tifoha.utils.io.FstSerializationHelper;
import net.tifoha.utils.io.SerializationHelper;
import net.tifoha.utils.io.SerializationHelper.Deserializer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws URISyntaxException, IOException {
//        URL url = App.class.getClassLoader().getResource("test.txt");
//        System.out.println(url);
//        Files.lines(Paths.get(url.toURI())).forEach(System.out::println);
//        System.out.println(System.getProperty("aaa"));
        SerializationHelper helper = new FstSerializationHelper(16777216);
        Path path = Paths.get("\\ip2data\\data\\ipdata\\lucene\\ipnetranges\\ipnetranges.jdat2");
        InputStream in = Files.newInputStream(path);
        final int approximateSize = (int) (Files.size(path) / 8);
        System.out.println("approximateSize = " + approximateSize);
        Deserializer deserializer = helper.getDeserializer(in);
        LongArrayList list = new LongArrayList(approximateSize);
        AtomicInteger actualSize = new AtomicInteger();
        deserializer.longStream().forEach(l -> {
            actualSize.incrementAndGet();
            list.add(l);
        });
        list.trimToSize();
        System.out.println("actualSize = " + actualSize);
        System.out.println("list size = " + list.size());
        System.out.println();
    }
}
