package com.simple.core.route.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.simple.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * 日志
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.03.31 11:29.
 */
@Activate(group = "provider")
public class MsgLoggerFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(MsgLoggerFilter.class);
    private static long requestCount;

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        //before business,可在此解密报文
        requestCount++;

        logger.info("----------- Begin the [ {} ] times  Called.",requestCount);
        logger.info("Interface:[{}], Method:[{}], Arguments:{}", invoker.getInterface(), invocation.getMethodName(), Arrays.toString(invocation.getArguments()));

        long start = System.nanoTime();
        //doing business,调用具体服务
        Result result = invoker.invoke(invocation);

        //after business,处理服务结果
        Object value = result.getValue();

        logger.info("clear msg:[{}],", value);
        logger.info("----------- End the [ {} ] times  Called. {}", requestCount,StringUtil.getSpentTm(start));
        return result;
    }

}
