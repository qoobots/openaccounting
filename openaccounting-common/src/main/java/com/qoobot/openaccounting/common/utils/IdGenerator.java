package com.qoobot.openaccounting.common.utils;

import cn.hutool.core.util.IdUtil;

/**
 * 雪花ID生成器
 *
 * @author openaccounting
 */
public class IdGenerator {

    /**
     * 数据中心ID (0-31)
     */
    private static final long DATACENTER_ID = 1L;

    /**
     * 机器ID (0-31)
     */
    private static final long WORKER_ID = 1L;

    /**
     * 生成雪花ID
     */
    public static long nextId() {
        return IdUtil.getSnowflake(WORKER_ID, DATACENTER_ID).nextId();
    }

    /**
     * 生成雪花ID字符串
     */
    public static String nextIdStr() {
        return String.valueOf(nextId());
    }
}
