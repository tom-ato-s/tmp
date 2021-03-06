package web;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import model.Ticket;
import pages.*;
import elements.MainMenu;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class HelpdeskUITest {
    private WebDriver driver;
    private Ticket ticket;
    private Ticket ticketOfPage;
    @BeforeTest
    public void setup() throws IOException {
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("config.properties"));
        System.getProperties().load(ClassLoader.getSystemResourceAsStream("user.properties"));
        ChromeOptions options = new ChromeOptions();
        options.addArguments("user-data-dir=/home/tomato/.config/google-chrome");
        // Создание экземпляра драйвер
        // driver = new ChromeDriver(options);
        driver = new ChromeDriver();
        // Устанавливаем размер окна браузера, как максимально возможный
        driver.manage().window().maximize();
        // Установим время ожидания для поиска элементов
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        // Установить созданный драйвер для поиска в веб-страницах
        AbstractPage.setDriver(driver);
    }

    @Test
    public void createTicketTest() throws IOException {
        ticket = buildNewTicket();
        // todo: открыть главную страницу
        driver.get(System.getProperty("site.url"));
        WebElement newTicketBtn = driver.findElement(By.xpath(".//a[@href='/tickets/submit/']"));

        // todo: создать объект главной страницы и выполнить шаги по созданию тикета
        MainMenu mainMenu = new MainMenu(driver); // создает объекта меню
        mainMenu.newTicket();
        CreateTicketPage createTicketPage = new CreateTicketPage(); //создание объекта для создания тикета
        createTicketPage.createTicket(ticket); // заполнение полей станицы CreateTicket и нажатие кнопки сохранения тикета
        TicketPage ticketPage = new TicketPage(); // создали страницу объекта Тикета
        ticketPage.GoTologin(); // нажатие кнопики Логированния
        // todo: перейти к странице авторизации и выполнить вход "//select[@name='m_act[genre][]']"
        LoginPage loginPage = new LoginPage();
        loginPage.login(System.getProperty("user"),System.getProperty("password") );
        TicketsPage ticketsPage = new TicketsPage();

        // todo: найти созданный тикет и проверить поля
        ticketsPage.openTicket(ticket); //найти и открыть тикет

        ticketOfPage = buildNewTicket(ticketPage); // создание объекта тикет из данных окна TicketPage
       Assert.assertEquals(ticket.equals(ticketOfPage), true, "Объекты эквивалентны"); //сравнение тикетов: созданного в начале и полученного из окна

        // Закрываем текущее окно браузера
        driver.close();
    }

    /**
     * Создаём и заполняем объект тикета
     *
     * @return заполненный объект тикета
     */
    protected Ticket buildNewTicket() {
        Ticket ticket = new Ticket();

        ticket.setTitle(randomTitle());
        // todo: заполнить остальные необходимые поля тикета
        ticket.setQueueValue("Django Helpdesk");
        ticket.setDescriptionValue("We hava a few problem");
        ticket.setPriorityValue(4);
        ticket.setMailValue("firstl@mail.ru");

        return ticket;
    }

    public static String randomTitle() {

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
    return generatedString;
    }


    protected Ticket buildNewTicket(TicketPage ticketPage) {
        Ticket ticketOfPage = new Ticket();

        ticketOfPage.setTitle(ticketPage.getNameTitle());
        // todo: заполнить остальные необходимые поля тикета
        ticketOfPage.setQueueValue(ticketPage.getQueue());
        ticketOfPage.setDescriptionValue(ticketPage.getDescription());
        ticketOfPage.setPriorityValue(ticketPage.getPriority());
        ticketOfPage.setMailValue(ticketPage.getEmail());

        return ticket;
    }


    @AfterTest
    public void close() {
        // Закрываем все окна браузера и освобождаем ресурсы
        driver.quit();
    }
}