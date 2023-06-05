package com.techmaster.shopeetool.service;

import com.techmaster.shopeetool.dto.ProductDTO;
import com.techmaster.shopeetool.dto.ResponseProductDTO;
import com.techmaster.shopeetool.dto.UserDto;
import com.techmaster.shopeetool.model.User;
import com.techmaster.shopeetool.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

@EnableScheduling
@Service
@Slf4j
public class ChromeService {
    @Autowired
    UserRepository userRepository;
    private Semaphore semaphore;
    public void Login(String userName,String passWord,WebDriver driver)throws InterruptedException{
        WebElement inputUserName = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div/div/div/div[2]/form/div/div[2]/div[2]/div[1]/input"));
        inputUserName.sendKeys(userName);
        WebElement inputPassWord = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div/div/div/div[2]/form/div/div[2]/div[3]/div[1]/input"));
        inputPassWord.sendKeys(passWord);
        Thread.sleep(2000);
//        WebElement loginButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"main\"]/div/div[2]/div/div/div/div[2]/form/div/div[2]/button")));
        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div/div/div/div[2]/form/div/div[2]/button"));
        loginButton.click();
        Thread.sleep(2000);
        WebElement adCloseButton = null;
        try {
            adCloseButton = driver.findElement(By.cssSelector("button.shopee-popup__close-btn"));
            Thread.sleep(1000);
        } catch (NoSuchElementException e) {
            System.out.println();
        }
        if (adCloseButton != null) {
            adCloseButton.click();
        }
    }
    public void searchProduct(String productName,WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        // tắt quảng cáo
        WebElement ad = null;
        try{
            ad = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div/shopee-banner-popup-stateful//div/div/div/div/div"));
        }
        catch (NoSuchElementException e){
            System.out.println("not found ad pop up");
        }
        if(ad != null){
            ad.click();
        }
        // nhập tên sản phẩm muốn tìm kiếm
        // 1.lấy ra input bar
        WebElement inputBar = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"main\"]/div/header/div[2]/div/div[1]/div[1]/div/form/input")));
        // 2.sendKeys
        inputBar.sendKeys(productName);
        // 3.bấm search
        // 3.1 lấy ra button search
        WebElement searchButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("shopee-searchbar__search-button")));
        // 3.2 click search
        searchButton.click();
    }
    public void filterPriceLowToHigh(WebDriver driver) throws InterruptedException{
        Thread.sleep(2000);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(300));
//         1. lấy ra div cha lọc giá
        WebElement parentElement = wait
                            .until(ExpectedConditions
                                    .visibilityOfElementLocated(
                                            By.xpath("//*[@id=\"main\"]/div/div[2]/div/div/div[2]/div[2]/div[1]/div[1]/div[4]/div/div")
                                    ));
        // 2. chuyển đến div " giá từ thấp đến cao "
        // Khởi tạo đối tượng Actions
        Actions actions = new Actions(driver);
        // Hover vào phần tử cha để hiển thị phần tử con
        actions.moveToElement(parentElement).perform();
        // Tìm và click vào phần tử con đầu tiên
        WebElement firstChildElement = parentElement.findElement(By.xpath(".//div/*[1]/*[1]"));
        firstChildElement.click();
    }
    public void filterDistributor(String[] distributors,WebDriver driver) throws InterruptedException{
        // tìm ô search các tỉnh thành
        // click thêm
        for (String dis:distributors) {
            WebElement moreDis = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div/div/div[1]/div[4]/div[2]/div[5]"));
            moreDis.click();
            WebElement difDis = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div/div/div[1]/div[4]/div[2]/div[5]/div[2]/div/div[17]"));
            difDis.click();
            Thread.sleep(2000);
            WebElement inputSearchDis = driver.findElement(By.className("KBqfKf"));
            inputSearchDis.sendKeys(dis);
            // chọn
            Thread.sleep(2000);
            WebElement checkBox = driver.findElement(By.xpath("//*[@id=\"stardust-popover8\"]/div[2]/div/div[2]/div/div[1]/div/div/div[2]/div/label/div"));
            checkBox.click();
            Thread.sleep(2000);
            WebElement confirm = driver.findElement(By.className("lricHh"));
            confirm.click();
        }
    }
    public void filterShippingOpition(String[] shippingOps,WebDriver driver) throws InterruptedException{
        Thread.sleep(3000);
        Map<String,WebElement> webElements = new HashMap<>();
        for (int i = 0; i < 3; i++) {
            WebElement dummyText = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div/div/div[1]/div[5]/div[2]/div["+(i+1)+"]"));
            //*[@id="main"]/div/div[2]/div/div/div[1]/div[5]/div[2]/div[1]/div/label/div
            // lưu các đơn vị vận chuyển với key = tên và value = webEle
            webElements.put(dummyText.getText(), dummyText.findElement(By.xpath("./div/label/div")));
        }
        Arrays.stream(shippingOps).forEach(shippingOp->webElements.get(shippingOp).click());
    }
    public void filterRatting(int minRate,WebDriver driver){
        // lấy ra list các lựa chọn
        WebElement choose = null;
        try{
            // min 5 sao <=> div[1]
            // min 4 sao <=> div[2]
            // => tổng của số sao ít nhất + div[number] luôn = 6
            // => 6 - minRate = number
            choose = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div/div/div[1]/div[11]/div[2]/div["+(6-minRate)+"]"));
        }
        catch (NoSuchElementException e){
            System.out.println("not found choose rating!");
        }
        if(choose != null){
            choose.click();
        }
    }
    public void filterShopMall(WebDriver driver){
//// Tạo đối tượng JavascriptExecutor
//        JavascriptExecutor js = (JavascriptExecutor) driver;
//
//// Cuộn xuống đến vị trí cụ thể trên trang
//        js.executeScript("window.scrollBy(0, 500)"); // Cuộn xuống 500 pixel

        WebElement shopMall = null;
        try {
            Thread.sleep(3000);
            shopMall = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div/div/div[1]/div[8]/div[2]/div[1]"));
//            System.out.println(shopMall.getText());
        }catch (NoSuchElementException | InterruptedException e){
            System.out.println("not found choose shop mall");
        }
        if(shopMall != null){
            System.out.println(shopMall.getText());
            shopMall.click();
        }
    }
    public void buy(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement productTarget = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"main\"]/div/div[2]/div/div/div[2]/div/div[2]/div[1]")));
        productTarget.click();
//        WebElement buyNowButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"main\"]/div/div[2]/div[1]/div/div[1]/div[2]/div[2]/div[3]/div/div[5]/div/div/button[2]")));
//        buyNowButton.click();
//        WebElement buyButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"main\"]/div/div[2]/div/div/div[3]/div[2]/div[7]/button[4]")));
//        buyButton.click();
    }

    public ResponseProductDTO startFindProduct(ProductDTO productDTO) throws InterruptedException{

        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\Tommy\\Desktop\\Learn\\tool\\chromedriver_win32\\chromedriver.exe");
        //Create a map to store  preferences
        Map<String, Object> prefs = new HashMap<>();
        //add key and value to map as follows to switch off browser notification
        //Pass the argument 1 to allow and 2 to block
        prefs.put("profile.default_content_setting_values.notifications", 2);
        //Create an instance of ChromeOptions
        ChromeOptions options = new ChromeOptions();
        // set ExperimentalOption - prefs
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(options);
        // Open website
        driver.get("https://shopee.vn/buyer/login?is_from_signup=true&next=https%3A%2F%2Fshopee.vn%2F%3Fis_from_signup%3Dtrue");
        // Maximize the browser
        driver.manage().window().maximize();
        System.out.println(productDTO.getUsername());
        String passwordShopeeEncoded = userRepository.findByEmail(productDTO.getUsername()).get().getShopeePassword();
        byte[] passwordEncoded = Base64.getDecoder().decode(passwordShopeeEncoded);
        String passwordDecoded = new String(passwordEncoded);
        Login(productDTO.getUsername(), passwordDecoded, driver);
        searchProduct(productDTO.getProductName(), driver);
        filterPriceLowToHigh(driver);
        String[] shipOps = {"Nhanh"};
        filterShippingOpition(shipOps,driver);
        filterRatting(5,driver);
        filterDistributor(new String[]{productDTO.getShopDistributor()},driver);
        Thread.sleep(5000);
        // Tạo đối tượng JavascriptExecutor
        JavascriptExecutor js = (JavascriptExecutor) driver;
        // Cuộn xuống đến vị trí cụ thể trên trang
        js.executeScript("window.scrollBy(0, -800)"); // Cuộn lên 800 pixel
        filterShopMall(driver);
        buy(driver);
        return responseProduct(driver);
    }
    public ResponseProductDTO responseProduct(WebDriver driver) throws InterruptedException {
//        System.setProperty("webdriver.chrome.driver",
//                "C:\\Users\\Tommy\\Desktop\\Learn\\tool\\chromedriver_win32\\chromedriver.exe");
//        //Create a map to store  preferences
////        Map<String, Object> prefs = new HashMap<String, Object>();
////        //add key and value to map as follows to switch off browser notification
////        //Pass the argument 1 to allow and 2 to block
////        prefs.put("profile.default_content_setting_values.notifications", 2);
////        //Create an instance of ChromeOptions
////        ChromeOptions options = new ChromeOptions();
////        // set ExperimentalOption - prefs
////        options.setExperimentalOption("prefs", prefs);
//        WebDriver driver = new ChromeDriver();
//        // Open website
//        driver.get("https://shopee.vn/buyer/login?is_from_signup=true&next=https%3A%2F%2Fshopee.vn%2F%3Fis_from_signup%3Dtrue");
        ResponseProductDTO responseProductDTO = new ResponseProductDTO();
        WebElement element = null;
        Thread.sleep(3000);
        try{
            element = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[1]/div/div[1]/div[2]/div[2]/div[3]/div/div[1]"));
        }catch (NoSuchElementException e){
            System.out.println("Cant find product name");
        }
        if(element != null){
            System.out.println(element.getText());
            responseProductDTO.setName(element.getText());
        }
//        String productImageLink = "";
//        // Khởi tạo chuỗi hành động
        Actions actions = new Actions(driver);
        WebElement imageElement = null;
        try {
            imageElement = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[1]/div/div/div[2]/div[2]/div[2]/div[1]/div[2]/div[2]/div"));
        }catch (java.util.NoSuchElementException e){
            System.out.println("cant find img element");
        }
        if(imageElement!=null){
//            imageElement.click();
            actions.moveToElement(imageElement).build().perform();
        }
        Thread.sleep(3000);
        WebElement productImageLink = null;
        try {
             productImageLink = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[1]/div/div[1]/div[2]/div[2]/div[2]/div[1]/div[1]/div/div[2]/div"));
        }catch (NoSuchElementException e) {
            System.out.println("cant find img link");
        }
        if(productImageLink != null){
            System.out.println("css not null");
            System.out.println(getLinkImg(productImageLink.getAttribute("style")));
            responseProductDTO.setLinkPhoto(getLinkImg(productImageLink.getAttribute("style")));
        }
//        Map<Integer,String> choices = new HashMap<>();
//        // kiểm tra xem sản phẩm có mấy lựa chọn
//        WebElement choose = null;
//        // nếu 1
//
//        // > 1
//        try {
//            choose = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[1]/div/div[1]/div[2]/div[2]/div[3]/div/div[4]/div/div[5]"));
//        }catch (java.util.NoSuchElementException e){
//            System.out.println("sản phẩm chỉ có một lựa chọn!");
//        }
        WebElement priceElement = null;
        try {
            priceElement = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div[1]/div/div[1]/div[2]/div[2]/div[3]/div/div[3]/div/div/div[1]/div/div[2]/div[1]"));
        }catch (NoSuchElementException e){
            System.out.println("cant find price element! ");
        }
        if(priceElement!=null){
            System.out.println("price: "+priceElement.getText());
            responseProductDTO.setPrice(priceElement.getText());
        }
        return responseProductDTO;
    }
    public String getLinkImg(String styleAttribute){
        int startIndex = styleAttribute.indexOf("(\"")+2;
        int endIndex = styleAttribute.indexOf("\")");
        return styleAttribute.substring(startIndex,endIndex);
    }

//    {1 2 4 2 6 7 }
//    public Queue<UserDto> getAccountFromDatabase(){
//        // take a queue of account and pass word from repository
//        Queue<UserDto> queueUsers = new LinkedList<>();
//        List<User> usersFromRepo =  userRepository.findAll();
//
//    }
    @Scheduled(cron = "10 25 23 * * ?")
    public void autoFarmShopeeCoins() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\Tommy\\Desktop\\Learn\\tool\\chromedriver_win32\\chromedriver.exe");
        //Create a map to store  preferences
        Map<String, Object> prefs = new HashMap<>();
        //add key and value to map as follows to switch off browser notification
        //Pass the argument 1 to allow and 2 to block
        prefs.put("profile.default_content_setting_values.notifications", 2);
        //Create an instance of ChromeOptions
        ChromeOptions options = new ChromeOptions();
        // set ExperimentalOption - prefs
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--remote-allow-origins=*");
        // Get list of users
        List<User> users = userRepository.findAll();
        this.semaphore = new Semaphore(users.size());
        Instant startTime = Instant.now(); // Lưu thời điểm bắt đầu chạy
        // Create a fixed thread pool with a maximum of 5 threads
        ExecutorService executor = Executors.newFixedThreadPool(3);
        Thread.sleep(2000);
        //Iterate through each user account and submit a task to the thread pool
        for (User userAccount : users) {
            executor.submit(() -> {
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                WebDriver driver = new ChromeDriver(options);
                // Open website
                driver.get("https://shopee.vn/shopee-coins");
                // Maximize the browser
                driver.manage().window().maximize();
                WebElement loginBtn = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div/main/section[1]/div[1]/div/section/div[2]/div/button"));
                loginBtn.click();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    byte[] passwordEncoded = Base64.getDecoder().decode(userAccount.getShopeePassword());
                    String passwordDecoded = new String(passwordEncoded);
                    System.out.println(userAccount.getUsername());
                    Login(userAccount.getUsername(), passwordDecoded, driver);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                WebElement claimsBtn = driver.findElement(By.xpath("//*[@id=\"main\"]/div/div[2]/div/main/section[1]/div[1]/div/section/div[2]/div/button"));
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                claimsBtn.click();
                driver.quit();
                semaphore.release();
            });
        }

        //Shutdown the thread pool when all tasks have been completed
        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        Instant endTime = Instant.now(); // Lấy thời điểm hiện tại
        Duration totalTime = Duration.between(startTime, endTime); // Tính thời gian chạy
        System.out.println("Total time: " + totalTime.getSeconds() + " seconds");
    }
}
