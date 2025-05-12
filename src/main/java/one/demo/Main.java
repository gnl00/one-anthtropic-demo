package one.demo;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.core.http.StreamResponse;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.Model;
import com.anthropic.models.messages.RawContentBlockDeltaEvent;
import com.anthropic.models.messages.RawMessageDeltaEvent;
import com.anthropic.models.messages.RawMessageStreamEvent;

/**
 * @see <a href="https://github.com/anthropics/anthropic-sdk-java">anthropic-sdk-java</a>
 */
public class Main {
    public static void main(String[] args) {
        AnthropicClient client = AnthropicOkHttpClient.fromEnv();

        MessageCreateParams params = MessageCreateParams.builder()
                .maxTokens(1024L)
                .addUserMessage("Hello, Claude, tell me the weather today")
                .model(Model.CLAUDE_3_5_SONNET_LATEST)
                .build();

        /*Message message = client.messages().create(params);
        System.out.println(message);*/

        try (StreamResponse<RawMessageStreamEvent> streamResponse = client.messages().createStreaming(params)) {
            streamResponse.stream().forEach(chunk -> {
                // System.out.println(chunk);
                RawContentBlockDeltaEvent rawContentBlockDeltaEvent = chunk.contentBlockDelta().orElse(null);
                System.out.println(rawContentBlockDeltaEvent.delta().asText().text());
            });
            System.out.println("No more chunks!");
        }
    }
}
