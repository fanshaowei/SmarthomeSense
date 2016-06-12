package cn.com.papi.NettyClient.WebMessage;

import cn.com.papi.NettyClient.protobuf.WebMessagePojo;
import cn.com.papi.NettyClient.protobuf.WebMessagePojo.WebMessage;

public class WebMessageBuilderImpl implements WebMessageBuilder {

	public WebMessage WebMessageBuild(int ctrlType, String to, String data) {
		// 获取protobuf中工厂
		WebMessagePojo.WebMessage.Builder builder = WebMessagePojo.WebMessage
				.newBuilder();

		// 设置消息序列字段值
		builder.setCtrlType(ctrlType);
		builder.setTo(to);
		builder.setData(data);

		// 获取完整工厂实例
		WebMessage msgBuilder = builder.build();

		return msgBuilder;
	}

}
