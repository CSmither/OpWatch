import org.junit.Test;
import org.junit.runner.RunWith;
import org.smither.opwatch.server.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ApplicationTest {
    private Application application;

    @Test
    public void checkSpringBoots() {
        SpringApplication.run(Application.class);
    }
}
