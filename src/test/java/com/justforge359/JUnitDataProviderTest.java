package com.justforge359;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Configuration.baseUrl;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class JUnitDataProviderTest extends TestBase {

    @ParameterizedTest(name = "Проверить, что кнопки {0} присутствуют на главной странице")
    @ValueSource(
            strings = {"Сообщения",
                    "Mой Лабиринт",
                    "Отложено",
                    "Корзина"}
    )
    public void checkThatPersonalHeaderHasButtonsTest(String textHeader) {
        open(baseUrl);
        $(".b-header-b-personal-wrapper").shouldHave(text(textHeader));

    }
    @ParameterizedTest(name = "ввести в поле поиска {0}, выполнить поиск")
    @DisplayName("Проверка ")
    @CsvSource(value = {
            "артур дойл затерянный мир, 872576, Автор: Дойл Артур Конан",
            "нил гейман история с кладбищем, 462042, Автор: Гейман Нил",
            "татьяна толстая кысь, 564887, Автор: Толстая Татьяна Никитична"
    })
    public void checkBookTitleTest(String searchQuery, int bookId, String bookAuthor) {
        open(baseUrl);
        $("#search-field").setValue(searchQuery).pressEnter();
        $(".genres-carousel__container").$("[data-product-id='" + bookId + "']").click();
        $("#product-specs").shouldHave(text("ID товара: " + bookId));
        $("#product-specs .authors").shouldHave(text(bookAuthor));

    }

    private static Stream<Arguments> checkBookDetailsTest() {
        return Stream.of(
                Arguments.of(872576, "Артур Дойл: Затерянный мир", "Аннотация к книге \"Затерянный мир\"", false),
                Arguments.of(462042, "Нил Гейман: История с кладбищем", "Аннотация к книге \"История с кладбищем\"", true),
                Arguments.of(564887, "Татьяна Толстая: Кысь", "Аннотация к книге \"Кысь\"", false)
        );
    }
    @ParameterizedTest
    @MethodSource()
    void checkBookDetailsTest(int bookId, String bookTitle, String annotation, boolean hasEnglishTitle) {
        open(baseUrl + "books/" + bookId);
        $("#product-title").shouldHave(text(bookTitle));
        $("#product-about").shouldHave(text(annotation));
        if (hasEnglishTitle) {
            $(".h2_eng").shouldBe(visible);
        }
    }
}
