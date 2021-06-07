package com.xylope.stream_ex;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamExam {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println(Color.CYAN + "create stream");
        createStream(); //create stream
        System.out.println(Color.CYAN + "connect stream");
        connectStream(); //connect stream

        //intermediate operations
        System.out.println(Color.YELLOW + "\nChapter - intermediate operations");
        System.out.println(Color.CYAN + "filtering stream");
        filteringStream(); //filtering stream
        System.out.println(Color.CYAN + "mapping stream");
        mappingStream(); //mapping stream
        flatMappingStream(); //flat mapping stream (nested structure)
        System.out.println(Color.CYAN + "sorting stream");
        sortStreamAsc();
        sortStreamLength();
        System.out.println(Color.CYAN + "peeking stream");
        peekStream();
    }

    private static void peekStream() throws InterruptedException {
        Stream<String> wordStream =  Stream.of("grass", "of", "frog", "cancel", "senorita", "sky", "pneumococcus");

        List<String> wordList = new ArrayList<>();
        Stream<String> stream = wordStream.peek(wordList::add);
        printStream(stream, "wordStream");
        printStream(wordList.stream(), "wordList");
    }

    private static void sortStreamLength() {
        Stream<String> wordStream = Stream.of("grass", "of", "frog", "cancel", "senorita", "sky", "pneumococcus");
        printStream(wordStream.sorted((Comparator.comparingInt(String::length))), "wordStream");
    }

    private static void sortStreamAsc() {
        Stream<Integer> numberStream = Stream.iterate(0, n -> new Random().nextInt(100)).limit(5);
        printStream(numberStream.sorted(), "numberStream");
    }

    private static void flatMappingStream() {
        List<List<String>> doubleDimensionStream = Arrays.asList(
                Arrays.asList("a1", "a2", "a3", "a4"),
                Arrays.asList("b1", "b2", "b3", "b4"),
                Arrays.asList("c1", "c2", "c3", "c4"),
                Arrays.asList("d1", "d2", "d3", "d4"));

        printStream(doubleDimensionStream.stream()
                .flatMap(Collection::stream), "doubleDimensionStream");
    }

    private static void mappingStream() {
        Stream<String> nameStream = Stream.of("Steven Kyle", "Elena Cruz", "Shine Alice", "Steven Kun", "Tom Cruz", "Stanly Dia", "Alexa Sept");
        printStream(nameStream.map(String::toUpperCase), "upperCaseNameStream");
    }

    private static void filteringStream() {
        Stream<String> nameStream = Stream.of("Steven Kyle", "Elena Cruz", "Shine Alice", "Steven Kun", "Tom Cruz", "Stanly Dia", "Alexa Sept");
        Stream<String> filteringStream = nameStream.filter(name -> name.contains("Steven"));
        printStream(filteringStream, "filteringStream");
    }

    private static void connectStream() {
        Stream<String> nameStream = Stream.of("Steven Kyle", "Elena", "Alice", "Steven Kun", "Tom", "Stanly", "Alexa");
        Stream<String> concatStream = Stream.concat(nameStream, Stream.of("Seraphin"));
        printStream(concatStream, "concatStream");
    }

    public static void createStream() throws IOException {
        String[] nameArr = {"Steve", "Elena", "Alice", "Kun", "Tom", "Stanly", "Alexa"};
        List<String> nameList = Arrays.asList(nameArr);

        AtomicInteger count = new AtomicInteger(29);

        Stream<String> nameArrStream = Arrays.stream(nameArr);
        Stream<String> nameListStream = nameList.stream();
        Stream<String> nameStringStream = Pattern.compile(", ").splitAsStream("Steve, Elena, Alice, Kun, Tom, Stanly, Alexa");
        Stream<String> nameFileStream = Files.lines(Paths.get("src/com/xylope/stream_ex/fileStream.txt"), StandardCharsets.UTF_8);

        Stream<String> emptyStream = Stream.empty();

        Stream<Integer> numberBuilderStream = Stream.<Integer>builder()
                .add(30)
                .add(31)
                .add(32)
                .add(33)
                .add(34)
                .build();
        Stream<Integer> numberGenerateStream = Stream.generate(count::incrementAndGet).limit(5);
        Stream<Integer> numberIterateStream = Stream.iterate(30, n -> n+1).limit(5);
        Stream<Integer> numberIntStream = IntStream.range(30, 35).boxed();

        //check is created
        printStream(nameArrStream, "nameArrStream");
        printStream(nameListStream, "nameListStream");
        printStream(nameStringStream, "nameStringStream");
        printStream(nameFileStream, "nameFileStream");
        printStream(emptyStream, "emptyStream");
        printStream(numberBuilderStream, "numberBuilderStream");
        printStream(numberGenerateStream, "numberGenerateStream");
        printStream(numberIterateStream, "numberIterateStream");
        printStream(numberIntStream, "numberIntStream");
    }

    public static <T> void printStream(Stream<T> stream, String streamName) {
        System.out.printf(Color.BLACK_BRIGHT  + "%25s - | ", streamName);
        stream.forEach((el) -> System.out.print(el + " | "));
        System.out.println();
    }
}
