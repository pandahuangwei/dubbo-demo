package com.simple.core.route.filter;

import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.fastjson.JSON;
import com.simple.core.common.entity.Message;
import com.simple.core.common.entity.MessageCipher;
import com.simple.core.common.entity.MessageClear;
import com.simple.core.common.entity.MsgOrig;
import com.simple.core.route.util.RoutePropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * 报文解析接口，处理加密、解密过滤器
 *
 * @author Panda.HuangWei.
 * @version 1.0 .
 * @since 2017.03.31 11:29.
 */
@Activate(group = "provider")
public class MsgEncryptFilter implements Filter {
    private static Logger logger = LoggerFactory.getLogger(MsgEncryptFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        //before business,可在此解密报文

        long start = System.currentTimeMillis();
        //doing business,调用具体服务
        Result result = invoker.invoke(invocation);

        //after business,处理服务结果
        Object value = result.getValue();
        logger.info("Interface:[{}], Method:[{}], Arguments:{}", invoker.getInterface(), invocation.getMethodName(), Arrays.toString(invocation.getArguments()));
        logger.info("clear msg:[{}],", value);

       /* if (RoutePropertyUtil.getInstance().isMsgEncrypt()) {
            result = encryptMsg(result);
            logger.info("Cipher msg:[{}]", result.getValue());
        }*/
        long elapsed = System.currentTimeMillis() - start;
        logger.info("spend time(ms):[{}]   ", elapsed);
        return result;
    }

    private Result encryptMsg(Object value) {
        try {
            Message msg = new MessageCipher("entrypt", JSON.parseObject(value.toString(), MessageClear.class));
            return new RpcResult(msg);
        } catch (Exception e) {
            logger.error("encryptMsg err:{}", e);
        }
        return null;
    }
}
