package net.tifoha;

import java.awt.peer.ListPeer;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws URISyntaxException, IOException {
//        URL url = App.class.getClassLoader().getResource("test.txt");
//        System.out.println(url);
//        Files.lines(Paths.get(url.toURI())).forEach(System.out::println);
        System.out.println(System.getProperty("aaa"));
    }
}
