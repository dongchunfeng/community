package com.nowcoder.community.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class SensitiveFilter {

    //替换符
    private static final String REPLACEMENT = "***";

    //根节点
    private TireNode rootNode = new TireNode();

    @PostConstruct
    public void init() {
        try (
                InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        ) {
            String keyWord;
            while ((keyWord = bufferedReader.readLine()) != null) {
                //添加至前缀树
                this.addKeyWord(keyWord);
            }
        } catch (IOException e) {
            log.error("读取敏感词文件失败:" + e.getMessage());
        }
    }

    private void addKeyWord(String keyWord) {
        //当前节点的根节点
        TireNode tempNode = rootNode;
        for (int i = 0; i < keyWord.length(); i++) {
            char c = keyWord.charAt(i);
            //查询是否有子节点
            TireNode subNode = tempNode.getSubNode(c);
            //如果没有子节点 在创建
            if (subNode == null) {
                subNode = new TireNode();
                //在根节点加入子节点
                tempNode.addSubNode(c, subNode);
            }

            //指向直接点  继续下一个循环
            tempNode = subNode;

            //设置结束标志
            if (i == keyWord.length() - 1) {
                tempNode.setKeyWordEnd(true);
            }

        }
    }


    /**
     * @param text 文本
     * @return 过滤后的结果
     */
    public String filter(String text) {
        //根节点
        TireNode tempNode = rootNode;
        //指针1
        int begin = 0;
        //指针2
        int position = 0;
        //结果
        StringBuilder sb = new StringBuilder();

        while (position < text.length()) {
            char c = text.charAt(position);
            //跳过字符
            if (isSymbol(c)) {
                //指针1处于根节点 将此符号计入结果
                if (tempNode == rootNode) {
                    sb.append(c);
                    begin++;
                }
                position++;
            }

            //检查下级节点
            tempNode = tempNode.getSubNode(c);
            if (tempNode == null) {
                //以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                //进入下一个位置
                position = ++begin;
                //重新指向根节点
                tempNode = rootNode;
            } else if (tempNode.isKeyWordEnd) {
                //发现敏感词
                sb.append(REPLACEMENT);
                begin = ++position;
                //重新指向根节点
                tempNode = rootNode;
            } else {
                //继续检查下个字符
                position++;
            }
        }
        //将最后一批字符计入结果
        sb.append(text.substring(begin));
        return sb.toString();
    }

    //判断是否为符号
    private boolean isSymbol(Character c) {
        //0x2E80  0x9FFF  东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    //前缀树
    public class TireNode {

        //关键词结束标识
        private boolean isKeyWordEnd = false;

        //子节点
        private Map<Character, TireNode> subNodes = new HashMap<>();

        public void addSubNode(Character c, TireNode t) {
            subNodes.put(c, t);
        }

        public TireNode getSubNode(Character c) {
            return subNodes.get(c);
        }

        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }
    }


}
