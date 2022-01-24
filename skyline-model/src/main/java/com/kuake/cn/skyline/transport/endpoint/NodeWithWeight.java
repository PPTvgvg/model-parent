package com.kuake.cn.skyline.transport.endpoint;

import java.nio.channels.Channel;
import java.util.Objects;

/**
 * 描述：
 *
 * @author: kuake.cn
 * @create: 2021-04-02 15:56
 **/
public class NodeWithWeight {
    private String ipPort;
    private int weight;
    private Channel channel;

    public NodeWithWeight(String ipPort, int weight) {
        this.ipPort = ipPort;
        this.weight = weight;
    }

    public NodeWithWeight(String ipPort) {
        this.ipPort = ipPort;
    }

    public String getIpPort() {
        return ipPort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeWithWeight that = (NodeWithWeight) o;
        return Objects.equals(ipPort, that.ipPort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipPort);
    }

    public void setIpPort(String ipPort) {
        this.ipPort = ipPort;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
