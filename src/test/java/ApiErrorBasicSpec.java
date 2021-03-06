import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.japierrors.ApiError;
import io.github.cdimascio.japierrors.ApiErrorCreator;
import io.github.cdimascio.japierrors.basic.ApiErrorBasic;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.github.cdimascio.japierrors.ApiError.badRequest;
import static io.github.cdimascio.japierrors.ApiError.tooManyRequests;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ApiErrorBasicSpec {
    private ObjectMapper m = new ObjectMapper();

    @BeforeAll
    public static void beforeAll() {
        ApiError.creator(ApiErrorCreator.BASIC);
    }

    @Test
    public void badRequestErr() {
        String message = "oh no";
        ApiErrorBasic error = Assertions.assertThrows(ApiErrorBasic.class, () -> {
            throw badRequest(message);
        });
        Assertions.assertNotNull(error.getError());
        assertEquals(400, error.getCode());
        assertEquals(message, error.getError());
    }

    @Test
    public void badRequestJson() {
        ApiErrorBasic error = ApiError.badRequest();
        JsonNode node = m.convertValue(error, JsonNode.class);
        assertNull(node.get("message"));
        assertEquals("bad request", node.get("error").asText());
        assertEquals(400, node.get("code").asInt());
    }

    @Test
    public void tooManyRequestsErr() {
        String message = "oh no";
        ApiErrorBasic error = Assertions.assertThrows(ApiErrorBasic.class, () -> {
            throw tooManyRequests(message);
        });
        Assertions.assertNotNull(error.getError());
        assertEquals(429, error.getCode());
        assertEquals(message, error.getError());
    }

    @Test
    public void tooManyRequestsJson() {
        ApiErrorBasic error = ApiError.tooManyRequests();
        JsonNode node = m.convertValue(error, JsonNode.class);
        assertNull(node.get("message"));
        assertEquals("too many requests", node.get("error").asText());
        assertEquals(429, node.get("code").asInt());
    }
}
