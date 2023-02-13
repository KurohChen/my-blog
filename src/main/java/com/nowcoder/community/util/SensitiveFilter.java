package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);
    //替换符
    private static final String REPLACEMENT = "***";
    //根节点
    private  TrieNode rootNode = new TrieNode();

    //在容器实例化这个bean时自动调用init，在调用这个构造器之后init方法就会被自动调用
    @PostConstruct
    public void init(){
        try (
            //读文件中的字符
            //要重新编译才能在target里找到这个文件
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
            //缓冲读
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ){
            String keyword;
            //一行一个敏感词
            while ((keyword = reader.readLine()) != null){
                // 添加到前缀树
                this.addKeyword(keyword);

            }
        }catch (IOException e){
            logger.error("加载敏感词失败："+e.getMessage());
        }

    }

    //将一个敏感词添加到前缀树中
    private void addKeyword(String keyword){
        TrieNode tempNode = rootNode;
        // 遍历添加
        for (int i = 0;i < keyword.length();i++){
            char c = keyword.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            //如果没有的添加 有就往下
            if(subNode == null){
                //初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c,subNode);
            }
            //将指针指向子节点
            tempNode = subNode;
            //设置结束表示
            if(i == keyword.length()-1){
                tempNode.setKeyWordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     * @param text 待过滤文本
     * @return 已过滤文本
     */
    public String filter(String text){
        if (StringUtils.isBlank(text)){
            return null;
        }
        //指针1
        TrieNode tempNode = rootNode;
        //指针2
        int begin = 0;
        //指针3
        int position = 0;
        StringBuilder sb = new StringBuilder();
        while(position < text.length()){
            char c = text.charAt(position);
            // 跳过符号
            if(isSymbol(c)){
                //若指针1处于根节点，将此符号计入结果，让指针2向下走一步
                if(tempNode == rootNode){
                    sb.append(c);
                    begin++;
                }
                //无论符号在哪，指针三都得向下走题一步
                position++;
                continue;
            }
            //检查下级节点
            tempNode = tempNode.getSubNode(c);
            if(tempNode == null){
                //以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                position = ++begin;
                tempNode = rootNode;
            }else if(tempNode.isKeyWordEnd()) {
                //发现敏感词，将begin-position替换
                sb.append(REPLACEMENT);
                begin = ++position;
                tempNode = rootNode;
            }else {
                //检查下一步
                position++;
            }

        }
        //将最后一批字符计入结果
        sb.append(text.substring(begin));
        return sb.toString();
    }

    //判断是否为符号
    private boolean isSymbol(Character c){
        //CharUtils.isAsciiAlphanumeric判断是不是普通字符
        //0x2E80~0x9FFF文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    //前缀树
    private class TrieNode{
        // 关键词结束标识
        private boolean isKeyWordEnd = false;
        // 子节点(key是下级字符，value是下级节点)
        private Map<Character,TrieNode> subNodes = new HashMap<>();

        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }

        // 添加子节点
        public void addSubNode(Character c,TrieNode node){
            subNodes.put(c,node);
        }

        //获取子节点
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }

    }



}
