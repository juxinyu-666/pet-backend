package com.example.demo.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Spring AI 配置类
 * 手动装配 ChatModel 与 ChatClient，对接 OpenAI 兼容协议的大模型接口
 * （默认指向火山方舟/豆包；未配置 API Key 时由 AIController 降级为本地回答）
 */
@Configuration
public class AIConfig {

    @Value("${spring.ai.openai.base-url}")
    private String baseUrl;

    @Value("${spring.ai.openai.api-key:sk-placeholder}")
    private String apiKey;

    @Value("${spring.ai.openai.chat.options.model:doubao-seed-1-6-250615}")
    private String modelName;

    /** 对话模型：OpenAI 兼容协议客户端（豆包/火山方舟等）；@Primary 覆盖自动配置 */
    @Bean
    @Primary
    public ChatModel chatModel() {
        OpenAiApi api = OpenAiApi.builder()
                .baseUrl(baseUrl)
                .apiKey(apiKey)
                .build();
        return OpenAiChatModel.builder()
                .openAiApi(api)
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(modelName)
                        .build())
                .build();
    }

    /** Spring AI 对话客户端：聊天接口通过它调用大模型 */
    @Bean
    public ChatClient chatClient(ChatModel model) {
        return ChatClient.builder(model).build();
    }
}
