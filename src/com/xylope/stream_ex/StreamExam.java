package com.xylope.stream_ex;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StreamExam {

    public static void main(String[] args) throws IOException {
        //create stream
        String[] nameArr = {"Steve", "Elena", "Alice", "Kun", "Tom", "Stanly", "Alexa"};
        List<String> nameList = Arrays.asList(nameArr);

        AtomicInteger count = new AtomicInteger(29);

        Stream<String> nameArrStream = Arrays.stream(nameArr);
        Stream<String> nameListStream = nameList.stream();
        Stream<String> nameStringStream = Pattern.compile(", ").splitAsStream("Steve, Elena, Alice, Kun, Tom, Stanly, Alexa");
        Stream<String>nameFileStream = Files.lines(Paths.get("src/com/xylope/stream_ex/fileStream.txt"), StandardCharsets.UTF_8);

        Stream<Object> emptyStream = Stream.empty();

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
        System.out.printf("%25s - ", streamName);
        stream.forEach((el) -> System.out.print(el + " | "));
        System.out.println();
    }
}
