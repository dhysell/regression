package repository.gw.helpers;

import java.io.IOException;

public class Murder {
    public static void main(String[] args) throws IOException {
        Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe");
        Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe");
        Runtime.getRuntime().exec("taskkill /F /IM geckodriver.exe");
        Runtime.getRuntime().exec("taskkill /F /IM edgedriver.exe");
    }
}
