import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.junit.jupiter.api.condition.EnabledIf;

public class CustomConditionDemo {
    @Test
    @EnabledIf("customCondition")
    void enabled() {
        // ...
    }

    @Test
    @DisabledIf("customCondition")
    void disabled() {
        // ...
    }

    boolean customCondition() {
        return true;
    }
}

class ExternalCustomConditionDemo {

    @Test
    @EnabledIf("ExternalCondition#customCondition")
    void enabled() {
        // ...
    }

}

class ExternalCondition {

    static boolean customCondition() {
        return true;
    }

}