import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

public class OperatingSystemConditionDemo {
    @Test
    @EnabledOnOs(OS.MAC)
    void onlyOnMacOs() {
        // ...
    }

    @TestOnMac
    void testOnMac() {
        // ...
    }

    @Test
    @EnabledOnOs({ OS.LINUX, OS.MAC })
    void onLinuxOrMac() {
        // ...
    }

    @Test
    @DisabledOnOs(OS.WINDOWS)
    void notOnWindows() {
        // ...
    }

}
