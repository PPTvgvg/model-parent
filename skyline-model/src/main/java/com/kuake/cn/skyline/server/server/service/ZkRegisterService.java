package com.kuake.cn.skyline.server.server.service;

import com.kuake.cn.skyline.common.constants.Constants;
import com.kuake.cn.skyline.common.environment.Environment;
import com.kuake.cn.skyline.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.CreateMode;

import java.text.MessageFormat;

/**
 * 描述：ZK注册服务接口
 *
 * @author: kuake.cn
 * @create: 2021-03-29 21:08
 **/
@Slf4j
public class ZkRegisterService {
    private static final CuratorFramework client = CuratorClientHolder.getCuratorFramework();
    private static final ZkRegisterService zkRegisterService = new ZkRegisterService();

    public static ZkRegisterService getInstance() {
        return zkRegisterService;
    }

    public void register() throws Exception {
        String serverPath = getServerPath();
        createPersistentZkNode(serverPath, "10");
        createEphemeral(serverPath.concat("/stat"), System.currentTimeMillis() + "");
    }

    private void createEphemeral(String path, String weight) throws Exception {
        if (checkExist(path)) {
            createZkNode(path,weight);
        }else {
            try {
                deleteZkNode(path);
            }finally {
                createZkNode(path, weight);
            }
        }
    }

    private void deleteZkNode(String path) throws Exception {
        client.delete().forPath(path);
    }

    private void createZkNode(String path, String weight) throws Exception {
        if (ObjectUtils.isNotEmpty(weight)) {
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path, weight.getBytes());
        }else {
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(path);
        }
    }

    private void createPersistentZkNode(String path, String weight) {
        try {
            if (checkExist(path)) {
                if (ObjectUtils.isNotEmpty(weight)) {
                    client.create()
                            .creatingParentsIfNeeded()
                            .withMode(CreateMode.PERSISTENT)
                            .forPath(path, weight.getBytes());
                }else {
                    client.create()
                            .creatingParentsIfNeeded()
                            .withMode(CreateMode.PERSISTENT)
                            .forPath(path);
                }
            }
        }catch (Exception e) {
            log.warn("节点已存在：{}", path);
        }
    }

    private boolean checkExist(String path) throws Exception {
        return client.checkExists().forPath(path) == null;
    }

    private String getServerPath() throws Exception {
        String ipPort = String.join(":", IPUtils.getHost(), String.valueOf(Environment.getRpcPort()));
        return MessageFormat.format(Constants.SKYLINE_SERVER_MACHINES_NODE, ipPort);
    }

    public void shutdown() {

    }
}
