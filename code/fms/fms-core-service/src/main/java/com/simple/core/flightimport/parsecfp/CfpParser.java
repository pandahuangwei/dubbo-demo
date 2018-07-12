package com.simple.core.flightimport.parsecfp;

import com.simple.core.flightimport.entity.CfpExt;

import java.io.UnsupportedEncodingException;

/**
 * @author Simple.WuPengfei.
 * @Create 2017.01.09 10:31.
 * @Version 1.0 .
 */
public interface CfpParser
{
    CfpExt parse(String cfpText) throws UnsupportedEncodingException;
    CfpExt parse(byte[] cfpArray);
}
