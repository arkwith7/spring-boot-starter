package com.arkwith.starter.chatgpt;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arkwith.starter.CommonUtil;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;

@RestController
@RequestMapping(value = "openai", produces = MediaType.APPLICATION_JSON_VALUE)
public class ChatRestController {
	@Value("${openai.api.chat.default.role}")
	private String defaultRole;
	
	@Autowired
	private ChatGPTClientService chatGPTClientService;
	
	@Autowired
	private CommonUtil markDownToHtmlService;

    @GetMapping("chat/{prompt}")
    public ChatResponse chatGptRequest(@PathVariable String prompt) {
    	
    	final OpenAiService service = chatGPTClientService.getOpenAiService();
    	final ChatCompletionRequest chatRequest = chatGPTClientService.getChatCompletionRequest(List.of(new ChatMessage(defaultRole, prompt)));
    	final String chatResponse = service.createChatCompletion(chatRequest).getChoices().get(0).getMessage().getContent();
    	
		return new ChatResponse(prompt, chatResponse, markDownToHtmlService.markdown(chatResponse));
    }
}
