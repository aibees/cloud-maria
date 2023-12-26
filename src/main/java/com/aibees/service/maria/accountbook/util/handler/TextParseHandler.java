package com.aibees.service.maria.accountbook.util.handler;

import com.aibees.service.maria.accountbook.entity.mapper.AccountCardInfoMapper;
import com.aibees.service.maria.accountbook.entity.mapper.AccountMapper;
import com.aibees.service.maria.accountbook.util.AccConstant;

import java.util.Map;

public class TextParseHandler {
    private TextParseHandler() {};

    private static TextParser parser;

    public static Map<String, Object> textParser(String type, String texts, AccountMapper mapper) {
        if(AccConstant.CARD_HANA.equals(type)) {
            parser = new TextParserForHANACARD((AccountCardInfoMapper) mapper);
        } else if(AccConstant.CARD_HYUNDAI.equals(type)) {
            parser = new TextParserForHYUNDAICARD((AccountCardInfoMapper) mapper);
        }

        return parser.process(splitText(texts));
    }

    private static String[] splitText(String text) {
        String[] paramText;
        if(text.contains(AccConstant.NEWLINE_STR)) {
            paramText = text.split(AccConstant.NEWLINE_STR);
        } else {
            paramText = text.split(AccConstant.SPACE_STR);
        }

        return paramText;
    }
}
