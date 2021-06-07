package com.xylope.stream_ex;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.ObjIntConsumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamExam {

    public static void main(String[] args) throws IOException {
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

        //terminal operations
        System.out.println(Color.YELLOW + "\nChapter - terminal operations");
        System.out.println(Color.CYAN + "calculate stream");
        calculateStream();
        System.out.println(Color.CYAN + "reduction stream");
        reductionStream();
        System.out.println(Color.CYAN + "collect stream");
        collectStream();
        System.out.println(Color.CYAN + "match stream");
        matchStream();
    }

    private static void matchStream() {
        Stream<Integer> intStream = IntStream.rangeClosed(1, 10).boxed();
        printStream(intStream, "intStream");

        intStream = IntStream.rangeClosed(1, 10).boxed();
        boolean allMatch = intStream.allMatch(n -> n <= 10);
        intStream = IntStream.rangeClosed(1, 10).boxed();
        boolean anyMatch = intStream.anyMatch(n -> n == 3);
        intStream = IntStream.rangeClosed(1, 10).boxed();
        boolean noneMatch = intStream.noneMatch(n -> n > 10);

        System.out.printf("             allMatch : %b\n", allMatch);
        System.out.printf("             anyMatch : %b\n", anyMatch);
        System.out.printf("             noneMatch : %b\n", noneMatch);
    }

    private static void collectStream() {
        Stream<String> wordStream = Stream.of("grass", "of", "frog", "cancel", "senorita", "sky", "pneumococcus");
        printStream(wordStream, "wordStream");

        wordStream = Stream.of("grass", "of", "frog", "cancel", "senorita", "sky", "pneumococcus");
        List<String> wordList = wordStream.collect(Collectors.toList());

        System.out.printf("%25s - |", "wordList");
        wordList.forEach(el -> System.out.printf(" %s |", el));
        System.out.println();

        wordStream = Stream.of("grass", "of", "frog", "cancel", "senorita", "sky", "pneumococcus");
        String wordString = wordStream.collect(
                Collectors.joining(" | ", String.format("%25s - ", "wordString"), " |"));

        System.out.println(wordString);

        Stream<Integer> numberStream = IntStream.of(1, 2, 3, 4, 5, 10, 10).boxed();
        Double avg =  numberStream.collect(Collectors.averagingInt(value -> value));
        numberStream = IntStream.of(1, 2, 3, 4, 5, 10, 10).boxed();
        printStream(numberStream, "numberStream");

        System.out.printf("             avg : %f (%b)\n", avg, avg == 5);

        IntStream intStream = IntStream.of(1, 2, 3, 4, 5, 10, 10);
        int sum = intStream.sum();

        System.out.printf("             sum : %d (%b)\n", sum, sum == 35);

        numberStream = IntStream.of(1, 2, 3, 4, 5, 10, 10).boxed();
        IntSummaryStatistics statistics = numberStream.collect(Collectors.summarizingInt(value -> value));


        System.out.print("      int statistics      \n");
        System.out.printf("             cnt : %d (%b)\n", statistics.getCount(), statistics.getCount() == 7);
        System.out.printf("             sum : %d (%b)\n", statistics.getSum(), statistics.getSum() == 35);
        System.out.printf("             avg : %f (%b)\n", statistics.getAverage(), statistics.getAverage() == 5);
        System.out.printf("             min : %d (%b)\n", statistics.getMin(), statistics.getMin() == 1);
        System.out.printf("             max : %d (%b)\n", statistics.getMax(), statistics.getMax() == 10);
    }

    private static void reductionStream() {
        IntStream numberStream = IntStream.rangeClosed(1, 10);
        printStream(numberStream.boxed(), "numberStream");
        numberStream = IntStream.rangeClosed(1, 10);
        numberStream.reduce(Integer::sum).ifPresent(value ->
            System.out.printf("             (OneParam) sum : %d (%b)\n", value, value == 5));

        numberStream = IntStream.range(1, 10);
        int value = numberStream.reduce(10, Integer::sum);
        System.out.printf("             (TwoParams) sum : %d (%b)\n", value, value == 55);
    }

    private static void calculateStream() {
        IntStream numberStream = IntStream.rangeClosed(1, 10);

        long count = numberStream.count();

        numberStream = IntStream.rangeClosed(1, 10);
        long sum = numberStream.sum();
        numberStream = IntStream.rangeClosed(1, 10);
        OptionalInt optionalMax = numberStream.max();
        numberStream = IntStream.rangeClosed(1, 10);
        OptionalInt optionalMin = numberStream.min();
        int max = optionalMax.isPresent() ? optionalMax.getAsInt() : Integer.MAX_VALUE;
        int min = optionalMin.isPresent() ? optionalMin.getAsInt() : 0;
        AtomicReference<Double> avg = new AtomicReference<>();
        DoubleStream.of(1.1, 2.2, 3.3, 4.4, 5.5)
                .average()
                .ifPresent(avg::set);

        numberStream = IntStream.range(1, 10);
        
        printStream(numberStream.boxed(), "numberStream");
        System.out.printf("             count : %d (%b)\n", count, count == 10);
        System.out.printf("             sum : %d (%b)\n", sum, sum == 55);
        System.out.printf("             max : %d (%b)\n", max, max == 10);
        System.out.printf("             min : %d (%b)\n", min, min == 1);
        System.out.printf("             avg : %f (%b)\n", avg.get(), avg.get() == 3.3);
    }

    private static void peekStream() {
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
