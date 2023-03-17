package com.liangwj.tools2k.utils.other;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.liangwj.tools2k.utils.net.HttpUtils2;

public class TelegramBot {
	private final String token;
	private final int chatId;

	private final String apiUrl;

	private final HttpUtils2 client = HttpUtils2.createInstance(5000);

	public TelegramBot(String token, int chatId) {
		super();
		this.token = token;
		this.chatId = chatId;
		this.apiUrl = String.format("https://api.telegram.org/bot%s/sendMessage", this.token);
	}

	public void sendText(String text, boolean disableNotification) throws IOException {
		// 构造要传递的参数
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("chat_id", String.valueOf(this.chatId)));
		params.add(new BasicNameValuePair("text", text));
		params.add(new BasicNameValuePair("disable_notification", String.valueOf(disableNotification)));

		// 调用API
		this.client.post(apiUrl, params, null);

	}

}
