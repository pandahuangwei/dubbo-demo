package com.simple.core.flightimport.parsecfp.impl;


import com.simple.core.flightimport.entity.CfpExt;
import com.simple.core.flightimport.parsecfp.CfpParser;
import org.springframework.stereotype.Service;

/**
 * 东方航空的飞行计划文件解析
 * @author Simple.WuPengfei.
 * @Create 2017.01.09 10:31.
 * @Version 1.0 .
 */
@Service("MUCfpParser")
public class MUCfpParserImpl implements CfpParser {

    /**
     * 解析飞行计划
     * @param cfpText
     * @return
     */
    @Override
    public CfpExt parse(String cfpText) {
        return null;
    }

    @Override
    public CfpExt parse(byte[] cfpArray) {
        return null;
    }
}
