import com.example.project.Gender;
import com.example.project.Person;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.JavaTimeConversionPattern;
import org.junit.jupiter.params.provider.*;
import org.junit.platform.commons.util.StringUtils;

import org.junit.jupiter.params.ParameterizedTest;

import java.io.File;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.junit.jupiter.params.provider.EnumSource.Mode.*;

public class ParameterizedTestDemo {
    @ParameterizedTest
    @ValueSource(strings = { "racecar", "radar", "able was I ere I saw elba" })
    public void palindromes(String candidate) {
        assertTrue(isPalindrome(candidate));
    }

    private boolean isPalindrome(String s) {
        return true;
    }

    /**
     * ValueSourceTag usecases
     *
     * @param argument int
     */
    @ParameterizedTest
    @ValueSource(ints = { 1, 2, 3 })
    void testWithValueSource(int argument) {
        assertTrue(argument > 0 && argument < 4);
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = { " ", "   ", "\t", "\n" })
    void nullEmptyAndBlankStrings1(String text) {
        assertTrue(text == null || text.trim().isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { " ", "   ", "\t", "\n" })
    void nullEmptyAndBlankStrings2(String text) {
        assertTrue(text == null || text.trim().isEmpty());
    }

    @ParameterizedTest
    @EnumSource(ChronoUnit.class)
    void testWithEnumSource(TemporalUnit unit) {
        assertNotNull(unit);
    }

    @ParameterizedTest
    @EnumSource
    void testWithEnumSourceWithAutoDetection(ChronoUnit unit) {
        assertNotNull(unit);
    }

    @ParameterizedTest
    @EnumSource(names = { "DAYS", "HOURS" })
    void testWithEnumSourceInclude(ChronoUnit unit) {
        assertTrue(EnumSet.of(ChronoUnit.DAYS, ChronoUnit.HOURS).contains(unit));
    }

    @ParameterizedTest
    @EnumSource(mode = EXCLUDE, names = { "ERAS", "FOREVER" })
    void testWithEnumSourceExclude(ChronoUnit unit) {
        assertFalse(EnumSet.of(ChronoUnit.ERAS, ChronoUnit.FOREVER).contains(unit));
    }

    @ParameterizedTest
    @EnumSource(mode = MATCH_ALL, names = "^.*DAYS$")
    void testWithEnumSourceRegex(ChronoUnit unit) {
        assertTrue(unit.name().endsWith("DAYS"));
    }

    @ParameterizedTest
    @MethodSource("stringProvider")
    void testWithExplicitLocalMethodSource(String argument) {
        assertNotNull(argument);
    }

    static Stream<String> stringProvider() {
        return Stream.of("apple", "banana");
    }

    @ParameterizedTest
    @MethodSource
    void testWithDefaultLocalMethodSource(String argument) {
        assertNotNull(argument);
    }

    static Stream<String> testWithDefaultLocalMethodSource() {
        return Stream.of("apple", "banana");
    }

    @ParameterizedTest
    @MethodSource("range")
    void testWithRangeMethodSource(int argument) {
        assertNotEquals(9, argument);
    }

    static IntStream range() {
        return IntStream.range(0, 20).skip(10);
    }

    @ParameterizedTest
    @MethodSource("stringIntAndListProvider")
    void testWithMultiArgMethodSource(String str, int num, List<String> list) {
        assertEquals(5, str.length());
        assertTrue(num >=1 && num <=2);
        assertEquals(2, list.size());
    }

    static Stream<Arguments> stringIntAndListProvider() {
        return Stream.of(
                arguments("apple", 1, Arrays.asList("a", "b")),
                arguments("lemon", 2, Arrays.asList("x", "y"))
        );
    }

    @ParameterizedTest
    @CsvSource({
            "apple,         1",
            "banana,        2",
            "'lemon, lime', 0xF1",
            "strawberry,    700_000"
    })
    void testWithCsvSource1(String fruit, int rank) {
        assertNotNull(fruit);
        assertNotEquals(0, rank);
    }

    @ParameterizedTest(name = "[{index}] {arguments}")
    @CsvSource(useHeadersInDisplayName = true, textBlock = """
    FRUIT,         RANK
    apple,         1
    banana,        2
    'lemon, lime', 0xF1
    strawberry,    700_000
    """)
    void testWithCsvSource2(String fruit, int rank) {
        // ...
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', quoteCharacter = '"', textBlock = """
    #-----------------------------
    #    FRUIT     |     RANK
    #-----------------------------
         apple     |      1
    #-----------------------------
         banana    |      2
    #-----------------------------
      "lemon lime" |     0xF1
    #-----------------------------
       strawberry  |    700_000
    #-----------------------------
    """)
    void testWithCsvSource3(String fruit, int rank) {
        // ...
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/two-column.csv", numLinesToSkip = 1)
    void testWithCsvFileSourceFromClasspath(String country, int reference) {
        assertNotNull(country);
        assertNotEquals(0, reference);
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/two-column.csv", numLinesToSkip = 1)
    void testWithCsvFileSourceFromFile(String country, int reference) {
        assertNotNull(country);
        assertNotEquals(0, reference);
    }

    @ParameterizedTest(name = "[{index}] {arguments}")
    @CsvFileSource(resources = "/two-column.csv", useHeadersInDisplayName = true)
    void testWithCsvFileSourceAndHeaders(String country, int reference) {
        assertNotNull(country);
        assertNotEquals(0, reference);
    }

    @ParameterizedTest
    @ArgumentsSource(MyArgumentsProvider.class)
    void testWithArgumentsSource(String argument) {
        assertNotNull(argument);
    }

    /**
     * For example, if a @ParameterizedTest declares a parameter of type TimeUnit and
     * the actual type supplied by the declared source is a String,
     * the string will be automatically converted into the corresponding TimeUnit enum constant.
     * @param argument
     */
    @ParameterizedTest
    @ValueSource(strings = "SECONDS")
    void testWithImplicitArgumentConversion(ChronoUnit argument) {
        assertNotNull(argument.name());
    }

    /**
     * For example, in the following @ParameterizedTest method,
     * the Book argument will be created by invoking the Book.fromTitle(String) factory method
     * and passing "42 Cats" as the title of the book.
     * @param book
     */
    @ParameterizedTest
    @ValueSource(strings = "42 Cats")
    void testWithImplicitFallbackArgumentConversion(Book book) {
        assertEquals("42 Cats", book.getTitle());
    }

    @ParameterizedTest
    @EnumSource(ChronoUnit.class)
    void testWithExplicitArgumentConversion(
            @ConvertWith(ToStringArgumentConverter.class) String argument) {

        assertNotNull(ChronoUnit.valueOf(argument));
    }

    @ParameterizedTest
    @ValueSource(strings = { "01.01.2017", "31.12.2017" })
    void testWithExplicitJavaTimeConverter(
            @JavaTimeConversionPattern("dd.MM.yyyy") LocalDate argument) {

        assertEquals(2017, argument.getYear());
    }

    @ParameterizedTest
    @CsvSource({
            "Jane, Doe, F, 1990-05-20",
            "John, Doe, M, 1990-10-22"
    })
    void testWithArgumentsAccessor(ArgumentsAccessor arguments) {
        Person person = new Person(arguments.getString(0),
                arguments.getString(1),
                arguments.get(2, Gender.class),
                arguments.get(3, LocalDate.class));

        if (person.getFirstName().equals("Jane")) {
            assertEquals(Gender.F, person.getGender());
        }
        else {
            assertEquals(Gender.M, person.getGender());
        }
        assertEquals("Doe", person.getLastName());
        assertEquals(1990, person.getDateOfBirth().getYear());
    }

    @ParameterizedTest
    @CsvSource({
            "Jane, Doe, F, 1990-05-20",
            "John, Doe, M, 1990-10-22"
    })
    void testWithArgumentsAggregator(@AggregateWith(PersonAggregator.class) Person person) {
        // perform assertions against person
    }

    @ParameterizedTest
    @CsvSource({
            "Jane, Doe, F, 1990-05-20",
            "John, Doe, M, 1990-10-22"
    })
    void testWithCustomAggregatorAnnotation(@CsvToPerson Person person) {
        // perform assertions against person
    }

    @DisplayName("Display name of container")
    @ParameterizedTest(name = "{index} ==> the rank of ''{0}'' is {1}")
    @CsvSource({ "apple, 1", "banana, 2", "'lemon, lime', 3" })
    void testWithCustomDisplayNames(String fruit, int rank) {
    }

    @DisplayName("A parameterized test with named arguments")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("namedArguments")
    void testWithNamedArguments(File file) {
    }

    static Stream<Arguments> namedArguments() {
        return Stream.of(arguments(Named.of("An important file", new File("path1"))),
                arguments(Named.of("Another file", new File("path2"))));
    }
}
