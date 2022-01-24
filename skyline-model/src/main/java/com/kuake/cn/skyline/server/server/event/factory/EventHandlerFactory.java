package com.kuake.cn.skyline.server.server.event.factory;

import com.kuake.cn.skyline.server.server.event.EventHandler;
import com.kuake.cn.skyline.server.server.event.RequestEventHandler;
import com.kuake.cn.skyline.server.server.event.ResponseEventHandler;
import com.kuake.cn.skyline.server.server.event.UnkownEventHandler;
import com.kuake.cn.skyline.transport.protocol.Request;
import com.kuake.cn.skyline.transport.protocol.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：请求事件处理器工厂
 * @author: kuake.cn
 * @create: 2021-03-28 16:35
 **/
@Slf4j
public class EventHandlerFactory {
    private static final Map<String, EventHandler> eventHandlers = new HashMap<String, EventHandler>() {
        {
            //request
            put(Request.class.getName(), new RequestEventHandler());

            //response
            put(Response.class.getName(),new  ResponseEventHandler());
        }
    };

    public static EventHandler getEventHandler(String className) {
        EventHandler eventHandler = eventHandlers.get(className);
        if (ObjectUtils.isEmpty(eventHandler)) {
            log.warn("handler not found:[handlerKey:{}]", className);
        }

        return eventHandler == null ? new UnkownEventHandler() : eventHandler;
    }
}
